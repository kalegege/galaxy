package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年09月10日
 */
@Alias("ChanelItemTeamQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelItemTeamQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long teamId;

	private Long itemId;

	private Long orderId;

	/**
	 * 修改时间
	 */
	private Date gmtModified;

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

}
