package com.wasu.ptyw.galaxy.core.api.wxprotocol;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import com.tencent.common.Configure;
import com.tencent.common.Signature;
import com.wasu.ptyw.galaxy.core.util.XmlUtil;

/**
 * User: rizenguo
 * Date: 2014/10/22
 * Time: 16:42
 */

/**
 * 被扫支付提交Post数据给到API之后，API会返回XML格式的数据，这个类用来装这些数据
 */
public class ScanQrcodeResData {
	// 协议层
	private String return_code = "";
	private String return_msg = "";

	// 协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
	private String appid = "";
	private String mch_id = "";
	private String nonce_str = "";
	private String result_code = "";
	private String err_code_des = "";
	private String sign = "";

	// 业务返回的具体数据（以下字段在return_code 和result_code 都为SUCCESS 的时候有返回）
	private String prepay_id = "";// 预支付ID,调用统一下单接口生成的预支付ID

	public ScanQrcodeResData() {

	}

	public ScanQrcodeResData(String prepayId) {
		setReturn_code(WxConstant.SUCCESS);
		setResult_code(WxConstant.SUCCESS);

		// 微信分配的公众号ID（开通公众号之后可以获取到）
		setAppid(Configure.getAppid());

		// 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		setMch_id(Configure.getMchid());

		// 随机字符串，不长于32 位
		setNonce_str(RandomStringUtils.random(20, true, true));

		// 预支付ID,调用统一下单接口生成的预支付ID
		setPrepay_id(prepayId);

		// 根据API给的签名规则进行签名
		String sign = Signature.getSign(toMap());
		setSign(sign);// 把签名数据设置到Sign这个属性中
	}

	public static String getFail(String msg) {
		ScanQrcodeResData data = new ScanQrcodeResData();
		data.setReturn_code(WxConstant.FAIL);
		data.setReturn_msg(msg);
		return XmlUtil.objToXml(data);
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
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

}
