/**
 * 
 */
package com.wasu.ptyw.galaxy.common.enums;

/**
 * @author wenguang
 * @date 2014年6月9日
 */
public enum DbStatus {
	D(0, "默认的"), VALID(1, "有效的"), UNVALID(-1, "无效的");

	private final Integer code;

	private final String name;

	DbStatus(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static DbStatus get(Integer code) {
		for (DbStatus e : DbStatus.values()) {
			if (e.getCode().equals(code))
				return e;
		}
		return null;
	}
}
