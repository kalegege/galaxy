package com.wasu.ptyw.galaxy.web.controller;

import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.core.ao.GalaxyOrderFilmAO;
import com.wasu.ptyw.galaxy.core.dto.OrderFilmDTO;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;

/**
 * 订单控制类
 * 
 * @author wenguang
 * @date 2015年06月05日
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;

	@RequestMapping(value = "/list")
	public ModelAndView list(HttpServletRequest request, ModelMap model) {
		int status = NumberUtils.toInt(request.getParameter("status"), OrderFilmStatus.SUCCESS.getCode());
		model.put("status", status);
		return new ModelAndView("order/list", model);
	}

	/**
	 * 获取订单列表
	 */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Object listDatas(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		GalaxyOrderFilmQuery query = buildListQuery(request);
		Result<List<OrderFilmDTO>> result = galaxyOrderFilmAO.queryWithStatus(query);
		Map<String, Object> json = dealResult(result);
		if (result.isSuccess()) {
			json.put("datas", result.getValue());
			dealPage(json, query);
		}
		return dealJsonp(request, json);
	}

	/**
	 * 关闭订单
	 */
	@RequestMapping(value = "/close.json")
	@ResponseBody
	public Object close(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		long orderId = NumberUtils.toLong(request.getParameter("order_id"));
		Result<GalaxyOrderFilmDO> result = galaxyOrderFilmAO.getById(orderId);
		Map<String, Object> json = dealResult(result);
		if (!result.isSuccess()) {
			return dealJsonp(request, json);
		}
		GalaxyOrderFilmDO order = result.getValue();
		if (OrderFilmStatus.isEnd(order.getStatus())) {
			json.put("code", ResultCode.ORDER_STATUS_INVALID.getCode());
			json.put("msg", ResultCode.ORDER_STATUS_INVALID.getMessage());
			return dealJsonp(request, json);
		}

		if (!StringUtils.equals(order.getOutUserId(), getOutUserId(request))
				|| !StringUtils.equals(order.getRegionCode(), getRegionCode(request))) {
			json.put("code", ResultCode.ORDER_USER_INVALID.getCode());
			json.put("msg", ResultCode.ORDER_USER_INVALID.getMessage());
			
			return dealJsonp(request, json);
		}
		int status = OrderFilmStatus.isNew(order.getStatus()) ? OrderFilmStatus.CANCELED.getCode()
				: OrderFilmStatus.CLOSED.getCode();
		boolean close = galaxyOrderFilmAO.closeOrder(Lists.newArrayList(order), status);
		if (!close) {
			json.put("code", ResultCode.SYSTEM_ERROR.getCode());
			json.put("msg", ResultCode.SYSTEM_ERROR.getMessage());
		}
		return dealJsonp(request, json);
	}

	/**
	 * 获取指定商品的订购情况
	 */
	@RequestMapping(value = "/querySuccess.json")
	@ResponseBody
	@Deprecated
	public Object querySuccess(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		GalaxyOrderFilmQuery query = buildStatusQuery(request);
		Result<List<GalaxyOrderFilmDO>> result = galaxyOrderFilmAO.query(query);
		Map<String, Object> json = dealResult(result);
		if (result.isSuccess()) {
			// 转为map，过滤相同的商品订购情况
			// Map<String, GalaxyOrderFilmDO> mappedMovies =
			// Maps.uniqueIndex(result.getValue(),
			// new Function<GalaxyOrderFilmDO, String>() {
			// public String apply(GalaxyOrderFilmDO from) {
			// return from.getContCode();
			// }
			// });
			List<String> successCodeList = Lists.newArrayList();
			for (GalaxyOrderFilmDO order : result.getValue()) {
				if (order != null && order.isSuccess() && !successCodeList.contains(order.getContCode()))
					successCodeList.add(order.getContCode());
			}
			json.put("datas", successCodeList);
		}
		return dealJsonp(request, json);
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
		query.setPageSize(NumberUtils.toInt(request.getParameter("page_size"), 500));
		query.setCurrentPage(1);
		query.setOrderBy("id desc");
		if (StringUtils.isEmpty(query.getOutUserId())) {
			return null;
		}
		return query;
	}

	/**
	 * 订单查询信息
	 * 
	 * @param status
	 *            {@link OrderFilmStatus}
	 */
	private GalaxyOrderFilmQuery buildListQuery(HttpServletRequest request) {
		int status = NumberUtils.toInt(request.getParameter("status"), OrderFilmStatus.SUCCESS.getCode());
		GalaxyOrderFilmQuery query = new GalaxyOrderFilmQuery();
		if (status == 9) {
			// 购买记录,包括3种状态
			query.setStatusList(Lists.newArrayList(OrderFilmStatus.SUCCESS.getCode(), OrderFilmStatus.PREPAY.getCode(),
					OrderFilmStatus.CLOSED.getCode()));
		} else {
			query.setStatus(status);
		}

		query.setOutUserId(getOutUserId(request));
		query.setRegionCode(getRegionCode(request));
		query.setCurrentPage(NumberUtils.toInt(request.getParameter("current_page"), 1));
		query.setPageSize(NumberUtils.toInt(request.getParameter("page_size"), 5));
		if (StringUtils.isEmpty(query.getOutUserId())) {
			return null;
		}
		return query;
	}

}
