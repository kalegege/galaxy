package com.wasu.ptyw.galaxy.dal.persist;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SqlTaskExecutor {

	private static ThreadLocal<Boolean> isInBatch = new ThreadLocal<Boolean>();

	private static ThreadLocal<List<SqlTask>> taskList = new ThreadLocal<List<SqlTask>>();

	private static List<SqlTask> getTaskList() {
		List<SqlTask> resultList = taskList.get();

		if (resultList == null) {
			resultList = Lists.newArrayList();
			taskList.set(resultList);
		}

		return resultList;
	}

	public static void startBatch() {
		isInBatch.set(true);
	}

	public static void executeBatch() throws Exception {
		doExecuteBatch();
		isInBatch.set(false);
	}

	public static void cancelBatch() {
		taskList.remove();
		isInBatch.set(false);
	}

	private static void doExecuteBatch() throws Exception {
		Map<SqlSessionFactory, SqlSession> sessionMap = Maps.newHashMap();

		for (SqlTask task : getTaskList()) {
			SqlSession session = sessionMap.get(task.getSqlSessionFactory());

			if (session == null) {
				session = task.getSqlSessionFactory().openSession(
						ExecutorType.BATCH);
				sessionMap.put(task.getSqlSessionFactory(), session);
			}

			task.execute(session);
		}
	}

	public static Object execute(SqlTask task) throws Exception {
		if (Boolean.TRUE.equals(isInBatch.get())) {
			if (task.isLazyable()) {
				getTaskList().add(task);
				return null;
			} else {
				doExecuteBatch();
				return task.execute();
			}
		}

		return task.execute();
	}
}