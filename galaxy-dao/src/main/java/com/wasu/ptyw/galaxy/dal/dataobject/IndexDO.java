package com.wasu.ptyw.galaxy.dal.dataobject;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Alias("IndexDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class IndexDO extends BaseDO{
	private static final long serialVersionUID = 1L;
	public IndexDO() {

	}
	private List<IndexDataDO> recommend;
	
	private IndexSelfcontrolDO selfcontrol;
	
	private List<ChanelActorDO> actor;
}
