/**
 * 
 */
package com.wasu.ptyw.galaxy.common.dataobject;

/**
 * @author wenguang
 * 
 */
public enum ResultCode {

	/* 通用 */
	SUCCESS("0", "成功"), SYSTEM_ERROR("9999", "系统异常"), DAO_DEF_EXCEPTION("DB1000", "数据操作异常"), DB_DATA_NOEXIST("DB1001",
			"数据不存在"), CACHE_MULP_FAIL("CACHE1000", "操作缓存失败"),

	PARSE_ERROR("PASER001", "解析异常"), OUT_ERROR("OUT001", "外部系统错误"), OUT_RESPONSE_NULL("OUT002", "外部系统返回数据为空"),

	CHECK_IS_FAIL("CHECK0001", "数据校验失败"), PARAM_INPUT_NULL("CHECK0002", "入参不能为空"), PARAM_INPUT_INVALID("CHECK0003",
			"入参异常"),

	DISCOUNT_COUNT_PERMIT("DISCOUNT0001", "抽奖次数已用完！"),

	ORDER_STATUS_INVALID("ORDER0001", "订单状态不对！"),

	ORDER_USER_INVALID("ORDER0002", "您无权操作此订单！"),
	
	ORDER_WASU_CREATE_FAIL("ORDER0003", "外部订单生成失败！"),

	;

	private String code;

	private String message;

	ResultCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static String getMessageByCode(String code) {
		ResultCode[] ae = ResultCode.values();
		for (int i = 0; i < ae.length; i++) {
			if (ae[i].getCode().equals(code)) {
				return ae[i].getMessage();
			}
		}
		return null;
	}
}
