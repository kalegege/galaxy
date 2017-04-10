package com.wasu.ptyw.galaxy.core.dto;

import lombok.Data;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2015年09月22日
 */
@Alias("GalaxyFilmDO")
@Data
public class FilmDTO {
	public FilmDTO() {

	}

	/**
	 * 自增ID
	 */
	private Long id;

	/**
	 * 资产ID
	 */
	private String assetId;

	/**
	 * 资产类型:13是视频，36是电影
	 */
	private Integer assetType;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 高标清:0标清，1高清
	 */
	private Integer ishd;

	/**
	 * 价格:单位分
	 */
	private Integer price;

	/**
	 * 发行年份
	 */
	private String year;

	/**
	 * 演员
	 */
	private String actors;

	/**
	 * 导演
	 */
	private String directors;

	/**
	 * 看点
	 */
	private String viewpoint;

	/**
	 * RtspUrl
	 */
	private String rtspUrl;

	/**
	 * 播放地址
	 */
	private String assetUrl;

	/**
	 * 星级:0-5
	 */
	private Integer starlevel;

	/**
	 * 类别
	 */
	private String leixing;

	/**
	 * 海报
	 */
	private String picUrl;

	/**
	 * 状态：1上线,其他下线
	 */
	private Integer status;
}
