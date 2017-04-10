package com.wasu.ptyw.galaxy.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.core.ao.GalaxyHistoryAO;
import com.wasu.ptyw.galaxy.core.dto.HistoryDTO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyHistoryDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyHistoryQuery;

/**
 * 播放历史
 * 
 * @author wenguang
 * @date 2015年07月01日
 */
@Controller
@RequestMapping("/history")
public class HistoryController extends BaseController {
	@Resource
	GalaxyHistoryAO galaxyHistoryAO;

	@RequestMapping(value = "/list")
	public ModelAndView list(ModelMap model) {
		return new ModelAndView("history/list", model);
	}

	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Object listDatas(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		GalaxyHistoryQuery query = buildQuery(request);
		Result<List<HistoryDTO>> result = galaxyHistoryAO.queryHistory(query);
		Map<String, Object> json = dealResult(result);
		if (result.isSuccess()) {
			json.put("datas", result.getValue());
			dealPage(json, query);
		}
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/clear.json")
	@ResponseBody
	public Object clear(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		GalaxyHistoryQuery query = buildClearQuery(request);
		Result<Integer> result = galaxyHistoryAO.deleteByQuery(query);
		Map<String, Object> json = dealResult(result);
		if (result.isSuccess()) {
			json.put("deleteNum", result.getValue());
		}
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/add.js")
	@ResponseBody
	public void add(HttpServletRequest request, HttpServletResponse response) {
		long orderId = NumberUtils.toLong(request.getParameter("order_id"));
		long hisId = NumberUtils.toLong(request.getParameter("his_id"));
		galaxyHistoryAO.save(orderId, hisId);
		response.setContentType("text/javascript");
	}

	private GalaxyHistoryQuery buildQuery(HttpServletRequest request) {
		GalaxyHistoryQuery query = new GalaxyHistoryQuery();
		query.setOutUserId(getOutUserId(request));
		query.setRegionCode(getRegionCode(request));
		query.setCurrentPage(NumberUtils.toInt(request.getParameter("current_page"), 1));
		query.setPageSize(NumberUtils.toInt(request.getParameter("page_size"), 5));
		query.setOrderBy("gmt_modified desc");
		if (StringUtils.isEmpty(query.getOutUserId())) {
			return null;
		}
		return query;
	}

	private GalaxyHistoryQuery buildClearQuery(HttpServletRequest request) {
		GalaxyHistoryQuery query = new GalaxyHistoryQuery();
		query.setOutUserId(getOutUserId(request));
		query.setRegionCode(getRegionCode(request));
		if (StringUtils.isEmpty(query.getOutUserId())) {
			return null;
		}
		return query;
	}

	private List<HistoryDTO> toDto(List<GalaxyHistoryDO> list) {
		List<HistoryDTO> dtoList = Lists.newArrayList();
		try {
			for (GalaxyHistoryDO item : list) {
				HistoryDTO dto = new HistoryDTO();
				BeanUtils.copyProperties(dto, item);
				dto.setExpiredDateStr(DateUtil.formatBetweenWithEncode(item.getExpiredDate()));
				dtoList.add(dto);
			}
		} catch (Exception e) {
		}
		return dtoList;
	}
}
