package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年09月09日
 */
@Alias("ChanelItemQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelItemQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	private Long id;

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

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
	private Date gmtModified;

}
