package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("MenuInfo")
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuInfoDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public MenuInfoDO() {

	}

	private String eventId;

	private String eventName;
	
	private String startTime;
	
	private String endTime;
}
