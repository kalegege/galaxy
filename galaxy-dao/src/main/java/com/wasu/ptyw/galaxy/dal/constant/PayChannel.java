/**
 * 
 */
package com.wasu.ptyw.galaxy.dal.constant;

/**
 * 订单状态
 * 
 * @author wenguang
 * @date 2015年6月25日
 */
public enum PayChannel {
	WEIXIN(1, "微信"), ALIPAY(2, "支付宝"), WASUPAY(3, "家银通");

	private final Integer code;

	private final String name;

	PayChannel(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static PayChannel get(Integer code) {
		for (PayChannel e : PayChannel.values()) {
			if (e.getCode().equals(code))
				return e;
		}
		return null;
	}

	public static boolean isWeixin(Integer code) {
		return WEIXIN.getCode().equals(code);
	}

	public static boolean isAlipay(Integer code) {
		return ALIPAY.getCode().equals(code);
	}

}
