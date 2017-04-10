package com.wasu.ptyw.galaxy.dal.persist;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

public class MapQuery extends HashMap<String, Object> {
	private static final long serialVersionUID = 36641694783048665L;

	private @Setter@Getter Integer totalItem = 0;
	
	private @Setter@Getter Integer pageSize = 20;
	
	private @Setter@Getter Integer currentPage = 1;
	
	public MapQuery(){
	}
	
	public MapQuery(String key, Object value){
		put(key, value);
	}
	
	public MapQuery(String k1, Object v1, String k2, Object v2){
		put(k1, v1);
		put(k2, v2);
	}
	
	public MapQuery(String k1, Object v1, String k2, Object v2, String k3, Object v3){
		put(k1, v1);
		put(k2, v2);
		put(k3, v3);
	}
	
	public Object put(String key, Object value) {
		if(value != null){
			return super.put(key, value);
		}
		return null;
	}

	public int getTotalPage() {
		if (totalItem == null || pageSize == null || pageSize == 0) {
			return 0;
		}

		return (totalItem - 1) / pageSize + 1;
	}
	
	public int getPageFirstItem() {
		return (currentPage - 1) * pageSize + 1;
	}

	public int getPageEndItem(){
		return Math.min(currentPage * pageSize, totalItem);
	}
}