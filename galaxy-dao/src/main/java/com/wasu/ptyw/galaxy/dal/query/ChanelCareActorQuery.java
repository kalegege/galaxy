package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年11月17日
 */
@Alias("ChanelCareActorQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelCareActorQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String uid;

	private Integer actorId;

	private String regionId;

	private Integer sequence;

	private Integer status;

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
	private Date gmtModified;

}
