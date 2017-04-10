package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("TeamDetailDTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamDetailDTO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public TeamDetailDTO() {

	}

//	private String desc;
//
//	private String hdChId;

	private String chNo;
	
	private String chId;
	
//	private String casid;
//	
//	private Integer isHdCh;
//	
//	private TeamUrlDO ipLive;
//	
//	private Integer type;
	
	private String chName;
	
//	private String chLogoURL;
//	
//	private String capid;
//	
//	private TeamInfoDO dvbLive;
//	
//	private String menunow;
//	
//	private String menunext;
}
