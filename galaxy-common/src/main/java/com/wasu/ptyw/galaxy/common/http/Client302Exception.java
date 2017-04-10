/**
 * 
 */
package com.wasu.ptyw.galaxy.common.http;

import java.io.IOException;

/**
 * @author wenguang
 * @date 2015年9月29日
 */
public class Client302Exception extends IOException {
	private static final long serialVersionUID = 3683262403335238398L;

	private String loaction;// 302跳转地址

	public Client302Exception(final String location) {
		super();
		this.loaction = location;
	}

	public Client302Exception(final String location, final String msg) {
		super(msg);
		this.loaction = location;
	}

	public String getLoaction() {
		return loaction;
	}

}
