/**
 * 
 */
package com.wasu.ptyw.galaxy.core.api;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tencent.WXPay;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;
import com.tencent.common.Util;
import com.tencent.protocol.close_protocol.CloseOrderReqData;
import com.tencent.protocol.close_protocol.CloseOrderResData;
import com.tencent.protocol.pay_protocol.ScanPayUnitReqData;
import com.tencent.protocol.pay_protocol.ScanPayUnitResData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.common.util.NetUtil;
import com.wasu.ptyw.galaxy.common.util.PropertiesUtil;
import com.wasu.ptyw.galaxy.common.util.RequestUtil;
import com.wasu.ptyw.galaxy.core.ao.GalaxyWeixinFollowAO;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.BaseResData;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.PayNotifyData;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.ScanQrcodeReqData;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.ScanQrcodeResData;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.WxConstant;
import com.wasu.ptyw.galaxy.core.manager.GalaxyOrderFilmManager;
import com.wasu.ptyw.galaxy.core.manager.GalaxyUserManager;
import com.wasu.ptyw.galaxy.core.timetask.AsyncWorker;
import com.wasu.ptyw.galaxy.core.util.XmlUtil;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.constant.PayChannel;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;

/**
 * @author wenguang
 * @date 2015年6月18日
 */
@Service("wxpayService")
@Slf4j
public class WxpayService extends BaseOutService {
	String qrcodeUrl = "weixin://wxpay/bizpayurl?";
	String appID = "";
	String mchID = "";

	@Resource
	GalaxyOrderFilmManager galaxyOrderFilmManager;
	@Resource
	GalaxyUserManager galaxyUserManager;
	@Resource
	GalaxyWeixinFollowAO galaxyWeixinFollowAO;

	@Resource
	AlipayService alipayService;

	@Resource
	AsyncWorker asyncWorker;

	@PostConstruct
	public void init() {
		this.appID = PropertiesUtil.getValue("wx_appID");
		this.mchID = PropertiesUtil.getValue("wx_mchID");
		String key = PropertiesUtil.getValue("wx_key");
		String certLocalPath = PropertiesUtil.getValue("wx_certLocalPath");
		String certPassword = PropertiesUtil.getValue("wx_certPassword");
		WXPay.initSDKConfiguration(key, appID, mchID, null, certLocalPath, certPassword);
	}

	/**
	 * 生成二维码地址
	 * 
	 * @param productId
	 *            商户定义的商品id 或者订单号
	 */
	public String generateQrcode(String productId) {
		// 时间戳,自1970年1月1日 0点0分0秒以来的秒数
		long timeStamp = System.currentTimeMillis() / 1000;
		// 随机字符串, 不长于32位
		String nonceStr = RandomStringGenerator.getRandomStringByLength(4);
		// RandomStringUtils.random(32, true, true);
		Map<String, Object> params = Maps.newHashMap();
		params.put("appid", this.appID);
		params.put("mch_id", this.mchID);
		params.put("product_id", productId);
		params.put("time_stamp", timeStamp);
		params.put("nonce_str", nonceStr);
		String sign = Signature.getSign(params);
		params.put("sign", sign);
		return RequestUtil.buidlUrl(this.qrcodeUrl, params);
	}

