/**
 * 
 */
package com.wasu.ptyw.galaxy.core.api;

import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;

import com.wasu.ptyw.galaxy.core.cache.LocalCache;

/**
 * @author wenguang
 * @date 2015年10月20日
 */
public class LogicUtil {
	public static final int EXPIRED_HOUR = 48;

	/**
	 * 获取订单超时时间
	 */
	public static Date getOrderExpiredTime() {
		return DateTime.now()
				.plusHours(NumberUtils.toInt(LocalCache.get(LocalCache.KEY_ORDER_EXPIRED_HOUR), EXPIRED_HOUR)).toDate();
	}

	public static void sleep(long millis) {
		try {
			if (millis > 0)
				Thread.sleep(millis);
		} catch (Exception e) {
		}
	}
}
