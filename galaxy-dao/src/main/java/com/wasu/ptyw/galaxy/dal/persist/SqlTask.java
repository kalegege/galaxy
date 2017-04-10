package com.wasu.ptyw.galaxy.dal.persist;

import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Getter;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

@AllArgsConstructor
public class SqlTask {

	private @Getter
	SqlSessionFactory sqlSessionFactory;

	private Class<?> mapperClass;

	private Method method;

	private Object[] args;

	public Object execute() throws Exception {
		@Cleanup
		SqlSession session = sqlSessionFactory.openSession();
		return execute(session);
	}

	public Object execute(SqlSession session) throws Exception {
		Object dao = session.getMapper(mapperClass);
		Object result = method.invoke(dao, args);
		session.commit();
		return result;
	}

	public boolean isLazyable() {
		return method.getReturnType() == void.class;
	}
}