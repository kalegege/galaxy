package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

@Data
public class ChanelActor1DO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public ChanelActor1DO() {

	}

	private String name;

	private String sex;

	private String birth;
	
    private String des;
    
	private String background;

	private String backgroundName;
	
	private String poster;
	
	private String posterName;

	private Integer status;

}
