package com.wasu.ptyw.galaxy.common.util;

import java.util.Date;

import org.apache.commons.beanutils.converters.DateTimeConverter;

public class DateConverter extends DateTimeConverter {
	public DateConverter() {
		super();
	}

	public DateConverter(Object defaultValue) {
		super(defaultValue);
	}

	protected Class getDefaultType() {
		return Date.class;
	}

	public Object convert(Class type, Object value) {
		if (value == null) {
			return null;
		} else {
			return super.convert(type, value);
		}
	}
}
