package com.wasu.ptyw.galaxy.common.util;

import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据UserHomePlaceholderConfigurer初始化spring placehold时触发加载属性文件
 * 
 * @author wenguang
 * @date 2015年5月14日
 */
@Slf4j
public class PropertiesUtil {

	/**
	 * @Fields properties : 属性对象
	 */
	private static Properties properties;

	/**
	 * @Description: 根据属性key获取属性value
	 * @param key
	 * @return value
	 */
	public static String getValue(String key) {
		if (properties == null) {
			log.error("init config.properties error");
			return null;
		}
		return properties.getProperty(key);
	}

	/**
	 * 设置properties
	 * 
	 * @param prop
	 */
	public static void setProperties(Properties prop) {
		properties = prop;
	}

}
