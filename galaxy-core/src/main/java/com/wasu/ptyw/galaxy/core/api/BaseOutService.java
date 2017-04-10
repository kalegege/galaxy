/**
 * 
 */
package com.wasu.ptyw.galaxy.core.api;

import org.apache.commons.lang.StringUtils;

import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;

/**
 * @author wenguang
 * @date 2015年11月10日
 */
public class BaseOutService {
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	public static String buildOutTradeNo(GalaxyOrderFilmDO order) {
		return DbContant.TRADE_NO_PRE + order.getRegionCode() + "-" + order.getId();
	}

	public static long toOrderId(String outTradeNo) {
		long orderId = 0;
		try {
			int index = StringUtils.indexOf(outTradeNo, "-");
			if (index > 0) {
				orderId = Long.parseLong(outTradeNo.substring(index + 1));
			} else {
				orderId = Long.parseLong(StringUtils.remove(outTradeNo, DbContant.TRADE_NO_PRE));
			}
		} catch (Exception e) {
		}
		return orderId;
	}

}
