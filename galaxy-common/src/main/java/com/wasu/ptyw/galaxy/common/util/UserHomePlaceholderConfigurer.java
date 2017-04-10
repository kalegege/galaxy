/**
 * 
 */
package com.wasu.ptyw.galaxy.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * 从用户目录下获取配置文件导入到spring placeholde的方法
 * location的位置是取用户目录下相对位置，比如linux下的root帐号是/root
 * 
 * @author wenguang
 * @date 2015年5月14日
 */
public class UserHomePlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	@Override
	public void setLocations(Resource[] locations) {
		if (locations == null || locations.length == 0)
			return;
		String userHomeDir = System.getProperty("user.home");

		// 导入到spring的placehold中
		Resource[] userHomelocations = new Resource[locations.length];
		for (int i = 0; i < locations.length; i++) {
			userHomelocations[i] = new FileSystemResource(userHomeDir + File.separator + locations[i].getFilename());
		}
		super.setLocations(userHomelocations);

		// 导入到PropertiesUtil中
		Properties prop = new Properties();
		for (Resource location : userHomelocations) {
			InputStream is = null;
			try {
				is = location.getInputStream();
				prop.load(is);
				PropertiesUtil.setProperties(prop);
			} catch (Exception e) {
				logger.error("[UserHomePlaceholderConfigurer-setLocations] Could not load properties from " + location
						+ ": " + e.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}

		}
	}
}
