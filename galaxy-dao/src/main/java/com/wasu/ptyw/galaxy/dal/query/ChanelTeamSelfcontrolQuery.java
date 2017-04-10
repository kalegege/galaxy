package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年09月13日
 */
@Alias("ChanelTeamSelfcontrolQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelTeamSelfcontrolQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String chId;

	private Integer chNo;

	private String chName;

	private String chPicture;

	private String chImage;

	private Integer chStatus;

	private Integer chWeight;

	private String chAlias;

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
