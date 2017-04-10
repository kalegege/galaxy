package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2015年08月24日
 */
@Alias("SysSettingDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSettingDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public SysSettingDO() {

	}

	/**
	 * 设置名字
	 */
	private String name;

	/**
	 * 设置值
	 */
	private String data;

	/**
	 * 备注
	 */
	private String remark;

}
