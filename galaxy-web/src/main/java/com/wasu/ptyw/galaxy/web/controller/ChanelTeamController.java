package com.wasu.ptyw.galaxy.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.ao.ChanelTeamAO;
import com.wasu.ptyw.galaxy.core.ao.ChanelTeamRecommendAO;
import com.wasu.ptyw.galaxy.core.ao.ChanelTeamSelfcontrolAO;
import com.wasu.ptyw.galaxy.core.dto.ChanelTeamRecommendDTO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamSelfcontrolDO;
import com.wasu.ptyw.galaxy.dal.dataobject.IndexDO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamAllDTO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamBlistDTO;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamRecommendQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamSelfcontrolQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Controller
@RequestMapping("/team")
public class ChanelTeamController extends BaseController {
	@Resource
	private ChanelTeamAO chanelTeamAO;
	@Resource
	private ChanelTeamRecommendAO chanelTeamRecommendAO;
	@Resource
	private ChanelTeamSelfcontrolAO chanelTeamSelfcontrolAO;
	// 缓存
	private static Cache<String, Result<TeamAllDTO>> cache;

	private static Cache<String, Result<IndexDO>> cache1;

	@RequestMapping(value = "")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return new ModelAndView("index", model);
	}

	@RequestMapping(value = "/insert")
	@ResponseBody
	public Object insert(HttpServletRequest request) {
		Result<Long> result = chanelTeamAO.save(buildModel(request));
		Map<String, Object> json = dealResult(result);
		json.put("id", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(HttpServletRequest request) {
		Result<Integer> result = chanelTeamAO.update(buildModel(request));
		Map<String, Object> json = dealResult(result);
		return json;
	}

	@RequestMapping(value = "/get/{id}")
	@ResponseBody
	public Object getById(@PathVariable long id, HttpServletRequest request) {
		Result<ChanelTeamDO> result = chanelTeamAO.getById(id);
		Map<String, Object> json = dealResult(result);
		json.put("data", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/gets/{ids}")
	@ResponseBody
	public Object getByIds(@PathVariable String ids, HttpServletRequest request) {
		Result<List<ChanelTeamDO>> result = chanelTeamAO.getByIds(NumUtil.toLongs(ids, ","));
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query(HttpServletRequest request) {
		ChanelTeamQuery query = buildQuery(request);
		Result<List<ChanelTeamDO>> result = chanelTeamAO.query(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		json.put("query", query);
		return dealJsonp(request, json);
	}

	// 获取所有频道当天节目单
	@RequestMapping(value = "/getmenu/{regionid}")
	@ResponseBody
	public Object getMenu(@PathVariable String regionid, HttpServletRequest request) {
		ChanelTeamQuery query = buildQuery(request);
		query.setRegionId(regionid);
		Result<MenuAllDO> result = chanelTeamAO.getMenu(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	// 获取单个频道当天直播节目单
	@RequestMapping(value = "/getsinglemenu/{regionid}/{chid}")
	@ResponseBody
	public Object getSingleMenu(@PathVariable String regionid, @PathVariable String chid, HttpServletRequest request) {
		ChanelTeamQuery query = buildQuery(request);
		query.setRegionId(regionid);
		Result<MenuAllDO> result = chanelTeamAO.getSingleMenu(query, chid);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	// 获取多个频道当天直播节目单
	@RequestMapping(value = "/getmanymenu/{regionid}/{chids}")
	@ResponseBody
	public Object getManyMenu(@PathVariable String regionid, @PathVariable String chids, HttpServletRequest request) {
		String[] chid = chids.split("\\|");
		ChanelTeamQuery query = buildQuery(request);
		query.setRegionId(regionid);
		Result<List<MenuAllDO>> result = chanelTeamAO.getManyMenu(query, chid);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	// 获取所有分组及频道信息接口
	@RequestMapping(value = "/getteam/{regionid}")
	@ResponseBody
	public Object getTeam(@PathVariable String regionid, HttpServletRequest request) {
		ChanelTeamQuery query = buildQuery(request);
		query.setRegionId(regionid);
//		 Result<TeamAllDTO> result = getTeamCache(query);
		
		Result<TeamAllDTO> result = new Result<TeamAllDTO>(false);
		TeamAllDTO teamList=new TeamAllDTO();
		List<TeamBlistDTO> bList = chanelTeamAO.getTeamDetails(regionid);
		teamList.setVersion("0");
		teamList.setResult("0");
		teamList.setResultDesc("event get success");
		teamList.setBList(bList);
		result.setValue(teamList);
		if(bList.size()>0){
			result.setSuccess(true);
		}
		
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	// 获取所有分组及频道信息接口
	@RequestMapping(value = "/getindex/{regionid}")
	@ResponseBody
	public Object getIndex(@PathVariable String regionid, HttpServletRequest request) {
		ChanelTeamRecommendQuery chanelTeamRecommendQuery = new ChanelTeamRecommendQuery();
		chanelTeamRecommendQuery.setRegionId(regionid);

		Result<IndexDO> result = getIndexCache(chanelTeamRecommendQuery);
		// Result<IndexDO> result
		// =chanelTeamRecommendAO.getindex(chanelTeamRecommendQuery);

		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	// 获取所有分组接口
	@RequestMapping(value = "/getpersonalteam/{regionid}")
	@ResponseBody
	public Object getpersonalteam(@PathVariable String regionid, HttpServletRequest request) {
		ChanelTeamQuery query = buildQuery(request);
		query.setRegionId(regionid);
		// Result<List<ChanelTeamDO>> result =
		// chanelTeamAO.queryByRegion(query);
		Result<List<ChanelTeamDO>> result = chanelTeamAO.query(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	// 获取推荐首页接口
	@RequestMapping(value = "/getrecommend/{regionid}")
	@ResponseBody
	public Object getrecommend(@PathVariable String regionid, HttpServletRequest request) {
		ChanelTeamRecommendQuery chanelTeamRecommendQuery = new ChanelTeamRecommendQuery();
		ChanelTeamSelfcontrolQuery chanelTeamSelfcontrolQuery = new ChanelTeamSelfcontrolQuery();
		chanelTeamRecommendQuery.setRegionId(regionid);
		chanelTeamSelfcontrolQuery.setRegionId(regionid);
		Result<List<ChanelTeamRecommendDTO>> result = chanelTeamRecommendAO.getrecommend(chanelTeamRecommendQuery);
		Result<List<ChanelTeamSelfcontrolDO>> result_self = chanelTeamSelfcontrolAO
				.getrecommend(chanelTeamSelfcontrolQuery);

		Map<String, Object> json = dealResult(result);
		Map<String, Object> jsoninside = dealResult(result);
		jsoninside.put("self", result_self.getValue());
		jsoninside.put("recommend", result.getValue());
		json.put("data", jsoninside);
		return dealJsonp(request, json);
	}

	// 测试数据
	@RequestMapping(value = "/gettest")
	@ResponseBody
	public Object getTest(HttpServletRequest request) {
		ChanelTeamQuery query = buildQuery(request);
		query.setRegionId("01_4001");
		Result<TeamAllDO> result = chanelTeamAO.getTeam1(query);
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return dealJsonp(request, json);
	}

	private ChanelTeamQuery buildQuery(HttpServletRequest request) {
		ChanelTeamQuery query = new ChanelTeamQuery();
		return query;
	}

	private ChanelTeamDO buildModel(HttpServletRequest request) {
		ChanelTeamDO obj = new ChanelTeamDO();
		return obj;
	}

	// 从接口获取数据做缓存
	public Result<TeamAllDTO> getTeamCache(final ChanelTeamQuery query) {

		final String key = query.getRegionId() + "_" + new Date().getDate();
		if (cache == null) {
			cache = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(1, TimeUnit.HOURS)
					.expireAfterAccess(3, TimeUnit.MINUTES).build(new CacheLoader<String, Result<TeamAllDTO>>() {
						@Override
						public Result<TeamAllDTO> load(String s) throws Exception {
							return null;
						}
					});
		}

		try {
			Result<TeamAllDTO> result = (Result<TeamAllDTO>) cache.get(key, new Callable<Result<TeamAllDTO>>() {
				@Override
				public Result<TeamAllDTO> call() throws MyException {
					return chanelTeamAO.getTeam(query);
					// return chanelTeamManager.getMenu(query);
				}
			});

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// 从接口获取数据做缓存
	public Result<IndexDO> getIndexCache(final ChanelTeamRecommendQuery query) {

		final String key = query.getRegionId() + "_" + new Date().getDate();
		if (cache1 == null) {
			cache1 = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(1, TimeUnit.HOURS)
					.expireAfterAccess(3, TimeUnit.MINUTES).build(new CacheLoader<String, Result<IndexDO>>() {
						@Override
						public Result<IndexDO> load(String s) throws Exception {
							return null;
						}
					});
		}

		try {
			Result<IndexDO> result = (Result<IndexDO>) cache1.get(key, new Callable<Result<IndexDO>>() {
				@Override
				public Result<IndexDO> call() throws MyException {
					return chanelTeamRecommendAO.getindex(query);
					// return chanelTeamManager.getMenu(query);
				}
			});

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 清空首页缓存
	 */
	public static void cleanCache(){
		System.out.println("清空首页缓存");
		cache.cleanUp();
		cache1.cleanUp();
	}

}
