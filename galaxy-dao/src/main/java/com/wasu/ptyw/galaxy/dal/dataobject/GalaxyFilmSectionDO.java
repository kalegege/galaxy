package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

/**
 * @author wenguang
 * @date 2015年12月14日
 */
@Alias("GalaxyFilmSectionDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyFilmSectionDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public GalaxyFilmSectionDO() {

	}

	/**
	 * 地区编码
	 */
	private String regionCode;

	/**
	 * 影片ID
	 */
	private Long filmId;

	/**
	 * 1今日推荐，2院线热映，3在线购票
	 */
	private Integer secId;

	/**
	 * 状态，0预发，1发布
	 */
	private Integer status;

	/**
	 * 优先级
	 */
	private Integer priority;

}
