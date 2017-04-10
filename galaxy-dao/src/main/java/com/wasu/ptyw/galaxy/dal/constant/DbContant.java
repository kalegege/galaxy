/**
 * 
 */
package com.wasu.ptyw.galaxy.dal.constant;

/**
 * 数据库相关常量类
 */
public class DbContant {
	public static final String TRADE_NO_PRE = "TV_";
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final int PAGE_SIZE = 200;
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;

	// 订单表FEATURES字段的key
	public static final String FEA_PREPAY_ALI = "PREPAY_ALI";
	public static final String FEA_PREPAY_WX = "PREPAY_WX";
	public static final String FEA_DOING = "doing";

	public static final String FEA_WX_FOLLOW = "WX_FOLLOW";// 首次关注微信0.1元看大片活动标志
	public static final String FEA_WX_QRCODE = "wx_qrcode";// 二维码地址，用户表
	public static final String FEA_WX_QRCODE_TIME = "wx_qrcode_time";// 二维码地址，用户表
	public static final String FEA_DISCOUNT_ID = "discount_id";// 折扣抽奖记录ID，订单表
	public static final String FEA_DISCOUNT_COUNT = "discount_count";// 抽奖次数、用户表
	public static final String FEA_DISCOUNT_ORDER = "orderId";// 使用的订单ID,抽奖记录表

	public static final String KEY_WX_FOLLOW_PRICE = "wx_follow_price";// 微信抽奖价格
	public static final int WX_FOLLOW_PRICE = 1;// 微信关注1分钱

}
