package com.wasu.ptyw.galaxy.common.util.monitor;

import java.io.File;


/**
 * 应用埋点常量类
 * 
 * @author wenguang
 * 
 */
public class MonitorConstants {

    public static final String GBK = "GBK";
    public static final String YYYY_MM_DD = "'.'yyyy-MM-dd";
    public static final String M_N = "%m%n";
    public static final String M = "%m";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String USER_HOME = "user.home";
    public static final String LOG_STAT_NAME_STAT_LOG = "log-monitor:name=monitor-client";

    public static final String DIR_NAME = "logs" + File.separator + "monitor";

    public static final String APP_FILE_NAME = "monitor.app.";
    public static final String MIDDLEWARE_FILE_NAME = "monitor.middleware.";
    public static final String FILE_SUFFIX = ".log";

    public static final String WRITETHREAD_NAME = "LOGSTAT-WRITE";

    public static final String SPLITTER_1 = "" + (char) 1;
    public static final String SPLITTER_2 = "" + (char) 2;

    public static final String KEY_LEVEL_JVM = "JVM";
    public static final String KEY_LEVEL_CPU = "CPU";
    public static final String KEY_LEVEL_MEMORY = "MEMORY";
    public static final String KEY_LEVEL_SITUATION = "SITUATION";
    public static final String KEY_LEVEL_USAGE = "USAGE";
    public static final String KEY_LEVEL_THREAD = "THREAD";
    public static final String KEY_LEVEL_TOTAL = "TOTAL";

}