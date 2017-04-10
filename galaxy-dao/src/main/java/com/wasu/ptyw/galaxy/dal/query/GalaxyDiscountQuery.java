package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2015年12月17日
 */
@Alias("GalaxyDiscountQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyDiscountQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	public GalaxyDiscountQuery() {

	}

	public GalaxyDiscountQuery(boolean queryCount) {
		setQueryCount(queryCount);
	}

	/**
	 * 自增主键
	 */
	private Long id;

	/**
	 * 地区编码
	 */
	private String regionCode;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 名称
	 */
	private String likeName;

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

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
	private Date gmtModified;

}
