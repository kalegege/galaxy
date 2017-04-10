/**
 * 
 */
package com.wasu.ptyw.galaxy.core.api;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.http.LocalHttpRequest;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.common.util.PropertiesUtil;
import com.wasu.ptyw.galaxy.core.ao.GalaxyOrderFilmAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyWeixinFollowAO;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.BaseResData;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.WasupayResData;
import com.wasu.ptyw.galaxy.core.cache.LocalCache;
import com.wasu.ptyw.galaxy.core.manager.GalaxyOrderFilmManager;
import com.wasu.ptyw.galaxy.core.manager.GalaxyUserManager;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.constant.PayChannel;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;

/**
 * 华数家银通接口
 * 
 * @author wenguang
 * @date 2015年12月7日
 */
@Service("wasupayService")
@Slf4j
public class WasupayService extends BaseOutService {
	@Resource
	GalaxyUserManager galaxyUserManager;
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;
	@Resource
	WxpayService wxpayService;
	@Resource
	GalaxyWeixinFollowAO galaxyWeixinFollowAO;

	@Resource
	GalaxyOrderFilmManager galaxyOrderFilmManager;

	String wasupayUrl;
	String wasupayToken;
	String wasupayNotifyUrl;

	@PostConstruct
	public void init() {
		wasupayUrl = PropertiesUtil.getValue("wasupay_url");
		wasupayToken = PropertiesUtil.getValue("wasupay_token");
		wasupayNotifyUrl = PropertiesUtil.getValue("wasupay_notify_url");
	}

	/**
	 * 预下单
	 */
	public String prePay(long orderId, GalaxyOrderFilmDO order) {
		String qrcodeUrl = "";
		if (order == null) {
			Result<GalaxyOrderFilmDO> result = galaxyOrderFilmAO.getById(orderId);
			if (!result.isSuccess()) {
				return qrcodeUrl;
			}
			order = result.getValue();
		}

		if (OrderFilmStatus.isEnd(order.getStatus())) {
			return qrcodeUrl;
		}

		Map<String, String> params = buildParams(order);
		WasupayResData resData = null;
		try {
			String s = LocalHttpRequest.post(wasupayUrl, params);
			if (StringUtils.isEmpty(s))
				return qrcodeUrl;
			log.info("prePay:" + s);
			resData = JSON.parseObject(s, WasupayResData.class);
			if (!"SUCCESS".equals(resData.getSuccessful())) {
				return qrcodeUrl;
			}
			return resData.getQrcode();
		} catch (Exception e) {
			log.error("WasupayService->prePay error", e);
		}
		if (StringUtils.isNotEmpty(qrcodeUrl)) {
			order.setStatus(OrderFilmStatus.PREPAY.getCode());
			order.setPayChannel(PayChannel.WASUPAY.getCode());
			galaxyOrderFilmAO.update(order);
			if (StringUtils.isNotEmpty(order.getFeature(DbContant.FEA_WX_FOLLOW))) {
				List<Long> ids = Lists.newArrayList(NumberUtils.toLong(order.getFeature(DbContant.FEA_WX_FOLLOW)));
				galaxyWeixinFollowAO.updateUsedStatusByIds(ids, DbContant.TWO);
			}
		}
		return qrcodeUrl;
	}

	/**
	 * 支付回调处理
	 */
	public String callbackPay(WasupayResData resData) {
		try {
			if (resData == null) {
				log.info("callbackPay data is null");
				return getFail("param is null");
			}
			log.info("callbackPay , resData:" + resData);
			if (!"SUCCESS".equalsIgnoreCase(resData.getSuccessful())) {
				return getFail("successful is not SUCCESS");
			}

			if (StringUtils.isEmpty(resData.getBusiSeqNo())) {
				return getFail("busiSeqNo is null");
			}

			checkSign(resData);

			GalaxyOrderFilmDO order = galaxyOrderFilmManager.getById(Long.parseLong(resData.getBusiSeqNo()));
			if (order == null) {
				return getFail("order not found");
			}

			order.setPayChannel(PayChannel.WASUPAY.getCode());
			order.setPayTradeNo(resData.getTradeNo());
			order.setPayTimeEnd(DateUtil.now(DateUtil.FORMAT_7));
			order.setPayTotalFee(order.getTotalPrice());
			order.setStatus(OrderFilmStatus.SUCCESS.getCode());
			order.setExpiredDate(LogicUtil.getOrderExpiredTime());
			galaxyOrderFilmManager.update(order);

			// 增加抽奖次数
			GalaxyUserDO user = galaxyUserManager.getById(order.getUserId());
			if (user != null) {
				int count = NumberUtils.toInt(user.getFeature(DbContant.FEA_DISCOUNT_COUNT)) + 1;
				user.putFeature(DbContant.FEA_DISCOUNT_COUNT, count + "");
				galaxyUserManager.update(user);
			}
			return getSuccess();
		} catch (Exception e) {
			log.error("callbackPay error,", e);
			return BaseResData.getFail("系统异常");
		}
	}

