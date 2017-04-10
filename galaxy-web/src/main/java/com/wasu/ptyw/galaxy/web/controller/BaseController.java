/**
 * 
 */
package com.wasu.ptyw.galaxy.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.core.constant.HttpParams;
import com.wasu.ptyw.galaxy.core.spring.interceptor.TerminalInfo;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;
import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;
import com.wasu.ptyw.galaxy.web.jsonp.JSONPObject;

/**
 * @author wenguang
 * @date 2014年5月29日
 */
public class BaseController {

	public <V> Map<String, Object> dealResult(Result<V> result) {
		Map<String, Object> json = Maps.newHashMap();
		json.put("success", result.isSuccess());
		if (!result.isSuccess()) {
			json.put("code", result.getCode());
			json.put("msg", result.getMessage());
		}
		return json;
	}

	public Object dealJsonp(HttpServletRequest request, Map<String, Object> json) {
		String callback = request.getParameter(HttpParams.PARAM_CALLBACK);
		if (StringUtils.isNotEmpty(callback)) {
			return new JSONPObject(callback, json);
		}
		return json;
	}

	public void dealPage(Map<String, Object> json, SimpleQuery query) {
		json.put("totalItem", query.getTotalItem());
		json.put("totalPage", query.getTotalPage());
		json.put("currentPage", query.getCurrentPage());
		json.put("pageSize", query.getPageSize());
	}

	public String getRegionCode(HttpServletRequest request) {
		return StringUtils.defaultIfEmpty(request.getParameter(HttpParams.PARAM_REGION_CODE), "gansu");// 默认甘肃
	}

	public String getOutUserId(HttpServletRequest request) {
		return request.getParameter(HttpParams.PARAM_UID);
	}

	public void copyProperties(Object dest, Object orig) {
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
		}
	}

	/**
	 * 用户信息
	 */
	public GalaxyUserDO buildUser(HttpServletRequest request) {
		GalaxyUserDO user = new GalaxyUserDO();
		user.setRegionCode(getRegionCode(request));
		user.setOutUserId(getOutUserId(request));
		//user.setStbId(request.getParameter(HttpParams.PARAM_STBID));
		if (TerminalInfo.getCurrentInstance() != null) {
			user.setStbId(TerminalInfo.getCurrentInstance().getStbId());
		}
		if (StringUtils.isEmpty(user.getOutUserId())) {
			return null;
		}
		return user;
	}

}
