package com.wasu.ptyw.galaxy.common.util;

/**
 * 内存工具类
 * 
 * @author wenguang
 * @version 1.0, 2011-1-7
 */
public class MemoryUtil {
	/***************************************************************************
	 * @desc 判断一个类所需要占用的空间大小,_class必须要用默认构造函数
	 * @author shixian
	 * @param _class
	 *            :类 count: 类实例数字
	 **************************************************************************/
	public static int sizeOf(Class<?> _class, int count) throws Exception {
		if (count <= 0)
			count = 10000;
		runGC();
		usedMemory();
		Object[] objects = new Object[count];
		long heap1 = 0;
		for (int i = -1; i < count; ++i) {
			Object object;
			object = _class.newInstance();
			if (i >= 0)
				objects[i] = object;
			else {
				object = null;
				runGC();
				heap1 = usedMemory();
			}
		}
		runGC();
		long heap2 = usedMemory();
		return Math.round(((float) (heap2 - heap1)) / count);
	}

	public static int sizeOf(Class<?> _class) throws Exception {
		return sizeOf(_class, 10000);
	}

	private static int sizeOf(Create create, int length) throws Exception {
		runGC();
		usedMemory();
		Object[] tmp = new Object[length];
		runGC();
		long heap1 = usedMemory();
		create.create(tmp);
		runGC();
		long heap2 = usedMemory();
		return Math.round(((float) (heap2 - heap1)) / length);
	}

	public static long start() throws Exception {
		runGC();
		usedMemory();
		runGC();
		return usedMemory();
	}

	public static long end() throws Exception {
		runGC();
		return usedMemory();
	}

	public static int sizeOf(Create create) throws Exception {
		return sizeOf(create, 100000);
	}

	// a helper method for creating Strings of desired length
	// and avoiding getting tricked by String interning:
	public static String createString(final int length) {
		final char[] result = new char[length];
		for (int i = 0; i < length; ++i)
			result[i] = (char) i;

		return new String(result);
	}

	// this is our way of requesting garbage collection to be run:
	// [how aggressive it is depends on the JVM to a large degree, but
	// it is almost always better than a single Runtime.gc() call]
	private static void runGC() throws Exception {
		// for whatever reason it helps to call Runtime.gc()
		// using several method calls:
		for (int r = 0; r < 4; ++r)
			_runGC();
	}

	private static void _runGC() throws Exception {
		long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;
		for (int i = 0; (usedMem1 < usedMem2) && (i < 1000); ++i) {
			s_runtime.runFinalization();
			s_runtime.gc();
			Thread.yield();

			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}

	private static long usedMemory() {
		return s_runtime.totalMemory() - s_runtime.freeMemory();
	}

	private static final Runtime s_runtime = Runtime.getRuntime();

	public interface Create {
		/**
		 * 创建对象
		 * **/
		public void create(Object[] _obj);
	}
}
