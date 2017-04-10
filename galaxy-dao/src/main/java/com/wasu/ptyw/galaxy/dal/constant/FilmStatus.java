/**
 * 
 */
package com.wasu.ptyw.galaxy.dal.constant;

/**
 * 影片状态
 * 
 * @author wenguang
 * @date 2015年9月25日
 */
public enum FilmStatus {
	NEW(0, "新导入"), ONLINE(1, "上线"), OFFLINE(2, "手动下线"), OFFLINESYS(3, "系统下线");
	private final Integer code;

	private final String name;

	FilmStatus(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static FilmStatus get(Integer code) {
		for (FilmStatus e : FilmStatus.values()) {
			if (e.getCode().equals(code))
				return e;
		}
		return null;
	}

	public static boolean isNew(Integer code) {
		return NEW.code.equals(code);
	}

	public static boolean isOnline(Integer code) {
		return ONLINE.code.equals(code);
	}
}
