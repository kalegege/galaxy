/**
 * 
 */
package com.wasu.ptyw.galaxy.core.api;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.common.util.PropertiesUtil;
import com.wasu.ptyw.galaxy.core.ao.GalaxyOrderFilmAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyWeixinFollowAO;
import com.wasu.ptyw.galaxy.core.manager.GalaxyUserManager;
import com.wasu.ptyw.galaxy.core.timetask.AsyncWorker;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.constant.PayChannel;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;

/**
 * 支付宝服务
 * 
 * @author wenguang
 * @date 2015年6月18日
 */
@Service("alipayService")
@Slf4j
public class AlipayService extends BaseOutService {
	public static String SIGN_CHARSET = "UTF-8";
	public static String SUCCESS = "success";

	public static String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";// 交易创建，等待买家付款
	public static String TRADE_CLOSED = "TRADE_CLOSED";// 未付款交易超时关闭,支付完成后全额退款
	public static String TRADE_SUCCESS = "TRADE_SUCCESS";// 交易支付成功
	public static String TRADE_FINISHED = "TRADE_FINISHED";// 交易结束，不可退款

	/** API调用客户端 */
	AlipayClient alipayClient;

	String publicKey;

	@Resource
	GalaxyUserManager galaxyUserManager;
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;
	@Resource
	WxpayService wxpayService;
	@Resource
	GalaxyWeixinFollowAO galaxyWeixinFollowAO;
	@Resource
	AsyncWorker asyncWorker;

	@PostConstruct
	public void init() {
		String gateway = PropertiesUtil.getValue("alipay_gateway");
		String appID = PropertiesUtil.getValue("alipay_appid");
		String privateKey = PropertiesUtil.getValue("alipay_my_private_key");
		publicKey = PropertiesUtil.getValue("alipay_public_key");
		alipayClient = new DefaultAlipayClient(gateway, appID, privateKey, "json", SIGN_CHARSET, publicKey);
	}

	/**
	 * 预下单
	 */
	public String prePay(long orderId) {
		String qrcodeUrl = "";
		GalaxyOrderFilmDO order = null;
		boolean isDoing = false;
		do {
			Result<GalaxyOrderFilmDO> result = galaxyOrderFilmAO.getById(orderId);
			if (!result.isSuccess()) {
				return qrcodeUrl;
			}
			order = result.getValue();
			if (OrderFilmStatus.isEnd(order.getStatus())) {
				return qrcodeUrl;
			}
			isDoing = order.isExist(DbContant.FEA_DOING);
			if (isDoing) {
				sleep(100);
			}
		} while (isDoing);// 避免支付宝微信重复

		order.putFeature(DbContant.FEA_DOING, DbContant.YES);
		galaxyOrderFilmAO.update(order);

		String content = buildContent(order);

		// 使用SDK，构建群发请求模型
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizContent(content);
		request.setNotifyUrl(PropertiesUtil.getValue("alipay_notify_url"));
		AlipayTradePrecreateResponse response = null;
		try {
			// 使用SDK，调用交易下单接口
			response = alipayClient.execute(request);

			log.info("request content:" + content + ", response isSuccess:" + response.isSuccess() + ", body:"
					+ response.getBody() + ", msg:" + response.getMsg() + ", qrcode:" + response.getQrCode());

			if (null != response && response.isSuccess()) {
				if (response.getCode().equals("10000")) {
					qrcodeUrl = response.getQrCode();
				} else {
					log.warn("response error, SubCode:" + response.getSubCode() + ", SubMsg:" + response.getSubMsg());
				}
			}

		} catch (AlipayApiException e) {
			log.error("prePay AlipayApiException", e);
		}
		if (StringUtils.isNotEmpty(qrcodeUrl)) {
			order.setStatus(OrderFilmStatus.PREPAY.getCode());
			order.setPayChannel(PayChannel.ALIPAY.getCode());
			order.putFeature(DbContant.FEA_PREPAY_ALI, DbContant.YES);

			if (StringUtils.isNotEmpty(order.getFeature(DbContant.FEA_WX_FOLLOW))) {
				List<Long> ids = Lists.newArrayList(NumberUtils.toLong(order.getFeature(DbContant.FEA_WX_FOLLOW)));
				galaxyWeixinFollowAO.updateUsedStatusByIds(ids, DbContant.TWO);
			}
		}
		order.putFeature(DbContant.FEA_DOING, DbContant.NO);
		galaxyOrderFilmAO.update(order);
		return qrcodeUrl;
	}

	/**
	 * 查询订单
	 */
	public AlipayTradeQueryResponse query(long orderId) {
		Result<GalaxyOrderFilmDO> result = galaxyOrderFilmAO.getById(orderId);
		if (!result.isSuccess()) {
			return null;
		}
		JSONObject json = new JSONObject();
		json.put("out_trade_no", buildOutTradeNo(result.getValue()));
		String content = json.toString();
		// 使用SDK，构建群发请求模型
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizContent(content);
		AlipayTradeQueryResponse response = null;
		try {
			// 使用SDK，调用交易下单接口
			response = alipayClient.execute(request);

			log.info("request content:" + content + ", response isSuccess:" + response.isSuccess() + ", body:"
					+ response.getBody() + ", msg:" + response.getMsg());

			if (null != response && response.isSuccess()) {
				if (response.getCode().equals("10000")) {
					return response;
				} else {
					log.warn("response error, SubCode:" + response.getSubCode() + ", SubMsg:" + response.getSubMsg());
				}
			}
		} catch (AlipayApiException e) {
			log.error("query AlipayApiException", e);
		}
		return null;
	}

