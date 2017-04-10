/**
 * 
 */
package com.wasu.ptyw.galaxy.common.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author DEV1
 * 
 */
public class CommonUtil {
	private static final String checkEmailRule = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	private static final String checkNickNameRule = "^[\u4E00-\u9FA5A-Za-z0-9_-]+${4,30}";
	private static final String checkPasswordRule = "[0-9a-zA-Z_]{6,16}";
	private static final String checkRealNameRule = "[\u4E00-\u9FA5]{2,4}";
	private static final String checkBirthdayRule = "\\d{1,4}([-/])\\d{1,2}\\1\\d{1,2}";

	public static boolean checkEmailRule(String email) {
		Pattern regex = Pattern.compile(checkEmailRule);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();

	}

	public static boolean checkRealNameRule(String realName) {
		Pattern regex = Pattern.compile(checkRealNameRule);
		Matcher matcher = regex.matcher(realName);
		return matcher.matches();
	}

	public static boolean checkBirthdayRule(String string) {

		Pattern regex = Pattern.compile(checkBirthdayRule);
		Matcher matcher = regex.matcher(string);
		return matcher.matches();
	}

	public static boolean checkNickNameRule(String nickname) {
		Pattern regex = Pattern.compile(checkNickNameRule);
		Matcher matcher = regex.matcher(nickname);
		return matcher.matches();

	}

	public static boolean checkPassword(String password) {
		Pattern regex = Pattern.compile(checkPasswordRule);
		Matcher matcher = regex.matcher(password);
		return matcher.matches();
	}

	public static boolean checkPassword(String pwd1, String pwd2) {
		if (CommonUtil.MD5(pwd1).equals(CommonUtil.MD5(pwd2)))
			return true;
		return false;
	}

	public static boolean checkStrLenth(String string, int length) {
		if (string.length() > length)
			return false;
		return true;
	}

	public static Date strChangeDate(String str) {
		if (!StringUtil.isEmpty(str)) {
			String string = str.replace("/", "-");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
				date = df.parse(string);
			} catch (ParseException e) {
				
			}
			return date;
		}
		return null;
	}

	public final static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16)
					sb.append("0");
				sb.append(Integer.toHexString(val));
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(MD5("111222"));
	}
}
