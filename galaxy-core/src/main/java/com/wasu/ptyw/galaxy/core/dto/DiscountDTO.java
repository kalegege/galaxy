package com.wasu.ptyw.galaxy.core.dto;

import lombok.Data;

import com.wasu.ptyw.galaxy.common.util.NumUtil;

/**
 * @author wenguang
 * @date 2015年09月01日
 */
@Data
public class DiscountDTO {
	public DiscountDTO() {

	}

	/**
	 * 抽奖记录ID
	 */
	private Long discountId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 折扣价：0表示不变
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
	 * 过期时间
	 */
	private String expiredDateStr;

	public int minPrice(int orgPrice) {
		int destPrice = NumUtil.toInt(getPrice(), 0);
		int percent = NumUtil.toInt(getPercent(), 100);
		orgPrice = orgPrice * percent / 100;
		return destPrice == 0 ? orgPrice : Math.min(orgPrice, destPrice);
	}

}
