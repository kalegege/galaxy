package com.wasu.ptyw.galaxy.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.ao.ChanelActorAO;
import com.wasu.ptyw.galaxy.core.ao.ChanelCareActorAO;
import com.wasu.ptyw.galaxy.core.manager.ChanelActorManager;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelActor1DO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelActorDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelCareActorDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelActorQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelCareActorQuery;

/**
 * @author wenguang
 * @date 2016年11月07日
 */
@Controller
@RequestMapping("/careactor")
public class ChanelCareActorController extends BaseController {
	@Resource
	private ChanelCareActorAO chanelCareActorAO;
	@Resource
	private ChanelActorAO chanelActorAO;

	@RequestMapping(value = "")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return new ModelAndView("index", model);
	}

	//艺人关注接口
	@RequestMapping(value = "/insert")
	@ResponseBody
	public Object insert(HttpServletRequest request,String stbid,String name) {
//		ChanelCareActorDO chanelCareActorDO=new ChanelCareActorDO();
//		chanelCareActorDO.setActorName(name);
//		chanelCareActorDO.setUid(stbid);
//		chanelCareActorDO.setStatus(1);
//		Result<Long> result = chanelCareActorAO.insertcare(chanelCareActorDO);
		Result<Long> result = chanelCareActorAO.insert(name,stbid);
		Map<String, Object> json = dealResult(result);
		json.put("id", result.getValue());
		return dealJsonp(request, json);
	}
	
	//艺人取消关注接口
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(HttpServletRequest request,String stbid,String name) {
//		ChanelCareActorQuery chanelCareActorQuery=new ChanelCareActorQuery();
//		chanelCareActorQuery.setActorName(name);
//		chanelCareActorQuery.setUid(stbid);
//		chanelCareActorQuery.setStatus(1);
//		Result<List<ChanelCareActorDO>> actornum=chanelCareActorAO.query(chanelCareActorQuery);
		Result<List<ChanelCareActorDO>> actornum=chanelCareActorAO.queryDelete(name,stbid);
		long id=actornum.getValue().get(0).getId();
		Result<Integer> result = chanelCareActorAO.deleteById(id);
		Map<String, Object> json = dealResult(result);
		json.put("id", result.getValue());
		return dealJsonp(request, json);
	}
	
	
	//查询关注的艺人接口
	@RequestMapping(value = "/query")
	@ResponseBody
	public Object query(String stbid,HttpServletRequest request,String pageSize,String currentPage) {
		ChanelCareActorQuery chanelCareActorQuery=new ChanelCareActorQuery();
		if(pageSize != null){
			chanelCareActorQuery.setPageSize(Integer.parseInt(pageSize));
		}else{
			chanelCareActorQuery.setPageSize(5);
		}
		
		if(currentPage != null){
			chanelCareActorQuery.setCurrentPage(Integer.parseInt(currentPage));
		}else{
			chanelCareActorQuery.setCurrentPage(1);
		}
		
		chanelCareActorQuery.setUid(stbid);
		Result<List<ChanelActorDO>> result=chanelCareActorAO.query(chanelCareActorQuery);
		List<ChanelActor1DO> chanelList1 = new ArrayList<ChanelActor1DO>();
		for(ChanelActorDO item:result.getValue()){
			ChanelActor1DO chanelActor1DO = new ChanelActor1DO();
			chanelActor1DO.setName(item.getName());
			chanelActor1DO.setPoster(item.getPoster());
			chanelList1.add(chanelActor1DO);
		}
		Map<String, Object> json = dealResult(result);
		ChanelActorQuery chanelActorQuery = new ChanelActorQuery();
		chanelActorQuery.setStatus(1);
		chanelActorQuery.setPublishid(-1);
		Result<List<ChanelActorDO>> resultRecommend =chanelActorAO.query(chanelActorQuery);
		List<ChanelActor1DO>chanelList = new ArrayList<ChanelActor1DO>();
		for(ChanelActorDO item1:resultRecommend.getValue()){
			ChanelActor1DO recommend = new ChanelActor1DO();
			recommend.setName(item1.getName());
			recommend.setPoster(item1.getPoster());
			chanelList.add(recommend);
		}
		json.put("care",chanelList1);
//		json.put("care", result.getValue());
		json.put("totalNum", chanelCareActorQuery.getTotalItem());
		json.put("totalPage", chanelCareActorQuery.getTotalPage());
		json.put("recommend", chanelList);
		return dealJsonp(request, json);
	}
	
	//查询关注的艺人接口
	@RequestMapping(value = "/queryCare")
	@ResponseBody
	public Object queryCare(String stbid,HttpServletRequest request,String name) {
		ChanelActorQuery chanelActorQuery=new ChanelActorQuery();
		ChanelCareActorQuery chanelCareActorQuery=new ChanelCareActorQuery();
		chanelActorQuery.setName(name);
		chanelCareActorQuery.setUid(stbid);
		Result<List<ChanelActorDO>> chanelList=chanelActorAO.query(chanelActorQuery);
//		long id=chanelList.getValue().get(0).getId().longValue();
		chanelCareActorQuery.setActorId(Integer.parseInt(chanelList.getValue().get(0).getId().toString()));
		Result<List<ChanelActorDO>> result=chanelCareActorAO.query(chanelCareActorQuery);
		Map<String, Object> json = dealResult(result);
		if(result.getValue().size() > 0){
			json.put("careStatus", 1);
		}else{
			json.put("careStatus", 0);
		}
		List<ChanelActor1DO> chanelList1=new ArrayList<ChanelActor1DO>();
		for(ChanelActorDO item:chanelList.getValue()){
			ChanelActor1DO chanelActor1DO=new ChanelActor1DO();
			chanelActor1DO.setName(item.getName());
			chanelActor1DO.setBirth(item.getBirth());
			chanelActor1DO.setSex(item.getSex());
			chanelActor1DO.setDes(item.getDes());
			chanelActor1DO.setBackground(item.getBackground());
			chanelList1.add(chanelActor1DO);
		}
//		json.put("care", chanelList.getValue().get(0));
		json.put("care", chanelList1);
		return dealJsonp(request, json);
	}
	
	
	//查询所有的艺人接口
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Object queryAll(HttpServletRequest request,String pageSize,String currentPage) {
		ChanelActorQuery chanelActorQuery=new ChanelActorQuery();
		
		
		if(pageSize != null){
			chanelActorQuery.setPageSize(Integer.parseInt(pageSize));
		}else{
			chanelActorQuery.setPageSize(5);
		}
		
		if(currentPage != null){
			chanelActorQuery.setCurrentPage(Integer.parseInt(currentPage));
		}else{
			chanelActorQuery.setCurrentPage(1);
		}
		chanelActorQuery.setStatus(1);
		chanelActorQuery.setPublishid(-1);
		Result<List<ChanelActorDO>> result=chanelActorAO.query(chanelActorQuery);
		Map<String, Object> json = dealResult(result);
		List<ChanelActor1DO> chanelList1 = new ArrayList<ChanelActor1DO>();
		for(ChanelActorDO item:result.getValue()){
			ChanelActor1DO chanelActor1DO = new ChanelActor1DO();
			chanelActor1DO.setName(item.getName());
			chanelActor1DO.setBackgroundName(item.getPoster());
			chanelList1.add(chanelActor1DO);
		}
		//json.put("care", result.getValue());
		json.put("totalNum", chanelActorQuery.getTotalItem());
		json.put("totalPage", chanelActorQuery.getTotalPage());
		json.put("care", chanelList1);
		return dealJsonp(request, json);
	}
	
	//推荐关注艺人接口
	@RequestMapping(value = "/recommend")
	@ResponseBody
	public Object recommend(HttpServletRequest request) throws MyException {
		
		Result<List<ChanelActorDO>> result =chanelActorAO.getRecommend(new ChanelActorQuery());
		Map<String, Object> json = dealResult(result);
		json.put("data", result.getValue());
		return dealJsonp(request, json);
	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(HttpServletRequest request) {
		Result<Integer> result = chanelCareActorAO.update(buildModel(request));
		Map<String, Object> json = dealResult(result);
		return dealJsonp(request, json);
	}
	
	

	@RequestMapping(value = "/get/{id}")
	@ResponseBody
	public Object getById(@PathVariable long id) {
		Result<ChanelCareActorDO> result = chanelCareActorAO.getById(id);
		Map<String, Object> json = dealResult(result);
		json.put("data", result.getValue());
		return json;
	}

	@RequestMapping(value = "/gets/{ids}")
	@ResponseBody
	public Object getByIds(@PathVariable String ids) {
		Result<List<ChanelCareActorDO>> result = chanelCareActorAO.getByIds(NumUtil.toLongs(ids, ","));
		Map<String, Object> json = dealResult(result);
		json.put("datas", result.getValue());
		return json;
	}

	@RequestMapping(value = "/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable long id) {
		List<Long> ids = Lists.newArrayList(id);
		if (id > 0) {
			ids.add(id);
		}
		Result<Integer> result = chanelCareActorAO.updateStatusByIds(ids, -1);
		Map<String, Object> json = dealResult(result);
		return json;
	}

	@RequestMapping(value = "/updateStatus")
	@ResponseBody
	public Object updateStatus(HttpServletRequest request) {
		List<Long> ids = NumUtil.toLongs(request.getParameter("ids"), ",");
		int status = NumberUtils.toInt(request.getParameter("status"));
		Result<Integer> result = chanelCareActorAO.updateStatusByIds(ids, status);
		Map<String, Object> json = dealResult(result);
		json.put("updateNum", result.getValue());
		return dealJsonp(request, json);
	}

	private ChanelCareActorQuery buildQuery(HttpServletRequest request) {
		ChanelCareActorQuery query = new ChanelCareActorQuery();
		return query;
	}

	private ChanelCareActorDO buildModel(HttpServletRequest request) {
		ChanelCareActorDO obj = new ChanelCareActorDO();
		return obj;
	}
}
