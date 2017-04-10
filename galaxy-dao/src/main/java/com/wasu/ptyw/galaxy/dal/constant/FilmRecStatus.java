/**
 * 
 */
package com.wasu.ptyw.galaxy.dal.constant;

/**
 * 影片推荐状态
 * 
 * @author wenguang
 * @date 2015年9月25日
 */
public enum FilmRecStatus {
	NEW(0, "未推荐"), TODAY(1, "今日推荐"), HOT(2, "院线热映"), TICKET(3, "在线购票");

	private final Integer code;

	private final String name;

	FilmRecStatus(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static FilmRecStatus get(Integer code) {
		for (FilmRecStatus e : FilmRecStatus.values()) {
			if (e.getCode().equals(code))
				return e;
		}
		return null;
	}

	public static boolean isNew(Integer code) {
		return NEW.code.equals(code);
	}

	public static boolean isToday(Integer code) {
		return TODAY.code.equals(code);
	}

	public static boolean isHot(Integer code) {
		return HOT.code.equals(code);
	}

	public static boolean isTicket(Integer code) {
		return TICKET.code.equals(code);
	}
}
