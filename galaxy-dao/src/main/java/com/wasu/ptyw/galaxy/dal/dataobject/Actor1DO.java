package com.wasu.ptyw.galaxy.dal.dataobject;

import java.util.List;

import lombok.Data;

@Data
public class Actor1DO {
	private String totalPages;
	
	private String totalItems;
	
	private String pageIndex;
	
	private String correctionKeyWord;
	
	private List<Actor1DTO> contents;
}
