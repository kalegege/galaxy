package com.wasu.ptyw.galaxy.core.dto;

import lombok.Data;

/**
 * @author wenguang
 * @date 2015年07月07日
 */
@Data
public class HistoryDTO {
	/**
	 * 自增ID
	 */
	private Long id;
	
	/**
	 * 影片ID
	 */
	private Long filmId;

	/**
	 * 片源code
	 */
	private String contCode;

	/**
	 * 片源名称
	 */
	private String contName;

	/**
	 * 片源图片地址
	 */
	private String contImage;

	/**
	 * 观看时间，单位秒
	 */
	private Integer playTime;

	/**
	 * 过期时间
	 */
	private String expiredDateStr;

	/**
	 * RtspUrl
	 */
	private String rtspUrl;

	/**
	 * 播放地址
	 */
	private String assetUrl;

	/**
	 * 上线状态：0下线，1上线,
	 */
	private Integer filmStatus = 0;

}
