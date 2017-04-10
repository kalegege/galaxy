package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;

/**
 * @author wenguang
 * @date 2015年09月25日
 */
@Alias("GalaxyDiscountAccessDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyDiscountAccessDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public GalaxyDiscountAccessDO() {

	}

	/**
	 * 本系统用户ID
	 */
	private Long userId;

	/**
	 * 外部用户ID
	 */
	private String outUserId;

	/**
	 * 地区编码
	 */
	private String regionCode;

	/**
	 * IP地址
	 */
	private String ip;

	/**
	 * 折扣描述
	 */
	private String des;

	/**
	 * 每日时间
	 */
	private String gmtDay;

	/**
	 * 状态：0未中奖，1中奖, 2已使用
	 */
	private Integer status;

	private JSONObject desJson;

	/**
	 * 是否存在
	 */
	public boolean isExist(String key) {
		return DbContant.YES.equals(getFeature(key));
	}

	/**
	 * 获取属性值
	 */
	public String getFeature(String key) {
		initDesJson();
		return desJson.getString(key);
	}

	/**
	 * 设置属性
	 */
	public void putDes(String key, String value) {
		initDesJson();
		desJson.put(key, value);
	}

	public String getDes() {
		if (desJson != null)
			return desJson.toString();
		return des;
	}

	private void initDesJson() {
		if (desJson == null) {
			if (StringUtils.isNotEmpty(des)) {
				desJson = JSON.parseObject(des);
			} else {
				desJson = new JSONObject();
			}
		}
	}

}
