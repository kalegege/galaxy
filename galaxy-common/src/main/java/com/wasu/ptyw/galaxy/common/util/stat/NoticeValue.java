package com.wasu.ptyw.galaxy.common.util.stat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;

/**
 * 统计平均值，访问详情页中用到
 * 
 * @author wenguang
 */
@Slf4j
public class NoticeValue {
	private static Map<String, NoticeValue> noticeMap = new ConcurrentHashMap<String, NoticeValue>();

	private static final int MAX_NUM = 100;

	private AtomicReference<long[]> count = new AtomicReference<long[]>();

	public NoticeValue() {
		long[] l = new long[2];
		this.count.set(l);
	}

	/**
	 * 取得当前值
	 */
	public long[] getData() {
		long[] data = count.get();
		return data;
	}

	/**
	 * 取得当前值并将初始值设置为0
	 */
	public long[] getDataAndReset() {
		long[] data = count.get();
		downCount(data);
		return data;
	}
	
	/**
	 * 减少
	 */
	public void downCount(long[] v) {
		long[] data = null;
		long[] newData = new long[2];
		do {
			data = count.get();
			newData[0] = data[0] - v[0];
			newData[1] = data[1] - v[1];
		} while (!count.compareAndSet(data, newData));
	}

	/**
	 * 累加
	 */
	public void addCount(long v) {
		long[] data = null;
		long[] newData = new long[2];
		do {
			data = count.get();
			newData[0] = data[0] + 1;
			newData[1] = data[1] + v;
		} while (!count.compareAndSet(data, newData));
	}

	/**
	 * 统计MAX_NUM次总访问时间
	 */
	public static void doNoticeCount(String name, long num) {
		NoticeValue nv = addNotice(name,num);
		long[] result = nv.getData();
		if (result[0] > MAX_NUM) {
			long[] result1 = nv.getDataAndReset();
			printMessage(name + ":" + result1[1]);
		}
	}

	/**
	 * 统计平均每次访问时间
	 */
	public static void doNoticeAverage(String name, long num) {
		NoticeValue nv = addNotice(name,num);
		long[] result = nv.getData();
		if (result[0] > MAX_NUM) {
			long[] result1 = nv.getDataAndReset();
			printMessage(name + ":" + (result1[1] / result1[0]));
		}
	}
	
	private static NoticeValue addNotice(String name, long num){
		NoticeValue nv = noticeMap.get(name);
		if (nv == null) {
			nv = new NoticeValue();
			noticeMap.put(name, nv);
		}
		nv.addCount(num);
		return nv;
	}

	private static void printMessage(String message) {
		// 打印日志
		log.warn(message);
	}

}
