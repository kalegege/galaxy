package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年08月30日
 */
@Alias("ChanelTeamRecommendDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelTeamRecommendDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public ChanelTeamRecommendDO() {

	}

	private String chTeam;

	private String chId;

	private String chName;

	private String name;

	private String startDate;

	private String startTime;

	private String stopDate;

	private String stopTime;

	private String aliasName;

	private String picture;

	private String pictureName;

	/**
	 * 2回放1点播
	 */
	private Integer bStatus;

	private String bName;

	private String bDate;

	private String bTime;

	private String bAliasName;

	private String bVodName;

	private String bUrl;

	private String bFolder;

	private String bAssertid;

	/**
	 * 2回放1点播
	 */
	private Integer aStatus;

	private String aName;

	private String aDate;

	private String aTime;

	private String aAliasName;

	private String aVodName;

	private String aUrl;

	private String aFolder;

	private String aAssertid;

	private String regionId;

}
