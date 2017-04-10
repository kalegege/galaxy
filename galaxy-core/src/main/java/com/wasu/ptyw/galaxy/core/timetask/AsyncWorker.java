/**
 * 
 */
package com.wasu.ptyw.galaxy.core.timetask;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.core.ao.GalaxyOrderFilmAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyWeixinFollowAO;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;

/**
 * 异步执行的任务
 * 
 * @author wenguang
 * @date 2015年9月21日
 */
@Service("asyncWorker")
@Slf4j
public class AsyncWorker {
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;
	@Resource
	GalaxyWeixinFollowAO galaxyWeixinFollowAO;

	@Async
	@Deprecated
	public void wxFollowPaySuccess(String followIdStr, Long userId) {
		long followId = NumberUtils.toLong(followIdStr);
		if (followId < 1 || userId == null || userId < 1)
			return;

		//galaxyWeixinFollowAO.updateUsedStatusByIds(Lists.newArrayList(followId), DbContant.ONE);
		// closeWxOrder(followIdStr, userId);
	}

	/**
	 * 关闭微信订单,status 1->4,0->3
	 */
	private void closeWxOrder(String followIdStr, long userId) {
		log.info("closeWxOrder start, userId=" + userId);
		int updateCount = 0;
		// 处理待付款订单
		GalaxyOrderFilmQuery query = buildWxQuery(OrderFilmStatus.PREPAY, userId);
		Result<List<GalaxyOrderFilmDO>> orderResult = galaxyOrderFilmAO.query(query);
		if (orderResult.isSuccess() && CollectionUtils.isNotEmpty(orderResult.getValue())) {
			List<Long> ids = Lists.newArrayList();
			for (GalaxyOrderFilmDO order : orderResult.getValue()) {
				if (StringUtils.equals(followIdStr, order.getFeature(DbContant.FEA_WX_FOLLOW)))
					ids.add(order.getId());
			}
			if (CollectionUtils.isNotEmpty(ids)) {
				// 更新为取消状态
				galaxyOrderFilmAO.updateStatusByIds(ids, OrderFilmStatus.CLOSED.getCode());
				updateCount += ids.size();
			}

		}

		// 处理新订单
		query = buildWxQuery(OrderFilmStatus.NEW, userId);
		orderResult = galaxyOrderFilmAO.query(query);
		if (orderResult.isSuccess() && CollectionUtils.isNotEmpty(orderResult.getValue())) {
			List<Long> ids = Lists.newArrayList();
			for (GalaxyOrderFilmDO order : orderResult.getValue()) {
				if (StringUtils.equals(followIdStr, order.getFeature(DbContant.FEA_WX_FOLLOW)))
					ids.add(order.getId());
			}
			if (CollectionUtils.isNotEmpty(ids)) {
				// 更新为取消状态
				galaxyOrderFilmAO.updateStatusByIds(ids, OrderFilmStatus.CANCELED.getCode());
				updateCount += ids.size();
			}
		}
		log.info("closeWxOrder end, updateCount:" + updateCount);
	}

	private GalaxyOrderFilmQuery buildWxQuery(OrderFilmStatus status, long userId) {
		GalaxyOrderFilmQuery query = new GalaxyOrderFilmQuery();
		query.setUserId(userId);
		query.setStatus(status.getCode());
		query.setPageSize(Integer.MAX_VALUE);
		query.setCurrentPage(1);
		return query;
	}
}
