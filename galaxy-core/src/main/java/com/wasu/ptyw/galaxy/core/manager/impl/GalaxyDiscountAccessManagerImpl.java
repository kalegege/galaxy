package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.GalaxyDiscountAccessManager;
import com.wasu.ptyw.galaxy.dal.dao.GalaxyDiscountAccessDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountAccessDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.GalaxyDiscountAccessQuery;

/**
 * @author wenguang
 * @date 2015年09月25日
 */
@Service("galaxyDiscountAccessManager")
public class GalaxyDiscountAccessManagerImpl implements GalaxyDiscountAccessManager {
	@Resource
	private GalaxyDiscountAccessDAO galaxyDiscountAccessDao;

	@Override
	public Long insert(GalaxyDiscountAccessDO obj) throws MyException {
		try {
			galaxyDiscountAccessDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(GalaxyDiscountAccessDO obj) throws MyException {
		try {
			return galaxyDiscountAccessDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return galaxyDiscountAccessDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public GalaxyDiscountAccessDO getById(long id) throws MyException {
		try {
			return galaxyDiscountAccessDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<GalaxyDiscountAccessDO> getByIds(List<Long> ids) throws MyException {
		try {
			return galaxyDiscountAccessDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<GalaxyDiscountAccessDO> query(GalaxyDiscountAccessQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = galaxyDiscountAccessDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return galaxyDiscountAccessDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public int queryCount(GalaxyDiscountAccessQuery query) throws MyException {
		try {
			int count = galaxyDiscountAccessDao.queryCount(query);
			return count;
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public GalaxyDiscountAccessDO queryFirst(GalaxyDiscountAccessQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<GalaxyDiscountAccessDO> list = galaxyDiscountAccessDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

	@Override
	public int updateStatusByIds(List<Long> ids, int status) throws MyException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", status);
			map.put("ids", ids);
			return galaxyDiscountAccessDao.updateStatusByIds(map);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids, status);
		}
	}

}
