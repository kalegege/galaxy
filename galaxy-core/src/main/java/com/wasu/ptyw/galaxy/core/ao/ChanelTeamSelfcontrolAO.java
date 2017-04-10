package com.wasu.ptyw.galaxy.core.ao;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.manager.ChanelTeamSelfcontrolManager;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamSelfcontrolDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamSelfcontrolQuery;

/**
 * @author wenguang
 * @date 2016年07月21日
 */
@Service("chanelTeamSelfcontrolAO")
@Slf4j
public class ChanelTeamSelfcontrolAO extends BaseAO {
	@Resource
	private ChanelTeamSelfcontrolManager chanelTeamSelfcontrolManager;

	/**
	 * 新增
	 * 
	 * @param ChanelTeamSelfcontrolDO
	 * @return 对象ID
	 */
	public Result<Long> save(ChanelTeamSelfcontrolDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = chanelTeamSelfcontrolManager.insert(obj);
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
	 * @param ChanelTeamSelfcontrolDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(ChanelTeamSelfcontrolDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = chanelTeamSelfcontrolManager.update(obj);
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
			int num = chanelTeamSelfcontrolManager.deleteById(id);
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
	 * @return ChanelTeamSelfcontrolDO
	 */
	public Result<ChanelTeamSelfcontrolDO> getById(Long id) {
		Result<ChanelTeamSelfcontrolDO> result = new Result<ChanelTeamSelfcontrolDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelTeamSelfcontrolDO obj = chanelTeamSelfcontrolManager.getById(id);
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
	 * @return List<ChanelTeamSelfcontrolDO>
	 */
	public Result<List<ChanelTeamSelfcontrolDO>> getByIds(List<Long> ids) {
		Result<List<ChanelTeamSelfcontrolDO>> result = new Result<List<ChanelTeamSelfcontrolDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamSelfcontrolDO> list = chanelTeamSelfcontrolManager.getByIds(ids);
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
	 * @return List<ChanelTeamSelfcontrolDO>
	 */
	public Result<List<ChanelTeamSelfcontrolDO>> query(ChanelTeamSelfcontrolQuery query) {
		Result<List<ChanelTeamSelfcontrolDO>> result = new Result<List<ChanelTeamSelfcontrolDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamSelfcontrolDO> list = chanelTeamSelfcontrolManager.query(query);
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
	 * @return List<ChanelTeamSelfcontrolDO>
	 */
	public Result<List<ChanelTeamSelfcontrolDO>> getrecommend(ChanelTeamSelfcontrolQuery query) {
		Result<List<ChanelTeamSelfcontrolDO>> result = new Result<List<ChanelTeamSelfcontrolDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamSelfcontrolDO> list = chanelTeamSelfcontrolManager.getrecommend(query);
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
}
