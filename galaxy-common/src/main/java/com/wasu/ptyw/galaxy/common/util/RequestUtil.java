/**
 * 
 */
package com.wasu.ptyw.galaxy.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author wenguang
 * @date 2015年6月18日
 */
public class RequestUtil {
	/**
	 * 连接上增加参数
	 */
	public static String buidlUrl(String baseUrl, Map<String, Object> params) {
		if (StringUtils.isEmpty(baseUrl) || MapUtils.isEmpty(params)) {
			return baseUrl;
		}
		StringBuilder sb = new StringBuilder(baseUrl);
		if (baseUrl.indexOf("?") < 0) {
			sb.append("?");
		}
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 获取所有request请求参数key-value
	 */
	public static Map<String, String> getRequestParams(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		if (null != request) {
			Set<String> paramsKey = request.getParameterMap().keySet();
			for (String key : paramsKey) {
				params.put(key, request.getParameter(key));
			}
		}
		return params;
	}
}
