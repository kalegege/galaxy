/**
 * 
 */
package com.wasu.ptyw.galaxy.core.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Maps;
import com.wasu.ptyw.galaxy.common.constant.BasicConstant;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.enums.CacheKey;
import com.wasu.ptyw.galaxy.core.ao.SysSettingAO;
import com.wasu.ptyw.galaxy.core.util.BeanUtil;
import com.wasu.ptyw.galaxy.dal.dataobject.SysSettingDO;

/**
 * 本地缓存类
 * 
 * @author wenguang
 * @date 2015年6月4日
 */
public class LocalCache {
	public static final String KEY_ORDER_EXPIRED_HOUR ="order_expired_hour";
	private static SysSettingAO ao = (SysSettingAO) BeanUtil.getBean("sysSettingAO");
	private static DbCache<CacheKey, Map<String, SysSettingDO>> cache;

	/**
	 * 获取系统配置项的值
	 */
	public static String get(String key) {
		SysSettingDO set = getConf(key);
		return set != null ? set.getData() : BasicConstant.EMPTY_STRING;
	}

	/**
	 * 获取系统配置对象
	 */
	public static SysSettingDO getConf(String key) {
		CacheKey cacheKey = CacheKey.SETTING;
		if (cache == null) {
			cache = new DbCache<CacheKey, Map<String, SysSettingDO>>() {
				@Override
				public Map<String, SysSettingDO> fetch(CacheKey key) {
					Result<List<SysSettingDO>> result = ao.queryAll();
					if (result.isSuccess())
						return listToMap(result.getValue());
					return Maps.newConcurrentMap();
				}
			};
		}
		return cache.get(cacheKey).get(key);
	}

	/**
	 * 清除系统配置项缓存
	 */
	public static void clearConf() {
		if (cache == null) {
			return;
		}
		cache.clear(CacheKey.SETTING);
	}

	/**
	 * 清除所有缓存
	 */
	public static void clearAll() {
		if (cache == null) {
			return;
		}
		cache.clearAll();
		cache = null;
	}

	/**
	 * 转为map
	 */
	private static Map<String, SysSettingDO> listToMap(List<SysSettingDO> list) {
		Map<String, SysSettingDO> map = Maps.newConcurrentMap();
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysSettingDO set : list) {
				map.put(set.getName(), set);
			}
		}
		return map;
	}

}
