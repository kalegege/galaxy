/**
 * 
 */
package com.wasu.ptyw.galaxy.common.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

/**
 * @author wenguang
 * @date 2015年9月29日
 */
public class LocalHttpClient {
	public static final String DEFAULT_CHARSET = "UTF-8";

	protected static HttpClient httpClient = HttpClientFactory.createHttpClient(100, 10);

	private static Map<String, HttpClient> httpClient_mchKeyStore = new HashMap<String, HttpClient>();

	public static void init(int maxTotal, int maxPerRoute) {
		httpClient = HttpClientFactory.createHttpClient(maxTotal, maxPerRoute);
	}

	/**
	 * 初始化 MCH HttpClient KeyStore
	 * 
	 * @param mch_id
	 * @param keyStoreFilePath
	 */
	public static void initMchKeyStore(String mch_id, String keyStoreFilePath) {
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(keyStoreFilePath));
			keyStore.load(instream, mch_id.toCharArray());
			instream.close();
			HttpClient httpClient = HttpClientFactory.createKeyMaterialHttpClient(keyStore, mch_id,
					new String[] { "TLSv1" });
			httpClient_mchKeyStore.put(mch_id, httpClient);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HttpResponse execute(HttpUriRequest request) throws Client302Exception {
		try {
			return httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if (e instanceof Client302Exception) {
				throw (Client302Exception) e;
			}
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) throws Client302Exception {
		try {
			return httpClient.execute(request, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if (e instanceof Client302Exception) {
				throw (Client302Exception) e;
			}
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 数据返回自动JSON对象解析
	 * 
	 * @param request
	 * @param clazz
	 * @return
	 * @throws Client302Exception
	 */
	public static <T> T executeJsonResult(HttpUriRequest request, Class<T> clazz, final String charsetName)
			throws Client302Exception {
		return execute(request, JsonResponseHandler.createResponseHandler(clazz, charsetName));
	}

	/**
	 * 数据返回自动JSON对象解析
	 */
	public static <T> List<T> executeJsonListResult(HttpUriRequest request, Class<T> clazz, final String charsetName)
			throws Client302Exception {
		return execute(request, JsonResponseListHandler.createResponseHandler(clazz));
	}

	/**
	 * 数据返回自动XML对象解析
	 */
	public static <T> T executeXmlResult(HttpUriRequest request, Class<T> clazz, final String charsetName)
			throws Client302Exception {
		return execute(request, XmlResponseHandler.createResponseHandler(clazz, charsetName));
	}

	/**
	 * MCH keyStore 请求 数据返回自动XML对象解析
	 * 
	 * @param mch_id
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static <T> T keyStoreExecuteXmlResult(String mch_id, HttpUriRequest request, Class<T> clazz) {
		try {
			return httpClient_mchKeyStore.get(mch_id).execute(request,
					XmlResponseHandler.createResponseHandler(clazz, DEFAULT_CHARSET));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String post(String url, String body) {
		try {
			RequestBuilder requestBuilder = RequestBuilder.post().setUri(url);
			if (body != null)
				requestBuilder.setEntity(new StringEntity(body, "UTF-8"));
			HttpResponse response = LocalHttpClient.execute(requestBuilder.build());
			int status = response.getStatusLine().getStatusCode();
			if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
