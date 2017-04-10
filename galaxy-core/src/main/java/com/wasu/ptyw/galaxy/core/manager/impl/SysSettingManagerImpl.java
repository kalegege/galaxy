package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.SysSettingManager;
import com.wasu.ptyw.galaxy.dal.dao.SysSettingDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.SysSettingDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.SysSettingQuery;

/**
 * @author wenguang
 * @date 2015年09月16日
 */
@Service("sysSettingManager")
public class SysSettingManagerImpl implements SysSettingManager {
	@Resource
	private SysSettingDAO sysSettingDao;

	@Override
	public Long insert(SysSettingDO obj) throws MyException {
		try {
			sysSettingDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(SysSettingDO obj) throws MyException {
		try {
			return sysSettingDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return sysSettingDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public SysSettingDO getById(long id) throws MyException {
		try {
			return sysSettingDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<SysSettingDO> getByIds(List<Long> ids) throws MyException {
		try {
			return sysSettingDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<SysSettingDO> query(SysSettingQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = sysSettingDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return sysSettingDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public SysSettingDO queryFirst(SysSettingQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<SysSettingDO> list = sysSettingDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

}
