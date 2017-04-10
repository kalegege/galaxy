/**
 * 
 */
package com.wasu.ptyw.galaxy.web.bus;

import lombok.Data;

import com.sun.xml.txw2.annotation.XmlElement;

/**
 * @author wenguang
 * @date 2015年11月19日
 */
@Data
@XmlElement(value = "Recommend")
public class HeNanRecommend {
	/**
	 * 推荐类型：1：业务推荐，2：栏目推荐，3：影片推荐(华数此值取3)
	 */
	private int type = 3;

	/**
	 * 推荐唯一标识，双向系统将根据该标识确认推荐数据是否发生变化
	 */
	private String Code;

	/**
	 * 推荐标题
	 */
	private String title;

	/**
	 * 推荐海报ftp下载地址，多个中间用竖线隔开
	 */
	private String poster;

	/**
	 * 07推荐详情页地址
	 */
	private String detailURL07;

	/**
	 * 07推荐播放地址（详情页地址和播放地址不能同时为空，如果详情页不为空，点击推荐海报时进入详情页，否则进入播放）
	 */
	private String playURL07;
}
