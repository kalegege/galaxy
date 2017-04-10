package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2015年12月14日
 */
@Alias("GalaxyFilmSectionQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyFilmSectionQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	public GalaxyFilmSectionQuery() {

	}

	public GalaxyFilmSectionQuery(boolean queryCount) {
		setQueryCount(queryCount);
	}

	private Long id;

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

	private Date gmtCreate;

	private Date gmtModified;

	/**
	 * 影片ID
	 */
	private List<Long> filmIds;

}
