package com.wasu.ptyw.galaxy.core.dto;

import lombok.Data;

/**
 * @author wenguang
 * @date 2016年08月11日
 */
@Data
public class ChanelItemDTO {

	public ChanelItemDTO() {

	}

	private long id;

	private String chName;
	
	private String byname;

	private Integer type;

	private String chlogourl;
	
	private String chlogoName;
	
	private Long orderId;
	
	private Integer chtype;

}
