/**
 * 
 */
package com.wasu.ptyw.galaxy.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wenguang
 * 
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

	@RequestMapping(value = "")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return getModelAndView();
	}

	@RequestMapping(value = "/404")
	public ModelAndView e404() {
		return getModelAndView();
	}

	@RequestMapping(value = "/500")
	public ModelAndView e500() {
		return getModelAndView();
	}

	@RequestMapping(value = "/all")
	public ModelAndView all() {
		return getModelAndView();
	}

	private ModelAndView getModelAndView() {
		return new ModelAndView("common/error");
	}
}
