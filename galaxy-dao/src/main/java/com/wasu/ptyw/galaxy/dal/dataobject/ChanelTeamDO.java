package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月29日
 */
@Alias("ChanelTeamDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelTeamDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public ChanelTeamDO() {

	}

	private String bId;

	private String bName;

	private String regionId;

	private String firstchid;

	private Integer sortby;

}
