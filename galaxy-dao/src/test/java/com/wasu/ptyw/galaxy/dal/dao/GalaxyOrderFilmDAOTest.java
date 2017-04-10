package com.wasu.ptyw.galaxy.dal.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.dao.GalaxyOrderFilmDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;

@ContextConfiguration(locations = "classpath:conf/persistence.xml")
public class GalaxyOrderFilmDAOTest extends AbstractJUnit4SpringContextTests {
	@Resource
	private GalaxyOrderFilmDAO galaxyOrderFilmDao;
	
	private void print(String methodName,Object obj) throws DAOException {
		System.out.println(methodName+": "+obj);
	}
	
	//@Test	
	public void testInsert() throws DAOException {
		try {
			GalaxyOrderFilmDO baseDO = new GalaxyOrderFilmDO();
			baseDO.setId(1L);
			Long id = galaxyOrderFilmDao.insert(baseDO);
			Assert.assertTrue(id > 0);
			print("insert",id);	
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	//@Test
	public void testUpdate() throws DAOException {
		try {
			GalaxyOrderFilmDO baseDO = new GalaxyOrderFilmDO();
			baseDO.setId(1L);
			int num = galaxyOrderFilmDao.update(baseDO);
			Assert.assertTrue(num > 0);
			print("update", num);	
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	
	
	//@Test
	public void testDeleteById() throws DAOException {		
		try {
			long id = 1L;
			int num = galaxyOrderFilmDao.deleteById(id);
			Assert.assertTrue(num > 0);
			print("deleteById", num);
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}	
	}		
	
	@Test
	public void testGetById() throws DAOException {
		try {
			long id = 4L;
			GalaxyOrderFilmDO t = galaxyOrderFilmDao.getById(id);
			Assert.assertNotNull(t);
			print("getById", t);
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}		
	}

	@Test
	public void testGetByIds() throws DAOException {			
		try {
			List<Long> ids =new ArrayList<Long>();
			ids.add(1L);
			ids.add(5L);
			List<GalaxyOrderFilmDO> list = galaxyOrderFilmDao.getByIds(ids);
			Assert.assertNotNull(list);
			print("getByIds", list);
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}	
	}
	
	@Test
	public void testQuery() throws DAOException {			
		try {
			GalaxyOrderFilmQuery query = new GalaxyOrderFilmQuery();
			query.setContCode("item1");
			query.setOutUserId("1");
			query.setOrderBy("id desc");
			query.setStatus(2);
			int totalItem = galaxyOrderFilmDao.queryCount(query);
			print("queryCount", totalItem);
			List<GalaxyOrderFilmDO> list = galaxyOrderFilmDao.query(query);
			Assert.assertNotNull(list);
			print("query", list);
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}	
	}
	
	//@Test
	public void testUpdateStatusByIds() throws DAOException {		
		try {
			List<Long> ids =new ArrayList<Long>();
			ids.add(4L);
			ids.add(5L);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("status", 3);
			map.put("ids", ids);
			int num = galaxyOrderFilmDao.updateStatusByIds(map);
			Assert.assertTrue(num > 0);
			print("updateStatusByIds", num);	
		} catch (DAOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
}

