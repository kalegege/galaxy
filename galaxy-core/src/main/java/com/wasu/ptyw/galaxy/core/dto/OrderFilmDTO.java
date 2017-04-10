package com.wasu.ptyw.galaxy.core.dto;

import lombok.Data;

/**
 * @author wenguang
 * @date 2015年06月26日
 */
@Data
public class OrderFilmDTO {
	/**
	 * 自增ID
	 */
	private Long id;

	/**
	 * 影片ID
	 */
	private Long filmId;

	/**
	 * 总金额，单位分
	 */
	private Integer totalPrice;

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
	 * 支付渠道，1：微信，2:支付宝
	 */
	private Integer payChannel;

	/**
	 * 支付完成时间
	 */
	private String payTimeEnd;

	/**
	 * 过期时间，只有订单成功后才会有
	 */
	private String expiredDateStr;

	private String gmtCreateStr;

	/**
	 * 订单状态，0:新订单，1：已生成外部订单未付款，2:交易成功，3：取消订单，4：交易关闭
	 */
	private Integer status;

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
