/**
 * 
 */
package com.wasu.ptyw.galaxy.dal.dataobject;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;

/**
 * @author wenguang
 * @date 2015年9月16日
 */
public class FeaturesDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	/**
	 * 扩展属性,数据库字段
	 */
	private String features;

	private JSONObject featuresJson;

	/**
	 * 是否存在
	 */
	public boolean isExist(String key) {
		String feature = getFeature(key);
		return DbContant.YES.equals(feature);
	}

	/**
	 * 获取属性值
	 */
	public String getFeature(String key) {
		initFeaturesJson();
		return featuresJson.getString(key);
	}

	/**
	 * 设置属性
	 */
	public void putFeature(String key, String value) {
		initFeaturesJson();
		featuresJson.put(key, value);
	}

	public String getFeatures() {
		if (featuresJson != null)
			return featuresJson.toString();
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	private void initFeaturesJson() {
		if (featuresJson == null) {
			if (StringUtils.isNotEmpty(features)) {
				featuresJson = JSON.parseObject(features);
			} else {
				featuresJson = new JSONObject();
			}
		}
	}
}
