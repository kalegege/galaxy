package com.wasu.ptyw.galaxy.core.api.wxprotocol;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import lombok.Data;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.Lists;
import com.tencent.common.Configure;
import com.tencent.common.MD5;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;

/**
 * 家银通支付API需要提交的数据
 */
@Data
public class WasupayReqData {
	// 每个字段具体的意思请查看API文档
	private String serviceName = "getQrcode";// 接口名称
	private String signType = "MD5";// 签名方式
	// TODO
	private String merchantId = "01010115071500002";// 商户号

	// TODO
	private String returnURL = "";// 支付结果通知路径
	private String extraCommonParams = "";// 公共回传参数
	private String payType = "";// 支付方式,ali:支付宝， weixin:微信
	private String signValue = "";// 签名

	private String terminalId = "";// 终端设备号
	private String areaId = "";// 商户号
	private String userId = "";// 用户编号
	private String productName = "";// 商品名称
	private String tradeMoney = ""; // 支付金额,单位为分
	private String busiSeqNo = "";// 第三方订单号

	private String userIdType = "2";// 用户编号类型,彩虹院线账号类型固定为2
	private String payDeskMode = "10";// 收银台类型,彩虹院线：10
	private String billType = "0";// 交易类型， 0：缴费
	private String hdFlag = "0";// 高标清, 彩虹院线固定为0，高清
	private String terminalChannel = "100";// 渠道号,目前固定1000
	private String _inputCharset = "UTF-8";// 字符集,目前只支持UTF-8
	private String accountNo = "";// 商户在家银通平台注册的账号，可以不传

	public WasupayReqData(GalaxyOrderFilmDO order) {
		setTerminalChannel(order.getOutUserId());
		setAreaId(order.getRegionCode());
		setUserId(order.getUserId() + "");
		setProductName(order.getContName());
		setTradeMoney(order.getTotalPrice() + "");
		setBusiSeqNo(order.getId() + "");
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public String getSign2(Map<String, Object> map) {
		LinkedList<String> list = Lists.newLinkedList();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					list.add(field.getName() + "=" + obj + "&");
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		int size = list.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(list.get(i));
		}
		String result = sb.toString();
		result = DigestUtils.md5Hex(result);
		return result;
	}

	public String getSign(Map<String, Object> map) {
		LinkedList<String> lists = Lists.newLinkedList();
		ArrayList<String> list = new ArrayList<String>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					list.add(field.getName() + "=" + obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		for (Map.Entry<String, Object> entry : map.entrySet()) {
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
		result += "key=" + Configure.getKey();
		// Util.log("Sign Before MD5:" + result);
		result = MD5.MD5Encode(result).toUpperCase();
		// Util.log("Sign Result:" + result);
		return result;
	}
}
