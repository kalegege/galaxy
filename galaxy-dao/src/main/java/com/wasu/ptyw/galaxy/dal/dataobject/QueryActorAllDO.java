package com.wasu.ptyw.galaxy.dal.dataobject;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;
@Alias("QueryActorAllDO")
@Data
public class QueryActorAllDO {
	public QueryActorAllDO() {

	}
	private List<QueryActorDO> data;
}
