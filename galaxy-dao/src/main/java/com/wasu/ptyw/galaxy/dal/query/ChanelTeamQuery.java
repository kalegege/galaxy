package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年06月29日
 */
@Alias("ChanelTeamQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelTeamQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String bId;

	private String bName;

	private String regionId;

	private String firstchid;

	private Integer sortby;

	/**
	 * 修改时间
	 */
	private Date gmtModified;

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

}
