package com.wasu.ptyw.galaxy.core.dto;

import java.util.List;

import lombok.Data;

import com.google.common.collect.Lists;

/**
 * @author wenguang
 * @date 2015年09月22日
 */
@Data
public class FilmListDTO {
	public FilmListDTO() {

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
	 * 价格:单位分
	 */
	private Integer price;

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
	 * 海报
	 */
	private String picUrl;

	/**
	 * 购票图片
	 */
	private String ticketPicUrl;

	/**
	 * 微信购票编码
	 */
	private String weixinCode;

	/**
	 * 淘宝购票编码
	 */
	private String taobaoCode;

	/**
	 * 片花
	 */
	private List<FilmListDTO> linkFilms = Lists.newArrayList();

	/**
	 * 是否购买过，0未购买，1已购买
	 */
	private int pay = 0;
}
