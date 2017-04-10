/*
 * @(#)TvbAuctionTimeTaskImpl.java	1.0 2010-6-8
 *
 * Copyright 2010 taobao.com. All rights reserved.
 */
package com.wasu.ptyw.galaxy.core.timetask;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.core.ao.GalaxyDiscountAccessAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyOrderFilmAO;
import com.wasu.ptyw.galaxy.core.ao.GalaxyWeixinFollowAO;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;

/**
 * 订单相关定时任务
 * 
 * @author wenguang
 * @date 2015年7月14日
 */
@Service("syncOrderTimeTask")
@Slf4j
public class SyncOrderTimeTask {
	// 每个线程需要执行的最大次数
	protected final static int executeMaxCount = 1000;

	// 每次从数据库取得的记录数
	protected final static int pageSize = 200;

	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;
	@Resource
	GalaxyWeixinFollowAO galaxyWeixinFollowAO;
	@Resource
	GalaxyDiscountAccessAO galaxyDiscountAccessAO;

	/**
	 * 关闭订单
	 */
	public void sync() {
		closeNewOrder();
		closePreOrder();
	}

	/**
	 * 关闭新建订单,status 0->3
	 */
	public void closeNewOrder() {
		log.info("closeNewOrder start");
		// 执行分页查询次数
		int i = 0;
		// 更新总数
		int updateCount = 0;
		GalaxyOrderFilmQuery query = buildQuery(OrderFilmStatus.NEW);
		while (++i < executeMaxCount) {
			Result<List<GalaxyOrderFilmDO>> orderResult = galaxyOrderFilmAO.query(query);

			// 取不到数据，表示全部处理完，结束
			if (!orderResult.isSuccess() || CollectionUtils.isEmpty(orderResult.getValue())) {
				break;
			}

			galaxyOrderFilmAO.closeOrder(orderResult.getValue(), OrderFilmStatus.CANCELED.getCode());

			int size = orderResult.getValue().size();
			updateCount += size;

			// 查询比预期的少，表示执行到最后页了。
			if (size < query.getPageSize()) {
				break;
			}
			sleep(200);
		}
		log.info("closeNewOrder end, updateCount:" + updateCount);
	}

	/**
	 * 关闭待付款订单,status 1->4
	 */
	public void closePreOrder() {
		log.info("closePreOrder start");
		// 执行分页查询次数
		int i = 0;
		// 更新总数
		int updateCount = 0;
		GalaxyOrderFilmQuery query = buildQuery(OrderFilmStatus.PREPAY);
		while (++i < executeMaxCount) {
			Result<List<GalaxyOrderFilmDO>> orderResult = galaxyOrderFilmAO.query(query);

			// 取不到数据，表示全部处理完，结束
			if (!orderResult.isSuccess() || CollectionUtils.isEmpty(orderResult.getValue())) {
				break;
			}

			galaxyOrderFilmAO.closeOrder(orderResult.getValue(), OrderFilmStatus.CLOSED.getCode());

			int size = orderResult.getValue().size();
			updateCount += size;

			// 查询比预期的少，表示执行到最后页了。
			if (size < query.getPageSize()) {
				break;
			}
			sleep(200);
		}
		log.info("closePreOrder end, updateCount:" + updateCount);
	}

	private GalaxyOrderFilmQuery buildQuery(OrderFilmStatus status) {
		GalaxyOrderFilmQuery query = new GalaxyOrderFilmQuery();
		query.setStatus(status.getCode());
		query.setGmtModifiedEnd(DateTime.now().plusDays(-1).toDate());// 1天前的订单
		query.setPageSize(pageSize);
		query.setCurrentPage(1);// 一直获取第一页数据，直到取不到为止
		return query;
	}

	private void sleep(long sleepTime) {
		if (sleepTime < 1)
			return;
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			log.error("线程等待发生异常", e);
		}
	}

}
