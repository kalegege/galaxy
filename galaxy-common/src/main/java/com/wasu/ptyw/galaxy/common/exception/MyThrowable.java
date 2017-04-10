/**
 * 
 */
package com.wasu.ptyw.galaxy.common.exception;

/**
 * 
 * @author wenguang
 * @date 2015年6月3日
 */
public interface MyThrowable {
	/**系统未知错误*/
	public final static String SYS_ERROR = "100_SYS_ERROR";
	
	/**
	 * 获取错误编码
	 * @return
	 */
	public String getErrorCode();

	/**
	 * 获取错误提示信息
	 * @return
	 */
	public String getMessage();

	/**
	 * 获取错误异常cause
	 * @return
	 */
	public Throwable getCause();
}
