package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Alias("MenuDetail")
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuDetailDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public MenuDetailDO() {

	}

	private String chId;
	
	private String isPlayback;
	
	private String chName;
	
	private String folder;
	
	private String assetId;
	
	private String contentId;
	
	private String chlogourl;

	private List<MenuInfoDO> eventList;
	
	private List<MenuInfoDO> menuList;
	
}
