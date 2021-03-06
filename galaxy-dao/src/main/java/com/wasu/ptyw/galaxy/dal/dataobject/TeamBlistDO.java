package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("TeamBlist")
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamBlistDO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public TeamBlistDO() {

	}

	private Integer sortby;

	private String firstChId;

	private String bId;
	
	private String bName;
	
	private List<TeamDetailDO> chList;
}
