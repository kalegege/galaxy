package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年08月30日
 */
@Alias("ChanelTeamRecommendQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelTeamRecommendQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	private Long id;

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

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
	private Date gmtModified;

}
