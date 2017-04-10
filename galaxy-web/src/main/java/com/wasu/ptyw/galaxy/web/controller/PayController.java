package com.wasu.ptyw.galaxy.web.controller;

import java.io.OutputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.core.ao.PayAO;
import com.wasu.ptyw.galaxy.core.util.QrcodeUtil;
import com.wasu.ptyw.galaxy.dal.constant.PayChannel;

/**
 * 微信接口控制类
 * 
 * @author wenguang
 * @date 2015年06月05日
 */
@Controller
@RequestMapping("/pay")
public class PayController extends BaseController {
	@Resource
	PayAO payAO;

	/**
	 * 获取二维码
	 */
	@RequestMapping(value = "/qrcode")
	@ResponseBody
	public void qrcode(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		try {
			int payChannel = NumberUtils.toInt(request.getParameter("pay_channel"), PayChannel.WEIXIN.getCode());
			long orderId = NumberUtils.toLong(request.getParameter("order_id"));
			response.setContentType("image/png");
			OutputStream outStream = response.getOutputStream();
			Result<String> result = payAO.getQrcodeUrl(orderId, payChannel);
			if (result.isSuccess()) {
				QrcodeUtil.build(result.getValue(), outStream, 300, 300);// 实际生成的图片会比240大点
			}
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 查询支付状态
	 */
	@RequestMapping(value = "/status")
	@ResponseBody
	public Object status(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		try {
			int payChannel = NumberUtils.toInt(request.getParameter("pay_channel"), PayChannel.WEIXIN.getCode());
			long orderId = NumberUtils.toLong(request.getParameter("order_id"));
			Result<Boolean> result = payAO.isPaySuccess(orderId, payChannel);
			Map<String, Object> json = dealResult(result);
			if (result.isSuccess()) {
				json.put("pay_success", result.getValue());
			}
			return dealJsonp(request, json);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 查询支付状态
	 */
	@RequestMapping(value = "/notify")
	@ResponseBody
	public Object notify(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		try {

			return "success";
		} catch (Exception e) {
		}
		return "false";
	}
}
