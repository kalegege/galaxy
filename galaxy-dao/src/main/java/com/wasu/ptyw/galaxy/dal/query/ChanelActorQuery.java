package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2016年12月01日
 */
@Alias("ChanelActorQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChanelActorQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String sex;

	private String birth;

	private String des;

	private String image;

	private String imageName;

	private String poster;

	private String posterName;
	
	private String background;

	private String backgroundName;

	private Integer status;

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
	private Date gmtModified;
	
    private Integer publishid;

}
