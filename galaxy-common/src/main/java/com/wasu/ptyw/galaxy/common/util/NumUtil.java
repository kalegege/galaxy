/**
 * 
 */
package com.wasu.ptyw.galaxy.common.util;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.Lists;

/**
 * @author wenguang
 * 
 */
public class NumUtil {
	public static int toInt(Number l1, int defaultValue) {
		if (l1 == null)
			return defaultValue;
		return l1.intValue();
	}

	public static boolean isGreaterZero(Integer l1) {
		return l1 != null && l1 > 0;
	}

	public static boolean isGreaterZero(Long l1) {
		return l1 != null && l1 > 0;
	}

	public static boolean isLessZero(Long l1) {
		return l1 != null && l1 < 0;
	}

	public static boolean isEq(Long l1, Long l2) {
		if (l1 != null && l2 != null) {
			return l1.equals(l2);
		}
		return false;
	}

	public static boolean isNotEq(Long l1, Long l2) {
		return !isEq(l1, l2);
	}

	public static <T> boolean isEq(T l1, T l2) {
		if (l1 != null && l2 != null) {
			return l1.equals(l2);
		}
		return false;
	}

	public static float toFloat(String s) {
		float f = 0;
		try {
			f = Float.valueOf(s);
		} catch (Exception e) {

		}
		return f;
	}

	public static float toFloat(String s, String filter) {
		float f = 0;
		try {
			f = Float.valueOf(s.replaceAll(filter, ""));
		} catch (Exception e) {

		}
		return f;
	}

	public static List<Long> toLongs(String s, String split) {
		if (s == null || s.length() == 0) {
			return Lists.newArrayList();
		}
		return toLongs(s.split(split));
	}

	public static List<Long> toLongs(String[] array) {
		List<Long> list = Lists.newArrayList();
		if (array == null || array.length == 0) {
			return list;
		}
		try {
			for (String s : array) {
				list.add(Long.parseLong(s));
			}
		} catch (Exception e) {
			list = Lists.newArrayList();
		}
		return list;
	}

	public static void main(String[] args) {
		String replace = "[\u4ebf\u4e07]";// [亿万]
		System.out.println(toFloat("5809万", replace));
		String key = "liveWASU12#$34&*";
		// key = "liveWASU12#$56&*";
		String time = "201510211001";
		String fileName = "/btws/z.m3u8";
		String md5 = DigestUtils.md5Hex(key + time + fileName);
		String url = new StringBuffer().append("http://apklive2-cnc.wasu.cn/").append(time).append('/').append(md5)
				.append(fileName).toString();
		System.out.println(url);

	}
}
