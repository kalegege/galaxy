package com.wasu.ptyw.galaxy.web.controller.vm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.ao.AuthAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyDiscountAccessAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyFilmAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyOrderFilmAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyUserAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyWeixinFollowAO;
import com.wasu.ptyw.galaxy.core.ao.PayAO;
import com.wasu.ptyw.galaxy.core.cache.LocalCache;
import com.wasu.ptyw.galaxy.core.dto.DiscountDTO;
import com.wasu.ptyw.galaxy.core.dto.FilmDTO;
import com.wasu.ptyw.galaxy.core.dto.FilmListDTO;
import com.wasu.ptyw.galaxy.core.timetask.AsyncWorker;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.FollowStatus;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.constant.PayChannel;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyWeixinFollowDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;
import com.wasu.ptyw.galaxy.dal.query.GalaxyWeixinFollowQuery;
import com.wasu.ptyw.galaxy.web.controller.BaseController;

/**
 * 影片相关接口
 * 
 * @author wenguang
 * @date 2015年09月26日
 */
@Controller(value="vmFilmController")
@RequestMapping("/vm/film")
public class FilmController extends BaseController {
	@Resource
	GalaxyFilmAO galaxyFilmAO;
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;

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

	/**
	 * 首页
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request, ModelMap model) {
		Result<Map<String, Object>> result = galaxyFilmAO.queryIndex();
		if (result.isSuccess()) {
			model.put("datas", result.getValue().get("today"));
			model.put("jsonDatas", JSON.toJSONString(result.getValue().get("today")));
		}
		// 插入用户,不管失败成功
		galaxyUserAO.queryWithSave(buildUser(request));
		return new ModelAndView("/film/index", model);
	}

	/**
	 * 院线热映
	 */
	@RequestMapping(value = "/hot")
	@ResponseBody
	public Object hot(HttpServletRequest request, ModelMap model) {
		// 获取成功购买过的影片
		GalaxyOrderFilmQuery query = buildStatusQuery(request);
		Result<List<GalaxyOrderFilmDO>> orderResult = galaxyOrderFilmAO.query(query);
		Map<String, Object> json = dealResult(orderResult);
		if (!orderResult.isSuccess()) {
			return dealJsonp(request, json);
		}
		Set<String> successFilmids = Sets.newHashSet();
		for (GalaxyOrderFilmDO order : orderResult.getValue()) {
			if (order != null && order.isSuccess() && !successFilmids.contains(order.getContCode()))
				successFilmids.add(order.getContCode());
		}

		Result<List<FilmListDTO>> result = galaxyFilmAO.queryHot();
		json = dealResult(result);
		if (result.isSuccess()) {
			for (FilmListDTO item : result.getValue()) {
				if (successFilmids.contains(item.getAssetId()))
					item.setPay(1);// 购买过的影片标志
			}
			json.put("datas", result.getValue());
		}
		return dealJsonp(request, json);
	}

	/**
	 * 在线购票
	 */
	@RequestMapping(value = "/ticket")
	@ResponseBody
	public Object ticket(HttpServletRequest request, ModelMap model) {
		Result<List<FilmListDTO>> result = galaxyFilmAO.queryTicket();
		Map<String, Object> json = dealResult(result);
		if (result.isSuccess()) {
			json.put("datas", result.getValue());
		}
		return dealJsonp(request, json);
	}

