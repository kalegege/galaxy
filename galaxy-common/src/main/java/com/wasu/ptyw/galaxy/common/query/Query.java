package com.wasu.ptyw.galaxy.common.query;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.wasu.ptyw.galaxy.common.dataobject.Paginator;




/**
 * 多用途Query,作用是： 存放参数 + 分页 + URL渲染
 * 可用于层间的双向传递
 * screen < - > ao < - > manager < - > dao
 * @author xiaofeng
 */
public abstract class Query implements Serializable {

    /**
	 * 序列号
	 */
	private static final long serialVersionUID = -7529570395689793396L;

	/**
     * 分页代理类
     */
    private Paginator paginator = new Paginator();


    //=========================将分页代理给Paginator=========================//
    /**
     * 当前是否是第一页
     * @return 是否
     */
    public boolean isFirstPage() {
        return getPaginator().isFirstPage();
    }

    /**
     * 获取上一页页号
     * @return 上一页页号
     */
    public int getPreviousPage() {
        return getPaginator().getPreviousPage();
    }

    /**
     * 当前是否是最后一页
     * @return
     */
    public boolean isLastPage() {
        return getPaginator().isLastPage();
    }

    /**
     * 获取下一页页号
     * @return 下一页页号
     */
    public int getNextPage() {
        return getPaginator().getNextPage();
    }

    /**
     * 取当前页号
     * @return 当前页号
     */
    public Integer getCurrentPage() {
        return getPaginator().getCurrentPage();
    }

    /**
     * 字符串方式设置当前页号
     * @param currentPage
     */
    public void setCurrentPageString(String currentPage) {
        getPaginator().setCurrentPageString(currentPage);
    }

    /**
     * 设置当前页号
     * @param fPage
     */
    public void setCurrentPage(Integer fPage) {
        getPaginator().setCurrentPage(fPage);
    }

    /**
     * 获取每页记录数
     * @return
     */
    public Integer getPageSize() {
        return getPaginator().getPageSize();
    }

    /**
     * 字符串方式设置每页记录数
     * @param pageSizeString
     */
    public void setPageSizeString(String pageSizeString) {
        getPaginator().setPageSizeString(pageSizeString);
    }

    /**
     * 设置每页记录数
     * @param pSize
     */
    public void setPageSize(Integer pSize) {
        getPaginator().setPageSize(pSize);
    }

    /**
     * 获取总记录数
     * @return 总记录数
     */
    public int getTotalItem() {
        return getPaginator().getTotalItem();
    }

    /**
     * 设置总记录数
     * @param tItem
     */
    public void setTotalItem(int tItem) {
        getPaginator().setTotalItem(tItem);
    }

    /**
     * 获取总页数
     * @return 总页数
     */
    public int getTotalPage() {
        return getPaginator().getTotalPage();
    }

    /**
     * 计算开始行并返回
     * @return 开始行号
     */
    public int getPageFirstItem() {
        return getPaginator().getPageFirstItem() - 1;
    }

    /**
     * 计算结束行并返回
     * @return 结束行号
     */
    public int getPageLastItem() {
        return getPaginator().getPageLastItem();
    }

    /**
     * 获取开始行
     * @return the startRow
     */
    public int getStartRow() {
        return getPaginator().getStartRow()-1;
    }

    /**
     * 获取结束行
     * @return the endRow
     */
    public int getEndRow() {
        return getPaginator().getEndRow();
    }

    /**
     * 获取分页
     * @return Paginator
     */
    public Paginator getPaginator() {
        return paginator;
    }
    //添加offset和length属性，供mysql的limit offset,length查询使用  add by 惊羽
    public int getOffset(){
    	return paginator.getStartRow() -1;
    }
    public int getLength(){
    	return paginator.getEndRow() - paginator.getStartRow() + 1;
    }

    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }

}
