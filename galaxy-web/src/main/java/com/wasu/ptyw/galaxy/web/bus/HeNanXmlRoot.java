/**
 * 
 */
package com.wasu.ptyw.galaxy.web.bus;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

import com.google.common.collect.Lists;

/**
 * @author wenguang
 * @date 2015年11月19日
 */
@Data
@XmlRootElement(namespace = "RecommendList")
public class HeNanXmlRoot {
	List<HeNanRecommend> list = Lists.newArrayList();
}
