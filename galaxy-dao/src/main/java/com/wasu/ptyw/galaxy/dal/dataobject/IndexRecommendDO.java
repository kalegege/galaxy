package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年08月10日
 */
@Alias("IndexRecommendDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class IndexRecommendDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public IndexRecommendDO() {

	}

	private String chId;

	private String menu;

	private String name;

	private String date;

	private String time;

//	private String stopDate;
//
//	private String stopTime;

//	private String aliasName;

	private String picture;
	
	private String status;
	
	private String url;
	
	private String chNo;
	
	private String folder;
	
	private String assertId;
	
	private CastDTO cast;
	
//	private Integer bStatus;
//
//	private String bName;
//
//	private String bDate;
//
//	private String bTime;
//
//	private String bAliasName;
//
//	private String bUrl;
//
//	private Integer aStatus;
//
//	private String aName;
//
//	private String aDate;
//
//	private String aTime;
//
//	private String aAliasName;
//
//	private String aUrl;

//	private String regionId;
	


}
