package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("LiveReal")
@Data
@EqualsAndHashCode(callSuper = true)
public class LiveRealDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public LiveRealDO() {

	}

	private String totalCount;

	private List<LiveDetailDO> items;
	
	
}
