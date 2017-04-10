package com.wasu.ptyw.galaxy.dal.dataobject;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;

/**
 * @author wenguang
 * @date 2015年06月26日
 */
@Alias("GalaxyOrderFilmDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyOrderFilmDO extends FeaturesDO {
	private static final long serialVersionUID = 1L;

	public GalaxyOrderFilmDO() {

	}

	/**
	 * 本系统用户ID
	 */
	private Long userId;

	/**
	 * 外部用户ID
	 */
	private String outUserId;

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
	 * 预支付交易会话标识，有效期为2小时
	 */
	private String payPreId;

	/**
	 * 买家登录账号
	 */
	private String payBuyerLogonId;

	/**
	 * 外部订单号
	 */
	private String payTradeNo;

	/**
	 * 实际支付金额
	 */
	private Integer payTotalFee;

	/**
	 * 支付完成时间
	 */
	private String payTimeEnd;

	/**
	 * 与支付订单生成时间
	 */
	private Date payPreTime;

	/**
	 * 支付帐号
	 */
	private String payOpenid;

	/**
	 * 片子运营商ID
	 */
	private Integer businessId;

	/**
	 * 过期时间，只有订单成功后才会有
	 */
	private Date expiredDate;

	/**
	 * 地区编码
	 */
	private String regionCode;

	/**
	 * 订单状态，0:新订单，1：已生成外部订单未付款，2:交易成功，3：取消订单，4：交易关闭
	 */
	private Integer status;

	public boolean isSuccess() {
		return OrderFilmStatus.SUCCESS.getCode().equals(getStatus()) && expiredDate != null
				&& expiredDate.after(new Date());
	}

}
