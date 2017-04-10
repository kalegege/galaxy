package com.wasu.ptyw.galaxy.dal.dataobject;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Alias("IndexDataDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class IndexDataDO extends BaseDO{
	private static final long serialVersionUID = 1L;
	public IndexDataDO() {

	}
	private IndexRecommendDO data;
	
	private CastDTO cast;
}
