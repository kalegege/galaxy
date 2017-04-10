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
import com.wasu.ptyw.galaxy.core.ao.ChanelTeamSelfcontrolAO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamSelfcontrolDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamSelfcontrolQuery;

/**
 * @author wenguang
 * @date 2016年07月21日
 */
@Controller
@RequestMapping("/teamselfcontrol")
public class ChanelTeamSelfcontrolController extends BaseController {
	@Resource
	private ChanelTeamSelfcontrolAO chanelTeamSelfcontrolAO;

	@RequestMapping(value = "")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return new ModelAndView("index", model);
	}

	@RequestMapping(value = "/insert")
	@ResponseBody
	public Object insert(HttpServletRequest request) {
		Result<Long> result = chanelTeamSelfcontrolAO.save(buildModel(request));
		Map<String, Object> json = dealResult(result);
		json.put("id", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(HttpServletRequest request) {
		Result<Integer> result = chanelTeamSelfcontrolAO.update(buildModel(request));
		Map<String, Object> json = dealResult(result);
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/get/{id}")
	@ResponseBody
	public Object getById(@PathVariable long id,HttpServletRequest request) {
		Result<ChanelTeamSelfcontrolDO> result = chanelTeamSelfcontrolAO.getById(id);
		Map<String, Object> json = dealResult(result);
		json.put("data", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/gets/{ids}")
	@ResponseBody
	public Object getByIds(@PathVariable String ids,HttpServletRequest request) {
		Result<List<ChanelTeamSelfcontrolDO>> result = chanelTeamSelfcontrolAO.getByIds(NumUtil.toLongs(ids, ","));
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query(HttpServletRequest request) {
		ChanelTeamSelfcontrolQuery query = buildQuery(request);
		Result<List<ChanelTeamSelfcontrolDO>> result = chanelTeamSelfcontrolAO.query(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		json.put("query", query);
		return dealJsonp(request, json);
	}

	private ChanelTeamSelfcontrolQuery buildQuery(HttpServletRequest request) {
		ChanelTeamSelfcontrolQuery query = new ChanelTeamSelfcontrolQuery();
		return query;
	}

	private ChanelTeamSelfcontrolDO buildModel(HttpServletRequest request) {
		ChanelTeamSelfcontrolDO obj = new ChanelTeamSelfcontrolDO();
		return obj;
	}
}
