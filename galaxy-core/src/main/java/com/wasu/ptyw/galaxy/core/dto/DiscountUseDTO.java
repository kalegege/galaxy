package com.wasu.ptyw.galaxy.core.dto;

import lombok.Data;

import com.wasu.ptyw.galaxy.common.util.NumUtil;

/**
 * @author wenguang
 * @date 2015年09月01日
 */
@Data
public class DiscountUseDTO {
	public DiscountUseDTO() {

	}

	/**
	 * 抽奖记录ID
	 */
	private Long discountAccessId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 折扣价：-1表示不变
	 */
	private Integer price;

	/**
	 * 折扣百分比：0到100
	 */
	private Integer percent;

	/**
	 * 图片地址
	 */
	private String pic;

	/**
	 * 描述
	 */
	private String des;
	
	/**
	 * 状态：0未中奖，1中奖, 2已使用
	 */
	private Integer status;

	/**
	 * 订单ID
	 */
	private Long orderId;

	/**
	 * 购买影片名称
	 */
	private String filmName;

	/**
	 * 购买影片名称地址
	 */
	private String filmPic;

	/**
	 * 过期时间
	 */
	private String expiredDateStr;

	private String gmtCreateStr;

	public int minPrice(int orgPrice) {
		int destPrice = NumUtil.toInt(getPrice(), 0);
		int percent = NumUtil.toInt(getPercent(), 100);
		orgPrice = orgPrice * percent;
		return destPrice == 0 ? orgPrice : Math.min(orgPrice, destPrice);
	}

}
