package com.wasu.ptyw.galaxy.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wasu.ptyw.galaxy.common.util.monitor.MonitorLog;
import com.wasu.ptyw.galaxy.core.timetask.SyncOrderTimeTask;
import com.wasu.ptyw.galaxy.core.timetask.SyncFilmTimeTask;

@Controller
@Slf4j
public class CommonController {
	@Resource
	SyncOrderTimeTask syncOrderTimeTask;
	@Resource
	SyncFilmTimeTask syncFilmTimeTask;

	@RequestMapping(value = "")
	public ModelAndView toIndex(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return index(request, response, model);
	}

	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		try {
			MonitorLog.addStat("a", "b", "c");
			MonitorLog.addStat("a1", "b1", "c1");
			log.info("index info");
			log.warn("index warn");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("index", model);
	}

	/**
	 * 统计用
	 */
	@RequestMapping(value = "/stats")
	@ResponseBody
	public Object stats(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// spm=""
		return "";
	}

	/**
	 * 订单定时任务
	 */
	@RequestMapping(value = "/timetask/order/sync")
	@ResponseBody
	public Object closeOrder(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		syncOrderTimeTask.sync();
		return "start syncOrderTimeTask.sync()";
	}

	/**
	 * 影片定时任务
	 */
	@RequestMapping(value = "/timetask/film/sync")
	@ResponseBody
	public Object syncFilm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		syncFilmTimeTask.sync();
		return "start syncFilmTimeTask.sync()";
	}
}
