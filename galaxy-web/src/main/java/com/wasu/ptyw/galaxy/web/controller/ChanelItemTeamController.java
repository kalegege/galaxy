package com.wasu.ptyw.galaxy.web.controller;

import java.util.ArrayList;
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
import com.wasu.ptyw.galaxy.core.ao.ChanelItemAO;
import com.wasu.ptyw.galaxy.core.ao.ChanelItemTeamAO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemTeamDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemTeamQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Controller
@RequestMapping("/itemteam")
public class ChanelItemTeamController extends BaseController {
	@Resource
	private ChanelItemTeamAO chanelItemTeamAO;
	@Resource
	private ChanelItemAO chanelItemAO;

	@RequestMapping(value = "")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return new ModelAndView("index", model);
	}

	@RequestMapping(value = "/insert")
	@ResponseBody
	public Object insert(HttpServletRequest request) {
		Result<Long> result = chanelItemTeamAO.save(buildModel(request));
		Map<String, Object> json = dealResult(result);
		json.put("id", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(HttpServletRequest request) {
		Result<Integer> result = chanelItemTeamAO.update(buildModel(request));
		Map<String, Object> json = dealResult(result);
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/get/{id}")
	@ResponseBody
	public Object getById(@PathVariable long id,HttpServletRequest request) {
		Result<ChanelItemTeamDO> result = chanelItemTeamAO.getById(id);
		Map<String, Object> json = dealResult(result);
		json.put("data", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/gets/{ids}")
	@ResponseBody
	public Object getByIds(@PathVariable String ids,HttpServletRequest request) {
		Result<List<ChanelItemTeamDO>> result = chanelItemTeamAO.getByIds(NumUtil.toLongs(ids, ","));
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query(HttpServletRequest request) {
		ChanelItemTeamQuery query = buildQuery(request);
		Result<List<ChanelItemTeamDO>> result = chanelItemTeamAO.query(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		json.put("query", query);
		return dealJsonp(request, json);
	}
	
	private ChanelItemTeamQuery buildQuery(HttpServletRequest request) {
		ChanelItemTeamQuery query = new ChanelItemTeamQuery();
		return query;
	}

	private ChanelItemTeamDO buildModel(HttpServletRequest request) {
		ChanelItemTeamDO obj = new ChanelItemTeamDO();
		return obj;
	}
}
