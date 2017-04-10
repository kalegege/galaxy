package com.wasu.ptyw.galaxy.core.ao;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.manager.SysSettingManager;
import com.wasu.ptyw.galaxy.dal.dataobject.SysSettingDO;
import com.wasu.ptyw.galaxy.dal.query.SysSettingQuery;

/**
 * @author wenguang
 * @date 2015年08月24日
 */
@Service("sysSettingAO")
@Slf4j
public class SysSettingAO extends BaseAO {
	@Resource
	private SysSettingManager sysSettingManager;

	/**
	 * 新增
	 * 
	 * @param SysSettingDO
	 * @return 对象ID
	 */
	public Result<Long> save(SysSettingDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = sysSettingManager.insert(obj);
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
	 * @param SysSettingDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(SysSettingDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = sysSettingManager.update(obj);
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
			int num = sysSettingManager.deleteById(id);
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
	 * @return SysSettingDO
	 */
	public Result<SysSettingDO> getById(Long id) {
		Result<SysSettingDO> result = new Result<SysSettingDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			SysSettingDO obj = sysSettingManager.getById(id);
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
	 * @return List<SysSettingDO>
	 */
	public Result<List<SysSettingDO>> getByIds(List<Long> ids) {
		Result<List<SysSettingDO>> result = new Result<List<SysSettingDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<SysSettingDO> list = sysSettingManager.getByIds(ids);
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
	 * @return List<SysSettingDO>
	 */
	public Result<List<SysSettingDO>> query(SysSettingQuery query) {
		Result<List<SysSettingDO>> result = new Result<List<SysSettingDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<SysSettingDO> list = sysSettingManager.query(query);
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
	 * 查询第一条
	 * 
	 * @param query
	 * @return List<SysSettingDO>
	 */
	public Result<SysSettingDO> queryFirst(SysSettingQuery query) {
		Result<SysSettingDO> result = new Result<SysSettingDO>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			SysSettingDO obj = sysSettingManager.queryFirst(query);
			if (obj == null) {
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
			result.setValue(obj);
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
	 * 查询所有配置项
	 * 
	 * @param query
	 * @return List<SysSettingDO>
	 */
	public Result<List<SysSettingDO>> queryAll() {
		Result<List<SysSettingDO>> result = new Result<List<SysSettingDO>>(false);
		try {
			SysSettingQuery query = new SysSettingQuery();
			query.setPageSize(Integer.MAX_VALUE);
			List<SysSettingDO> list = sysSettingManager.query(query);
			result.setValue(list);
			result.setSuccess(true);
		} catch (MyException e) {
			log.error("queryAll error,", e);
			setErrorMessage(result, e.getResultCode());
		}
		return result;
	}

}
