package com.wasu.ptyw.galaxy.common.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.wasu.ptyw.galaxy.common.util.XMLConverUtil;

public class XmlResponseHandler{

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
					if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
						HttpEntity entity = response.getEntity();
						String str = EntityUtils.toString(entity, charsetName);
						// return XMLConverUtil.convertToObject(clazz, new
						// String(str.getBytes("iso-8859-1"), "utf-8"));
						return XMLConverUtil.convertToObject(clazz, str);
					} else if (status == HttpStatus.SC_MOVED_TEMPORARILY) {
						Header header = response.getFirstHeader("Location");
						if (header != null)
							throw new Client302Exception(header.getValue());
						else
							throw new ClientProtocolException("Unexpected response status: " + status);
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
