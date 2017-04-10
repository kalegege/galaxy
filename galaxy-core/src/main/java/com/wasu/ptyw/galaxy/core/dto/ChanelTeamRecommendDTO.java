package com.wasu.ptyw.galaxy.core.dto;

import lombok.Data;

/**
 * @author wenguang
 * @date 2016年08月11日
 */
@Data
public class ChanelTeamRecommendDTO {
	public ChanelTeamRecommendDTO() {

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

	private String regionId;

}
