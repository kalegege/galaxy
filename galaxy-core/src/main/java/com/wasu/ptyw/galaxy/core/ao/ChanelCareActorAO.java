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
import com.wasu.ptyw.galaxy.core.manager.ChanelActorManager;
import com.wasu.ptyw.galaxy.core.manager.ChanelCareActorManager;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelActorDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelCareActorDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelActorQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelCareActorQuery;

/**
 * @author wenguang
 * @date 2016年11月07日
 */
@Service("chanelCareActorAO")
@Slf4j
public class ChanelCareActorAO extends BaseAO {
	@Resource
	private ChanelCareActorManager chanelCareActorManager;
	@Resource
	private ChanelActorManager chanelActorManager;

	/**
	 * 新增
	 * 
	 * @param ChanelCareActorDO
	 * @return 对象ID
	 */
	public Result<Long> save(ChanelCareActorDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = chanelCareActorManager.insert(obj);
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
	 * 新增关注
	 * 
	 * @param ChanelCareActorDO
	 * @return 对象ID
	 */
	public Result<Long> insertcare(ChanelCareActorDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = chanelCareActorManager.update(obj);
			if (num == 0) {
				Long id = chanelCareActorManager.insert(obj);
				result.setValue(id);
				result.setSuccess(true);
			} else {
				result.setValue(null);
				result.setSuccess(false);
			}
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
	 * 新增关注
	 * 
	 * @param ChanelCareActorDO
	 * @return 对象ID
	 */
	public Result<Long> insert(String name, String stbid) {
		Result<Long> result = new Result<Long>(false);
		try {
			if ((name == null) || (stbid == null)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			ChanelActorQuery chanelActorQuery = new ChanelActorQuery();
			chanelActorQuery.setName(name);
			List<ChanelActorDO> actorList = chanelActorManager.query(chanelActorQuery);
			if (actorList.size() > 0) {
				ChanelCareActorDO chanelCareActorDO = new ChanelCareActorDO();
				chanelCareActorDO.setActorId(Integer.parseInt(actorList.get(0).getId().toString()));
				chanelCareActorDO.setUid(stbid);
				chanelCareActorDO.setStatus(1);
				Long id = chanelCareActorManager.insert(chanelCareActorDO);
				result.setValue(id);
				result.setSuccess(true);
			} else {
				result.setValue(null);
				result.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("insert error," + name + stbid, e);
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
	 * @param ChanelCareActorDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(ChanelCareActorDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = chanelCareActorManager.update(obj);
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
			int num = chanelCareActorManager.deleteById(id);
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
	 * @return ChanelCareActorDO
	 */
	public Result<ChanelCareActorDO> getById(Long id) {
		Result<ChanelCareActorDO> result = new Result<ChanelCareActorDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelCareActorDO obj = chanelCareActorManager.getById(id);
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
	 * @return List<ChanelCareActorDO>
	 */
	public Result<List<ChanelCareActorDO>> getByIds(List<Long> ids) {
		Result<List<ChanelCareActorDO>> result = new Result<List<ChanelCareActorDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelCareActorDO> list = chanelCareActorManager.getByIds(ids);
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
	 * @return List<ChanelCareActorDO>
	 */
	public Result<List<ChanelActorDO>> query(ChanelCareActorQuery query) {
		Result<List<ChanelActorDO>> result = new Result<List<ChanelActorDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelCareActorDO> list = chanelCareActorManager.query(query);
			if (list.size() > 0) {
				List<Integer> listNum = new ArrayList<Integer>();
				for (int i = 0; i < list.size(); i++) {
					listNum.add(list.get(i).getActorId());
				}
				List<ChanelActorDO> chanelActorList = chanelActorManager.getByActorIds(listNum);
				result.setValue(chanelActorList);
				result.setSuccess(true);
			} else {
				result.setValue(new ArrayList<ChanelActorDO>());
				result.setSuccess(true);
			}
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
	 * @return List<ChanelCareActorDO>
	 */
	public Result<ChanelActorDO> queryCare(ChanelCareActorQuery query,String name) {
		Result<ChanelActorDO> result = new Result<ChanelActorDO>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelCareActorDO> list = chanelCareActorManager.query(query);
			if (list.size() > 0) {
				List<Integer> listNum = new ArrayList<Integer>();
				for (int i = 0; i < list.size(); i++) {
					listNum.add(list.get(i).getActorId());
				}
				List<ChanelActorDO> chanelActorList = chanelActorManager.getByActorIds(listNum);
				for(ChanelActorDO chanleItem:chanelActorList){
					if(chanleItem.getName().equals(name)){
						result.setValue(chanleItem);
					}
				}
				if(result.getValue() != null){
					result.setSuccess(true);
				}else{
					result.setSuccess(false);
				}
			} else {
				result.setValue(new ChanelActorDO());
				result.setSuccess(true);
			}
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
	 * @return List<ChanelCareActorDO>
	 */
	public Result<List<ChanelCareActorDO>> queryDelete(String name, String stbid) {
		Result<List<ChanelCareActorDO>> result = new Result<List<ChanelCareActorDO>>(false);
		try {
			if ((name == null) || (stbid == null)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}

			ChanelActorQuery chanelActorQuery = new ChanelActorQuery();
			chanelActorQuery.setName(name);
			List<ChanelActorDO> actorList = chanelActorManager.query(chanelActorQuery);
			if (actorList.size() > 0) {
				ChanelCareActorQuery chanelCareActorQuery = new ChanelCareActorQuery();
				chanelCareActorQuery.setActorId(Integer.parseInt(actorList.get(0).getId().toString()));
				chanelCareActorQuery.setUid(stbid);
				chanelCareActorQuery.setStatus(1);
				List<ChanelCareActorDO> chanelCareList = chanelCareActorManager.query(chanelCareActorQuery);
				result.setValue(chanelCareList);
				result.setSuccess(true);
			} else {
				result.setValue(null);
				result.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("query error," + name + stbid, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 根据多个id更新状态
	 * 
	 * @param ids
	 * @return 更新成功的记录数
	 */
	public Result<Integer> updateStatusByIds(List<Long> ids, int status) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			int num = chanelCareActorManager.updateStatusByIds(ids, status);
			result.setValue(num);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("updateStatusByIds error,ids=" + ids + ", status=" + status, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

}
