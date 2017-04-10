package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2015年09月16日
 */
@Alias("GalaxyWeixinFollowQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyWeixinFollowQuery extends SimpleQuery {
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
	 * 微信账号
	 */
	private String wxUserName;

	/**
	 * 扩展属性
	 */
	private String features;

	/**
	 * 使用状态：0未使用，1已使用
	 */
	private Integer usedStatus;

	/**
	 * 状态，0:新关注，1：取消关注，2:再次关注
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
