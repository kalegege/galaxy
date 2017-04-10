/**
 * 
 */
package com.wasu.ptyw.galaxy.web.bus;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wasu.ptyw.galaxy.common.util.PropertiesUtil;
import com.wasu.ptyw.galaxy.core.timetask.SyncOrderTimeTask;
import com.wasu.ptyw.galaxy.core.timetask.SyncFilmTimeTask;

/**
 * @author wenguang
 * @date 2015年8月27日
 */
@Component("SyncStarter")
@Slf4j
public class SyncStarter {
	@Autowired
	SyncOrderTimeTask syncOrderTimeTask;

	@Resource
	SyncFilmTimeTask syncFilmTimeTask;

	// 每2小时执行订单同步任务
	@Scheduled(cron = "0 0 0/2 * * ? ")
	public void syncOrder() {
		log.info("syncOrder start");
		if (!"true".equals(PropertiesUtil.getValue("exec_time_task"))) {
			return;
		}
		syncOrderTimeTask.sync();
	}

	@Scheduled(cron = "0 3,33 * * * ? ")
	public void syncFilm() {
		log.info("SyncStarter syncFilm start");
		if (!"true".equals(PropertiesUtil.getValue("exec_time_task"))) {
			return;
		}
		syncFilmTimeTask.sync();
	}
}