	/**
	 * 撤销订单
	 */
	public boolean cancel(GalaxyOrderFilmDO order) {
		JSONObject json = new JSONObject();
		json.put("out_trade_no", buildOutTradeNo(order));
		String content = json.toString();
		// 使用SDK，构建群发请求模型
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizContent(content);
		AlipayTradeCancelResponse response = null;
		try {
			// 使用SDK，调用交易下单接口
			response = alipayClient.execute(request);

			log.info("request content:" + content + ", response isSuccess:" + response.isSuccess() + ", body:"
					+ response.getBody() + ", msg:" + response.getMsg());

			if (null != response && response.isSuccess()) {
				if (response.getCode().equals("10000")) {
					// response.getAction(); //close：直接撤销，无退款；refund：撤销，有退款。
					return true;
				} else {
					if (response.getCode().equals("40004")) {
						// response.getRetryFlag()
						// Y：可继续发起撤销请求；N：不可继续发起撤销请求，即后续的撤销请求也不会成功。
					}
					log.warn("response error, SubCode:" + response.getSubCode() + ", SubMsg:" + response.getSubMsg());
				}
			}
		} catch (AlipayApiException e) {
			log.error("cancel AlipayApiException", e);
		}
		return false;
	}

	/**
	 * 支付回调处理
	 */
	public String callbackPay(Map<String, String> params) {
		try {
			if (MapUtils.isEmpty(params)) {
				return "params is null";
			}
			log.info("callbackPay params=" + params.toString());

			params.remove("sign_type");
			if (!AlipaySignature.rsaCheckV2(params, publicKey, SIGN_CHARSET)) {
				return "verify sign fail.";
			}

			if (!"trade_status_sync".equals(params.get("notify_type"))) {
				return SUCCESS;
			}
			String outTradeNo = params.get("out_trade_no");

			Result<GalaxyOrderFilmDO> result = galaxyOrderFilmAO.getById(toOrderId(outTradeNo));
			if (!result.isSuccess()) {
				return "trade not found";
			}

			GalaxyUserDO user = null;
			GalaxyOrderFilmDO order = result.getValue();

			String tradeStatus = params.get("trade_status");
			if (TRADE_SUCCESS.equals(tradeStatus)) {
				// 成功了关闭微信订单
				if (OrderFilmStatus.isPrepay(order.getStatus())
						&& DbContant.YES.equals(order.getFeature(DbContant.FEA_PREPAY_WX))) {
					boolean closeFlg = wxpayService.cancel(order);
					log.warn("close weixin order, order_id=" + order.getId() + "result:" + closeFlg);
				}
				order.setPayChannel(PayChannel.ALIPAY.getCode());
				order.setPayTradeNo(params.get("trade_no"));
				order.setPayOpenid(params.get("open_id"));
				order.setPayTotalFee((int) (NumberUtils.toDouble(params.get("total_amount")) * 100));
				order.setPayBuyerLogonId(params.get("buyer_logon_id"));
				order.setPayTimeEnd(getPaymentTime(params.get("gmt_payment")));
				order.setStatus(OrderFilmStatus.SUCCESS.getCode());
				order.setExpiredDate(LogicUtil.getOrderExpiredTime());
				galaxyOrderFilmAO.update(order);

				// 微信关注活动
				// if
				// (StringUtils.isNotEmpty(order.getFeature(DbContant.FEA_WX_FOLLOW)))
				// {
				// asyncWorker.wxFollowPaySuccess(order.getFeature(DbContant.FEA_WX_FOLLOW),
				// order.getUserId());
				// }

				// 增加抽奖次数
				user = galaxyUserManager.getById(order.getUserId());
				if (user != null) {
					int count = NumberUtils.toInt(user.getFeature(DbContant.FEA_DISCOUNT_COUNT)) + 1;
					user.putFeature(DbContant.FEA_DISCOUNT_COUNT, count + "");
				}
			} else if (TRADE_CLOSED.equals(tradeStatus)) {
				order.setStatus(OrderFilmStatus.CLOSED.getCode());
				galaxyOrderFilmAO.update(order);
			}
			if (StringUtils.isNotEmpty(order.getPayBuyerLogonId())) {
				if (user == null) {
					user = new GalaxyUserDO();
					user.setId(order.getUserId());
				}
				user.setBindAlipayUserId(order.getPayBuyerLogonId());
				user.setBindAlipayTime(DateUtil.now(DateUtil.FORMAT_7));
			}
			if (user != null) {
				galaxyUserManager.update(user);
			}
			return SUCCESS;
		} catch (Exception e) {
			log.error("callbackPay error,", e);
			return "Exception";
		}
	}

	private static String buildContent(GalaxyOrderFilmDO order) {
		String time_expire = DateTime.now().plusHours(24).toString(DateUtil.FORMAT_1);

		JSONObject json = new JSONObject();
		json.put("out_trade_no", buildOutTradeNo(order));
		json.put("total_amount", order.getTotalPrice() / 100.0);
		json.put("subject", order.getContName());
		// json.put("body", "test");
		json.put("terminal_id", order.getRegionCode());
		json.put("time_expire", time_expire);
		return json.toString();
	}

	private String getPaymentTime(String gmtPayment) {
		try {
			if (StringUtils.isNotEmpty(gmtPayment)) {
				return DateUtil.convert(gmtPayment, DateUtil.FORMAT_1, DateUtil.FORMAT_7);
			}
		} catch (Exception e) {

		}
		return DateUtil.now(DateUtil.FORMAT_7);
	}

	public static void main(String[] args) {
		System.out.println(10 / 100.0);
		buildContent(null);
	}
}
