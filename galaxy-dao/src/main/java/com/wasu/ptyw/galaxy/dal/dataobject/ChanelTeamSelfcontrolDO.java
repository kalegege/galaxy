package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年09月13日
 */
@Alias("ChanelTeamSelfcontrolDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelTeamSelfcontrolDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public ChanelTeamSelfcontrolDO() {

	}

	private String chId;

	private Integer chNo;

	private String chName;

	private String chPicture;

	private String chImage;

	private Integer chStatus;

	private Integer chWeight;

	private String chAlias;

	private String regionId;

}
