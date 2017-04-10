package com.wasu.ptyw.galaxy.core.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.http.LocalHttpRequest;
import com.wasu.ptyw.galaxy.core.manager.ChanelTeamManager;
import com.wasu.ptyw.galaxy.dal.dao.ChanelTeamDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.AssertMenuAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemTeamDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamDO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuDetailDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamAllDTO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemTeamQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Service("chanelTeamManager")
public class ChanelTeamManagerImpl implements ChanelTeamManager {
	@Resource
	private ChanelTeamDAO chanelTeamDao;
	// 缓存
	private static Cache<String, MenuAllDO> cacheMenuAllDO;

	private static Cache<String, List<ChanelTeamDO>> cacheChanelTeamDO;
	// static LocalMenu localMenu = new LocalMenu();

	@Override
	public Long insert(ChanelTeamDO obj) throws MyException {
		try {
			chanelTeamDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public Long insertTeam(ChanelTeamDO obj) throws MyException {
		try {
			// 检查是否存在优先级重复字段
			ChanelTeamQuery chanelTeamQuery = new ChanelTeamQuery();
			// 判断优先级是否输入
			if (obj.getSortby() != null) {
				int priority = obj.getSortby();
				chanelTeamQuery.setSortby(priority);
				chanelTeamQuery.setRegionId(obj.getRegionId());
				chanelTeamQuery.setPageSize(1);
				chanelTeamQuery.setOrderBy("id");

				int count = chanelTeamDao.queryCount(chanelTeamQuery);
				if (count == 1) {
					chanelTeamDao.updateSortByAdd(priority);
				}
			}
			// 没有优先级直接插入

			chanelTeamDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(ChanelTeamDO obj) throws MyException {
		try {
			return chanelTeamDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int updateTeam(ChanelTeamDO obj) throws MyException {
		try {
			ChanelTeamQuery chanelTeamQuery = new ChanelTeamQuery();
			// 检查是否存在优先级重复字段
			long id = obj.getId();
			// 获取新的优先级
			int now = obj.getSortby();
			ChanelTeamDO count = chanelTeamDao.getById(id);
			chanelTeamQuery.setId(count.getId());
			if (count.getSortby() != null) {
				// 获取原来的优先级
				int before = count.getSortby();
				chanelTeamQuery.setSortby(before);
				// 检测新的优先级是否有人用
				List<ChanelTeamDO> result1 = chanelTeamDao.queryTeamOrder(chanelTeamQuery);
				if (result1.size() == 1) {
					chanelTeamDao.updateSortByDecrease(before);
				}
			}
			// 优先级更新
			chanelTeamQuery.setSortby(now);
			List<ChanelTeamDO> result2 = chanelTeamDao.queryTeamOrder(chanelTeamQuery);
			if (result2.size() == 1) {
				chanelTeamDao.updateSortByAdd(now);
			}

			return chanelTeamDao.update(obj);
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return chanelTeamDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public int deleteTeam(long id) throws MyException {
		try {
			// 查询删除数据的优先级
			ChanelTeamDO count = chanelTeamDao.getById(id);
			if (count.getSortby() != null) {
				int sortby = count.getSortby();
				// 优先级更新
				chanelTeamDao.updateSortByDecrease(sortby);
			}
			return chanelTeamDao.deleteById(id);
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public ChanelTeamDO getById(long id) throws MyException {
		try {
			return chanelTeamDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<ChanelTeamDO> getByIds(List<Long> ids) throws MyException {
		try {
			return chanelTeamDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<ChanelTeamDO> query(ChanelTeamQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelTeamDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelTeamDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public List<ChanelTeamDO> queryType(ChanelTeamQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelTeamDao.queryCountType(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelTeamDao.queryType(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public List<ChanelTeamDO> queryTeam(ChanelTeamQuery query) throws MyException {
		try {
			List<ChanelTeamDO> result = new ArrayList<ChanelTeamDO>();
			int num = query.getPageSize();
			if (num < Integer.MAX_VALUE) {
				int count1 = chanelTeamDao.queryCountTeam(query);
				int count2 = chanelTeamDao.queryCountNull(query);
				int count = count1 + count2;
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			List<ChanelTeamDO> result1 = chanelTeamDao.queryTeam(query);
			result.addAll(result1);
			int size1 = result1.size();
			int current = query.getCurrentPage();
			if (size1 < num) {
				query.setPageSize(num * current - size1);
				List<ChanelTeamDO> result2 = chanelTeamDao.queryNull(query);
				result.addAll(result2);
			}
			return result;
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public ChanelTeamDO queryFirst(ChanelTeamQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<ChanelTeamDO> list = chanelTeamDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

	@Override
	public List<ChanelTeamDO> queryByRegion(ChanelTeamQuery query) throws MyException {
		try {
			query.setOrderBy("id");
			return chanelTeamDao.queryByRegion(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public List<ChanelTeamDO> queryCacheByRegion(String regionId) throws MyException {
		ChanelTeamQuery query = new ChanelTeamQuery();
		query.setRegionId(regionId);
		return queryByRegion(query);
	}

	/**
	 * 从接口获取当天所有频道节目单
	 */
	@Override
	public MenuAllDO getMenu(String regionId) throws MyException {
		try {
			// String url = "http://125.210.135.104:8080/IPService/interface";
			String url = "http://125.210.138.210:8080/IPService/interface";
			Map<String, Object> message = new HashMap<String, Object>();
			message.put("command", "WASU_LIVE_EVENT");
			message.put("region", regionId);
			message.put("type", 1);
			message.put("chId", "002301");
			String result = LocalHttpRequest.post(url, JSON.toJSONString(message));
			// System.out.println(result);
			return JSON.parseObject(result, MenuAllDO.class);
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}

	// 从接口获取数据的条数
	@Override
	public AssertMenuAllDO getReplay(ChanelTeamQuery query) throws MyException {
		try {
			// 甘肃回放接口
			// String url =
			// "http://www.utc.gscatv.cn/a?f=daohang_7&profile=OSZJ22SCENARIO";
			// 杭州回放接口
			String url = "http://hd2.hzdtv.tv/template_images/test20151125/test/ajax_huifang_json.jsp";
			String json = LocalHttpRequest.get(url);
			return JSON.parseObject(json, AssertMenuAllDO.class);
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}

	// 获取频道当天节目单
	@Override
	public MenuAllDO getSingleMenu(ChanelTeamQuery query, String chid) throws MyException {
		try {
			// String url = "http://125.210.135.104:8080/IPService/interface";
			String url = "http://125.210.138.210:8080/IPService/interface";
			
			Map<String, Object> message = new HashMap<String, Object>();
			message.put("command", "WASU_LIVE_EVENT");
			message.put("region", query.getRegionId());
			message.put("type", 2);
			message.put("chId", chid);
			String result = LocalHttpRequest.post(url, JSON.toJSONString(message));
			// System.out.println(result);
			return JSON.parseObject(result, MenuAllDO.class);
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}

	// 获取分组及频道信息接口
	@Override
	public TeamAllDO getTeam(ChanelTeamQuery query) throws MyException {
		try {
			// String url = "http://125.210.135.104:8080/IPService/interface";
			String url = "http://125.210.138.210:8080/IPService/interface";
			Map<String, Object> message = new HashMap<String, Object>();
			message.put("command", "WASU_LIVE_CHANNEL");
			message.put("region", query.getRegionId());
			String result = LocalHttpRequest.post(url, JSON.toJSONString(message));
			TeamAllDO teamAllDO = JSON.parseObject(result, TeamAllDO.class);
			// System.out.println(result);
			return teamAllDO;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}

	@Override
	public int updateOnly(ChanelTeamDO chanelTeamDO) throws MyException {
		// TODO Auto-generated method stub
		try {
			return chanelTeamDao.updateOnly(chanelTeamDO);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, chanelTeamDO);
		}
	}

	@Override
	public ChanelTeamDO queryByBid(ChanelTeamQuery query) throws MyException {
		// TODO Auto-generated method stub
		try {
			return chanelTeamDao.queryByBid(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	// 从接口获取当天所有频道节目单缓存
	public MenuAllDO getMenuCache(final String regionId) {

		final String key = regionId + "_" + new Date().getDate();
		if (cacheMenuAllDO == null) {
			cacheMenuAllDO = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(1, TimeUnit.DAYS)
					.expireAfterAccess(1, TimeUnit.DAYS).build(new CacheLoader<String, MenuAllDO>() {
						@Override
						public MenuAllDO load(String s) throws Exception {
							return null;
						}
					});
		}

		try {
			MenuAllDO result = (MenuAllDO) cacheMenuAllDO.get(key, new Callable<MenuAllDO>() {
				@Override
				public MenuAllDO call() throws MyException {
					return getMenu(regionId);
					// return chanelTeamManager.getMenu(query);
				}
			});

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 从接口获取当天所有频道节目单缓存
	public List<ChanelTeamDO> getTeamCache(final String regionId) {

		final String key = regionId + "_" + new Date().getDate();
		if (cacheChanelTeamDO == null) {
			cacheChanelTeamDO = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(1, TimeUnit.DAYS)
					.expireAfterAccess(1, TimeUnit.DAYS).build(new CacheLoader<String, List<ChanelTeamDO>>() {
						@Override
						public List<ChanelTeamDO> load(String s) throws Exception {
							return null;
						}
					});
		}

		try {
			List<ChanelTeamDO> result = (List<ChanelTeamDO>) cacheChanelTeamDO.get(key,
					new Callable<List<ChanelTeamDO>>() {
						@Override
						public List<ChanelTeamDO> call() throws MyException {
							ChanelTeamQuery query = new ChanelTeamQuery();
							query.setRegionId(regionId);
							return queryByRegion(query);
							// return chanelTeamManager.getMenu(query);
						}
					});

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 获取频道当天节目单
	 */
	@Override
	public MenuAllDO getCurrentMenu(String regionId, String chid) throws MyException {
		MenuAllDO allMenu = getMenuCache(regionId);
		List<MenuDetailDO> items = allMenu.getChList();
		MenuAllDO result = new MenuAllDO();
		List<MenuDetailDO> resultItem = new ArrayList<MenuDetailDO>();
		// MenuDetailDO result=null;
		for (MenuDetailDO item : items) {
			if (chid.equals(item.getChId())) {
				resultItem.add(item);
				break;
			}
		}
		result.setChList(resultItem);
		result.setResult(allMenu.getResult());
		result.setResultDesc(allMenu.getResultDesc());
		result.setVersion(allMenu.getVersion());
		return result;
	}
}
