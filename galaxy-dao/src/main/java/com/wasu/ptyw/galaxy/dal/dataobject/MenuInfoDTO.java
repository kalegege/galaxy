package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("MenuInfoDTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuInfoDTO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public MenuInfoDTO() {

	}

	private String name;
	
	private String startDate;

	private String startTime;

	private String stopDate;

	private String stopTime;
}
