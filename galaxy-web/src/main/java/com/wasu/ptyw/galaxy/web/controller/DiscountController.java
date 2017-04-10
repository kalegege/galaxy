package com.wasu.ptyw.galaxy.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.ao.GalaxyDiscountAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyDiscountAccessAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyOrderFilmAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyUserAO;
import com.wasu.ptyw.galaxy.core.dto.DiscountDTO;
import com.wasu.ptyw.galaxy.core.dto.DiscountUseDTO;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountAccessDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyDiscountAccessQuery;

/**
 * 每日福利相关接口
 * 
 * @author wenguang
 * @date 2015年09月25日
 */
@Controller
@RequestMapping("/discount")
public class DiscountController extends BaseController {
	@Resource
	GalaxyDiscountAO galaxyDiscountAO;
	@Resource
	GalaxyDiscountAccessAO galaxyDiscountAccessAO;
	@Resource
	GalaxyUserAO galaxyUserAO;
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;

	/**
	 * 获取所有福利
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(HttpServletRequest request, ModelMap model) {
		Result<GalaxyUserDO> queryUserResult = galaxyUserAO.queryWithSave(buildUser(request));
		Map<String, Object> json = dealResult(queryUserResult);
		if (!queryUserResult.isSuccess()) {
			return dealJsonp(request, json);
		}
		Result<List<GalaxyDiscountDO>> discountResult = galaxyDiscountAO.queryOnline();
		json = dealResult(discountResult);
		if (discountResult.isSuccess()) {
			json.put("datas", toDiscountDTO(discountResult.getValue()));
		}

		Result<Integer> accessResult = galaxyDiscountAccessAO.queryTodayCount(queryUserResult.getValue().getId());
		if (accessResult.isSuccess()) {
			int count = accessResult.getValue();
			json.put("count", count);
			int permitCount = NumberUtils.toInt(queryUserResult.getValue().getFeature(DbContant.FEA_DISCOUNT_COUNT));
			permitCount = count == 0 ? permitCount + 1 : permitCount;
			json.put("permitCount", permitCount);
		} else {
			return dealJsonp(request, dealResult(accessResult));
		}
		return dealJsonp(request, json);
	}

	/**
	 * 抽奖
	 * 
	 * @link http://www.cnblogs.com/younggun/p/3249772.html
	 */
	@RequestMapping(value = "/random")
	@ResponseBody
	public Object random(HttpServletRequest request, ModelMap model) {
		Result<GalaxyUserDO> queryUserResult = galaxyUserAO.queryWithSave(buildUser(request));
		Map<String, Object> json = dealResult(queryUserResult);
		if (!queryUserResult.isSuccess()) {
			return dealJsonp(request, json);
		}

		Result<DiscountDTO> result = galaxyDiscountAO.random(buildDiscountAccess(queryUserResult.getValue(), request),
				queryUserResult.getValue());
		json = dealResult(result);
		if (result.isSuccess()) {
			json.put("isWin", result.getValue() != null);
			json.put("data", result.getValue());
		}
		return dealJsonp(request, json);
	}

	/**
	 * 获取今日已抽奖次数
	 * 
	 * @deprecated 功能已合并到list接口
	 */
	@RequestMapping(value = "/random/count/today")
	@ResponseBody
	public Object randomCountToday(HttpServletRequest request, ModelMap model) {
		Result<GalaxyUserDO> queryUserResult = galaxyUserAO.queryWithSave(buildUser(request));
		Map<String, Object> json = dealResult(queryUserResult);
		if (!queryUserResult.isSuccess()) {
			return dealJsonp(request, json);
		}
		Result<Integer> result = galaxyDiscountAccessAO.queryTodayCount(queryUserResult.getValue().getId());
		json = dealResult(result);
		if (result.isSuccess()) {
			int count = result.getValue();
			json.put("count", count);
			int permitCount = NumberUtils.toInt(queryUserResult.getValue().getFeature(DbContant.FEA_DISCOUNT_COUNT)) + 1;
			json.put("permitCount", permitCount);
		}
		return dealJsonp(request, json);
	}

