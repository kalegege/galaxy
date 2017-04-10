package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("LiveDetail")
@Data
@EqualsAndHashCode(callSuper = true)
public class LiveDetailDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public LiveDetailDO() {

	}

	private String contentId;

	private String name;
	
	private String programName;
	
	private String beginTime;
	
	private String endTime;
	
	private String count;
	
	private String description;
}
