package com.wasu.ptyw.galaxy.core.spring.interceptor;

import lombok.Data;

/**
 * 存放访问的终端信息
 * 
 * @author: 林汝松
 * @Date: 13-9-22 下午6:46
 */
@Data
public class TerminalInfo {
	/**
	 * 终端信息存放，用户可以不从session中取而从threadlocal中取，可以减少参数传递，
	 */
	private static final ThreadLocal<TerminalInfo> terminalInfoHold = new ThreadLocal<TerminalInfo>();

	/*-------- 虚拟机顶盒中的数据 ----*/
	private String stbId;

	private String userId;

	private String userAgent;

	private String ip;

	private String regionId;

	private String userProfile;

	private String regionCode;

	/*-------------terminalInfo相关方法----------------*/

	/**
	 * 获取当前线程中的用户对象
	 * 
	 * @return
	 */
	public static final TerminalInfo getCurrentInstance() {
		return (TerminalInfo) terminalInfoHold.get();
	}

	/**
	 * 设置当前线程的用户对象
	 * 
	 * @param user
	 */
	public static final void setCurrentUser(TerminalInfo t) {
		terminalInfoHold.set(t);
	}

	/**
	 * 移出当前线程用户对象
	 */
	public static final void remove() {
		terminalInfoHold.remove();
	}

	public static String getRegion() {
		if (terminalInfoHold.get() == null)
			return null;
		return terminalInfoHold.get().getRegionCode();
	}

}
