package com.wasu.ptyw.galaxy.common.util.monitor;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * 应用埋点客户调用接口
 * 
 */
public class MonitorLog {
	private static Logger logger = LoggerFactory.getLogger(MonitorLog.class);
	private static ch.qos.logback.classic.Logger appStatLog;

	private static final Map<Keys, ValueObject> appDatas = new ConcurrentHashMap<Keys, ValueObject>(
			100);

	private static final ReentrantLock lock = new ReentrantLock();
	private static final ReentrantLock timerLock = new ReentrantLock();
	private static final Condition condition = timerLock.newCondition();

	private static String hostName = "";
	private static String appName = null;

	/** MILLISECONDS static long waitTime = 120000L; */
	private static long waitTime = 12L;

	private static boolean writeLog = true;

	public static int appMaxKey = 100000;

	/** 写入数据的线程 */
	private static Thread writeThread = null;

	/**
	 * <p>
	 * 日志文件的绝对路径
	 * </p>
	 */
	private static String logFileAbsolutePath = "";

	private MonitorLog() {
	}

	static {
		/** 动态创建日志记录的配置 */
		String userHome = System.getProperty(MonitorConstants.USER_HOME);
		if (!userHome.endsWith(File.separator)) {
			userHome += File.separator;
		}
		final String filePath = userHome + MonitorConstants.DIR_NAME
				+ File.separator;
		final File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String classLoader = MonitorLog.class.getClassLoader().getClass()
				.getName();

		initAppSlf4j(filePath, classLoader);
		setHostName();

		runWriteThread();
	}

	/**
	 * 设置hostName
	 */
	public static void setHostName() {
		try {
			final InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (final UnknownHostException e) {
			logger.error("MonitorLog getLocalHost error", e);
		}
	}

	/**
	 * 初始化应用的日志记录器
	 * 
	 * @param filePath
	 * @param classLoader
	 */
	private static void initAppSlf4j(final String filePath,
			final String classLoader) {
		final String appFileName = filePath + MonitorConstants.APP_FILE_NAME
				+ classLoader;
		logFileAbsolutePath = appFileName + MonitorConstants.FILE_SUFFIX;

		appStatLog = (ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger("app-" + classLoader);
		LoggerContext loggerContext = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		appStatLog.detachAndStopAllAppenders();

		// define appender
		RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<ILoggingEvent>();
		// policy
		TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<ILoggingEvent>();
		
		policy.setContext(loggerContext);
		// policy.setMaxHistory(5);
		policy.setFileNamePattern(appFileName + ".%d{yyyy-MM-dd}"
				+ MonitorConstants.FILE_SUFFIX);
		policy.setParent(appender);
		policy.start();

		// encoder
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern("%-5level : %d{yyyy-MM-dd HH:mm:ss} %-30logger{20} - %m%n");
		encoder.start();

		// start appender
		//appender.setFile(logFileAbsolutePath);
		appender.setRollingPolicy(policy);
		appender.setContext(loggerContext);
		appender.setEncoder(encoder);
		appender.setPrudent(true); // support that multiple JVMs can safely write to the same file.
		appender.start();

		appStatLog.addAppender(appender);
		appStatLog.setLevel(Level.INFO);
		appStatLog.setAdditive(false);

	}

	public static void load(String externalConfigFileLocation)
			throws IOException, JoranException {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		File externalConfigFile = new File(externalConfigFileLocation);
		if (!externalConfigFile.exists()) {
			throw new IOException(
					"Logback External Config File Parameter does not reference a file that exists");
		} else {
			if (!externalConfigFile.isFile()) {
				throw new IOException(
						"Logback External Config File Parameter exists, but does not reference a file");
			} else {
				if (!externalConfigFile.canRead()) {
					throw new IOException(
							"Logback External Config File exists and is a file, but cannot be read.");
				} else {
					JoranConfigurator configurator = new JoranConfigurator();
					configurator.setContext(lc);
					lc.reset();
					configurator.doConfigure(externalConfigFileLocation);
					StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
				}
			}
		}
	}

	private static volatile boolean stopped = false;

	/**
	 * 执行写入线程
	 */
	public static void runWriteThread() {
		if (null != writeThread) { // 如果线程还存在，先interrupt一下
			try {
				writeThread.interrupt();
			} catch (final Exception e) {
				logger.error("interrupt write thread error", e);
			}
		}
		// 初始化线程
		writeThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 等待waitTime秒
				while (!stopped) {
					timerLock.lock();
					try {
						condition.await(waitTime, TimeUnit.SECONDS);
					} catch (final InterruptedException e) {
						// ignore
					} finally {
						timerLock.unlock();
					}
					MonitorLog.writeLog();
				}
			}
		});
		// THe write thread is daemon thread.
		writeThread.setDaemon(true);
		writeThread.setName(MonitorConstants.WRITETHREAD_NAME);
		writeThread.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				stopped = true;
				if (writeThread != null && writeThread.isAlive()) {
					writeThread.interrupt();
				}
				try {
					writeThread.join(3000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
	}

	public static String getLogFileAbsolutePath() {
		return logFileAbsolutePath;
	}

	/**
	 * 写入日志的主方法
	 */
	private static void writeLog() {
		// 写入一般应用
		final Map<Keys, ValueObject> tmp = new HashMap<Keys, ValueObject>(
				appDatas.size()); // 设置一个临时的数据是为了保证数据都写到文件后才减少datas的数据
		final StringBuilder sb = new StringBuilder();
		for (final Keys key : appDatas.keySet()) {
			final long[] values = appDatas.get(key).getValues();
			final long value1 = values[0];
			final long value2 = values[1];
			if (0 == value1 && 0 == value2) {
				continue;
			}

			sb.append(key.getString(MonitorConstants.SPLITTER_1)).append(
					MonitorConstants.SPLITTER_1);
			sb.append(value1).append(MonitorConstants.SPLITTER_1);
			sb.append(value2).append(MonitorConstants.SPLITTER_1);
			sb.append(hostName).append(MonitorConstants.SPLITTER_2);
			tmp.put(key, new ValueObject(value1, value2));
		}

		if (tmp.size() > 0 && writeLog) {
			appStatLog.info(sb.toString());
		}

		// 循环把已经写入文件的数据从datas中减少
		for (final Keys key : tmp.keySet()) {
			final long[] values = tmp.get(key).getValues();
			appDatas.get(key).deductCount(values[0], values[1]);
		}

	}

	/**
	 * keyOne,keyTwo,keyThree不能带有类似\n的回行
	 * 
	 * @param keyOne
	 * @param keyTwo
	 * @param keyThree
	 */
	public static void addStat(final String keyOne, final String keyTwo,
			final String keyThree) {
		final Keys keys = new Keys(keyOne, keyTwo, keyThree);
		addStat(keys, 1, 0);
	}

	/**
	 * keyOne,keyTwo,keyThree不能带有类似\n的回行
	 * 
	 * @param keyOne
	 * @param keyTwo
	 * @param keyThree
	 * @param value
	 */
	public static void addStatValue2(final String keyOne, final String keyTwo,
			final String keyThree, final long value) {
		final Keys keys = new Keys(keyOne, keyTwo, keyThree);
		addStat(keys, 1, value);
	}

	/**
	 * keyOne,keyTwo,keyThree不能带有类似\n的回行
	 * 
	 * @param keyOne
	 * @param keyTwo
	 * @param keyThree
	 * @param value1
	 * @param value2
	 */
	public static void addStat(final String keyOne, final String keyTwo,
			final String keyThree, final long value1, final long value2) {
		final Keys keys = new Keys(keyOne, keyTwo, keyThree);
		addStat(keys, value1, value2);
	}

	public static void addStat(final Keys keys, final long value1,
			final long value2) {
		setAppName(keys.getAppName());
		final ValueObject v = getAppValueObject(keys);
		if (v != null) {
			v.addCount(value1, value2);
		}
	}

	public static void immediateAddStat(final String keyOne,
			final String keyTwo, final String keyThree, final long value1,
			final long value2) {
		final StringBuilder sb = new StringBuilder();
		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				MonitorConstants.DATE_FORMAT);
		final String appTime = dateFormat.format(Calendar.getInstance()
				.getTime());

		sb.append(keyOne).append(MonitorConstants.SPLITTER_1);
		sb.append(keyTwo).append(MonitorConstants.SPLITTER_1);
		sb.append(keyThree).append(MonitorConstants.SPLITTER_1);
		sb.append(value1).append(MonitorConstants.SPLITTER_1);
		sb.append(value2).append(MonitorConstants.SPLITTER_1);
		sb.append(appTime).append(MonitorConstants.SPLITTER_1);
		sb.append(hostName).append(MonitorConstants.SPLITTER_2);
		appStatLog.info(sb.toString());
	}