	/**
	 * 统一下单接口，失败时返回null <br>
	 * 目前不处理各种异常情况
	 */
	public String prePay(long orderId) {
		String qrcodeUrl = "";
		try {
			GalaxyOrderFilmDO order = null;
			boolean isDoing = false;
			do {
				order = galaxyOrderFilmManager.getById(orderId);
				if (order == null || OrderFilmStatus.isEnd(order.getStatus())) {
					return qrcodeUrl;
				}
				isDoing = order.isExist(DbContant.FEA_DOING);
				if (isDoing) {
					sleep(100);
				}
			} while (isDoing);// 避免支付宝微信重复

			order.putFeature(DbContant.FEA_DOING, DbContant.YES);
			galaxyOrderFilmManager.update(order);

			ScanPayUnitReqData scanPayUnitReqData = buildScanPayUnitReqData(order);
			ScanPayUnitResData scanPayUnitResData = prePay(scanPayUnitReqData);
			if (scanPayUnitResData != null) {
				qrcodeUrl = scanPayUnitResData.getCode_url();
			}
			if (StringUtils.isNotEmpty(qrcodeUrl)) {
				order.setStatus(OrderFilmStatus.PREPAY.getCode());
				order.setPayChannel(PayChannel.WEIXIN.getCode());
				order.setPayPreId(scanPayUnitResData.getPrepay_id());
				order.putFeature(DbContant.FEA_PREPAY_WX, DbContant.YES);
				if (StringUtils.isNotEmpty(order.getFeature(DbContant.FEA_WX_FOLLOW))) {
					List<Long> ids = Lists.newArrayList(NumberUtils.toLong(order.getFeature(DbContant.FEA_WX_FOLLOW)));
					galaxyWeixinFollowAO.updateUsedStatusByIds(ids, DbContant.TWO);
				}
			}
			order.putFeature(DbContant.FEA_DOING, DbContant.NO);
			galaxyOrderFilmManager.update(order);
		} catch (Exception e) {
			log.error("scanPay error,", e);
		}
		return qrcodeUrl;
	}

	/**
	 * 统一下单接口，失败时返回null <br>
	 * 目前不处理各种异常情况
	 */
	public ScanPayUnitResData prePay(ScanPayUnitReqData scanPayUnitReqData) {
		ScanPayUnitResData scanPayUnitResData = null;
		String responseString;
		try {
			responseString = WXPay.requestScanPayUnitService(scanPayUnitReqData);
			log.info("WXPay.requestScanPayUnitService response:" + responseString);
			scanPayUnitResData = (ScanPayUnitResData) Util.getObjectFromXML(responseString, ScanPayUnitResData.class);
			if (scanPayUnitResData == null) {
				return null;
			}

			if (!WxConstant.SUCCESS.equals(scanPayUnitResData.getReturn_code())) {
				return null;
			}

			if (!Signature.checkIsSignValidFromResponseString(responseString)) {
				return null;
			}

			if (!WxConstant.SUCCESS.equals(scanPayUnitResData.getResult_code())) {
				return null;
			}
		} catch (Exception e) {
			log.error("scanPay error,", e);
			return null;
		}
		return scanPayUnitResData;
	}

	/**
	 * 关闭订单接口，失败时返回null <br>
	 * 目前不处理各种异常情况
	 */
	public boolean cancel(GalaxyOrderFilmDO order) {
		CloseOrderResData resData = null;
		String responseString;
		try {
			CloseOrderReqData closeOrderReqData = new CloseOrderReqData(buildOutTradeNo(order));
			responseString = WXPay.requestCloseOrderService(closeOrderReqData);
			log.info("WXPay.requestScanPayUnitService response:" + responseString);
			resData = (CloseOrderResData) Util.getObjectFromXML(responseString, CloseOrderResData.class);
			if (resData == null) {
				return false;
			}

			if (!WxConstant.SUCCESS.equals(resData.getReturn_code())) {
				return false;
			}

			if (!Signature.checkIsSignValidFromResponseString(responseString)) {
				return false;
			}

			if (!WxConstant.SUCCESS.equals(resData.getResult_code())) {
				return false;
			}
		} catch (Exception e) {
			log.error("closeOrder error,orderId=" + (order != null ? order.getId() : ""), e);
			return false;
		}
		return true;
	}

	/**
	 * 查询订单接口，失败时返回null <br>
	 * 目前不处理各种异常情况
	 */
	public ScanPayQueryResData query(long orderId) {
		ScanPayQueryResData resData = null;
		String responseString;
		try {
			GalaxyOrderFilmDO order = galaxyOrderFilmManager.getById(orderId);
			if (order == null) {
				return null;
			}
			ScanPayQueryReqData scanPayQueryReqData = new ScanPayQueryReqData("", buildOutTradeNo(order));
			responseString = WXPay.requestScanPayQueryService(scanPayQueryReqData);
			log.info("WXPay.requestScanPayUnitService response:" + responseString);
			resData = (ScanPayQueryResData) Util.getObjectFromXML(responseString, ScanPayQueryResData.class);
			if (resData == null) {
				return null;
			}

			if (!WxConstant.SUCCESS.equals(resData.getReturn_code())) {
				return null;
			}

			if (!Signature.checkIsSignValidFromResponseString(responseString)) {
				return null;
			}

			if (!WxConstant.SUCCESS.equals(resData.getResult_code())) {
				return null;
			}
		} catch (Exception e) {
			log.error("closeOrder error,orderId=" + orderId, e);
		}
		return resData;
	}

