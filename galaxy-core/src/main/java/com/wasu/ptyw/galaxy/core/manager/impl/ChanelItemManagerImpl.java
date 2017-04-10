package com.wasu.ptyw.galaxy.core.manager.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.http.LocalHttpRequest;
import com.wasu.ptyw.galaxy.core.manager.ChanelItemManager;
import com.wasu.ptyw.galaxy.dal.dao.ChanelItemDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.dataobject.IndexDO;
import com.wasu.ptyw.galaxy.dal.dataobject.LiveDetailDO;
import com.wasu.ptyw.galaxy.dal.dataobject.LiveRealDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamRecommendQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Service("chanelItemManager")
public class ChanelItemManagerImpl implements ChanelItemManager {
	@Resource
	private ChanelItemDAO chanelItemDao;

	private static Cache<String, ChanelItemDO> cache_ChanelItemDO;
	
	private static Cache<String, LiveRealDO> cache_LiveRealDO;
	
	

	@Override
	public Long insert(ChanelItemDO obj) throws MyException {
		try {
			chanelItemDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(ChanelItemDO obj) throws MyException {
		try {
			return chanelItemDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return chanelItemDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public ChanelItemDO getById(long id) throws MyException {
		try {
			return chanelItemDao.getById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<ChanelItemDO> getByIds(List<Long> ids) throws MyException {
		try {
			return chanelItemDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<ChanelItemDO> queryByIds(List<Long> ids, ChanelItemQuery query) throws MyException {
		try {
			Map<String, Object> item = new HashMap<String, Object>();
			if (query.getChName() != null) {
				item.put("chName", query.getChName());
			}
			item.put("list", ids);
			item.put("pageFirstItem", query.getPageFirstItem());
			item.put("pageSize", query.getPageSize());
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemDao.querydeleteCount(item);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelItemDao.queryByIds(item);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<ChanelItemDO> query(ChanelItemQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
				query.setOrderBy("id");
			}
			return chanelItemDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public List<ChanelItemDO> queryOrder(List<Long> itemIds) throws MyException {
		try {
			Map<String, Object> item = new HashMap<String, Object>();
			String rule = "'";
			item.put("list1", itemIds);
			for (long j : itemIds) {
				rule = rule + j + ",";
			}
			rule.substring(0, rule.length() - 1);
			rule = rule + "'";
			item.put("rule", rule);
			return chanelItemDao.queryOrder(item);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, itemIds);
		}
	}

	@Override
	public ChanelItemDO queryRePlay(ChanelItemQuery query) throws MyException {
		try {
			return chanelItemDao.queryRePlay(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public List<ChanelItemDO> queryByRegionId(ChanelItemQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelItemDao.queryByRegionId(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}
	
	@Override
	public List<ChanelItemDO> queryByRegionId(String regionId) throws MyException {
		try {
			return chanelItemDao.queryByRegionId1(regionId);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, regionId);
		}
	}

	@Override
	public ChanelItemDO queryByChid(ChanelItemQuery query) throws MyException {
		try {
			return chanelItemDao.queryByChid(query);
			// return null;
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public ChanelItemDO queryCacheByChid(String chid) throws MyException {
		return getChanelItemDOCache(chid);
	}

	@Override
	public ChanelItemDO queryFirst(ChanelItemQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<ChanelItemDO> list = chanelItemDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

	@Override
	public List<ChanelItemDO> queryadd(List<Long> itemIds, ChanelItemQuery chanelItemQuery) throws MyException {
		try {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("pageFirstItem", chanelItemQuery.getPageFirstItem());
			item.put("regionId", chanelItemQuery.getRegionId());
			item.put("pageSize", chanelItemQuery.getPageSize());
			if (chanelItemQuery.getChName() != null) {
				item.put("chName", chanelItemQuery.getChName());
			}
			item.put("list", itemIds);

			int count = chanelItemDao.queryaddCount(item);
			if (count == 0) {
				return Lists.newArrayList();
			}
			chanelItemQuery.setTotalItem(count);
			return chanelItemDao.queryadd(item);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, itemIds);
		}
	}

	@Override
	public List<ChanelItemDO> queryrecommend(ChanelItemQuery chanelItemQuery) throws MyException {
		try {
			if (chanelItemQuery.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemDao.queryrecommendCount(chanelItemQuery);
				if (count == 0) {
					return Lists.newArrayList();
				}
				chanelItemQuery.setTotalItem(count);
			}
			return chanelItemDao.queryrecommend(chanelItemQuery);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, chanelItemQuery);
		}
	}

	@Override
	public List<ChanelItemDO> queryAll(List<Long> itemIds, ChanelItemQuery chanelItemQuery) throws MyException {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("pageFirstItem", chanelItemQuery.getPageFirstItem());
			item.put("regionId", chanelItemQuery.getRegionId());
			item.put("pageSize", chanelItemQuery.getPageSize());
			// if(chanelItemQuery.getChName() != null){
			item.put("chName", chanelItemQuery.getChName());
			// }
			item.put("list", itemIds);

			int count = chanelItemDao.queryAllCount(item);
			if (count == 0) {
				return Lists.newArrayList();
			}
			chanelItemQuery.setTotalItem(count);
			return chanelItemDao.queryAll(item);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}

	@Override
	public int queryaddCount(List<Long> itemIds, ChanelItemQuery chanelItemQuery) throws MyException {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> item = new HashMap<String, Object>();
			if (chanelItemQuery.getChName() != null) {
				item.put("chName", chanelItemQuery.getChName());
			}
			item.put("regionId", chanelItemQuery.getRegionId());
			item.put("list", itemIds);
			return chanelItemDao.queryaddCount(item);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, itemIds);
		}
	}

	// 根据参数num反馈直播推荐的条数 5或 10
	@Override
	public LiveRealDO getLive(int num, ChanelItemQuery chanelItemQuery) throws MyException {
		try {
			for (int i = 50; i < 250; i = i + 20) {
				LiveRealDO result = getLivenum(i);
				if (result != null) {
					int count = chooseException(result, chanelItemQuery);
					if (count >= 10) {
						return returnLive(num, result);
					}
				} else {
					return null;
				}
			}
		} catch (MyException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
		return null;
	}

	// 排除当地没有的频道
	public int chooseException(LiveRealDO liveRealDO, ChanelItemQuery chanelItemQuery) throws MyException {
		try {
			Iterator<LiveDetailDO> iter = liveRealDO.getItems().iterator();
			while (iter.hasNext()) {
				LiveDetailDO detail = (LiveDetailDO) iter.next();
				// 根据chid查找
				chanelItemQuery.setChId(detail.getContentId());

				// 排除中央五套id不同
				if (detail.getName().equals("中央五套")) {
					chanelItemQuery.setChId("000000000000000000000000000002341");
				}
				int count = chanelItemDao.queryCount(chanelItemQuery);
				if (count == 0) {
					iter.remove();
					break;
				}
				// 获取当前时间进行比较
				Date nowTime = new Date();
				Date endTime = DateUtils.parseDate(detail.getEndTime(), new String[] { "yyyy-MM-dd HH:mm:ss" });
				if (endTime.before(nowTime)) {
					iter.remove();
				}
			}
			return liveRealDO.getItems().size();
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// 从接口获取数据的条数
	public LiveRealDO getLivenum(int num) throws MyException {
		try {
			String url = "http://125.210.122.63:8080/Query?command=liveRealTimeRank&siteCode=iptvtest&area=0&number=";
			url += num;
			return LocalHttpRequest.getJson(url, LiveRealDO.class);
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}

	// 根据NUM参数返回类的不同个数
	public LiveRealDO returnLive(int num, LiveRealDO liveRealDO) throws MyException {
		try {
			for (int i = liveRealDO.getItems().size() - 1; i >= num; i--) {
				liveRealDO.getItems().remove(i);
			}
			return liveRealDO;
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}

	@Override
	public List<ChanelItemDO> getBychId(List<String> bId) throws MyException {
		// TODO Auto-generated method stub
		try {
			return chanelItemDao.getBychId(bId);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, bId);
		}
	}

	@Override
	public int updateOnly(ChanelItemDO baseDO) throws MyException {
		// TODO Auto-generated method stub
		try {
			return chanelItemDao.updateOnly(baseDO);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}

	@Override
	public List<ChanelItemDO> querydelete(List<Long> itemIds, ChanelItemQuery chanelItemQuery) throws MyException {
		try {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("pageFirstItem", chanelItemQuery.getPageFirstItem());
			item.put("regionId", chanelItemQuery.getRegionId());
			item.put("pageSize", chanelItemQuery.getPageSize());
			if (chanelItemQuery.getChName() != null) {
				item.put("chName", chanelItemQuery.getChName());
			}
			item.put("list", itemIds);

			int count = chanelItemDao.querydeleteCount(item);
			if (count == 0) {
				return Lists.newArrayList();
			}
			chanelItemQuery.setTotalItem(count);
			return chanelItemDao.querydelete(item);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, itemIds);
		}
	}

	@Override
	public int init(ChanelItemQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelItemDao.initCount(query);
				if (count == 0) {
					return 0;
				}
				// query.setTotalItem(count);
			}
			return chanelItemDao.init(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	// 从接口获取数据做缓存
	public ChanelItemDO getChanelItemDOCache(final String chId) {

		final String key = chId;
		if (cache_ChanelItemDO == null) {
			cache_ChanelItemDO = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(1, TimeUnit.DAYS)
					.expireAfterAccess(1, TimeUnit.DAYS).build(new CacheLoader<String, ChanelItemDO>() {
						@Override
						public ChanelItemDO load(String s) throws Exception {
							return null;
						}
					});
		}

		try {
			ChanelItemDO result = (ChanelItemDO) cache_ChanelItemDO.get(key, new Callable<ChanelItemDO>() {
				@Override
				public ChanelItemDO call() throws MyException, DAOException {
					return chanelItemDao.queryByChid1(chId);
					// return chanelTeamManager.getMenu(query);
				}
			});

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 从接口获取数据做缓存
	public LiveRealDO getLiveRealDOCache(final int num) {

		final String key = num + new Date().getDate() + "";
		if (cache_LiveRealDO == null) {
			cache_LiveRealDO = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(1, TimeUnit.DAYS)
					.expireAfterAccess(1, TimeUnit.DAYS).build(new CacheLoader<String, LiveRealDO>() {
						@Override
						public LiveRealDO load(String s) throws Exception {
							return null;
						}
					});
		}

		try {
			LiveRealDO result = (LiveRealDO) cache_LiveRealDO.get(key, new Callable<LiveRealDO>() {
				@Override
				public LiveRealDO call() throws MyException, DAOException {
					return getLivenum(num);
					// return chanelTeamManager.getMenu(query);
				}
			});

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
