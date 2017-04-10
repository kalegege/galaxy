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
public enum OrderFilmStatus {
	NEW(0, "新订单"), PREPAY(1, "待付款"), SUCCESS(2, "交易成功"), CANCELED(3, "取消订单"), CLOSED(4, "交易关闭");

	private final Integer code;

	private final String name;

	OrderFilmStatus(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static OrderFilmStatus get(Integer code) {
		for (OrderFilmStatus e : OrderFilmStatus.values()) {
			if (e.getCode().equals(code))
				return e;
		}
		return null;
	}

	public static boolean isNew(Integer code) {
		return NEW.code.equals(code);
	}

	public static boolean isSuccess(Integer code) {
		return SUCCESS.code.equals(code);
	}

	public static boolean isPrepay(Integer code) {
		return PREPAY.code.equals(code);
	}

	public static boolean isEnd(Integer code) {
		return SUCCESS.code.equals(code) || CANCELED.code.equals(code) || CLOSED.code.equals(code);
	}
}
