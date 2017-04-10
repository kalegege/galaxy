/**
 * 
 */
package com.wasu.ptyw.galaxy.common.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author wenguang
 * 
 */
public class DateUtil {
	public static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_2 = "yyyy-MM-dd";
	public static final String FORMAT_3 = "yyyyMMdd";
	public static final String FORMAT_5 = "MM/dd/yyyy hh:mm:ss.SSSa";
	public static final String FORMAT_6 = "yyyy-MM-dd EEEE ZZZZ";
	public static final String FORMAT_7 = "yyyyMMddHHmmss";
	public static final int DAY_7 = 7 * 24 * 60 * 60;// 7天的秒数
	public static final int DAY_6 = 6 * 24 * 60 * 60;// 6天的秒数

	public static String now() {
		return now(FORMAT_3);
	}

	public static String now(String format) {
		return DateTime.now().toString(format);
	}

	public static String convert(String s, String fromFormat, String toFormat) {
		if (s == null || s.length() == 0)
			return s;
		try {
			DateTimeFormatter format = DateTimeFormat.forPattern(fromFormat);
			DateTime dateTime = DateTime.parse(s, format);
			return dateTime.toString(toFormat);
		} catch (Exception e) {
			return s;
		}
	}

	public static String format(Date date, String format) {
		return new DateTime(date).toString(format);
	}

	public static String parse(String str, String format) {
		return DateTime.parse(str).toString(format);
	}

	public static String format(long time, String format) {
		return new DateTime(time).toString(format);
	}

	public static String formatBetween(Date date) {
		if (date == null)
			return "";
		DateTime start = DateTime.now();
		DateTime end = new DateTime(date);

		int days = Days.daysBetween(start, end).getDays();
		if (days > 0) {
			start = start.plusDays(-days);
		}

		int hours = Hours.hoursBetween(start, end).getHours();
		if (hours > 0) {
			start.plusHours(-hours);
		}

		int minutes = Minutes.minutesBetween(start, end).getMinutes();

		if (days > 0) {
			if (hours > 0 || minutes > 0)
				days++;
			return "剩余" + days + "天";
		}

		if (hours > 0) {
			if (minutes > 0)
				hours++;
			return hours >= 24 ? "剩余1天" : "剩余" + hours + "小时";
		}
		if (minutes > 0) {
			return "剩余" + minutes + "分";
		}

		return end.compareTo(start) > 0 ? "1分钟" : "已过期";
	}

	public static String formatBetweenWithEncode(Date date) {
		String s = formatBetween(date);
		return s;
	}

	/**
	 * 判断时间与现在比，是否超过多少秒
	 * 
	 * @param date
	 *            时间
	 * @param second
	 *            超时时间（单位秒）
	 */
	public static boolean expire(Date date, int second) {
		if (date == null)
			return true;
		return expire(date.getTime(), second);
	}

	/**
	 * 判断时间与现在比，是否超过多少秒
	 * 
	 * @param date
	 *            时间
	 * @param second
	 *            超时时间（单位秒）
	 */
	public static boolean expire(long time, int second) {
		return System.currentTimeMillis() - time > second * 1000L;
	}

	/**
	 * 增加天数
	 */
	public static Date plusDay(Date date, int days) {
		if (date == null)
			return null;
		return new DateTime(date).plusDays(days).toDate();
	}

	public static void main(String[] args) {
		System.out.println(formatBetween(DateTime.now().plusMinutes(0).plusSeconds(11).toDate()));
	}
}
