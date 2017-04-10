package com.wasu.ptyw.galaxy.web.controller;

import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.core.ao.AuthAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyDiscountAccessAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyUserAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyWeixinFollowAO;
import com.wasu.ptyw.galaxy.core.ao.PayAO;
import com.wasu.ptyw.galaxy.core.dto.DiscountDTO;
import com.wasu.ptyw.galaxy.core.timetask.AsyncWorker;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.FollowStatus;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.constant.PayChannel;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;
import com.wasu.ptyw.galaxy.dal.query.GalaxyWeixinFollowQuery;

/**
 * 权限校验控制类
 * 
 * @author wenguang
 * @date 2015年06月25日
 */
@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController extends BaseController {
	@Resource
	AuthAO authAO;
	@Resource
	PayAO payAO;
	@Resource
	GalaxyWeixinFollowAO galaxyWeixinFollowAO;
	@Resource
	AsyncWorker asyncWorker;
	@Resource
	GalaxyUserAO galaxyUserAO;
	@Resource
	GalaxyDiscountAccessAO galaxyDiscountAccessAO;

	@RequestMapping(value = "/test")
	@ResponseBody
	public Object authTest(HttpServletRequest request) {
		String followId = request.getParameter("followId");
		long userId = NumberUtils.toLong(request.getParameter("userId"));
		asyncWorker.wxFollowPaySuccess(followId, userId);
		return "1";
	}

	/**
	 * 校验电影是否购买过,能否参加关注微信活动
	 * @deprecated
	 */
	@RequestMapping(value = "/film")
	@ResponseBody
	public Object authFilm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Result<GalaxyUserDO> queryUserResult = galaxyUserAO.queryWithSave(buildUser(request));
		Map<String, Object> json = dealResult(queryUserResult);
		if (!queryUserResult.isSuccess()) {
			return dealJsonp(request, json);
		}

		long userId = queryUserResult.getValue().getId();

		// 是否购买过
		Result<GalaxyOrderFilmDO> result = authAO.authFilm(buildFilmQuery(request, userId));
		json = dealResult(result);
		if (!result.isSuccess())
			return dealJsonp(request, json);

		boolean isCheck = "true".equalsIgnoreCase(request.getParameter("check"));

		boolean authSuccess = result.getValue() != null;
		json.put("auth", authSuccess);
		if (authSuccess) {
			json.put("order_id", result.getValue().getId());
			json.put("expiredDateStr", DateUtil.formatBetweenWithEncode(result.getValue().getExpiredDate()));
		}

		if (!authSuccess && !isCheck) {
			GalaxyOrderFilmDO order = buildOrder(request, userId);
			Result<Long> orderResult = payAO.preOrder(order);
			if (!orderResult.isSuccess()) {
				json.put("success", false);
			} else {
				json.put("order_id", orderResult.getValue());
			}
		}
		log.info(json.toString());
		return dealJsonp(request, json);
	}

	/**
	 * 构造订单查询条件
	 */
	private GalaxyOrderFilmQuery buildFilmQuery(HttpServletRequest request, long userId) {
		GalaxyOrderFilmQuery query = new GalaxyOrderFilmQuery();
		query.setFilmId(NumberUtils.toLong(request.getParameter("filmId")));
		query.setUserId(userId);
		query.setOrderBy("ID desc");
		query.setStatus(OrderFilmStatus.SUCCESS.getCode());
		if (query.getFilmId() < 1) {
			return null;
		}
		return query;
	}

	/**
	 * 构造微信关注查询条件
	 */
	private GalaxyWeixinFollowQuery buildFollowQuery(HttpServletRequest request, long userId) {
		GalaxyWeixinFollowQuery query = new GalaxyWeixinFollowQuery();
		query.setUserId(userId);
		query.setStatus(FollowStatus.FOLLOWED.getCode());
		query.setUsedStatus(DbContant.ZERO);
		return query;
	}

	/**
	 * 订单信息
	 */
	private GalaxyOrderFilmDO buildOrder(HttpServletRequest request, long userId) {
		GalaxyOrderFilmDO order = new GalaxyOrderFilmDO();
		order.setFilmId(NumberUtils.toLong(request.getParameter("filmId")));
		// order.setContImage(request.getParameter("image"));
		// order.setContCode(request.getParameter("code"));
		// order.setContName(getName(request));
		// order.setTotalPrice((int)
		// NumberUtils.toDouble(request.getParameter("price")));
		order.setPayChannel(NumberUtils.toInt(request.getParameter("pay_channel"), PayChannel.WEIXIN.getCode()));// 默认微信
		order.setUserId(userId);
		order.setRegionCode(getRegionCode(request));
		order.setOutUserId(getOutUserId(request));
		long followId = NumberUtils.toLong(request.getParameter("followId"));
		if (followId > 0) {
			order.putFeature(DbContant.FEA_WX_FOLLOW, followId + "");
		} else {
			long discountId = NumberUtils.toLong(request.getParameter("discountId"));
			if (discountId > 0) {
				order.putFeature(DbContant.FEA_DISCOUNT_ID, discountId + "");
			}
		}
		if (order.getFilmId() < 1) {
			return null;
		}

		return order;
	}

	private String getName(HttpServletRequest request) {
		String name = request.getParameter("name");
		String tempName = name;
		try {
			tempName = URLDecoder.decode(name, "GBK");
		} catch (Exception e) {
			log.error("decode error,name " + name, e);
		}
		log.info("name:" + name + ",tempName:" + tempName);
		return tempName;
	}

	private DiscountDTO getMyDiscount(long userId) {
		Result<DiscountDTO> result = galaxyDiscountAccessAO.queryMyDiscount(userId);
		if (result.isSuccess() && result.getValue() != null)
			return result.getValue();
		return null;
	}

}
