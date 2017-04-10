package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年12月01日
 */
@Alias("ChanelActorDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelActorDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public ChanelActorDO() {

	}

	private String name;

	private String sex;

	private String birth;

	private String des;

	private String image;

	private String imageName;

	private String poster;

	private String posterName;
	
	private String background;

	private String backgroundName;

	private Integer status;
	
	private Long publishid;
	

}
