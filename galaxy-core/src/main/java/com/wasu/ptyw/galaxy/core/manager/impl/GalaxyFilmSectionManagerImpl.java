package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.GalaxyFilmSectionManager;
import com.wasu.ptyw.galaxy.dal.dao.GalaxyFilmSectionDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmSectionDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.GalaxyFilmSectionQuery;

/**
 * @author wenguang
 * @date 2015年09月30日
 */
@Service("galaxyFilmSectionManager")
public class GalaxyFilmSectionManagerImpl implements GalaxyFilmSectionManager {
	@Resource
	private GalaxyFilmSectionDAO galaxyFilmSectionDao;

	@Override
	public Long insert(GalaxyFilmSectionDO obj) throws MyException {
		try {
			galaxyFilmSectionDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(GalaxyFilmSectionDO obj) throws MyException {
		try {
			return galaxyFilmSectionDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return galaxyFilmSectionDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public GalaxyFilmSectionDO getById(long id) throws MyException {
		try {
			return galaxyFilmSectionDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<GalaxyFilmSectionDO> getByIds(List<Long> ids) throws MyException {
		try {
			return galaxyFilmSectionDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<GalaxyFilmSectionDO> query(GalaxyFilmSectionQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = galaxyFilmSectionDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return galaxyFilmSectionDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public int queryCount(GalaxyFilmSectionQuery query) throws MyException {
		try {
			int count = galaxyFilmSectionDao.queryCount(query);
			return count;
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public GalaxyFilmSectionDO queryFirst(GalaxyFilmSectionQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<GalaxyFilmSectionDO> list = galaxyFilmSectionDao.query(query);
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
			return galaxyFilmSectionDao.updateStatusByIds(map);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids, status);
		}
	}

	@Override
	public int deleteByQuery(GalaxyFilmSectionQuery query) throws MyException {
		try {
			return galaxyFilmSectionDao.deleteByQuery(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}
}
