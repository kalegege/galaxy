package com.wasu.ptyw.galaxy.core.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wasu.ptyw.galaxy.core.constant.HttpParams;

/**
 * 终端信息拦截器
 * 
 * @author wenguang
 * @date 2015年6月5日
 */
public class TerminalIntercetor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		TerminalInfo terminal = new TerminalInfo();
		String stbId = request.getHeader("X-TERMINAL-ID");
		String userId = request.getHeader("X-USER-ID");
		String userAgent = request.getHeader("User-Agent");
		String regionId = request.getHeader("X-REGION-ID");
		String userProfile = request.getHeader("X-USERPROFILE");
		String ip = request.getRemoteHost();

		if (StringUtils.isNotEmpty(request.getParameter(HttpParams.PARAM_STBID))) {
			stbId = request.getParameter(HttpParams.PARAM_STBID);
		}

		if (StringUtils.isNotEmpty(request.getParameter(HttpParams.PARAM_UID))) {
			userId = request.getParameter(HttpParams.PARAM_UID);
		}
		
		terminal.setRegionCode(StringUtils.defaultIfEmpty(request.getParameter(HttpParams.PARAM_REGION_CODE), "gansu"));// 默认甘肃

		terminal.setIp(ip);
		terminal.setStbId(stbId);
		terminal.setUserId(userId);
		terminal.setUserAgent(userAgent);
		terminal.setRegionId(regionId);
		terminal.setUserProfile(userProfile);

		TerminalInfo.setCurrentUser(terminal);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
