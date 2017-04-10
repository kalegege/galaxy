package com.wasu.ptyw.galaxy.core.ao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.cache.LocalCache;
import com.wasu.ptyw.galaxy.core.manager.ChanelItemManager;
import com.wasu.ptyw.galaxy.core.manager.ChanelItemTeamManager;
import com.wasu.ptyw.galaxy.core.manager.ChanelTeamManager;
import com.wasu.ptyw.galaxy.core.manager.SysSettingManager;
import com.wasu.ptyw.galaxy.dal.dataobject.AssertMenuAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.AssertMenuDescDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemTeamDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamDO;
import com.wasu.ptyw.galaxy.dal.dataobject.IndexDO;
import com.wasu.ptyw.galaxy.dal.dataobject.LiveDetailDO;
import com.wasu.ptyw.galaxy.dal.dataobject.LiveRealDO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuDetailDO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuInfoDO;
import com.wasu.ptyw.galaxy.dal.dataobject.SysSettingDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamAllDTO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamBlistDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamBlistDTO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamDetailDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamDetailDTO;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemTeamQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamRecommendQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Service("chanelTeamAO")
@Slf4j
public class ChanelTeamAO extends BaseAO {
	@Resource
	private ChanelTeamManager chanelTeamManager;
	@Resource
	private ChanelItemManager chanelItemManager;
	@Resource
	private ChanelItemTeamManager chanelItemTeamManager;
	@Resource
	private SysSettingManager sysSettingManager;

	private static Cache<String, MenuAllDO> cache;

	private static Cache<String, List<ChanelItemDO>> cache_ChanelItemDO;

	private static Cache<String, Map<String, ChanelTeamDO>> cache_team;

	private static Cache<String, Map<String, ChanelItemDO>> cache_item;

