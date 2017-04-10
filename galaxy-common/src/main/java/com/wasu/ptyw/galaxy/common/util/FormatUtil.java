package com.wasu.ptyw.galaxy.common.util;

import java.text.MessageFormat;

/**
 * 字符格式化工具类
 * @author  wenguang
 * @version 1.0, 2011-5-17
 */
public class FormatUtil {
	public static String format(String format, Object ... args){
		return String.format(format, args);
	}
	
	public static String formatMsg(String format, Object ... args){
		return 	MessageFormat.format(format, args);
	}
	
	public static void main(String[] args) {
		System.err.println(format("%1$s^^^%2$s^^^%3$s", "aa", "bb", "cc"));
		System.err.println(formatMsg("{0}^^^{1}^^^{2}", "aa", "bb", "cc"));
	}
}
