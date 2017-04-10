package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("AssertMenuDTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AssertMenuDTO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public AssertMenuDTO() {
		
	}
	
	private String index;
	
	private String name;
	
	private String date;
	
	private String time;
	
	private String folder;
	
	private String assetId;
}
