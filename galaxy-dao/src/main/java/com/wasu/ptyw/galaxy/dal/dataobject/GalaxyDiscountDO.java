package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2015年12月17日
 */
@Alias("GalaxyDiscountDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyDiscountDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public GalaxyDiscountDO() {

	}

	/**
	 * 地区编码
	 */
	private String regionCode;

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
	 * 优先级
	 */
	private Integer priority;

	/**
	 * 中奖概率（0~100）
	 */
	private Integer rate;

	/**
	 * 奖品总数，-1不限制
	 */
	private Integer count;

	/**
	 * 状态，0:新建，1：上线
	 */
	private Integer status;

}
