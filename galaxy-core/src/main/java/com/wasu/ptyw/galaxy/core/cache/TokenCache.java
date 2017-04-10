/**
 * 
 */
package com.wasu.ptyw.galaxy.core.cache;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wasu.ptyw.galaxy.common.constant.BasicConstant;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.core.ao.SysSettingAO;
import com.wasu.ptyw.galaxy.dal.dataobject.SysSettingDO;
import com.wasu.ptyw.galaxy.dal.query.SysSettingQuery;

/**
 * 令牌缓存类
 * 
 * @author wenguang
 * @date 2015年6月4日
 */
@Service("tokenCache")
public class TokenCache extends DbCache<String, SysSettingDO> {
	String DEFAULT_KEY = "wx_token";
	int expireSeconds = 2 * 55 * 60;// 1小时50分钟，单位秒
	@Resource(name = "sysSettingAO")
	SysSettingAO ao;

	@PostConstruct
	public void init() {
		setExpireTime((long) expireSeconds);
	}

	@Override
	protected SysSettingDO fetch(String key) throws Exception {
		SysSettingQuery query = new SysSettingQuery();
		query.setName(DEFAULT_KEY);
		Result<SysSettingDO> result = ao.queryFirst(query);
		if (result.isSuccess() && result.getValue() != null) {
			return result.getValue();
		}
		throw new Exception("data is not found in db");
	}

	public String get() {
		SysSettingDO obj = super.get(DEFAULT_KEY);
		if (obj == null)
			return BasicConstant.EMPTY_STRING;
		if (DateUtil.expire(obj.getGmtModified(), expireSeconds))
			return BasicConstant.EMPTY_STRING;
		return obj.getData();
	}

	public void put(String token) {
		put(DEFAULT_KEY, token);
	}

	public void put(String key, String token) {
		SysSettingDO obj = get(key);
		if (obj == null) {
			obj = new SysSettingDO();
			obj.setName(key);
			obj.setData(token);
			ao.save(obj);
			put(key, obj);
		} else {
			obj.setData(token);
			obj.setGmtModified(new Date());
			ao.update(obj);
		}
		// clear(key);
	}
}
