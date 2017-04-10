package com.wasu.ptyw.galaxy.dal.persist;

import static com.wasu.ptyw.galaxy.common.util.BeanUtil.getFieldValue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import lombok.Setter;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.wasu.ptyw.galaxy.common.util.StringUtil;

public class SqlSessionFactoryBean implements BeanFactoryPostProcessor {

	private @Setter
	DataSource dataSource;

	private @Setter
	List<String> mapperPackages;

	private @Setter
	List<String> mappers;

	private @Setter
	String typeAliasesPackage;

	private @Setter
	String envId = "default";

	private SqlSessionFactory sqlSessionFactory;

	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
		try {
			Environment env = new Environment(envId, new ManagedTransactionFactory(), dataSource);
			Configuration conf = new Configuration(env);

			if (mapperPackages != null && !mapperPackages.isEmpty()) {
				for (String packageName : mapperPackages) {
					conf.addMappers(packageName);
				}
			}
			if (typeAliasesPackage != null && !typeAliasesPackage.isEmpty()) {
				conf.getTypeAliasRegistry().registerAliases(typeAliasesPackage, Object.class);
			}

			if (mappers != null && !mappers.isEmpty()) {
				for (String className : mappers) {
					Class<?> cls = Class.forName(className);
					conf.addMapper(cls);
				}
			}

			registerBeans(bf, conf);

			sqlSessionFactory = new SqlSessionFactoryBuilder().build(conf);
		} catch (Exception e) {
			throw new BeanCreationException("SqlSessionFactory初始化失败.", e);
		}
	}

	/**
	 * mybatis 3.2.2
	 */
	private void registerBeans(ConfigurableListableBeanFactory bf, Configuration conf) throws Exception {
		MapperRegistry mapperRegistry = getFieldValue(conf, "mapperRegistry");
		HashMap<Class<?>, MapperProxyFactory<?>> knownMappers = getFieldValue(mapperRegistry, "knownMappers");

		for (Class<?> cls : knownMappers.keySet()) {
			InvocationHandler h = new DaoProxyInvocationHandler(cls);
			Object dao = Proxy.newProxyInstance(cls.getClassLoader(), new Class[] { cls }, h);

			String beanName = StringUtil.toCamelCase(cls.getSimpleName());
			bf.registerSingleton(beanName, dao);
		}
	}

	/**
	 * mybatis 3.0.6
	 */
	private void registerBeans2(ConfigurableListableBeanFactory bf, Configuration conf) throws Exception {
		MapperRegistry mapperRegistry = getFieldValue(conf, "mapperRegistry");
		Set<Class<?>> knownMappers = getFieldValue(mapperRegistry, "knownMappers");

		for (Class<?> cls : knownMappers) {
			InvocationHandler h = new DaoProxyInvocationHandler(cls);
			Object dao = Proxy.newProxyInstance(cls.getClassLoader(), new Class[] { cls }, h);

			String beanName = StringUtil.toCamelCase(cls.getSimpleName());
			bf.registerSingleton(beanName, dao);
		}
	}

	private class DaoProxyInvocationHandler implements InvocationHandler {
		private Class<?> cls;

		DaoProxyInvocationHandler(Class<?> cls) {
			this.cls = cls;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				SqlTask task = new SqlTask(sqlSessionFactory, cls, method, args);
				return SqlTaskExecutor.execute(task);
			} catch (Exception e) {
				throw new DAOException(e);
			}
		}
	}
}