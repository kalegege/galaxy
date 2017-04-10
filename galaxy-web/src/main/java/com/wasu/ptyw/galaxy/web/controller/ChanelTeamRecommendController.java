package com.wasu.ptyw.galaxy.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.ao.ChanelTeamRecommendAO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamRecommendDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamRecommendQuery;

/**
 * @author wenguang
 * @date 2016年07月13日
 */
@Controller
@RequestMapping("/management/teamrecommend")
public class ChanelTeamRecommendController extends BaseController {
	@Resource
	private ChanelTeamRecommendAO chanelTeamRecommendAO;

	@RequestMapping(value = "")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return new ModelAndView("index", model);
	}

	@RequestMapping(value = "/insert")
	@ResponseBody
	public Object insert(HttpServletRequest request) {
		Result<Long> result = chanelTeamRecommendAO.save(buildModel(request));
		Map<String, Object> json = dealResult(result);
		json.put("id", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(HttpServletRequest request) {
		Result<Integer> result = chanelTeamRecommendAO.update(buildModel(request));
		Map<String, Object> json = dealResult(result);
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/get/{id}")
	@ResponseBody
	public Object getById(@PathVariable long id,HttpServletRequest request) {
		Result<ChanelTeamRecommendDO> result = chanelTeamRecommendAO.getById(id);
		Map<String, Object> json = dealResult(result);
		json.put("data", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/gets/{ids}")
	@ResponseBody
	public Object getByIds(@PathVariable String ids,HttpServletRequest request) {
		Result<List<ChanelTeamRecommendDO>> result = chanelTeamRecommendAO.getByIds(NumUtil.toLongs(ids, ","));
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query(HttpServletRequest request) {
		ChanelTeamRecommendQuery query = buildQuery(request);
		Result<List<ChanelTeamRecommendDO>> result = chanelTeamRecommendAO.query(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		json.put("query", query);
		return dealJsonp(request, json);
	}
	

	private ChanelTeamRecommendQuery buildQuery(HttpServletRequest request) {
		ChanelTeamRecommendQuery query = new ChanelTeamRecommendQuery();
		return query;
	}

	private ChanelTeamRecommendDO buildModel(HttpServletRequest request) {
		ChanelTeamRecommendDO obj = new ChanelTeamRecommendDO();
		return obj;
	}
}
