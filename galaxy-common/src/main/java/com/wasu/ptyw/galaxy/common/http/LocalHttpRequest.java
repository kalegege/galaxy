package com.wasu.ptyw.galaxy.common.http;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author wenguang
 */
@Slf4j
public class LocalHttpRequest {
	protected static final Header jsonHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,
			ContentType.APPLICATION_JSON.toString());
	protected static final Header xmlHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,
			ContentType.APPLICATION_XML.toString());
	protected static final RequestConfig config;
	static {
		config = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(3000).build();
	}

	public static String get(String url) {
		try {
			HttpUriRequest request = addHeader(RequestBuilder.get().setConfig(config).setUri(url)).build();
			HttpResponse response = LocalHttpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, LocalHttpClient.DEFAULT_CHARSET);
			} else if (status == HttpStatus.SC_MOVED_TEMPORARILY) {
				return get(response.getFirstHeader("Location").getValue());
			}
		} catch (Exception e) {
			log.error("getJson error", e);
		}
		return null;
	}
	
	public static String get(String url,String charset) {
		
		try {
			HttpUriRequest request = addHeader(RequestBuilder.get().setConfig(config).setUri(url)).build();
			HttpResponse response = LocalHttpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
				HttpEntity entity = response.getEntity();
				String result=EntityUtils.toString(entity, charset);
				log.info(result);
				return result;
				
			} else if (status == HttpStatus.SC_MOVED_TEMPORARILY) {
				return get(response.getFirstHeader("Location").getValue());
			}
		} catch (Exception e) {
			log.error("getJson error", e);
		}
		return null;
	}

	public static String post(String url, Map<String, String> params) {
		try {
			RequestBuilder requestBuilder = RequestBuilder.post().setConfig(config).setUri(url);
			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() != "") {
						NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue());
						requestBuilder.addParameter(nvp);
					}
				}
			}
			HttpUriRequest request = requestBuilder.build();
			HttpResponse response = LocalHttpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, LocalHttpClient.DEFAULT_CHARSET);
			}
		} catch (Exception e) {
			log.error("getJson error", e);
		}
		return null;
	}

	public static String post(String url, NameValuePair... nvps) {
		try {
			RequestBuilder requestBuilder = RequestBuilder.post().setConfig(config).setUri(url);

			HttpUriRequest request = addParameter(addHeader(requestBuilder), nvps).build();
			HttpResponse response = LocalHttpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, LocalHttpClient.DEFAULT_CHARSET);
			}
		} catch (Exception e) {
			log.error("getJson error", e);
		}
		return null;
	}

	public static String post(String url, String body) {
		try {
			RequestBuilder requestBuilder = RequestBuilder.post().setConfig(config).setUri(url);
			if (body != null)
				requestBuilder.setEntity(new StringEntity(body, LocalHttpClient.DEFAULT_CHARSET));
			HttpUriRequest request = addHeader(requestBuilder).build();
			HttpResponse response = LocalHttpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
				return EntityUtils.toString(response.getEntity(), LocalHttpClient.DEFAULT_CHARSET);
			}
		} catch (Exception e) {
			log.error("getJson error", e);
		}
		return null;
	}
	
	public static String post1(String url, String body) {
		try {
			RequestBuilder requestBuilder = RequestBuilder.post().setHeader("Content-Type","text/html;charset=utf-8").setConfig(config).setUri(url);
			if (body != null)
				requestBuilder.setEntity(new StringEntity(body, LocalHttpClient.DEFAULT_CHARSET));
			HttpUriRequest request = addHeader(requestBuilder).build();
			HttpResponse response = LocalHttpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
				return EntityUtils.toString(response.getEntity(), LocalHttpClient.DEFAULT_CHARSET);
			}
		} catch (Exception e) {
			log.error("getJson error", e);
		}
		return null;
	}

	public static <T> T getJson(String url, Class<T> clazz) {
		try {
			// HttpUriRequest request =
			// RequestBuilder.get().setHeader(jsonHeader).setUri(url).build();
			HttpUriRequest request = addHeader(RequestBuilder.get().setConfig(config).setUri(url)).build();
			return LocalHttpClient.executeJsonResult(request, clazz, LocalHttpClient.DEFAULT_CHARSET);
		} catch (Client302Exception e) {
			return getJson(e.getLoaction(), clazz);
		} catch (Exception e) {
			log.error("getJson error", e);
		}
		return null;
	}

	public static <T> List<T> getJsonList(String url, Class<T> clazz) {
		try {
			HttpUriRequest request = addHeader(RequestBuilder.get().setConfig(config).setUri(url)).build();
			return LocalHttpClient.executeJsonListResult(request, clazz, LocalHttpClient.DEFAULT_CHARSET);
		} catch (Client302Exception e) {
			return getJsonList(e.getLoaction(), clazz);
		} catch (Exception e) {
			log.error("getJson error", e);
		}
		return null;
	}

	public static <T> T getXml(String url, Class<T> clazz) {
		try {
			HttpUriRequest request = addHeader(RequestBuilder.get().setConfig(config).setUri(url)).build();
			return LocalHttpClient.executeXmlResult(request, clazz, LocalHttpClient.DEFAULT_CHARSET);
		} catch (Client302Exception e) {
			return getJson(e.getLoaction(), clazz);
		} catch (Exception e) {
			log.error("getJson error", e);
		}
		return null;
	}

	public static RequestBuilder addHeader(RequestBuilder requestBuilder) {
		requestBuilder
				.addHeader(HttpHeaders.USER_AGENT,
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
		requestBuilder.addHeader(HttpHeaders.ACCEPT, "*/*");
		requestBuilder.addHeader(HttpHeaders.CONNECTION, "keep-alive");
		requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, "*/*; charset=UTF-8");
		requestBuilder.addHeader(HttpHeaders.PRAGMA, "no-cache");
		requestBuilder.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
		requestBuilder.addHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		requestBuilder.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, sdch");
		if (requestBuilder.getUri() != null) {
			requestBuilder.addHeader(HttpHeaders.HOST, requestBuilder.getUri().getHost());
		}
		//
		// requestBuilder.addHeader("X-Requested-With", "XMLHttpRequest");
		// requestBuilder.addHeader("DNT", "1");
		return requestBuilder;
	}

	public static RequestBuilder addParameter(RequestBuilder requestBuilder, final NameValuePair... nvps) {
		if (nvps != null && nvps.length > 0)
			requestBuilder.addParameters(nvps);
		return requestBuilder;
	}
}
