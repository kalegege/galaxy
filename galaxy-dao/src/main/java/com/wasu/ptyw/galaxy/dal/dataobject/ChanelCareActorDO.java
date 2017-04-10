package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年11月17日
 */
@Alias("ChanelCareActorDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelCareActorDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public ChanelCareActorDO() {

	}

	private String uid;

	private Integer actorId;

	private String regionId;

	private Integer sequence;

	private Integer status;

}
