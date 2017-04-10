package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("TeamUrl")
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamUrlDO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public TeamUrlDO() {

	}

	private String playURL;

	
}
