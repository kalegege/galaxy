package com.wasu.ptyw.galaxy.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wasu.ptyw.galaxy.common.util.monitor.MonitorLog;

/**
 * 带页面连接地址
 * 
 * @author wenguang
 * @date 2015年06月05日
 */
@Controller
@RequestMapping("/vm")
public class VmController extends BaseController {
	
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		try {
			MonitorLog.addStat("a", "b", "c");
			MonitorLog.addStat("a1", "b1", "c1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("index", model);
	}
}