	/**
	 * 详情
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object detail(HttpServletRequest request, ModelMap model) {
		// 获取影片信息
		Long id = NumberUtils.toLong(request.getParameter("id"));
		Result<GalaxyFilmDO> result = galaxyFilmAO.getById(id);
		Map<String, Object> json = dealResult(result);
		if (result.isSuccess()) {
			json.put("data", toFilmDTO(result.getValue()));
		}
		GalaxyFilmDO film = result.getValue();

		boolean isCheck = "true".equalsIgnoreCase(request.getParameter("check"));
		if (!isCheck)
			return dealJsonp(request, json);

		// 用户相关信息
		Result<GalaxyUserDO> queryUserResult = galaxyUserAO.queryWithSave(buildUser(request));

		if (!queryUserResult.isSuccess()) {
			json.putAll(dealResult(queryUserResult));
			return dealJsonp(request, json);
		}
		long userId = queryUserResult.getValue().getId();

		// 是否购买过
		Result<GalaxyOrderFilmDO> orderResult = authAO.authFilm(buildFilmOrderQuery(request, userId));
		if (!orderResult.isSuccess()) {
			json.putAll(dealResult(queryUserResult));
			return dealJsonp(request, json);
		}

		boolean authSuccess = orderResult.getValue() != null;
		json.put("auth", authSuccess);
		if (authSuccess) {
			json.put("order_id", orderResult.getValue().getId());
			json.put("expiredDateStr", DateUtil.formatBetweenWithEncode(orderResult.getValue().getExpiredDate()));
			return dealJsonp(request, json);
		}
		// 微信关注活动
		Result<GalaxyWeixinFollowDO> followResult = galaxyWeixinFollowAO.queryFirst(buildFollowQuery(request, userId));
		if (followResult.isSuccess()) {
			GalaxyWeixinFollowDO obj = followResult.getValue();
			// 是否参加过首次关注微信0.1元看大片活动
			json.put("followId", obj.getId());
			json.put("followPrice",
					NumberUtils.toInt(LocalCache.get(DbContant.KEY_WX_FOLLOW_PRICE), DbContant.WX_FOLLOW_PRICE));
		} else {
			// 折扣活动
			DiscountDTO discount = getMyDiscount(userId);
			if (discount != null) {
				json.put("discountId", discount.getDiscountId());
				json.put("discountPrice", discount.minPrice(NumUtil.toInt(film.getFilmPrice(), 0)));
				json.put("discountDes", discount.getDes());
			}
		}
		return dealJsonp(request, json);
	}

	/**
	 * 购买影片
	 */
	@RequestMapping(value = "/buy")
	@ResponseBody
	public Object buy(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Result<GalaxyUserDO> queryUserResult = galaxyUserAO.queryWithSave(buildUser(request));
		Map<String, Object> json = dealResult(queryUserResult);
		if (!queryUserResult.isSuccess()) {
			return dealJsonp(request, json);
		}
		long userId = queryUserResult.getValue().getId();

		GalaxyOrderFilmDO order = buildOrder(request, userId);
		Result<Long> orderResult = payAO.preOrder(order);
		if (!orderResult.isSuccess()) {
			json.put("success", false);
		} else {
			json.put("order_id", orderResult.getValue());
		}
		return dealJsonp(request, json);
	}

	/**
	 * 转为详情对象
	 */
	private FilmDTO toFilmDTO(GalaxyFilmDO film) {
		FilmDTO dto = new FilmDTO();
		copyProperties(dto, film);
		dto.setName(film.getFilmName());
		dto.setPrice(film.getFilmPrice());
		dto.setPicUrl(film.getBeijingUrl());
		return dto;
	}

	/**
	 * 订单查询信息
	 * 
	 */
	private GalaxyOrderFilmQuery buildStatusQuery(HttpServletRequest request) {
		GalaxyOrderFilmQuery query = new GalaxyOrderFilmQuery();
		query.setStatus(OrderFilmStatus.SUCCESS.getCode());
		query.setOutUserId(getOutUserId(request));
		query.setRegionCode(getRegionCode(request));
		query.setPageSize(NumberUtils.toInt(request.getParameter("page_size"), 200));
		query.setCurrentPage(1);
		query.setOrderBy("id desc");
		if (StringUtils.isEmpty(query.getOutUserId())) {
			return null;
		}
		return query;
	}

	/**
	 * 构造订单查询条件
	 */
	private GalaxyOrderFilmQuery buildFilmOrderQuery(HttpServletRequest request, long userId) {
		GalaxyOrderFilmQuery query = new GalaxyOrderFilmQuery();
		query.setFilmId(NumberUtils.toLong(request.getParameter("id")));
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
		order.setFilmId(NumberUtils.toLong(request.getParameter("id")));
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

	private DiscountDTO getMyDiscount(long userId) {
		Result<DiscountDTO> result = galaxyDiscountAccessAO.queryMyDiscount(userId);
		if (result.isSuccess() && result.getValue() != null)
			return result.getValue();
		return null;
	}
}
