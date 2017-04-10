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
@Alias("AssertMenuSecond")
@Data
@EqualsAndHashCode(callSuper = true)
public class AssertMenuSecondDO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public AssertMenuSecondDO() {
		
	}
	
	private AssertMenuDetailDO chN;
	
	private AssertMenuDescDO chM;
}
