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
@Alias("TeamAll")
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamAllDO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public TeamAllDO() {
		
	}

	private Integer version;

	private Integer result;

	private String resultDesc;
	
	private String firstChId;
	
	private Integer isPurchased;
	
	private List<TeamBlistDO> bList;
}