	/**
	 * 新增
	 * 
	 * @param ChanelTeamDO
	 * @return 对象ID
	 */
	public Result<Long> save(ChanelTeamDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = chanelTeamManager.insertTeam(obj);
			result.setValue(id);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("save error," + obj, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 更新
	 * 
	 * @param ChanelTeamDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(ChanelTeamDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = chanelTeamManager.updateTeam(obj);
			if (num > 0) {
				result.setValue(num);
				result.setSuccess(true);
			} else {
				setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
		} catch (Exception e) {
			log.error("update error," + obj, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 根据id删除
	 * 
	 * @param id
	 * @return 删除成功的记录数
	 */
	public Result<Integer> deleteById(Long id) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			int num = chanelTeamManager.deleteTeam(id);
			if (num > 0) {
				result.setValue(num);
				result.setSuccess(true);
			} else {
				setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
		} catch (Exception e) {
			log.error("deleteById error," + id, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return ChanelTeamDO
	 */
	public Result<ChanelTeamDO> getById(Long id) {
		Result<ChanelTeamDO> result = new Result<ChanelTeamDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelTeamDO obj = chanelTeamManager.getById(id);
			if (obj != null) {
				result.setValue(obj);
				result.setSuccess(true);
			} else {
				setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
		} catch (Exception e) {
			log.error("getById error,id=" + id, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelTeamDO>
	 */
	public Result<List<ChanelTeamDO>> getByIds(List<Long> ids) {
		Result<List<ChanelTeamDO>> result = new Result<List<ChanelTeamDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamDO> list = chanelTeamManager.getByIds(ids);
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getByIds error," + ids, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelTeamDO>
	 */
	public Result<List<ChanelTeamDO>> query(ChanelTeamQuery query) {
		Result<List<ChanelTeamDO>> result = new Result<List<ChanelTeamDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamDO> list = chanelTeamManager.queryTeam(query);
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("query error," + query, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelTeamDO>
	 */
	public Result<List<ChanelTeamDO>> queryType(ChanelTeamQuery query) {
		Result<List<ChanelTeamDO>> result = new Result<List<ChanelTeamDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamDO> list = chanelTeamManager.queryType(query);
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("query error," + query, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @return List<ChanelTeamDO>
	 */
	public Result<List<ChanelTeamDO>> queryByRegion(ChanelTeamQuery query) {
		Result<List<ChanelTeamDO>> result = new Result<List<ChanelTeamDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamDO> list = chanelTeamManager.queryByRegion(query);
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("query error," + query, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 获取所有频道当天节目单
	 * 
	 * @param query
	 * @return MenuAllDO
	 */
	public Result<MenuAllDO> getMenu(ChanelTeamQuery query) {
		Result<MenuAllDO> result = new Result<MenuAllDO>(false);
		try {
			MenuAllDO menuAllDO = chanelTeamManager.getMenu(query.getRegionId());
			result.setValue(menuAllDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 获取频道当天节目单
	 * 
	 * @param query
	 * @return MenuAllDO
	 */
	public Result<MenuAllDO> getSingleMenu(ChanelTeamQuery query, String chid) {
		Result<MenuAllDO> result = new Result<MenuAllDO>(false);
		try {
			ChanelItemQuery chanelItemQuery = new ChanelItemQuery();
			chanelItemQuery.setChId(chid);
			MenuAllDO menuAllDO = chanelTeamManager.getSingleMenu(query, chid);
			ChanelItemDO chanelItemDO = chanelItemManager.queryByChid(chanelItemQuery);
			// 赋值
			if (menuAllDO.getChList() != null) {
				menuAllDO.getChList().get(0).setIsPlayback(chanelItemDO.getIsPlayback().toString());
				menuAllDO.getChList().get(0).setChlogourl(chanelItemDO.getChlogourl());
				MenuDetailDO menuDetailDO = menuAllDO.getChList().get(0);
				int menusize = menuDetailDO.getEventList().size();
				// 筛选直播节目单
				List<MenuInfoDO> menuInfoList = new ArrayList<MenuInfoDO>();
				Date nowDate = new Date();

				for (int l = 0; l < menusize; l++) {
					MenuInfoDO result1 = menuDetailDO.getEventList().get(l);
					String endtime = result1.getEndTime();
					String starttime = result1.getStartTime();
					SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date enDate = myFmt.parse(endtime);
					Date stDate = myFmt.parse(starttime);
					// 修改匹配时间为前一天当前时间
					if ((nowDate.after(stDate)) && (nowDate.before(enDate))) {
						menuInfoList.add(menuDetailDO.getEventList().get(l));
						menuInfoList.add(menuDetailDO.getEventList().get(l + 1));
					}
					// 解决节目单不是从00：00开始显示错误
				}
				menuDetailDO.setMenuList(menuInfoList);
				menuDetailDO.setEventList(null);
				// 根据回放标志获取数据
				if (chanelItemDO.getIsPlayback() == 1) {
					menuDetailDO.setFolder(chanelItemDO.getFolder());
					menuDetailDO.setAssetId(chanelItemDO.getAssetid());
					// 显示别名
					if (chanelItemDO.getByname() != null) {
						menuDetailDO.setChName(chanelItemDO.getByname());
					} else {
						menuDetailDO.setChName(chanelItemDO.getChName());
					}
					menuDetailDO.setContentId(chanelItemDO.getChId());
				}
				result.setValue(menuAllDO);
			} else {
				result.setValue(null);
			}
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	public Result<MenuAllDO> getSingleMenu1(ChanelTeamQuery query, String chid) {
		Result<MenuAllDO> result = new Result<MenuAllDO>(false);
		try {
			ChanelItemQuery chanelItemQuery = new ChanelItemQuery();
			chanelItemQuery.setChId(chid);

			MenuAllDO menuAllDO = new MenuAllDO();
			menuAllDO.setVersion("40");
			menuAllDO.setResult("0");
			menuAllDO.setResultDesc("event get success");

			ChanelItemDO chanelItemDO = chanelItemManager.queryByChid(chanelItemQuery);

			List<MenuDetailDO> menuDetail = new ArrayList<MenuDetailDO>();

			MenuDetailDO menuDetailDO = new MenuDetailDO();
			menuDetailDO.setChId(chanelItemDO.getChId());
			menuDetailDO.setChName(chanelItemDO.getChName());
			menuDetailDO.setChlogourl(chanelItemDO.getChlogourl());
			menuDetailDO.setIsPlayback(chanelItemDO.getIsPlayback().toString());
			// 筛选直播节目单
			List<MenuInfoDO> menuInfoList = new ArrayList<MenuInfoDO>();
			for (int j = 0; j < 2; j++) {
				MenuInfoDO menuInfoDO = new MenuInfoDO();
				menuInfoDO.setEventName("奔跑吧兄弟");
				menuInfoDO.setStartTime("2016-08-10 11:22:45");
				menuInfoDO.setEndTime("2016-08-10 13:22:45");
				menuInfoList.add(menuInfoDO);
			}
			menuDetailDO.setMenuList(menuInfoList);
			// 根据回放标志获取数据
			if (chanelItemDO.getIsPlayback().equals("1")) {
				menuDetailDO.setFolder(chanelItemDO.getFolder());
				menuDetailDO.setAssetId(chanelItemDO.getAssetid());
				menuDetailDO.setContentId(chanelItemDO.getChId());
			}
			menuDetail.add(menuDetailDO);
			menuAllDO.setChList(menuDetail);
			result.setValue(menuAllDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 后台使用的查看直播节目单接口
	 * 
	 * @param query
	 * @return MenuAllDO
	 */
	public Result<MenuAllDO> getCertainMenu(ChanelTeamQuery query, String chid) {
		Result<MenuAllDO> result = new Result<MenuAllDO>(false);
		try {
			ChanelItemQuery chanelItemQuery = new ChanelItemQuery();
			chanelItemQuery.setChId(chid);
			MenuAllDO menuAllDO = chanelTeamManager.getSingleMenu(query, chid);
			ChanelItemDO chanelItemDO = chanelItemManager.queryByChid(chanelItemQuery);
			MenuDetailDO menuDetailDO = menuAllDO.getChList().get(0);

			// 赋值
			menuDetailDO.setIsPlayback(chanelItemDO.getIsPlayback().toString());
			menuDetailDO.setChlogourl(chanelItemDO.getChlogourl());

			int menusize = menuDetailDO.getEventList().size();
			// 筛选直播节目单
			List<MenuInfoDO> menuInfoList = new ArrayList<MenuInfoDO>();
			for (int l = 0; l < menusize; l++) {
				String endtime = menuDetailDO.getEventList().get(l).getEndTime();
				String starttime = menuDetailDO.getEventList().get(l).getStartTime();
				SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date enDate = myFmt.parse(endtime);
				Date stDate = myFmt.parse(starttime);
				// 修改匹配时间为前一天当前时间
				Date nowDate = new Date();
				// String StrD = "2016-08-10 11:22:45";
				// Date nowDate = myFmt.parse(StrD);
				int year = nowDate.getYear();
				int month = nowDate.getMonth();
				int day = nowDate.getDate();
				int delta = stDate.getDate() - day;
				if ((stDate.getYear() == year) && (stDate.getMonth() == month) && (delta >= 0) && (delta < 3)) {
					menuInfoList.add(menuDetailDO.getEventList().get(l));
				}
			}
			menuDetailDO.setMenuList(menuInfoList);
			menuDetailDO.setEventList(null);
			// 根据回放标志获取数据
			if (chanelItemDO.getIsPlayback() == 1) {
				menuDetailDO.setFolder(chanelItemDO.getFolder());
				menuDetailDO.setAssetId(chanelItemDO.getAssetid());
				menuDetailDO.setChName(chanelItemDO.getChName());
				menuDetailDO.setContentId(chanelItemDO.getChId());
			}
			result.setValue(menuAllDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	// 后台造假的节目单
	public Result<MenuAllDO> getCertainMenu1(ChanelTeamQuery query, String chid) {
		Result<MenuAllDO> result = new Result<MenuAllDO>(false);
		try {
			ChanelItemQuery chanelItemQuery = new ChanelItemQuery();
			chanelItemQuery.setChId(chid);

			MenuAllDO menuAllDO = new MenuAllDO();
			menuAllDO.setVersion("40");
			menuAllDO.setResult("0");
			menuAllDO.setResultDesc("event get success");

			ChanelItemDO chanelItemDO = chanelItemManager.queryByChid(chanelItemQuery);

			List<MenuDetailDO> menuDetail = new ArrayList<MenuDetailDO>();

			MenuDetailDO menuDetailDO = new MenuDetailDO();
			menuDetailDO.setChId(chanelItemDO.getChId());
			menuDetailDO.setChName(chanelItemDO.getChName());
			menuDetailDO.setChlogourl(chanelItemDO.getChlogourl());
			menuDetailDO.setIsPlayback(chanelItemDO.getIsPlayback().toString());
			// 筛选直播节目单
			List<MenuInfoDO> menuInfoList = new ArrayList<MenuInfoDO>();
			Calendar c = Calendar.getInstance();
			for (int j = 0; j < 2; j++) {
				MenuInfoDO menuInfoDO = new MenuInfoDO();
				String date = (c.get(Calendar.YEAR)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + (c.get(Calendar.DATE))
						+ " 11:22:45";
				menuInfoDO.setEventName("奔跑吧兄弟" + date);
				menuInfoDO.setStartTime(date);
				menuInfoDO.setEndTime(date);
				menuInfoList.add(menuInfoDO);
			}
			c.add(Calendar.HOUR, 24);
			for (int j = 0; j < 2; j++) {
				MenuInfoDO menuInfoDO = new MenuInfoDO();
				String date = (c.get(Calendar.YEAR)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + (c.get(Calendar.DATE))
						+ " 11:22:45";
				menuInfoDO.setEventName("奔跑吧兄弟" + date);
				menuInfoDO.setStartTime(date);
				menuInfoDO.setEndTime(date);
				menuInfoList.add(menuInfoDO);
			}
			c.add(Calendar.HOUR, 24);
			for (int j = 0; j < 2; j++) {
				MenuInfoDO menuInfoDO = new MenuInfoDO();
				String date = (c.get(Calendar.YEAR)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + (c.get(Calendar.DATE))
						+ " 11:22:45";
				menuInfoDO.setEventName("奔跑吧兄弟" + date);
				menuInfoDO.setStartTime(date);
				menuInfoDO.setEndTime(date);
				menuInfoList.add(menuInfoDO);
			}
			menuDetailDO.setMenuList(menuInfoList);
			// 根据回放标志获取数据
			if (chanelItemDO.getIsPlayback().equals("1")) {
				menuDetailDO.setFolder(chanelItemDO.getFolder());
				menuDetailDO.setAssetId(chanelItemDO.getAssetid());
				menuDetailDO.setContentId(chanelItemDO.getChId());
			}
			menuDetail.add(menuDetailDO);
			menuAllDO.setChList(menuDetail);
			result.setValue(menuAllDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 获取多个频道当天节目单
	 * 
	 * @param query
	 * @return MenuAllDO
	 */
	public Result<List<MenuAllDO>> getManyMenu(ChanelTeamQuery query, String[] chid) {
		Result<List<MenuAllDO>> result = new Result<List<MenuAllDO>>(false);
		try {

			List<MenuAllDO> menuList = new ArrayList<MenuAllDO>();
			for (int i = 0; i < chid.length; i++) {
				ChanelItemQuery chanelItemQuery = new ChanelItemQuery();
				chanelItemQuery.setChId(chid[i]);
				 MenuAllDO currentMenu =chanelTeamManager.getSingleMenu(query,chid[i]);
//				MenuAllDO currentMenu = chanelTeamManager.getCurrentMenu(query.getRegionId(), chid[i]);
				 ChanelItemDO chanelItemDO=chanelItemManager.queryByChid(chanelItemQuery);
//				ChanelItemDO chanelItemDO = chanelItemManager.queryCacheByChid(chid[i]);
				// 赋值
				if (currentMenu.getChList().size() > 0) {
					MenuDetailDO result1 = currentMenu.getChList().get(0);
					result1.setIsPlayback(chanelItemDO.getIsPlayback().toString());
					result1.setChlogourl(chanelItemDO.getChlogourl());
					// 显示别名
					if (chanelItemDO.getByname() != null) {
						result1.setChName(chanelItemDO.getByname());
					} else {
						result1.setChName(chanelItemDO.getChName());
					}

					MenuDetailDO menuDetailDO = currentMenu.getChList().get(0);
					int menusize = menuDetailDO.getEventList().size();
					// 筛选直播节目单
					List<MenuInfoDO> menuInfoList = new ArrayList<MenuInfoDO>();
					Date nowDate = new Date();
					for (int l = 0; l < menusize; l++) {
						String endtime = menuDetailDO.getEventList().get(l).getEndTime();
						String starttime = menuDetailDO.getEventList().get(l).getStartTime();
						SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date enDate = myFmt.parse(endtime);
						Date stDate = myFmt.parse(starttime);
						// 修改时间

						// String StrD = "2016-08-10 11:22:45";
						// Date nowDate = myFmt.parse(StrD);
						if ((nowDate.after(stDate)) && (nowDate.before(enDate))) {
							menuInfoList.add(menuDetailDO.getEventList().get(l));
							menuInfoList.add(menuDetailDO.getEventList().get(l + 1));
						}
					}
					menuDetailDO.setMenuList(menuInfoList);
					menuDetailDO.setEventList(null);
					// 根据回放标志获取数据
					if (chanelItemDO.getIsPlayback() == 1) {
						menuDetailDO.setFolder(chanelItemDO.getFolder());
						menuDetailDO.setAssetId(chanelItemDO.getAssetid());
						// 显示别名
						if (chanelItemDO.getByname() != null) {
							menuDetailDO.setChName(chanelItemDO.getByname());
						} else {
							menuDetailDO.setChName(chanelItemDO.getChName());
						}
						menuDetailDO.setContentId(chanelItemDO.getChId());
					}
					menuList.add(currentMenu);
				} else {
					MenuAllDO aMenuAllDO = new MenuAllDO();
					List<MenuDetailDO> aList = new ArrayList<MenuDetailDO>();
					MenuDetailDO aMenuDetailDO = new MenuDetailDO();
					aMenuDetailDO.setIsPlayback(chanelItemDO.getIsPlayback().toString());
					aMenuDetailDO.setChlogourl(chanelItemDO.getChlogourl());
					// 显示别名
					if (chanelItemDO.getByname() != null) {
						aMenuDetailDO.setChName(chanelItemDO.getByname());
					} else {
						aMenuDetailDO.setChName(chanelItemDO.getChName());
					}
					aMenuDetailDO.setChId(chid[i]);
					aMenuDetailDO.setMenuList(new ArrayList<MenuInfoDO>());
					aList.add(aMenuDetailDO);
					aMenuAllDO.setChList(aList);
					aMenuAllDO.setResult("0");
					aMenuAllDO.setVersion("89");
					aMenuAllDO.setResultDesc("event get sucess");
					menuList.add(aMenuAllDO);
				}
			}
			result.setValue(menuList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	public Result<List<MenuAllDO>> getManyMenu1(ChanelTeamQuery query, String[] chid) {
		Result<List<MenuAllDO>> result = new Result<List<MenuAllDO>>(false);
		try {

			List<MenuAllDO> menuList = new ArrayList<MenuAllDO>();
			for (int i = 0; i < chid.length; i++) {
				ChanelItemQuery chanelItemQuery = new ChanelItemQuery();
				chanelItemQuery.setChId(chid[i]);

				MenuAllDO menuAllDO = new MenuAllDO();
				menuAllDO.setVersion("40");
				menuAllDO.setResult("0");
				menuAllDO.setResultDesc("event get success");

				ChanelItemDO chanelItemDO = chanelItemManager.queryByChid(chanelItemQuery);

				List<MenuDetailDO> menuDetail = new ArrayList<MenuDetailDO>();

				MenuDetailDO menuDetailDO = new MenuDetailDO();
				menuDetailDO.setChId(chanelItemDO.getChId());
				menuDetailDO.setChName(chanelItemDO.getChName());
				menuDetailDO.setChlogourl(chanelItemDO.getChlogourl());
				menuDetailDO.setIsPlayback(chanelItemDO.getIsPlayback().toString());
				// 筛选直播节目单
				List<MenuInfoDO> menuInfoList = new ArrayList<MenuInfoDO>();
				Calendar c = Calendar.getInstance();
				for (int j = 0; j < 2; j++) {
					MenuInfoDO menuInfoDO = new MenuInfoDO();
					String date = (c.get(Calendar.YEAR)) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
							+ (c.get(Calendar.DATE)) + " 11:22:45";
					menuInfoDO.setEventName("奔跑吧兄弟" + date);
					menuInfoDO.setStartTime(date);
					menuInfoDO.setEndTime(date);
					menuInfoList.add(menuInfoDO);
				}
				c.add(Calendar.HOUR, 24);
				for (int j = 0; j < 2; j++) {
					MenuInfoDO menuInfoDO = new MenuInfoDO();
					String date = (c.get(Calendar.YEAR)) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
							+ (c.get(Calendar.DATE)) + " 11:22:45";
					menuInfoDO.setEventName("奔跑吧兄弟" + date);
					menuInfoDO.setStartTime(date);
					menuInfoDO.setEndTime(date);
					menuInfoList.add(menuInfoDO);
				}
				c.add(Calendar.HOUR, 24);
				for (int j = 0; j < 2; j++) {
					MenuInfoDO menuInfoDO = new MenuInfoDO();
					String date = (c.get(Calendar.YEAR)) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
							+ (c.get(Calendar.DATE)) + " 11:22:45";
					menuInfoDO.setEventName("奔跑吧兄弟" + date);
					menuInfoDO.setStartTime(date);
					menuInfoDO.setEndTime(date);
					menuInfoList.add(menuInfoDO);
				}
				menuDetailDO.setMenuList(menuInfoList);
				// 根据回放标志获取数据
				if (chanelItemDO.getIsPlayback() == 1) {
					menuDetailDO.setFolder(chanelItemDO.getFolder());
					menuDetailDO.setAssetId(chanelItemDO.getAssetid());
					menuDetailDO.setContentId(chanelItemDO.getChId());
				}
				menuDetail.add(menuDetailDO);
				menuAllDO.setChList(menuDetail);
				menuList.add(menuAllDO);
			}
			result.setValue(menuList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getManyMenu error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 查询频道分组
	 * 
	 * @param query
	 * @return TeamAllDO
	 */
	public Result<TeamAllDTO> getTeam(ChanelTeamQuery query) {
		Result<TeamAllDTO> result = new Result<TeamAllDTO>(false);
		try {
			TeamAllDTO teamAllDTO = new TeamAllDTO();
			List<TeamBlistDTO> teamBlist = new ArrayList<TeamBlistDTO>();
			List<ChanelTeamDO> teamDO = chanelTeamManager.queryCacheByRegion(query.getRegionId());

			// 拼装大数据排行数据
			ChanelItemQuery chanelItemQuery = new ChanelItemQuery();
			chanelItemQuery.setRegionId(query.getRegionId());
			LiveRealDO liveRealDO = chanelItemManager.getLive(8, chanelItemQuery);
			TeamBlistDTO bBlistDTO = new TeamBlistDTO();
			// bBlistDTO.setBId("12345");
			bBlistDTO.setBName("排行");
			List<TeamDetailDTO> bDetail = new ArrayList<TeamDetailDTO>();
			if (liveRealDO != null) {
				for (LiveDetailDO liveDetailDO : liveRealDO.getItems()) {
					TeamDetailDTO teamDetailDTO = new TeamDetailDTO();
					ChanelItemDO itemDO = chanelItemManager.queryCacheByChid(liveDetailDO.getContentId());
					teamDetailDTO.setChId(liveDetailDO.getContentId());
					teamDetailDTO.setChName(liveDetailDO.getName());
					teamDetailDTO.setChNo(itemDO.getChNo().toString());
					bDetail.add(teamDetailDTO);
				}
			}
			bBlistDTO.setChList(bDetail);
			teamBlist.add(bBlistDTO);

			for (ChanelTeamDO chanelTeamDO : teamDO) {
				TeamBlistDTO teamBlistDTO = new TeamBlistDTO();
				teamBlistDTO.setBName(chanelTeamDO.getBName());

				List<ChanelItemDO> filmResult = getItemsCache(chanelTeamDO.getId());

				if (filmResult.size() > 0) {
					List<TeamDetailDTO> teamDetail = new ArrayList<TeamDetailDTO>();
					for (ChanelItemDO chanelItemDO : filmResult) {
						TeamDetailDTO teamDetailDTO = new TeamDetailDTO();
						teamDetailDTO.setChId(chanelItemDO.getChId());
						if (chanelItemDO.getByname() != null) {
							teamDetailDTO.setChName(chanelItemDO.getByname());
						} else {
							teamDetailDTO.setChName(chanelItemDO.getChName());
						}
						teamDetailDTO.setChNo(chanelItemDO.getChNo().toString());
						teamDetail.add(teamDetailDTO);
					}
					teamBlistDTO.setChList(teamDetail);
					teamBlist.add(teamBlistDTO);
				} else {
					// 分组内没有频道
					teamBlistDTO.setChList(null);
					teamBlist.add(teamBlistDTO);
				}

			}
			teamAllDTO.setVersion("0");
			teamAllDTO.setResult("0");
			teamAllDTO.setResultDesc("event get success");
			teamAllDTO.setBList(teamBlist);
			result.setValue(teamAllDTO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getTeam error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 通过区域id获取分组首页数据
	 * @param regionId
	 * @return
	 */
	public List<TeamBlistDTO> getTeamDetails(String regionId) {

		try {
			LinkedList<TeamBlistDTO> result = new LinkedList<TeamBlistDTO>();

			Map<String, ChanelItemDO> chanelItemMap = getItemCache(regionId);

			// 排行
			ChanelItemQuery chanelItemQuery = new ChanelItemQuery();
			chanelItemQuery.setRegionId(regionId);
			LiveRealDO liveRealDO = chanelItemManager.getLive(8, chanelItemQuery);
			TeamBlistDTO bBlistDTO = new TeamBlistDTO();
			bBlistDTO.setBName("排行");
			bBlistDTO.setBId("12345");
			List<TeamDetailDTO> bDetail = new ArrayList<TeamDetailDTO>();
			if (liveRealDO != null) {
				for (LiveDetailDO liveDetailDO : liveRealDO.getItems()) {
					TeamDetailDTO teamDetailDTO = new TeamDetailDTO();
					for (String key : chanelItemMap.keySet()) {
						if (chanelItemMap.get(key) != null) {
							ChanelItemDO tmp = chanelItemMap.get(key);
							if (liveDetailDO.getContentId().equals(tmp.getChId())) {
								teamDetailDTO.setChId(tmp.getChId());
								teamDetailDTO.setChName(tmp.getChName());
								teamDetailDTO.setChNo(tmp.getChNo().toString());
								bDetail.add(teamDetailDTO);
								break;
							}
						}
					}
				}
			}
			bBlistDTO.setChList(bDetail);
			result.add(bBlistDTO);
			
			List<ChanelTeamDO> chanelTeamList = chanelTeamManager.queryCacheByRegion(regionId);
			List<Long> newList = new LinkedList<Long>();
			Map<String, ChanelTeamDO> chanelTeamMap = new HashMap<String, ChanelTeamDO>();
			for (ChanelTeamDO item : chanelTeamList) {
				newList.add(item.getId());
				chanelTeamMap.put(item.getId().toString(), item);
				// 加分组
				TeamBlistDTO blistDTO = new TeamBlistDTO();
				blistDTO.setBName(item.getBName());
				blistDTO.setBId(item.getId().toString());
				result.add(blistDTO);
			}

			List<ChanelItemTeamDO> chanelItemTeamDOList = chanelItemTeamManager.queryAllByTeamIds(newList);
			
			// 具体分组
			for (int i = 0; i < chanelItemTeamDOList.size(); i++) {
				TeamBlistDTO blistDTO = null;
				for (TeamBlistDTO item : result) {
					if (item.getBId().equals(chanelItemTeamDOList.get(i).getTeamId() + "")) {
						blistDTO = item;
						break;
					}
				}
				// TeamBlistDTO blistDTO = result.getLast();

				ChanelItemDO itemDTO = chanelItemMap.get(chanelItemTeamDOList.get(i).getItemId().toString());
				TeamDetailDTO detailDTO = new TeamDetailDTO();
				detailDTO.setChId(itemDTO.getChId());
				detailDTO.setChName(itemDTO.getChName());
				detailDTO.setChNo(itemDTO.getChNo().toString());

				blistDTO.add(detailDTO);
			}
			return result;
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	boolean isIncluded(long temp, LinkedList<TeamBlistDTO> result) {
		for (TeamBlistDTO item : result) {
			if (item.getBId().equals(temp + "")) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 通过分组号获取分组内的所有频道
	 * 
	 * @param teamId
	 * @return
	 */
	private List<ChanelItemDO> getitemsByTeam(long teamId) {

		try {
			ChanelItemTeamQuery chanelItemTeamQuery = new ChanelItemTeamQuery();
			chanelItemTeamQuery.setTeamId(teamId);
			List<ChanelItemTeamDO> itemTeamList = chanelItemTeamManager.getByTeamId(chanelItemTeamQuery);

			if (itemTeamList.size() != 0) {
				// 分组内有频道
				List<Long> itemIds = new ArrayList<Long>();
				for (ChanelItemTeamDO chanelItemTeamDO : itemTeamList) {
					itemIds.add(chanelItemTeamDO.getItemId());
				}
				return chanelItemManager.queryOrder(itemIds);
			}

		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<ChanelItemDO>();
	}

	/**
	 * 查询直播推荐排名测试
	 * 
	 * @param query
	 * @return TeamAllDO
	 */
	public Result<TeamAllDO> getTeam1(ChanelTeamQuery query) {
		Result<TeamAllDO> result = new Result<TeamAllDO>(false);
		try {
			TeamAllDO teamAllDO = chanelTeamManager.getTeam(query);
			result.setValue(teamAllDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 查询回放接口
	 * 
	 * @param query
	 * @return TeamAllDO
	 */
	public Result<AssertMenuAllDO> getReplay(ChanelTeamQuery query) {
		Result<AssertMenuAllDO> result = new Result<AssertMenuAllDO>(false);
		try {
			AssertMenuAllDO assertMenuAllDO = chanelTeamManager.getReplay(query);
			result.setValue(assertMenuAllDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 分页查询
	 * 
	 * @param 同步所有频道
	 * @return List<ChanelTeamDO>
	 */
	public Result<List<ChanelTeamDO>> updateAll(ChanelTeamQuery query) {
		Result<List<ChanelTeamDO>> result = new Result<List<ChanelTeamDO>>(false);
		try {
			TeamAllDO teamAllDO = chanelTeamManager.getTeam(query);
			AssertMenuAllDO assertMenuAllDO = chanelTeamManager.getReplay(query);
			int psize = assertMenuAllDO.getCh().size();
			// 初始化回放标志位
			ChanelItemQuery initquery = new ChanelItemQuery();
			initquery.setRegionId(query.getRegionId());
			chanelItemManager.init(initquery);
			// 根据版本号判定节目单是否变化
			// if (teamAllDO.getVersion() == getTeamVersion()) {
			// result.setErrorMessage("201", "版本相同未更新");
			// result.setSuccess(false);
			// } else {
			// SysSettingDO sysSettingDO = new SysSettingDO();
			// sysSettingDO.setId(new Long(72));
			// sysSettingDO.setName("team_version");
			// sysSettingDO.setData(teamAllDO.getVersion().toString());
			int size = teamAllDO.getBList().size();
			for (int i = 0; i < size; i++) {
				ChanelTeamDO chanelTeamDO = new ChanelTeamDO();
				TeamBlistDO teamBlistDO = teamAllDO.getBList().get(i);
				chanelTeamDO.setRegionId(query.getRegionId());
				chanelTeamDO.setBId(teamBlistDO.getBId());
				chanelTeamDO.setBName(teamBlistDO.getBName());

				int csize = teamBlistDO.getChList().size();
				for (int j = 0; j < csize; j++) {
					ChanelItemDO chanelItemDO = new ChanelItemDO();
					TeamDetailDO teamDetailDO = teamBlistDO.getChList().get(j);
					if (teamDetailDO.getType() == 1) {
						// 赋值
						chanelItemDO.setRegionId(query.getRegionId());
						chanelItemDO.setChId(teamDetailDO.getChId());
						chanelItemDO.setChName(teamDetailDO.getChName());
						chanelItemDO.setChNo(teamDetailDO.getChNo());

						// 根据回放接口设置回访标志位
						for (int k = 0; k < psize; k++) {
							AssertMenuDescDO assertMenuDescDO = assertMenuAllDO.getCh().get(k).getAsset().getChM();
							if (assertMenuDescDO.getContentId().equals(teamDetailDO.getChId())) {
								chanelItemDO.setIsPlayback(1);
								chanelItemDO.setAssetid(assertMenuDescDO.getAssetId());
								chanelItemDO.setFolder(assertMenuAllDO.getFolder());
							}
						}
						// 先更新，更新不成功就插入新数据
						int itemCount = chanelItemManager.updateOnly(chanelItemDO);
						if (itemCount == 0) {
							chanelItemManager.insert(chanelItemDO);
						}
					}
				}
			}
			// 更新版本
			// sysSettingManager.update(sysSettingDO);
			result.setSuccess(true);
			// }
		} catch (Exception e) {
			log.error("error," + e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	private int getTeamVersion() {
		String s = LocalCache.get("team_version");
		return Integer.parseInt(s);
	}

	// 从接口获取数据做缓存
	public List<ChanelItemDO> getItemsCache(final long teamId) {

		final String key = teamId + "_" + new Date().getDate();
		if (cache_ChanelItemDO == null) {
			cache_ChanelItemDO = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(1, TimeUnit.DAYS)
					.expireAfterAccess(1, TimeUnit.DAYS).build(new CacheLoader<String, List<ChanelItemDO>>() {
						@Override
						public List<ChanelItemDO> load(String s) throws Exception {
							return null;
						}
					});
		}

		try {
			List<ChanelItemDO> result = (List<ChanelItemDO>) cache_ChanelItemDO.get(key,
					new Callable<List<ChanelItemDO>>() {
						@Override
						public List<ChanelItemDO> call() throws MyException {
							return getitemsByTeam(teamId);
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
	public Map<String, ChanelTeamDO> getTeamCache(final String regionId) {

		final String key = regionId + "_" + new Date().getDate();
		if (cache_team == null) {
			cache_team = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(1, TimeUnit.DAYS)
					.expireAfterAccess(1, TimeUnit.DAYS).build(new CacheLoader<String, Map<String, ChanelTeamDO>>() {
						@Override
						public Map<String, ChanelTeamDO> load(String s) throws Exception {
							return null;
						}
					});
		}

		try {
			Map<String, ChanelTeamDO> result = (Map<String, ChanelTeamDO>) cache_team.get(key,
					new Callable<Map<String, ChanelTeamDO>>() {
						@Override
						public Map<String, ChanelTeamDO> call() throws MyException {
							List<ChanelTeamDO> chanelTeamList = chanelTeamManager.queryCacheByRegion(regionId);
							List<Long> newList = new LinkedList<Long>();
							Map<String, ChanelTeamDO> chanelTeamMap = new HashMap<String, ChanelTeamDO>();
							for (ChanelTeamDO item : chanelTeamList) {
								newList.add(item.getId());
								chanelTeamMap.put(item.getId().toString(), item);
							}
							return chanelTeamMap;
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
	public Map<String, ChanelItemDO> getItemCache(final String regionId) {

		final String key = regionId + "_" + new Date().getDate();
		if (cache_item == null) {
			cache_item = CacheBuilder.newBuilder().maximumSize(1000).refreshAfterWrite(1, TimeUnit.DAYS)
					.expireAfterAccess(1, TimeUnit.DAYS).build(new CacheLoader<String, Map<String, ChanelItemDO>>() {
						@Override
						public Map<String, ChanelItemDO> load(String s) throws Exception {
							return null;
						}
					});
		}

		try {
			Map<String, ChanelItemDO> result = (Map<String, ChanelItemDO>) cache_item.get(key,
					new Callable<Map<String, ChanelItemDO>>() {
						@Override
						public Map<String, ChanelItemDO> call() throws MyException {
							List<ChanelItemDO> chanelItemDOList = chanelItemManager.queryByRegionId(regionId);
							Map<String, ChanelItemDO> chanelItemMap = new HashMap<String, ChanelItemDO>();
							for (ChanelItemDO item : chanelItemDOList) {
								chanelItemMap.put(item.getId().toString(), item);
							}
							return chanelItemMap;
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
