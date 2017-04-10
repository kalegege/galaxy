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

/**
 * 家银通支付API返回的数据
 */
@Data
public class WasupayResData {
	/*支付返回*/
	private String successful = "";// 成功标志，SUCCESS/FAIL,如果为FAIL，则有可能不包含MD5校验值
	private String errorMsg = "";// 错误信息
	private String qrcode = "";// 扫码支付的二维码地址

	/*通知返回*/
	private String tradeNo = "";// 家银通订单号
	private String busiSeqNo = "";// 第三方订单号
	private String errorCode = "";// 异常编码
	private String productName = "";// 商品名称,urldecode后的
	private String tradeMoney = ""; // 支付金额,单位为分
	private String terminalId = "";// 终端设备号
	private String signType = "";// 签名方式,目前只支付MD5方式
	private String signValue = "";// 签名

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
