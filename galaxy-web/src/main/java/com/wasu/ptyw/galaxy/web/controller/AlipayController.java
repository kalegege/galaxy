package com.wasu.ptyw.galaxy.web.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.RequestUtil;
import com.wasu.ptyw.galaxy.core.ao.GalaxyOrderFilmAO;
import com.wasu.ptyw.galaxy.core.api.AlipayService;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;

/**
 * 支付宝接口控制类
 * 
 * @author wenguang
 * @date 2015年06月26日
 */
@Controller
@RequestMapping("/alipay")
@Slf4j
public class AlipayController extends BaseController {
	@Resource
	AlipayService alipayService;
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;
	
	/**
	 * 支付回调
	 */
	@RequestMapping(value = "/callback/pay")
	public ModelAndView callbackPay(HttpServletRequest request, ModelMap model) {
		// 解析请求参数
		Map<String, String> params = RequestUtil.getRequestParams(request);
		model.put("data", alipayService.callbackPay(params));
		log.info("callback respone :" + model.get("data"));
		return new ModelAndView("/common/data", model);
	}
	
	/**
	 * 查询订单
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query(@RequestParam("order_id") long orderId, ModelMap model) {
		return alipayService.query(orderId);
	}
	
	/**
	 * 取消订单
	 */
	@RequestMapping(value = "/cancel")
	public ModelAndView cancel(@RequestParam("order_id") long orderId, ModelMap model) {
		Result<GalaxyOrderFilmDO> result = galaxyOrderFilmAO.getById(orderId);
		if (result.isSuccess()) {
			model.put("data", alipayService.cancel(result.getValue()));
		}
		return new ModelAndView("/common/data", model);
	}

	/**
	 * 支付回调
	 */
	@RequestMapping(value = "/callback/notify")
	public ModelAndView notify(HttpServletRequest request, ModelMap model) {
		// 解析请求参数
		Map<String, String> params = RequestUtil.getRequestParams(request);
		
		params.put("total_amount", "0.01");
		params.put("trade_no", "2015070221001004170092796580");
		params.put("notify_time", "2015-07-02 19:24:24");
		params.put("open_id", "20880036776777143689603451719217");
		params.put("subject", "测试商品1");
		params.put("sign_type", "RSA");
		params.put("buyer_logon_id", "zz6@qq.com");
		params.put("notify_type", "trade_status_sync");
		
		params.put("invoice_amount", "0.01");
		params.put("out_trade_no", "TV_13");
		params.put("gmt_payment", "2015-07-02 16:00:07");
		params.put("trade_status", "TRADE_SUCCESS");
		params.put("point_amount", "0.00");
		params.put("sign", "CSY0J3tJ2uhsWPzuFMf2iuLVfUh8hEyTjSsr1bH3jCm7AyUhM1RIxK+lj7e7iwmQBCzRCiypfqvteZI1sJEOLYospyTplVkFP4wVeYiEfihUeA8ogUQmgf7Axz22uo+HDlE/UCYbP++9T4GjJc3OO4GrkS/S4mE5JPxCn0KSdZs=");
		params.put("gmt_create", "2015-07-02 15:59:20");
		params.put("buyer_pay_amount", "0.01");
		params.put("receipt_amount", "0.01");
		params.put("fund_bill_list", "[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}]");
		
		params.put("app_id", "2015070200153623");
		params.put("seller_id", "2088021074217932");
		params.put("notify_id", "8d64ea2b29d14330f6779dd8b591b8bhb8");
		params.put("seller_email", "dsyxzhifubao@163.com");
		model.put("data", alipayService.callbackPay(params));
		log.info("callback respone :" + model.get("data"));
		return new ModelAndView("/common/data", model);
	}

}