	/**
	 * 我的福利
	 */
	@RequestMapping(value = "/me")
	@ResponseBody
	public Object me(HttpServletRequest request, ModelMap model) {
		Result<List<GalaxyDiscountAccessDO>> result = galaxyDiscountAccessAO.query(buildQuery(request));
		Map<String, Object> json = dealResult(result);
		if (result.isSuccess()) {
			json.put("datas", toDiscountAccessDTO(result.getValue()));
		}
		return dealJsonp(request, json);
	}

	/**
	 * 中奖记录详情
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public Object detail(HttpServletRequest request, ModelMap model) {
		long discountId = NumberUtils.toLong(request.getParameter("discountId"));
		Result<GalaxyDiscountAccessDO> result = galaxyDiscountAccessAO.getById(discountId);
		Map<String, Object> json = dealResult(result);
		if (result.isSuccess()) {
			GalaxyDiscountAccessDO access = result.getValue();
			DiscountUseDTO dto = JSON.parseObject(access.getDes(), DiscountUseDTO.class);
			dto.setStatus(access.getStatus());
			dto.setExpiredDateStr(DateUtil.formatBetweenWithEncode(DateUtil.plusDay(access.getGmtCreate(), 7)));
			dto.setGmtCreateStr(DateUtil.format(access.getGmtCreate(), DateUtil.FORMAT_1));

			// 获取订单相关影片信息
			if (DbContant.TWO == access.getStatus() && NumUtil.isGreaterZero(dto.getOrderId())) {
				Result<GalaxyOrderFilmDO> orderResult = galaxyOrderFilmAO.getById(dto.getOrderId());
				if (orderResult.isSuccess()) {
					GalaxyOrderFilmDO order = orderResult.getValue();
					dto.setFilmName(order.getContName());
					dto.setFilmPic(order.getContImage());
				}
			}
			json.put("data", dto);
		}
		return dealJsonp(request, json);
	}

	private List<DiscountDTO> toDiscountDTO(List<GalaxyDiscountDO> list) {
		List<DiscountDTO> dtoList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(list))
			return dtoList;
		for (GalaxyDiscountDO item : list) {
			DiscountDTO dto = new DiscountDTO();
			copyProperties(dto, item);
			dtoList.add(dto);
		}
		return dtoList;
	}

	private List<DiscountDTO> toDiscountAccessDTO(List<GalaxyDiscountAccessDO> list) {
		List<DiscountDTO> dtoList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(list))
			return dtoList;
		for (GalaxyDiscountAccessDO item : list) {
			DiscountDTO dto = JSON.parseObject(item.getDes(), DiscountDTO.class);
			dto.setDiscountId(item.getId());
			// 设置过期时间
			String expiredDateStr = "";
			if (DbContant.TWO == item.getStatus()) {
				expiredDateStr = "已使用";
			} else {
				expiredDateStr = DateUtil.formatBetweenWithEncode(DateUtil.plusDay(item.getGmtCreate(), 7));
			}
			dto.setExpiredDateStr(expiredDateStr);
			dtoList.add(dto);
		}
		return dtoList;
	}

	private GalaxyDiscountAccessQuery buildQuery(HttpServletRequest request) {
		GalaxyDiscountAccessQuery query = new GalaxyDiscountAccessQuery();
		query.setRegionCode(getRegionCode(request));
		query.setOutUserId(getOutUserId(request));
		query.setPageSize(DbContant.PAGE_SIZE);
		query.setStatusList(Lists.newArrayList(DbContant.ONE, DbContant.TWO));
		query.setOrderBy("id desc");
		if (StringUtils.isEmpty(query.getOutUserId())) {
			return null;
		}
		return query;
	}

	private GalaxyDiscountAccessDO buildDiscountAccess(GalaxyUserDO user, HttpServletRequest request) {
		GalaxyDiscountAccessDO obj = new GalaxyDiscountAccessDO();
		obj.setRegionCode(user.getRegionCode());
		obj.setOutUserId(user.getOutUserId());
		obj.setUserId(user.getId());
		obj.setIp(request.getRemoteAddr());
		obj.setGmtDay(DateUtil.now());
		return obj;
	}

}
