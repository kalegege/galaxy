package com.wasu.ptyw.galaxy.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.ao.ChanelActorAO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelActorDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelActorQuery;

/**
 * @author wenguang
 * @date 2016年08月24日
 */
@Controller
@RequestMapping("/actor")
public class ChanelActorController extends BaseController {
	@Resource
	private ChanelActorAO chanelActorAO;

	@RequestMapping(value = "")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return new ModelAndView("index", model);
	}

	@RequestMapping(value = "/insert")
	@ResponseBody
	public Object insert(HttpServletRequest request) {
		Result<Long> result = chanelActorAO.save(buildModel(request));
		Map<String, Object> json = dealResult(result);
		json.put("id", result.getValue());
		return json;
	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(HttpServletRequest request) {
		Result<Integer> result = chanelActorAO.update(buildModel(request));
		Map<String, Object> json = dealResult(result);
		return json;
	}

	@RequestMapping(value = "/get/{id}")
	@ResponseBody
	public Object getById(@PathVariable long id) {
		Result<ChanelActorDO> result = chanelActorAO.getById(id);
		Map<String, Object> json = dealResult(result);
		json.put("data", result.getValue());
		return json;
	}

	@RequestMapping(value = "/gets/{ids}")
	@ResponseBody
	public Object getByIds(@PathVariable String ids) {
		Result<List<ChanelActorDO>> result = chanelActorAO.getByIds(NumUtil.toLongs(ids, ","));
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return json;
	}

	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query(HttpServletRequest request) {
		ChanelActorQuery query = buildQuery(request);
		Result<List<ChanelActorDO>> result = chanelActorAO.query(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		json.put("query", query);
		return json;
	}

	@RequestMapping(value = "/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable long id) {
		List<Long> ids = Lists.newArrayList(id);
		if (id > 0) {
			ids.add(id);
		}
		Result<Integer> result = chanelActorAO.updateStatusByIds(ids, -1);
		Map<String, Object> json = dealResult(result);
		return json;
	}

	@RequestMapping(value = "/updateStatus")
	@ResponseBody
	public Object updateStatus(HttpServletRequest request) {
		List<Long> ids = NumUtil.toLongs(request.getParameter("ids"), ",");
		int status = NumberUtils.toInt(request.getParameter("status"));
		Result<Integer> result = chanelActorAO.updateStatusByIds(ids, status);
		Map<String, Object> json = dealResult(result);
		json.put("updateNum", result.getValue());
		return json;
	}

	private ChanelActorQuery buildQuery(HttpServletRequest request) {
		ChanelActorQuery query = new ChanelActorQuery();
		return query;
	}

	private ChanelActorDO buildModel(HttpServletRequest request) {
		ChanelActorDO obj = new ChanelActorDO();
		return obj;
	}
}