	/**
	 * 二维码扫描回调处理
	 */
	public String callbackQrcode(String xml) {
		try {
			log.info("callbackQrcode xml:" + xml);
			if (StringUtils.isEmpty(xml)) {
				return ScanQrcodeResData.getFail("参数为空");
			}
			ScanQrcodeReqData reqData = (ScanQrcodeReqData) XmlUtil.getObjectFromXML(xml, ScanQrcodeReqData.class);
			if (reqData == null) {
				return ScanQrcodeResData.getFail("参数错误");
			}

			if (!Signature.checkIsSignValidFromResponseString(xml)) {
				return ScanQrcodeResData.getFail("签名验证失败");
			}

			GalaxyOrderFilmDO order = galaxyOrderFilmManager.getById(toOrderId(reqData.getProduct_id()));
			if (order == null) {
				return ScanQrcodeResData.getFail("订单不存在");
			}

			if (OrderFilmStatus.isEnd(order.getStatus())) {
				return ScanQrcodeResData.getFail("订单状态错误");
			}

			// 调用微信统一下单接口
			ScanPayUnitReqData scanPayUnitReqData = buildScanPayUnitReqData(order);
			ScanPayUnitResData scanPayUnitResData = prePay(scanPayUnitReqData);
			if (scanPayUnitResData == null) {
				return ScanQrcodeResData.getFail("下单失败");
			}

			// 更新本地订单
			order.setStatus(OrderFilmStatus.PREPAY.getCode());
			order.setPayChannel(PayChannel.WEIXIN.getCode());
			order.setPayPreId(scanPayUnitResData.getPrepay_id());
			order.putFeature(DbContant.FEA_PREPAY_WX, DbContant.YES);
			galaxyOrderFilmManager.update(order);

			return XmlUtil.objToXml(new ScanQrcodeResData(scanPayUnitResData.getPrepay_id()));
		} catch (Exception e) {
			log.error("callbackQrcode error,", e);
			return ScanQrcodeResData.getFail("系统异常");
		}
	}

	/**
	 * 支付回调处理
	 */
	public String callbackPay(String xml) {
		try {
			log.info("callbackPay xml:" + xml);
			if (StringUtils.isEmpty(xml)) {
				return BaseResData.getFail("参数为空");
			}

			PayNotifyData reqData = (PayNotifyData) XmlUtil.getObjectFromXML(xml, PayNotifyData.class);
			if (reqData == null) {
				return BaseResData.getFail("参数错误");
			}

			if (!WxConstant.SUCCESS.equals(reqData.getReturn_code())) {
				return BaseResData.getFail("return_code返回失败");
			}

			if (!Signature.checkIsSignValidFromResponseString(xml)) {
				return BaseResData.getFail("签名验证失败");
			}

			if (!WxConstant.SUCCESS.equals(reqData.getResult_code())) {
				return BaseResData.getFail("result_code返回失败：" + reqData.getErr_code());
			}

			GalaxyOrderFilmDO order = galaxyOrderFilmManager.getById(toOrderId(reqData.getOut_trade_no()));

			if (order == null) {
				return BaseResData.getFail("订单找不到");
			}

			// 成功了关闭支付宝订单
			if (OrderFilmStatus.isPrepay(order.getStatus())
					&& DbContant.YES.equals(order.getFeature(DbContant.FEA_PREPAY_ALI))) {
				boolean closeFlg = alipayService.cancel(order);
				log.warn("close alipay order, order_id=" + order.getId() + "result:" + closeFlg);
			}

			order.setPayChannel(PayChannel.WEIXIN.getCode());
			order.setPayTradeNo(reqData.getTransaction_id());
			order.setPayOpenid(reqData.getOpenid());
			order.setPayTimeEnd(reqData.getTime_end());
			order.setPayTotalFee(reqData.getTotal_fee());
			order.setStatus(OrderFilmStatus.SUCCESS.getCode());
			order.setExpiredDate(LogicUtil.getOrderExpiredTime());
			galaxyOrderFilmManager.update(order);

			// 微信关注活动
			// if
			// (StringUtils.isNotEmpty(order.getFeature(DbContant.FEA_WX_FOLLOW)))
			// {
			// asyncWorker.wxFollowPaySuccess(order.getFeature(DbContant.FEA_WX_FOLLOW),
			// order.getUserId());
			// }

			// 增加抽奖次数
			GalaxyUserDO user = galaxyUserManager.getById(order.getUserId());
			if (user != null) {
				int count = NumberUtils.toInt(user.getFeature(DbContant.FEA_DISCOUNT_COUNT)) + 1;
				user.putFeature(DbContant.FEA_DISCOUNT_COUNT, count + "");
				galaxyUserManager.update(user);
			}

			return BaseResData.getSuccess();
		} catch (Exception e) {
			log.error("callbackPay error,", e);
			return BaseResData.getFail("系统异常");
		}
	}

