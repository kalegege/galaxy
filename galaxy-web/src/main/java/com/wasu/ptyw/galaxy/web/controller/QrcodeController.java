package com.wasu.ptyw.galaxy.web.controller;

import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.common.util.PropertiesUtil;
import com.wasu.ptyw.galaxy.common.util.StringUtil;
import com.wasu.ptyw.galaxy.core.ao.GalaxyUserAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyWeixinFollowAO;
import com.wasu.ptyw.galaxy.core.ao.PayAO;
import com.wasu.ptyw.galaxy.core.api.WxFollowService;
import com.wasu.ptyw.galaxy.core.util.QrcodeUtil;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.PayChannel;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;

/**
 * 生成二维码图片
 * 
 * @author wenguang
 * @date 2015年09月15日
 */
@Controller
@RequestMapping("/qrcode")
public class QrcodeController extends BaseController {
	@Resource
	WxFollowService wxFollowService;
	@Resource
	GalaxyWeixinFollowAO galaxyWeixinFollowAO;
	@Resource
	GalaxyUserAO galaxyUserAO;
	@Resource
	PayAO payAO;

	/**
	 * 获取支付二维码
	 */
	@RequestMapping(value = "/pay")
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
	 * 根据内容生成二维码
	 */
	@RequestMapping(value = "")
	@ResponseBody
	public void qrcode(HttpServletRequest request, HttpServletResponse response) {
		try {
			String content = request.getParameter("content");
			if (StringUtil.isEmpty(content)) {
				return;
			}
			int width = NumberUtils.toInt(request.getParameter("width"), QrcodeUtil.WIDTH);
			int height = NumberUtils.toInt(request.getParameter("height"), QrcodeUtil.HEIGHT);

			response.setContentType("image/png");
			OutputStream outStream = response.getOutputStream();

			QrcodeUtil.build(content, outStream, width, height);// 实际生成的图片会比240大点

			outStream.flush();
			outStream.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 生成购票二维码
	 */
	@RequestMapping(value = "/buy")
	@ResponseBody
	public void buy(HttpServletRequest request, HttpServletResponse response) {
		try {
			String qrcodeUrl = null;
			// 微信购票
			String id = request.getParameter("wx_id");
			if (StringUtils.isNotEmpty(id)) {
				qrcodeUrl = PropertiesUtil.getValue("qrcode_film_wx") + id;
			}

			if (StringUtils.isEmpty(qrcodeUrl)) {
				// 淘宝购票
				id = request.getParameter("tb_id");
				if (StringUtils.isEmpty(id)) {
					return;
				}
				qrcodeUrl = PropertiesUtil.getValue("qrcode_film_tb") + id;
			}

			int width = NumberUtils.toInt(request.getParameter("width"), QrcodeUtil.WIDTH);
			int height = NumberUtils.toInt(request.getParameter("height"), QrcodeUtil.HEIGHT);

			response.setContentType("image/png");
			OutputStream outStream = response.getOutputStream();

			QrcodeUtil.build(qrcodeUrl, outStream, width, height);// 实际生成的图片会比240大点

			outStream.flush();
			outStream.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 生成微信关注二维码
	 */
	@RequestMapping(value = "/wx")
	@ResponseBody
	public void qrcodeWx(HttpServletRequest request, HttpServletResponse response) {
		try {
			Result<GalaxyUserDO> result = galaxyUserAO.queryWithSave(buildUser(request));
			if (!result.isSuccess()) {
				return;
			}
			GalaxyUserDO user = result.getValue();
			String qrcodeUrl = user.getFeature(DbContant.FEA_WX_QRCODE);
			String qrcodeTime = user.getFeature(DbContant.FEA_WX_QRCODE_TIME);
			if (StringUtil.isEmpty(qrcodeUrl)
					|| DateUtil.expire(NumberUtils.toLong(qrcodeTime), DateUtil.DAY_6 - 10000)) {
				qrcodeUrl = wxFollowService.getQrcodeUrl(user.getId());
				if (StringUtil.isEmpty(qrcodeUrl)) {
					return;
				}
				user.putFeature(DbContant.FEA_WX_QRCODE, qrcodeUrl);
				user.putFeature(DbContant.FEA_WX_QRCODE_TIME, System.currentTimeMillis() + "");
				galaxyUserAO.update(user);
			}

			int width = NumberUtils.toInt(request.getParameter("width"), QrcodeUtil.WIDTH);
			int height = NumberUtils.toInt(request.getParameter("height"), QrcodeUtil.HEIGHT);

			response.setContentType("image/png");
			OutputStream outStream = response.getOutputStream();

			QrcodeUtil.build(qrcodeUrl, outStream, width, height);// 实际生成的图片会比240大点

			outStream.flush();
			outStream.close();
		} catch (Exception e) {

		}
	}
}
