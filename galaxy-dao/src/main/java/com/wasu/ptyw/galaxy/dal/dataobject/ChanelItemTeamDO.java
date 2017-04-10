package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年09月10日
 */
@Alias("ChanelItemTeamDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelItemTeamDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public ChanelItemTeamDO() {

	}

	private Long teamId;

	private Long itemId;

	private Long orderId;

}
