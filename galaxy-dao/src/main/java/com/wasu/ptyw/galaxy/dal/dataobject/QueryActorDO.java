package com.wasu.ptyw.galaxy.dal.dataobject;
import org.apache.ibatis.type.Alias;

import lombok.Data;
@Alias("QueryActorDO")
@Data
public class QueryActorDO {
	public QueryActorDO() {

	}
	private String type;

	private String name;

//	private String year;

	private String Ntype;
	
	private String id;
	
	private String folder;
	
	private String pic;
}
