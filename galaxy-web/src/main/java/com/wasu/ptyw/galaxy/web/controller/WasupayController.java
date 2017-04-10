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
import com.wasu.ptyw.galaxy.core.api.WasupayService;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.WasupayResData;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;

/**
 * 华数家银通接口控制类
 * 
 * @author wenguang
 * @date 2015年12月06日
 */
@Controller
@RequestMapping("/wasupay")
@Slf4j
public class WasupayController extends BaseController {
	@Resource
	WasupayService wasupayService;
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;

	/**
	 * 支付回调
	 */
	@RequestMapping(value = "/callback/pay")
	public ModelAndView callbackPay(HttpServletRequest request, ModelMap model, WasupayResData resData) {
		// 解析请求参数
		model.put("data", wasupayService.callbackPay(resData));
		log.info("callback respone :" + model.get("data"));
		return new ModelAndView("/common/data", model);
	}

	/**
	 * 取消订单
	 */
	@RequestMapping(value = "/cancel")
	public ModelAndView cancel(@RequestParam("order_id") long orderId, ModelMap model) {
		Result<GalaxyOrderFilmDO> result = galaxyOrderFilmAO.getById(orderId);
		if (result.isSuccess()) {
			model.put("data", wasupayService.cancel(result.getValue()));
		}
		return new ModelAndView("/common/data", model);
	}

}
