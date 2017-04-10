package com.wasu.ptyw.galaxy.core.ao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.manager.ChanelItemManager;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.dataobject.LiveDetailDO;
import com.wasu.ptyw.galaxy.dal.dataobject.LiveRealDO;
import com.wasu.ptyw.galaxy.dal.dataobject.TeamDetailDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Service("chanelItemAO")
@Slf4j
public class ChanelItemAO extends BaseAO {
	@Resource
	private ChanelItemManager chanelItemManager;

	/**
	 * 新增
	 * 
	 * @param ChanelItemDO
	 * @return 对象ID
	 */
	public Result<Long> save(ChanelItemDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = chanelItemManager.insert(obj);
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
	 * @param ChanelItemDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(ChanelItemDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = chanelItemManager.update(obj);
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
			int num = chanelItemManager.deleteById(id);
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
	 * @return ChanelItemDO
	 */
	public Result<ChanelItemDO> getById(Long id) {
		Result<ChanelItemDO> result = new Result<ChanelItemDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelItemDO obj = chanelItemManager.getById(id);
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
	 * @return List<ChanelItemDO>
	 */
	public Result<List<ChanelItemDO>> getByIds(List<Long> ids) {
		Result<List<ChanelItemDO>> result = new Result<List<ChanelItemDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemDO> list = chanelItemManager.getByIds(ids);
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
	 * 根据多个id查询
	 * 
	 * @param ids
	 * @return List<ChanelItemDO>
	 */
	public Result<List<ChanelItemDO>> queryByIds(List<Long> ids,ChanelItemQuery query) {
		Result<List<ChanelItemDO>> result = new Result<List<ChanelItemDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemDO> list = chanelItemManager.queryByIds(ids,query);
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
	 * @return List<ChanelItemDO>
	 */
	public Result<List<ChanelItemDO>> query(ChanelItemQuery query) {
		Result<List<ChanelItemDO>> result = new Result<List<ChanelItemDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemDO> list = chanelItemManager.query(query);
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
	 * @return List<ChanelItemDO>
	 */
	public Result<List<ChanelItemDO>> queryAll(ChanelItemQuery query) {
		Result<List<ChanelItemDO>> result = new Result<List<ChanelItemDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemDO> list = chanelItemManager.queryrecommend(query);
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
	 * @return List<ChanelItemDO>
	 */
	public Result<ChanelItemDO> queryRePlay(ChanelItemQuery query) {
		Result<ChanelItemDO> result = new Result<ChanelItemDO>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelItemDO list = chanelItemManager.queryRePlay(query);
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
	 * @return List<ChanelItemDO>
	 */
	public Result<List<ChanelItemDO>> queryByRegionId(ChanelItemQuery query) {
		Result<List<ChanelItemDO>> result = new Result<List<ChanelItemDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemDO> list = chanelItemManager.queryByRegionId(query);
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
	 * 查询直播推荐排名
	 * 
	 * @param getLive
	 * @return LiveRealDO
	 */
	public Result<List<TeamDetailDO>> getLive(Integer num, ChanelItemQuery chanelItemQuery) {
		Result<List<TeamDetailDO>> result = new Result<List<TeamDetailDO>>(false);
		try {
			LiveRealDO liveRealDO = chanelItemManager.getLive(num, chanelItemQuery);
			List<TeamDetailDO> teamDetailDO=new ArrayList<TeamDetailDO>();
			for(LiveDetailDO liveDetailDO:liveRealDO.getItems()){
				TeamDetailDO aa=new TeamDetailDO();
				ChanelItemQuery chanelItem=new ChanelItemQuery();
				chanelItem.setChId(liveDetailDO.getContentId());
				ChanelItemDO itemDO=chanelItemManager.queryByChid(chanelItem);
				aa.setChId(liveDetailDO.getContentId());
				aa.setChName(liveDetailDO.getName());
				aa.setChNo(itemDO.getChNo());
				teamDetailDO.add(aa);
			}
			result.setValue(teamDetailDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getLive error," + e);
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
	 * @param getBychId
	 * @return List<ChanelItemDO>
	 */
	public Result<List<ChanelItemDO>> getBychId(List<String> bId) {
		Result<List<ChanelItemDO>> result = new Result<List<ChanelItemDO>>(false);
		try {
			if (bId == null || bId.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemDO> list = chanelItemManager.getBychId(bId);
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getBychId error," + bId, e);
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
	 * @param queryadd
	 * @return List<ChanelItemDO>
	 */
	public Result<List<ChanelItemDO>> queryadd(List<Long> itemIds, ChanelItemQuery chanelItemQuery) {
		Result<List<ChanelItemDO>> result = new Result<List<ChanelItemDO>>(false);
		try {
			if (itemIds == null || itemIds.isEmpty()) {
				List<ChanelItemDO> list = chanelItemManager.queryAll(itemIds, chanelItemQuery);
				result.setValue(list);
			} else {
				List<ChanelItemDO> list = chanelItemManager.queryadd(itemIds, chanelItemQuery);
				result.setValue(list);
			}
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + itemIds, e);
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
	 * @param queryadd
	 * @return List<ChanelItemDO>
	 */
	public Result<List<ChanelItemDO>> queryrecommend(ChanelItemQuery chanelItemQuery) {
		Result<List<ChanelItemDO>> result = new Result<List<ChanelItemDO>>(false);
		try {
				List<ChanelItemDO> list = chanelItemManager.queryrecommend(chanelItemQuery);
				result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," +  e);
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
	 * @param queryadd
	 * @return List<ChanelItemDO>
	 */
	public Result<ChanelItemDO> queryByChid(ChanelItemQuery query) {
		Result<ChanelItemDO> result = new Result<ChanelItemDO>(false);
		try {
				ChanelItemDO list = chanelItemManager.queryByChid(query);
				result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," +  e);
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
	 * @param querydelete
	 * @return List<ChanelItemDO>
	 */
	public Result<List<ChanelItemDO>> querydelete(List<Long> itemIds,ChanelItemQuery chanelItemQuery) {
		Result<List<ChanelItemDO>> result = new Result<List<ChanelItemDO>>(false);
		try {
			if (itemIds == null || itemIds.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemDO> list = chanelItemManager.querydelete(itemIds,chanelItemQuery);
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryadd error," + itemIds, e);
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
	 * @param queryaddCount
	 * @return List<ChanelItemDO>
	 */
	public Result<Integer> queryaddCount(List<Long> itemIds, ChanelItemQuery chanelItemQuery) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (itemIds == null || itemIds.isEmpty()) {
				result.setValue(239);
				result.setSuccess(true);
				return result;
			}
			int num = chanelItemManager.queryaddCount(itemIds, chanelItemQuery);
			if (num >= 0) {
				result.setValue(num);
				result.setSuccess(true);
			} else {
				setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
		} catch (Exception e) {
			log.error("update error," + itemIds, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}
}
