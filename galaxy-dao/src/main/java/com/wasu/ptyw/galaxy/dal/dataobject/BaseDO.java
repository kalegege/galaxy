/**
 * 
 */
package com.wasu.ptyw.galaxy.dal.dataobject;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author wenguang
 * @date 2015年6月3日
 */
public class BaseDO implements Serializable {
	private static final long serialVersionUID = -5900252002442493334L;

	/**
	 * 自增ID
	 */
	private @Setter@Getter Long id;

	/**
	 * 创建时间
	 */
	private @Setter@Getter Date gmtCreate;

	/**
	 * 修改时间
	 */
	private @Setter@Getter Date gmtModified;

}
