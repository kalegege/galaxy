package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("MenuAll")
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuAllDO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public MenuAllDO() {

	}

	private String version;

	private String result;

	private String resultDesc;
	
	private List<MenuDetailDO> chList;
}
