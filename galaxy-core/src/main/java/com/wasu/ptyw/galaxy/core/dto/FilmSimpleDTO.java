package com.wasu.ptyw.galaxy.core.dto;

import lombok.Data;

/**
 * @author wenguang
 * @date 2015年09月22日
 */
@Data
public class FilmSimpleDTO {
	public FilmSimpleDTO() {

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
	 * 名称
	 */
	private String name;

	/**
	 * RtspUrl
	 */
	private String rtspUrl;

	/**
	 * 剧照（首页导航图片）
	 */
	//private String juzhaoUrl;

	/**
	 * 背景图
	 */
	//private String bgUrl;
	
	/**
	 * 标题图,今日推荐片名图
	 */
	private String biaotiUrl;
	
	/**
	 * 草图,今日推荐简介图
	 */
	private String caotuUrl;

}
