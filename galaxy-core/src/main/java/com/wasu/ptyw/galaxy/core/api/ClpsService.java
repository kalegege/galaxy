package com.wasu.ptyw.galaxy.core.api;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.http.Client302Exception;
import com.wasu.ptyw.galaxy.common.http.JsonResponseHandler;
import com.wasu.ptyw.galaxy.common.http.LocalHttpClient;
import com.wasu.ptyw.galaxy.common.util.PropertiesUtil;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.ClpsReqData;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;

@Service("clpsService")
@Slf4j
public class ClpsService {
	protected static final String charsetName = "GBK";
	protected static Header jsonHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,
			ContentType.APPLICATION_JSON.toString());
	String sync_ips_url;
	String sync_ips_url2;
	String sync_ips_url3;

	@PostConstruct
	public void init() {
		this.sync_ips_url = PropertiesUtil.getValue("sync_ips_url");
		this.sync_ips_url2 = PropertiesUtil.getValue("sync_ips_url2");
		this.sync_ips_url3 = PropertiesUtil.getValue("sync_ips_url3");
	}

	/**
	 * 获取正片
	 */
	public List<GalaxyFilmDO> getFilms() {
		return getFilms(sync_ips_url);
	}

	/**
	 * 获取正片片花
	 */
	public List<GalaxyFilmDO> getFilmClips() {
		return getFilms(sync_ips_url2);
	}

	/**
	 * 获取在线购票片花
	 */
	public List<GalaxyFilmDO> getFilmTickets() {
		return getFilms(sync_ips_url3);
	}

	public List<GalaxyFilmDO> getFilms(String url) {
		List<GalaxyFilmDO> clpsList = Lists.newArrayList();
		try {
			log.info("ClpsService->getFilms: " + url);
			HttpUriRequest httpUriRequest = RequestBuilder.get().setHeader(jsonHeader).setUri(url).build();
			// clpsList = LocalHttpClient.execute(httpUriRequest,
			// JsonResponseListHandler.createResponseHandler(GalaxyFilmDO.class));
			ClpsReqData reqData = LocalHttpClient.execute(httpUriRequest,
					JsonResponseHandler.createResponseHandler(ClpsReqData.class, charsetName));
			if (reqData != null && CollectionUtils.isNotEmpty(reqData.getNav0())) {
				clpsList = reqData.getNav0();
				for (GalaxyFilmDO item : clpsList) {
					item.setFolderCode(reqData.getFolderCode());
				}
			}
		} catch (Client302Exception e) {
			return getFilms(e.getLoaction());
		} catch (Exception e) {
			log.error("ClpsService->getFilms error", e);
		}
		return clpsList;
	}
	
	public static void main(String[] args) {
		ClpsService service = new ClpsService();
		String url = "http://taiyuan-utc.wasu.cn/a?f=mov_hot_69_5&profile=TAIYUAN22SCENARIO";
		List<GalaxyFilmDO>  list = service.getFilms(url);
		System.out.println(list.toString());
	}
}
