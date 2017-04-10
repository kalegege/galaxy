package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("TeamBlistDTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamBlistDTO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public TeamBlistDTO() {

	}

//	private Integer sortby;
//
//	private String firstChId;

	private String bId;
	
	private String bName;
	
	private List<TeamDetailDTO> chList;
	
	public void add(TeamDetailDTO obj){
		List<TeamDetailDTO> result=new LinkedList<TeamDetailDTO>();
		if(chList!=null){
			result.addAll(chList);
		}
		result.add(obj);
		this.chList=result;
	}
	
	public void add(List<TeamDetailDTO> obj){
		List<TeamDetailDTO> result=new LinkedList<TeamDetailDTO>();
		result.addAll(chList);
		result.addAll(obj);
		chList=result;
	}
	
	
}
