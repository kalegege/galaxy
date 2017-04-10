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
public enum FollowStatus {
	NEW(0, "待关注"), FOLLOWED(1, "已关注"), CANCELED(2, "取消关注");

	private final Integer code;

	private final String name;

	FollowStatus(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static FollowStatus get(Integer code) {
		for (FollowStatus e : FollowStatus.values()) {
			if (e.getCode().equals(code))
				return e;
		}
		return null;
	}

	public static boolean isFollowed(Integer code) {
		return FOLLOWED.code.equals(code);
	}
	
	public static boolean isNew(Integer code) {
		return NEW.code.equals(code);
	}
}
