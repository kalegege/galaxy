package com.wasu.ptyw.galaxy.dal.dataobject;

import java.util.List;

import lombok.Data;

@Data
public class Actor1DTO {
	private String actors;
	
	private String category;
	
	private String contentId;
	
	private String contentType;
	
	private String description;
	
	private String director;
	
	private String hot;
	
	private String items;
	
	private String keyword;
	
	private String name;
	
	private String programType;
	
	private String region;
	
	private String typeLargeItem;
	
	private String typeSecondItem;
	
	private String year;
	
	private List<ImageDO> imageFiles;

}
