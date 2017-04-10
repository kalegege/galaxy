package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("TeamAllDTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamAllDTO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public TeamAllDTO() {
		
	}

	private String version;

	private String result;

	private String resultDesc;
	
	private String firstChId;
	
	private String isPurchased;
	
	private List<TeamBlistDTO> bList;
	
	public void add(List<TeamBlistDTO> objs){
		List<TeamBlistDTO> newList=new ArrayList<TeamBlistDTO>();
		newList.addAll(bList);
		newList.addAll(objs);
		bList=newList;
	}
}
