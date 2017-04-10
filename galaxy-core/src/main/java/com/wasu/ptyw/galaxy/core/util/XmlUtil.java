/**
 * 
 */
package com.wasu.ptyw.galaxy.core.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * @author wenguang
 * @date 2015年6月19日
 */
public class XmlUtil {
	public static Object getObjectFromXML(String xml, Class<?> tClass) {
		return getObjectFromXML(xml, tClass, "xml");
	}

	public static Object getObjectFromXML(String xml, Class<?> tClass, String alias) {
		// 将从API返回的XML数据映射到Java对象
		XStream xStreamForResponseData = new XStream();
		xStreamForResponseData.alias(alias, tClass);
		xStreamForResponseData.ignoreUnknownElements();// 暂时忽略掉一些新增的字段
		return xStreamForResponseData.fromXML(xml);
	}

	public static String objToXml(Object xmlObj) {
		// 解决XStream对出现双下划线的bug
		XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xStreamForRequestPostData.alias("xml", xmlObj.getClass());
		return xStreamForRequestPostData.toXML(xmlObj);
	}
}
