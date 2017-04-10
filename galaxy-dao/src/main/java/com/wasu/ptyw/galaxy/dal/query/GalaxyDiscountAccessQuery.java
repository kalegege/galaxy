package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2015年09月25日
 */
@Alias("GalaxyDiscountAccessQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyDiscountAccessQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	private Long id;

	/**
	 * 本系统用户ID
	 */
	private Long userId;

	/**
	 * 外部用户ID
	 */
	private String outUserId;

	/**
	 * 地区编码
	 */
	private String regionCode;

	/**
	 * IP地址
	 */
	private String ip;

	/**
	 * 折扣描述
	 */
	private String des;

	/**
	 * 每日时间
	 */
	private String gmtDay;

	/**
	 * 状态：0未中奖，1中奖
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
	
	/**
	 * 多个状态
	 */
	private List<Integer> statusList;

}
