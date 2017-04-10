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
import com.wasu.ptyw.galaxy.core.ao.ChanelItemAO;
import com.wasu.ptyw.galaxy.core.ao.ChanelItemTeamAO;
import com.wasu.ptyw.galaxy.core.ao.ChanelTeamAO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.dataobject.LiveRealDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamDetailDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Controller
@RequestMapping("/item")
public class ChanelItemController extends BaseController {
	@Resource
	private ChanelItemAO chanelItemAO;
	@Resource
	private ChanelTeamAO chanelTeamAO;
	@Resource
	private ChanelItemTeamAO chanelItemTeamAO;
	
	@RequestMapping(value = "")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return new ModelAndView("index", model);
	}

	@RequestMapping(value = "/insert")
	@ResponseBody
	public Object insert(HttpServletRequest request) {
		Result<Long> result = chanelItemAO.save(buildModel(request));
		Map<String, Object> json = dealResult(result);
		json.put("id", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(HttpServletRequest request) {
		Result<Integer> result = chanelItemAO.update(buildModel(request));
		Map<String, Object> json = dealResult(result);
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/get/{id}")
	@ResponseBody
	public Object getById(@PathVariable long id,HttpServletRequest request) {
		Result<ChanelItemDO> result = chanelItemAO.getById(id);
		Map<String, Object> json = dealResult(result);
		json.put("data", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/gets/{ids}")
	@ResponseBody
	public Object getByIds(@PathVariable String ids,HttpServletRequest request) {
		Result<List<ChanelItemDO>> result = chanelItemAO.getByIds(NumUtil.toLongs(ids, ","));
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query(HttpServletRequest request) {
		ChanelItemQuery query = buildQuery(request);
		Result<List<ChanelItemDO>> result = chanelItemAO.query(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		json.put("query", query);
		return dealJsonp(request, json);
	}
	
	//获取大数据接口直播排名节目单
	@RequestMapping(value = "/getlive/{regionid}")
	@ResponseBody
	public Object getlive(@PathVariable String regionid,HttpServletRequest request) {
		ChanelItemQuery query = buildQuery(request);
		query.setRegionId(regionid);
		Result<List<TeamDetailDO>> result = chanelItemAO.getLive(7,query);
		//Result<LiveRealDO> result = chanelItemAO.getLive(7,query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}
	
	//获取后台维护分组接口
	@RequestMapping(value = "/getpersonallive/{regionid}")
	@ResponseBody
	public Object getpersonallive(@PathVariable String regionid,HttpServletRequest request) {
		ChanelItemQuery query = buildQuery(request);
		query.setRegionId(regionid);
		Result<List<ChanelItemDO>> result = chanelItemAO.queryByRegionId(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	private ChanelItemQuery buildQuery(HttpServletRequest request) {
		ChanelItemQuery query = new ChanelItemQuery();
		return query;
	}

	private ChanelItemDO buildModel(HttpServletRequest request) {
		ChanelItemDO obj = new ChanelItemDO();
		return obj;
	}
}
