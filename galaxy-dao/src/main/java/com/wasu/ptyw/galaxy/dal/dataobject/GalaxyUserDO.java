package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2015年07月02日
 */
@Alias("GalaxyUserDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyUserDO extends FeaturesDO {
	private static final long serialVersionUID = 1L;

	public GalaxyUserDO() {

	}

	/**
	 * 机顶盒号码
	 */
	private String stbId;

	/**
	 * 机顶盒对应的用户ID
	 */
	private String outUserId;

	/**
	 * 地区编码
	 */
	private String regionCode;

	/**
	 * 绑定的支付宝用户ID
	 */
	private String bindAlipayUserId;

	/**
	 * 支付宝绑定时间
	 */
	private String bindAlipayTime;

	/**
	 * 绑定的微信用户ID
	 */
	private String bindWeixinUserId;

	/**
	 * 微信绑定时间
	 */
	private String bindWeixinTime;

	/**
	 * 状态
	 */
	private Integer status;

}
