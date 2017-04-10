package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年08月11日
 */
@Alias("ChanelItemDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelItemDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public ChanelItemDO() {

	}

	private String chId;

	private Integer chNo;

	private String chName;

	private String byname;

	private String regionId;

	private String assetid;

	private String folder;

	private Integer isPlayback;

	private Integer isBoardcast;

	private Integer ishdch;

	private Integer type;
	
	private Integer chtype;

	private String playurl;

	private String chlogourl;
	
	private String chlogoName;

	private String desc;

	private String hdchid;

	private String freq;

	private String mod;

	private String rate;

	private String sid;

	private String casid;

	private String capid;

}
