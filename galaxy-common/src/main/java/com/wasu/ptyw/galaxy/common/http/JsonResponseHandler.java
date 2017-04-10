package com.wasu.ptyw.galaxy.common.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class JsonResponseHandler {

	private static Map<String, ResponseHandler<?>> map = new HashMap<String, ResponseHandler<?>>();

	@SuppressWarnings("unchecked")
	public static <T> ResponseHandler<T> createResponseHandler(final Class<T> clazz, final String charsetName) {

		if (map.containsKey(clazz.getName())) {
			return (ResponseHandler<T>) map.get(clazz.getName());
		} else {
			ResponseHandler<T> responseHandler = new ResponseHandler<T>() {
				@Override
				public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						String str = EntityUtils.toString(entity);
						return JSON.parseObject(str, clazz);
						//return JSON.parseObject(new String(str.getBytes("iso-8859-1"), charsetName), clazz);
					} else if(status == 302) {
						Header header = response.getFirstHeader("Location");
						if(header != null)
							throw new Client302Exception(header.getValue());
						else
							throw new ClientProtocolException("Unexpected response status: " + status);
					} else {
						Header str = response.getFirstHeader("Location");
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			map.put(clazz.getName(), responseHandler);
			return responseHandler;
		}
	}

}