	/**
	 * 关闭订单接口，失败时返回null <br>
	 * 目前不处理各种异常情况
	 */
	public boolean cancel(GalaxyOrderFilmDO order) {
		JSONObject resData = null;
		try {
			Map<String, String> params = Maps.newHashMap();
			params.put("merchantId", getMerchantId(order.getRegionCode()));// 商户号
			params.put("serviceName", "closeTrade");// 接口名称
			params.put("busiSeqNo", order.getId() + "");// 第三方订单号
			params.put("_inputCharset", "UTF-8");// 字符集,目前只支持UTF-8
			
			params.put("signValue", getSign(params));// 签名
			params.put("signType", "MD5");// 签名方式

			String s = LocalHttpRequest.post(wasupayUrl, params);
			
			if (StringUtils.isEmpty(s))
				return false;
			log.info("cancel:" + s);
			s = URLDecoder.decode(s, "UTF-8");
			
			resData = JSON.parseObject(s);
			if (!"0".equals(resData.getString("rltCode"))) {
				log.warn("cancel wasupay order error,errorMsg:" + resData.getString("rltMSG"));
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("closeOrder error,orderId=" + (order != null ? order.getId() : ""), e);
		}
		return false;
	}

	private Map<String, String> buildParams(GalaxyOrderFilmDO order) {
		Map<String, String> params = Maps.newHashMap();
		params.put("serviceName", "getQrcode");// 接口名称
		params.put("returnURL", wasupayNotifyUrl);// 支付结果通知路径

		params.put("merchantId", getMerchantId(order.getRegionCode()));// 商户号
		params.put("terminalId", order.getOutUserId());// 终端设备号
		params.put("userId", order.getUserId() + "");// 用户编号
		params.put("payDeskMode", "13");// 微信、支付宝统一支付收银台标志固定13
		params.put("entranceId", "9001");// 家银通入口标识, 彩虹院线：9001
		
		

		// TODO
		params.put("epgCode", "");// 机顶盒的区域编码，如果不是通过机顶盒登录，无需传入
		params.put("areaId", order.getRegionCode());// 用户区域编码， 商户接入时由家银通提供
		params.put("userIdType", "2");// 用户编号类型,彩虹院线账号类型固定为2,此数据由用户接入时由家银通提供

		params.put("productName", order.getContName());// 商品名称
		params.put("tradeMoney", order.getTotalPrice() + "");// 支付金额,单位为分
		params.put("busiSeqNo", order.getId() + "");// 第三方订单号

		params.put("billType", "0");// 交易类型， 0：缴费

		params.put("accountNo", "");// 商户在家银通平台注册的账号，可以不传
		params.put("hdFlag", "0");// 高标清, 彩虹院线固定为0，高清
		params.put("terminalChannel", "1000");// 渠道号,目前固定1000
		params.put("_inputCharset", "UTF-8");// 字符集,目前只支持UTF-8
		params.put("busiBody", "");// Json格式,传递个性化的业务信息，比如点播资产信息等

		params.put("signValue", getSign(params));// 签名
		params.put("productName", encode(order.getContName(), "UTF-8"));// 商品名称
		params.put("signType", "MD5");// 签名方式

		return params;
	}

	private String encode(String s, String enc) {
		try {
			return URLEncoder.encode(s, enc);
		} catch (Exception e) {
			return s;
		}
	}

	public String getSign(Map<String, String> map) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue() != "") {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result = result.substring(0, result.length() - 1) + wasupayToken;
		log.warn("md5Hex before:" + result);
		// tr84kc6xpi0v0c9v3m44sonw6meprcu5
		result = DigestUtils.md5Hex(result).toLowerCase();
		log.warn("md5Hex after:" + result);
		return result;
	}

	private boolean checkSign(WasupayResData resData) {
		Map<String, String> params = Maps.newHashMap();
		params.put("successful", resData.getSuccessful());
		params.put("tradeNo", resData.getTradeNo());
		params.put("busiSeqNo", resData.getBusiSeqNo());
		params.put("tradeMoney", resData.getTradeMoney());
		params.put("terminalId", resData.getTerminalId());
		params.put("errorCode", resData.getErrorCode());
		String sign = getSign(params);
		log.warn("checkSign-->sign:" + sign + ",param:" + params);
		return StringUtils.equals(resData.getSignValue(), sign);
	}

	private String getMerchantId(String regionCode) {
		String merchantId = "01010115071500002";
		try {
			String s = LocalCache.get("merchant_ids");
			JSONObject json = JSON.parseObject(s);
			merchantId = json.getString(regionCode);
		} catch (Exception e) {
		}
		return merchantId;
	}

	private String getFail(String msg) {
		JSONObject json = new JSONObject();
		json.put("successful", "FAIL");
		json.put("returnMsg", msg);
		return json.toJSONString();
	}

	private String getSuccess() {
		JSONObject json = new JSONObject();
		json.put("successful", "SUCCESS");
		json.put("returnMsg", "OK");
		return json.toJSONString();
	}

	public static void main(String[] args) {
		System.out.println(DigestUtils.md5Hex("abc").toLowerCase());
	}
}
