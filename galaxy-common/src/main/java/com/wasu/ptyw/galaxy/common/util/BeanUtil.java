package com.wasu.ptyw.galaxy.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.ConvertUtils;

public class BeanUtil extends org.apache.commons.beanutils.BeanUtils {
	private BeanUtil() {
	}

	static {
		ConvertUtils.register(new DateConverter(), java.util.Date.class);
		ConvertUtils.register(new DateConverter(), java.sql.Date.class);
		ConvertUtils.register(new DateConverter(), java.sql.Timestamp.class);
	}

	public static void copyProperties(Object target, Object source) throws InvocationTargetException,
			IllegalAccessException {
		org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
	}
	
//	public static <T> T cloneBean(T bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException{
//		return (T) org.apache.commons.beanutils.BeanUtils.cloneBean(bean);
//	}
	
	public static <T> T getFieldValue(Object obj, String fieldName) throws Exception {
		Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return (T) field.get(obj);
	}
}