	private ScanPayUnitReqData buildScanPayUnitReqData(GalaxyOrderFilmDO order) {
		DateTime date = DateTime.now();
		// 要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
		String body = order.getContName();
		// 支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回
		String attach = "";
		// 商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
		String outTradeNo = buildOutTradeNo(order);
		// 订单总金额，单位为“分”，只能整数
		int totalFee = order.getTotalPrice();
		// 商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
		String deviceInfo = order.getRegionCode();
		// 订单生成的机器IP
		String spBillCreateIP = NetUtil.getLocalHost();
		String timeStart = date.toString(DateUtil.FORMAT_7);
		String timeExpire = date.plusHours(24).toString(DateUtil.FORMAT_7);
		// 商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
		String goodsTag = "";
		// 通知地址
		String notify_url = PropertiesUtil.getValue("wx_pay_notify_url");
		// 二维码中包含的商品ID或订单ID
		String product_id = outTradeNo;
		ScanPayUnitReqData scanPayReqData = new ScanPayUnitReqData(body, attach, outTradeNo, totalFee, deviceInfo,
				spBillCreateIP, timeStart, timeExpire, goodsTag, notify_url, product_id);
		return scanPayReqData;
	}

	private ScanPayUnitReqData buildScanPayUnitReqDataTest(ScanQrcodeReqData reqData) {
		DateTime date = DateTime.now();
		// 要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
		String body = "测试商品1";
		// 支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回
		String attach = "";
		// 商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
		String outTradeNo = reqData.getProduct_id();
		// 订单总金额，单位为“分”，只能整数
		int totalFee = 1;
		// 商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
		String deviceInfo = "gansu";
		// 订单生成的机器IP
		String spBillCreateIP = NetUtil.getLocalHost();
		String timeStart = date.toString(DateUtil.FORMAT_7);
		String timeExpire = date.plusHours(1).toString(DateUtil.FORMAT_7);
		// 商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
		String goodsTag = "";
		// 通知地址
		String notify_url = PropertiesUtil.getValue("wx_pay_notify_url");
		// 二维码中包含的商品ID或订单ID
		String product_id = reqData.getProduct_id();
		ScanPayUnitReqData scanPayReqData = new ScanPayUnitReqData(body, attach, outTradeNo, totalFee, deviceInfo,
				spBillCreateIP, timeStart, timeExpire, goodsTag, notify_url, product_id);
		return scanPayReqData;
	}

	public static void main(String[] args) {
		System.out.println(new WxpayService().generateQrcode("1212"));
		System.out.println(NetUtil.getLocalHost());
		DateTime date = DateTime.now();
		System.out.println(date.plusHours(1).toString(DateUtil.FORMAT_7));
		System.out.println(XmlUtil.objToXml(new ScanQrcodeResData("12122")));

	}
}
