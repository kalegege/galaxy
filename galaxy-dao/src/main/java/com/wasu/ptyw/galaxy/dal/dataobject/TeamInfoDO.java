package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("TeamInfo")
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamInfoDO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public TeamInfoDO() {

	}

	private String sid;

	private String rate;

	private String mod;
	
	private String freq;
	
}
