package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("AssertMenuAll")
@Data
@EqualsAndHashCode(callSuper = true)
public class AssertMenuAllDO extends BaseDO implements Serializable{
	private static final long serialVersionUID = 1L;

	public AssertMenuAllDO() {
		
	}
	
	private List<AssertMenuFirstDO> ch;
	
	private String folder;
	
	private String cctvLength;
	
	private String siteLength;
	
	private String locaLength;
	
	private String chilLength;
}
