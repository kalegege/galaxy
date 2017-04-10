package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2015年09月16日
 */
@Alias("GalaxyWeixinFollowDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyWeixinFollowDO extends FeaturesDO {
	private static final long serialVersionUID = 1L;

	public GalaxyWeixinFollowDO() {

	}

	/**
	 * 本系统用户ID
	 */
	private Long userId;

	/**
	 * 微信账号
	 */
	private String wxUserName;
	
	/**
	 * 使用状态：0未使用，1已使用
	 */
	private Integer usedStatus;
	
	/**
	 * 状态，0:待关注，1：已关注，2:取消关注
	 */
	private Integer status;

}
