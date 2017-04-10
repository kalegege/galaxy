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
@Alias("AssertMenuDetail")
@Data
@EqualsAndHashCode(callSuper = true)
public class AssertMenuDetailDO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public AssertMenuDetailDO() {
		
	}
	
	private List<AssertMenuDO> today;
	
	private List<AssertMenuDO> ysday;
	
	private List<AssertMenuDO> beday;
}
