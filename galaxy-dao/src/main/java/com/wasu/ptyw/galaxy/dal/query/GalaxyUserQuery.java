package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2015年07月02日
 */
@Alias("GalaxyUserQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyUserQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	private Long id;

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
	 * 扩展属性
	 */
	private String features;

	/**
	 * 状态
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
