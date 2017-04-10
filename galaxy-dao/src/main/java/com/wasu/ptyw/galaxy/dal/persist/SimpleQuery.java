package com.wasu.ptyw.galaxy.dal.persist;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class SimpleQuery implements Serializable {
	private static final long serialVersionUID = 36641694783048665L;

	@Getter
	private Integer totalItem = 0;

	@Setter
	@Getter
	private Integer pageSize = 20;

	@Setter
	@Getter
	private Integer currentPage = 1;

	@Setter
	@Getter
	private String orderBy = "id desc";

	@Setter
	@Getter
	private boolean queryCount = true;

	public void setTotalItem(Integer totalItem) {
		this.totalItem = totalItem;
		int lastPage = getLastPage();
		currentPage = currentPage > lastPage ? lastPage : currentPage;
		currentPage = currentPage < 1 ? 1 : currentPage;
	}

	/**
	 * 获取总页数
	 */
	public int getTotalPage() {
		if (totalItem == 0 || pageSize == 0) {
			return 0;
		}

		return (totalItem - 1) / pageSize + 1;
	}

	/**
	 * 获取当前页的开始行数
	 */
	public int getPageFirstItem() {
		return (currentPage - 1) * pageSize;
	}

	/**
	 * 获取当前页的结束行数
	 */
	public int getPageEndItem() {
		return Math.min(currentPage * pageSize, totalItem);
	}

	/**
	 * 获取上一页
	 */
	public int getPrevPage() {
		return currentPage > 1 ? currentPage - 1 : 1;
	}

	/**
	 * 获取下一页
	 */
	public int getNextPage() {
		int lastPage = getLastPage();
		return currentPage < lastPage ? currentPage + 1 : lastPage;
	}

	/**
	 * 获取最后页
	 */
	public int getLastPage() {
		int totalPage = getTotalPage();
		totalPage = totalPage == 0 ? 1 : totalPage;
		return totalPage;
	}
}