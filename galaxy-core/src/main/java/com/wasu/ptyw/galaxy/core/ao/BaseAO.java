package com.wasu.ptyw.galaxy.core.ao;

import org.apache.commons.beanutils.BeanUtils;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;

/**
 * @author wenguang
 * @date 2015年06月09日
 */
public class BaseAO {
	public <T> Result<T> setErrorMessage(Result<T> t, ResultCode rc) {
		t.setErrorMessage(rc.getCode(), rc.getMessage());
		return t;
	}
	
	public void copyProperties(Object dest, Object orig) {
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
		}
	}
}
