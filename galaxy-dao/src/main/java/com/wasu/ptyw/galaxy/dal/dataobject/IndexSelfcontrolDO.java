package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年07月21日
 */
@Alias("IndexSelfcontrolDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class IndexSelfcontrolDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public IndexSelfcontrolDO() {

	}

	private String chNo;

	private String chName;

	private String chPicture;

//	private Integer chStatus;
//
//	private Integer chWeight;
//
//	private String chAlias;
//
//	private String regionId;

}
