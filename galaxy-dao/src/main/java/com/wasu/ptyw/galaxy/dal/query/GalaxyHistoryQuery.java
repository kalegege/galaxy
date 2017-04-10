package com.wasu.ptyw.galaxy.dal.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.dal.persist.SimpleQuery;

/**
 * @author wenguang
 * @date 2015年07月07日
 */
@Alias("GalaxyHistoryQuery")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyHistoryQuery extends SimpleQuery {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	private Long id;

	/**
	 * 本系统用户ID
	 */
	private Long userId;

	/**
	 * 外部用户ID
	 */
	private String outUserId;

	/**
	 * 影片ID
	 */
	private Long filmId;

	/**
	 * 片源code
	 */
	private String contCode;

	/**
	 * 片源名称
	 */
	private String contName;

	/**
	 * 片源图片地址
	 */
	private String contImage;

	/**
	 * 地区编码
	 */
	private String regionCode;

	/**
	 * 观看时间，单位秒
	 */
	private Integer playTime;

	/**
	 * 过期时间
	 */
	private Date expiredDate;

	/**
	 * 创建时间
	 */
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
	private Date gmtModified;

}