	public static boolean isWriteLog() {
		return writeLog;
	}

	public static void setWriteLog(final boolean writeLog) {
		MonitorLog.writeLog = writeLog;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(final long waitTime) {
		MonitorLog.waitTime = waitTime;
		MonitorLog.runWriteThread();
	}

	private static void setAppName(final String name) {
		if (name != null && appName == null) {
			appName = name;
		}
	}

	public int getAppMaxKey() {
		return appMaxKey;
	}

	public void setAppMaxKey(final int appMaxKey) {
		MonitorLog.appMaxKey = appMaxKey;
	}

	/**
	 * 这个方法将确保不会返回null。（当keys无对应的ValueObject时创建之）
	 */
	protected static ValueObject getAppValueObject(final Keys keys) {
		ValueObject v = appDatas.get(keys);
		if (null == v) {
			lock.lock();
			try {
				v = appDatas.get(keys);
				if (null == v) {
					if (appDatas.size() <= appMaxKey) {
						v = new ValueObject();
						appDatas.put(keys, v);
					} else {
						logger.warn("sorry,monitor app key is out size of "
								+ appMaxKey);
					}
				}
			} finally {
				lock.unlock();
			}
		}
		return v;
	}

	public static void main(final String args[]) {
		// System.out.println(MonitorLog.parseConfig("c://test.xml"));
		final Thread testwriteThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					timerLock.lock();
					try {
						condition.await(5, TimeUnit.MILLISECONDS);
					} catch (final Exception e) {
						logger.error("wait error", e);
					} finally {
						timerLock.unlock();
					}
					MonitorLog.addStat("TC2", null, null, 541, 256);
					MonitorLog.addStat("TC1", "SyncCreateOrder", "ERROR", 541,
							256); // ok
					MonitorLog
							.addStat("TC3", "SyncCreateOrder", null, 541, 256);
					MonitorLog.addStat("UIC4", "SyncCreateOrder", "timout",
							5621, 10);
					MonitorLog.addStat("IC5", "SyncCreateOrder", "normal", 999,
							5221);
				}
			}
		});
		testwriteThread.setName("TestTreadName");
		testwriteThread.start();
	}

}