/**
 * 
 */
package com.wasu.ptyw.galaxy.core.api;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import weixin.popular.api.QrcodeAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.EventMessage;
import weixin.popular.bean.QrcodeTicket;
import weixin.popular.bean.Token;
import weixin.popular.util.XMLConverUtil;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.constant.BasicConstant;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.http.LocalHttpClient;
import com.wasu.ptyw.galaxy.common.util.PropertiesUtil;
import com.wasu.ptyw.galaxy.core.api.wxprotocol.BaseResData;
import com.wasu.ptyw.galaxy.core.cache.TokenCache;
import com.wasu.ptyw.galaxy.core.manager.GalaxyOrderFilmManager;
import com.wasu.ptyw.galaxy.core.manager.GalaxyWeixinFollowManager;
import com.wasu.ptyw.galaxy.dal.constant.FollowStatus;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyWeixinFollowDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyWeixinFollowQuery;

/**
 * @author wenguang
 * @date 2015年9月18日
 */
@Service("wxFollowService")
@Slf4j
public class WxFollowService {
	String appID = "";
	String appSecret = "";
	int expireSeconds = 604800;// 7天
	String notifyFollowUrl = "";

	@Resource
	private GalaxyOrderFilmManager galaxyOrderFilmManager;

	@Resource
	private GalaxyWeixinFollowManager galaxyWeixinFollowManager;

	@Resource
	AlipayService alipayService;

	@Resource
	TokenCache tokenCache;

	@PostConstruct
	public void init() {
		this.appID = PropertiesUtil.getValue("wx_appID");
		this.appSecret = PropertiesUtil.getValue("wx_app_secret");
		this.notifyFollowUrl = PropertiesUtil.getValue("notify_follow_url");
	}

	/**
	 * 获取令牌
	 */
	public String getToken() {
		String tokenStr = tokenCache.get();
		if (StringUtils.isEmpty(tokenStr)) {
			Token token = TokenAPI.token(appID, appSecret);
			if (token.isSuccess()) {
				tokenStr = token.getAccess_token();
				tokenCache.put(tokenStr);
			} else {
				log.warn("getToken error, Errcode=" + token.getErrcode() + ",Errmsg=" + token.getErrmsg());
			}
		}
		return tokenStr;
	}

	/**
	 * 获取二维码地址
	 */
	public String getQrcodeUrl(long sceneId) {
		QrcodeTicket ticket = QrcodeAPI.qrcodeCreateTemp(getToken(), expireSeconds, sceneId);
		// QrcodeTicket ticket = QrcodeAPI.qrcodeCreateFinal(getToken(),
		// sceneId);
		if (!ticket.isSuccess()) {
			log.warn("getQrcodeUrl error，sceneId=" + sceneId + ",Errcode=" + ticket.getErrcode() + ",Errmsg="
					+ ticket.getErrmsg());
			return BasicConstant.EMPTY_STRING;
		}
		return ticket.getUrl();
	}

	/**
	 * 支付关注或取消关注处理
	 */
	public String callbackFollow(String xml) {
		try {
			log.info("callbackFollow xml:" + xml);
			if (StringUtils.isEmpty(xml)) {
				return BaseResData.getFail("参数为空");
			}

			// EventMessage reqData = (EventMessage)
			// XmlUtil.getObjectFromXML(xml, EventMessage.class);
			EventMessage reqData = XMLConverUtil.convertToObject(EventMessage.class, xml);
			if (reqData == null) {
				return BaseResData.getFail("参数错误");
			}
			if (!StringUtils.equals(reqData.getMsgType(), "event")) {
				return BasicConstant.EMPTY_STRING;
			}
			if (StringUtils.equalsIgnoreCase(reqData.getEvent(), "SCAN")) {
				// 已关注微信扫描
				scan(reqData.getEventKey(), reqData.getFromUserName(), xml);
			} else if (StringUtils.equalsIgnoreCase(reqData.getEvent(), "subscribe")) {
				// 关注
				subscribe(reqData.getEventKey(), reqData.getFromUserName(), xml);
			} else if (StringUtils.equalsIgnoreCase(reqData.getEvent(), "unsubscribe")) {
				// 取消关注
				unsubscribe(reqData.getFromUserName());
			}
			return BaseResData.getSuccess();
		} catch (Exception e) {
			log.error("callbackFollow error,", e);
			return BaseResData.getFail("系统异常");
		}
	}

	/**
	 * 已关注微信扫描
	 */
	void scan(String eventKey, String fromUserName, String xml) throws MyException {
		long userId = 0;
		if (StringUtils.isNotEmpty(eventKey)) {
			userId = NumberUtils.toLong(eventKey);
		}
		follow(userId, fromUserName, xml);
	}

	/**
	 * 关注微信
	 */
	void subscribe(String eventKey, String fromUserName, String xml) throws MyException {
		long userId = 0;
		if (StringUtils.isNotEmpty(eventKey) && eventKey.startsWith("qrscene_")) {
			userId = NumberUtils.toLong(eventKey.substring(8));
		}
		follow(userId, fromUserName, xml);
	}

	/**
	 * 取消关注微信
	 */
	void unsubscribe(String fromUserName) throws MyException {
		if (StringUtils.isEmpty(fromUserName)) {
			return;
		}
		GalaxyWeixinFollowQuery query = new GalaxyWeixinFollowQuery();
		query.setWxUserName(fromUserName);
		galaxyWeixinFollowManager.updateStatusByQuery(query, FollowStatus.NEW.getCode());
	}

	void follow(long userId, String fromUserName, String xml) throws MyException {
		if (StringUtils.isEmpty(fromUserName)) {
			return;
		}
		if (StringUtils.isNotEmpty(notifyFollowUrl) && userId > 999999999) {
			notifyFollow(xml);
			return;
		}

		GalaxyWeixinFollowQuery query = new GalaxyWeixinFollowQuery();
		query.setUserId(userId);
		query.setWxUserName(fromUserName);
		GalaxyWeixinFollowDO obj = galaxyWeixinFollowManager.queryFirst(query);
		if (obj == null) {
			obj = new GalaxyWeixinFollowDO();
			obj.setUserId(userId);
			obj.setStatus(FollowStatus.FOLLOWED.getCode());
			obj.setWxUserName(fromUserName);
			galaxyWeixinFollowManager.insert(obj);
		} else if (!FollowStatus.isFollowed(obj.getStatus())) {
			galaxyWeixinFollowManager.updateStatusByIds(Lists.newArrayList(obj.getId()),
					FollowStatus.FOLLOWED.getCode());
		}
	}

	public void notifyFollow(String body) {
		try {
			log.info("notify url: " + notifyFollowUrl);
			LocalHttpClient.post(notifyFollowUrl, body);
		} catch (Exception e) {
			log.error("notify error", e);
		}
	}

}
