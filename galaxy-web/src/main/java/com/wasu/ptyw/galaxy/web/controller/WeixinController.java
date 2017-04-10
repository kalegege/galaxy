package com.wasu.ptyw.galaxy.web.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.popular.util.SignatureUtil;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.PropertiesUtil;
import com.wasu.ptyw.galaxy.core.ao.GalaxyOrderFilmAO;
import com.wasu.ptyw.galaxy.core.api.WxFollowService;
import com.wasu.ptyw.galaxy.core.api.WxpayService;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;

/**
 * 微信接口控制类
 * 
 * @author wenguang
 * @date 2015年06月05日
 */
@Controller
@RequestMapping("/wx")
public class WeixinController extends BaseController {
	@Resource
	WxpayService wxpayService;
	@Resource
	WxFollowService wxFollowService;
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;

	String token;// 微信配置的令牌
	String view = "/common/data";

	@PostConstruct
	public void init() {
		this.token = PropertiesUtil.getValue("wx_token");
	}

	/**
	 * 微信回调，微信配置用到/微信关注等
	 */
	@RequestMapping(value = "/callback")
	public ModelAndView callbac(HttpServletRequest request, ModelMap model) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		String skipsign = request.getParameter("skipsign");
		// 首次请求申请验证,返回echostr
		if (StringUtils.isNotEmpty(echostr)) {
			model.put("data", echostr);
			return new ModelAndView(view, model);
		}

		if (!"true".equals(skipsign)
				&& (StringUtils.isEmpty(signature) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce))) {
			model.put("data", "sign params is null");
			return new ModelAndView(view, model);
		}

		// 验证请求签名
		if (!"true".equals(skipsign)
				&& !signature.equals(SignatureUtil.generateEventMessageSignature(token, timestamp, nonce))) {
			model.put("data", "sign is wrong");
			return new ModelAndView(view, model);
		}
		String xml = null;
		try {
			xml = IOUtils.toString(request.getInputStream(), "UTF-8");
		} catch (IOException e) {
		}
		model.put("data", wxFollowService.callbackFollow(xml));
		return new ModelAndView(view, model);
	}

	/**
	 * 微信支付回调
	 */
	@RequestMapping(value = "/callback/pay")
	public ModelAndView callbackPay(HttpServletRequest request, ModelMap model) {
		String xml = null;
		try {
			xml = IOUtils.toString(request.getInputStream(), "UTF-8");
		} catch (IOException e) {
		}
		model.put("data", wxpayService.callbackPay(xml));
		return new ModelAndView("/common/data", model);
	}

	/**
	 * 查询订单
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query(@RequestParam("order_id") long orderId, ModelMap model) {
		return wxpayService.query(orderId);
	}

	/**
	 * 取消订单
	 */
	@RequestMapping(value = "/cancel")
	public ModelAndView cancel(@RequestParam("order_id") long orderId, ModelMap model) {
		Result<GalaxyOrderFilmDO> result = galaxyOrderFilmAO.getById(orderId);
		if (result.isSuccess()) {
			model.put("data", wxpayService.cancel(result.getValue()));
		}
		return new ModelAndView("/common/data", model);
	}

	/**
	 * 微信二维码扫描回调
	 * 
	 * @deprecated 直接调用预下单接口生成订单了，不再用这个
	 */
	@RequestMapping(value = "/callback/qrcode")
	public ModelAndView callbackQrcode(HttpServletRequest request, ModelMap model) {
		String xml = null;
		try {
			xml = IOUtils.toString(request.getInputStream(), "UTF-8");
		} catch (IOException e) {
		}
		model.put("data", wxpayService.callbackQrcode(xml));
		return new ModelAndView("/common/data", model);
	}

	/**
	 * 微信关注回调
	 * 
	 * @Deprecated {参照/callback方法}
	 */
	@RequestMapping(value = "/callback/follow")
	@Deprecated
	public ModelAndView follow(HttpServletRequest request, ModelMap model) {
		String xml = null;
		try {
			xml = IOUtils.toString(request.getInputStream(), "UTF-8");
		} catch (IOException e) {
		}
		model.put("data", wxFollowService.callbackFollow(xml));
		return new ModelAndView("/common/data", model);
	}

}
