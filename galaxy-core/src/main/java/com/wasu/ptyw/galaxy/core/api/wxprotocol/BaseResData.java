package com.wasu.ptyw.galaxy.core.api.wxprotocol;

import com.wasu.ptyw.galaxy.core.util.XmlUtil;

/**
 * 微信支付基础返回消息类
 * 
 * @author wenguang
 * @date 2015年6月19日
 */
public class BaseResData {
	// 协议层
	private String return_code = "";
	private String return_msg = "";

	public BaseResData(String returnCode, String return_msg) {
		setReturn_code(returnCode);
		setReturn_msg(return_msg);
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public static String getSuccess() {
		return XmlUtil.objToXml(new BaseResData(WxConstant.SUCCESS, ""));
	}

	public static String getFail(String msg) {
		return XmlUtil.objToXml(new BaseResData(WxConstant.FAIL, msg));
	}
}
