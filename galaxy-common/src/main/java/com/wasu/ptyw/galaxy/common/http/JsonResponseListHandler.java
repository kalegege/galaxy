package com.wasu.ptyw.galaxy.common.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class JsonResponseListHandler {
	private static Map<String, ResponseHandler<?>> map = new HashMap<String, ResponseHandler<?>>();

	@SuppressWarnings("unchecked")
	public static <T> ResponseHandler<List<T>> createResponseHandler(final Class<T> clazz) {
		if (map.containsKey(clazz.getName())) {
			return (ResponseHandler<List<T>>) map.get(clazz.getName());
		} else {
			ResponseHandler<List<T>> responseHandler = new ResponseHandler<List<T>>() {
				@Override
				public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						String str = EntityUtils.toString(entity);
						return JSON.parseArray(new String(str.getBytes("iso-8859-1"), "GBK"), clazz);
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			map.put(clazz.getName(), responseHandler);
			return responseHandler;
		}
	}
}
