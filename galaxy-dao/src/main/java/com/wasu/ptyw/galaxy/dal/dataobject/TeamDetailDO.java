package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("TeamDetail")
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamDetailDO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public TeamDetailDO() {

	}

	private String desc;

	private String hdChId;

	private Integer chNo;
	
	private String chId;
	
	private String casid;
	
	private Integer isHdCh;
	
	private TeamUrlDO ipLive;
	
	private Integer type;
	
	private String chName;
	
	private String chLogoURL;
	
	private String capid;
	
	private TeamInfoDO dvbLive;
	
	private String menunow;
	
	private String menunext;
}
