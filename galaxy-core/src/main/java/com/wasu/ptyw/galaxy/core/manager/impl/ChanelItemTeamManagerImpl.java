package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.ChanelItemTeamManager;
import com.wasu.ptyw.galaxy.dal.dao.ChanelItemTeamDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemTeamDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemTeamQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Service("chanelItemTeamManager")
public class ChanelItemTeamManagerImpl implements ChanelItemTeamManager {
	@Resource
	private ChanelItemTeamDAO chanelItemTeamDao;

	@Override
	public Long insert(ChanelItemTeamDO obj) throws MyException {
		try {
			chanelItemTeamDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(ChanelItemTeamDO obj) throws MyException {
		try {
			return chanelItemTeamDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}
	
	@Override
	public int updateItemTeam(ChanelItemTeamDO obj) throws MyException {
		try {
			ChanelItemTeamQuery chanelItemTeamQuery=new ChanelItemTeamQuery();
			// 检查是否存在优先级重复字段
			long id = obj.getId();
			//获取新的优先级
			long now = obj.getOrderId();
			ChanelItemTeamDO count =chanelItemTeamDao.getById(id);
			chanelItemTeamQuery.setTeamId(count.getTeamId());
			if(count.getOrderId() != null){
				//获取原来的优先级
				long before = count.getOrderId();
				chanelItemTeamQuery.setOrderId(before);
				//检测新的优先级是否有人用
				List<ChanelItemTeamDO> result1=chanelItemTeamDao.queryTeamOrder(chanelItemTeamQuery);
				if(result1.size() == 1){
					chanelItemTeamDao.updateOrderIdDecrease(chanelItemTeamQuery);
				}
			}
			// 优先级更新
			chanelItemTeamQuery.setOrderId(now);
			List<ChanelItemTeamDO> result2=chanelItemTeamDao.queryTeamOrder(chanelItemTeamQuery);
			if(result2.size() == 1){
				chanelItemTeamDao.updateOrderIdAdd(chanelItemTeamQuery);
			}
			return chanelItemTeamDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return chanelItemTeamDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}
	
	@Override
	public int deleteByIds(ChanelItemTeamDO obj) throws MyException {
		try {
			return chanelItemTeamDao.deleteByIds(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}
	
	@Override
	public int deleteByOrder(ChanelItemTeamDO obj) throws MyException {
		try {
			// 查询删除数据的优先级
			ChanelItemTeamQuery chanelItemTeamQuery=new ChanelItemTeamQuery();
			chanelItemTeamQuery.setTeamId(obj.getTeamId());
			chanelItemTeamQuery.setItemId(obj.getItemId());
			List<ChanelItemTeamDO> count = chanelItemTeamDao.queryItemTeam(chanelItemTeamQuery);
			if(count.get(0).getOrderId() != null){
				long orderId=count.get(0).getOrderId();
				ChanelItemTeamQuery aChanelItemTeamQuery=new ChanelItemTeamQuery();
				aChanelItemTeamQuery.setTeamId(obj.getTeamId());
				aChanelItemTeamQuery.setOrderId(orderId);
			// 优先级更新
				chanelItemTeamDao.updateOrderIdDecrease(aChanelItemTeamQuery);
			}
			return chanelItemTeamDao.deleteByIds(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public ChanelItemTeamDO getById(long id) throws MyException {
		try {
			return chanelItemTeamDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<ChanelItemTeamDO> getByIds(List<Long> ids) throws MyException {
		try {
			return chanelItemTeamDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<ChanelItemTeamDO> query(ChanelItemTeamQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemTeamDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelItemTeamDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public ChanelItemTeamDO queryFirst(ChanelItemTeamQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<ChanelItemTeamDO> list = chanelItemTeamDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

	@Override
	public List<ChanelItemTeamDO> getByTeamId(ChanelItemTeamQuery query) throws MyException {
		// TODO Auto-generated method stub
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemTeamDao.queryCountByTeamId(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelItemTeamDao.getByTeamId(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}
	
	@Override
	public List<ChanelItemTeamDO> queryAllByTeamId(ChanelItemTeamQuery query) throws MyException {
		// TODO Auto-generated method stub
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemTeamDao.queryCountByTeamId(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelItemTeamDao.queryAllByTeamId(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}
	
	@Override
	public List<ChanelItemTeamDO> queryAllByTeamIds(List<Long> ids) throws MyException {
		try {
			return chanelItemTeamDao.queryAllByTeamIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}
	
	@Override
	public List<ChanelItemTeamDO> getByTeam(ChanelItemTeamQuery query) throws MyException {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> itemteam = new HashMap<String, Object>();
			itemteam.put("teamId", query.getTeamId());
			List<ChanelItemTeamDO> result=new  ArrayList<ChanelItemTeamDO>();
			int num=query.getPageSize();
			int currentpage=query.getCurrentPage();
			int count1=0;
			int count2=0;
			int count=0;
			if (num < Integer.MAX_VALUE) {
				 count1 = chanelItemTeamDao.queryCountTeam(query);
				 count2 = chanelItemTeamDao.queryCountNull(query);
				 count=count1 + count2;
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			List<ChanelItemTeamDO> result1=chanelItemTeamDao.queryTeam(query);
			if(currentpage == 1){
				result.addAll(result1);
			}else{
				query.setCurrentPage(1);
				result1=chanelItemTeamDao.queryTeam(query);
			}
			int size1=num * currentpage - result1.size();
			if(size1 == 0){
				return result;
			}else if(size1 == num){
				itemteam.put("pageStart", num * (currentpage - 1) );
				itemteam.put("pageStop", num );
			}else{
				if(currentpage == 1){
					itemteam.put("pageStart", num * (currentpage - 1) );
					itemteam.put("pageStop",  size1 );
				}else{
					itemteam.put("pageStart", size1 - num );
					itemteam.put("pageStop",  num   );
				}
			}
				List<ChanelItemTeamDO> result2=chanelItemTeamDao.queryNulls(itemteam);
				result.addAll(result2);
			return result;
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	
	@Override
	public List<ChanelItemTeamDO> getByTeam1(ChanelItemTeamQuery query) throws MyException {
		// TODO Auto-generated method stub
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemTeamDao.queryCountByTeamId(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			List<ChanelItemTeamDO> result=chanelItemTeamDao.queryByTeamId(query);
			return result;
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}
	
	@Override
	public List<ChanelItemTeamDO> getAllByTeamId(ChanelItemTeamQuery query) throws MyException {
		// TODO Auto-generated method stub
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemTeamDao.queryCountByTeamId(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelItemTeamDao.getAllByTeamId(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public int updateOnly(ChanelItemTeamDO baseDO) throws MyException {
		// TODO Auto-generated method stub
		try {
			return chanelItemTeamDao.updateOnly(baseDO);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, baseDO);
		}
	}
}
