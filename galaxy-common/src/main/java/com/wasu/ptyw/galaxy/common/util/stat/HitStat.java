/**
 * 
 */
package com.wasu.ptyw.galaxy.common.util.stat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wenguang
 * @date 2014年1月15日
 */
@Slf4j
public class HitStat {
	private static Map<String, HitStat> hitMap = new ConcurrentHashMap<String, HitStat>();

	private static final long delta = 10;

	private AtomicLong nextStatNum = new AtomicLong(10);

	/**
	 * 0位：总数,1位：失败数
	 */
	private AtomicReference<long[]> count = new AtomicReference<long[]>();

	public HitStat() {
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
	 * 成功数加1
	 */
	public void addSuccCount() {
		long[] data = null;
		long[] newData = new long[2];
		do {
			data = count.get();
			newData[0] = data[0] + 1;
		} while (!count.compareAndSet(data, newData));
	}

	/**
	 * 失败数加1
	 */
	public void addFailCount() {
		long[] data = null;
		long[] newData = new long[2];
		do {
			data = count.get();
			newData[0] = data[0] + 1;
			newData[1] = data[1] + 1;
		} while (!count.compareAndSet(data, newData));
	}

	/**
	 * 成功数加1并打印统计信息
	 */
	public static void doHitSucc(String name) {
		HitStat hit = getHitStat(name);
		hit.addSuccCount();
		printHit(name, hit);
	}

	/**
	 * 失败数加1并打印统计信息
	 */
	public static void doHitFail(String name) {
		HitStat hit = getHitStat(name);
		hit.addFailCount();
		printHit(name, hit);
	}

	/**
	 * 根据名字获取HitStat对象
	 */
	private static HitStat getHitStat(String name) {
		HitStat hit = hitMap.get(name);
		if (hit == null) {
			hit = new HitStat();
			hitMap.put(name, hit);
		}
		return hit;
	}

	/**
	 * 打印命中统计信息
	 */
	private static void printHit(String name, HitStat hit) {
		long[] result = hit.getData();
		if (result[0] > hit.nextStatNum.get()) {
			hit.nextStatNum.getAndAdd(delta);
			log.warn(name + ":total==" + result[0] + ",fail=" + result[1]
					+ ",hit=" + (100 * (result[0] - result[0]) / result[0])
					+ "%");
		}
	}

	public static void main(String[] args) {
		String name = "cache";
		for (int i = 0; i < 132; i++)
			HitStat.doHitSucc(name);
		for (int i = 0; i < 141; i++)
			HitStat.doHitFail(name);

	}
}
