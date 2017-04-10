/**
 * 
 */
package com.wasu.ptyw.galaxy.core.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author wenguang
 * 
 */
@Service("beanUtil")
public class BeanUtil implements ApplicationContextAware {

	private static ApplicationContext ac;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		BeanUtil.ac = applicationContext;
	}

	public static Object getBean(String name) throws BeansException {
		try {
			return ac.getBean(name);
		} catch (Exception e) {
		}
		return null;
	}

	public static Object getBean(String name, Class<?> requiredType)
			throws BeansException {
		try {
			ac.getBean(name, requiredType);
		} catch (Exception e) {
		}
		return null;
	}

	public static boolean containsBean(String name) {
		return ac.containsBean(name);
	}

	public static boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {
		try {
			ac.isSingleton(name);
		} catch (Exception e) {
		}
		return false;
	}

}
