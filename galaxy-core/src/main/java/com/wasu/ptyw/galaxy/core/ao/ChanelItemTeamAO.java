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
import com.wasu.ptyw.galaxy.core.dto.ChanelItemDTO;
import com.wasu.ptyw.galaxy.core.manager.ChanelItemManager;
import com.wasu.ptyw.galaxy.core.manager.ChanelItemTeamManager;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemTeamDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemTeamQuery;

/**
 * @author wenguang
 * @date 2016年06月27日
 */
@Service("chanelItemTeamAO")
@Slf4j
public class ChanelItemTeamAO extends BaseAO {
	@Resource
	private ChanelItemTeamManager chanelItemTeamManager;
	@Resource
	private ChanelItemManager chanelItemManager;

	/**
	 * 新增
	 * 
	 * @param ChanelItemTeamDO
	 * @return 对象ID
	 */
	public Result<Long> save(ChanelItemTeamDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = chanelItemTeamManager.insert(obj);
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
	 * @param ChanelItemTeamDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(ChanelItemTeamDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = chanelItemTeamManager.updateItemTeam(obj);
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
			int num = chanelItemTeamManager.deleteById(id);
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
	 * 根据id删除
	 * 
	 * @param id
	 * @return 删除成功的记录数
	 */
	public Result<Integer> deleteByIds(ChanelItemTeamDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			int num = chanelItemTeamManager.deleteByOrder(obj);
			if (num > 0) {
				result.setValue(num);
				result.setSuccess(true);
			} else {
				setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
		} catch (Exception e) {
			log.error("deleteByIds error," + obj, e);
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
	 * @return ChanelItemTeamDO
	 */
	public Result<ChanelItemTeamDO> getById(Long id) {
		Result<ChanelItemTeamDO> result = new Result<ChanelItemTeamDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelItemTeamDO obj = chanelItemTeamManager.getById(id);
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
	 * @return List<ChanelItemTeamDO>
	 */
	public Result<List<ChanelItemTeamDO>> getByIds(List<Long> ids) {
		Result<List<ChanelItemTeamDO>> result = new Result<List<ChanelItemTeamDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemTeamDO> list = chanelItemTeamManager.getByIds(ids);
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
	 * @return List<ChanelItemTeamDO>
	 */
	public Result<List<ChanelItemTeamDO>> query(ChanelItemTeamQuery query) {
		Result<List<ChanelItemTeamDO>> result = new Result<List<ChanelItemTeamDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemTeamDO> list = chanelItemTeamManager.query(query);
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
	 * @return List<ChanelItemTeamDO>
	 */
	public Result<List<ChanelItemTeamDO>> queryAllByTeamId(ChanelItemTeamQuery query) {
		Result<List<ChanelItemTeamDO>> result = new Result<List<ChanelItemTeamDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemTeamDO> list = chanelItemTeamManager.queryAllByTeamId(query);
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
	 * @return List<ChanelItemTeamDO>
	 */
	public Result<ChanelItemDTO> queryDTO(ChanelItemTeamQuery query) {
		Result<ChanelItemDTO> result = new Result<ChanelItemDTO>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelItemDTO chanelItemDTO=new ChanelItemDTO();
			//chanelItemQuery.setId(query.getItemId());
			List<ChanelItemTeamDO> list1 = chanelItemTeamManager.query(query);
			ChanelItemDO list2=chanelItemManager.getById(query.getItemId());
			chanelItemDTO.setChName(list2.getChName());
			chanelItemDTO.setOrderId(list1.get(0).getOrderId());
			chanelItemDTO.setId(list1.get(0).getId());
			result.setValue(chanelItemDTO);
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
	 * 根据id查询
	 * 
	 * @param id
	 * @return ChanelItemTeamDO
	 */
	public Result<List<ChanelItemTeamDO>> getByTeamId(ChanelItemTeamQuery query) {
		Result<List<ChanelItemTeamDO>> result = new Result<List<ChanelItemTeamDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemTeamDO> list = chanelItemTeamManager.getByTeamId(query);
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getByIds error," + query, e);
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
	 * @return ChanelItemTeamDO
	 */
	public Result<List<ChanelItemDTO>> getByTeam(ChanelItemTeamQuery query) {
		Result<List<ChanelItemDTO>> result = new Result<List<ChanelItemDTO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemTeamDO> list1 = chanelItemTeamManager.getByTeam1(query);
			List<ChanelItemDTO> list=new ArrayList<ChanelItemDTO>();
			if(list1.size() > 0){
			//获取数组
			List<Long> itemIds = new ArrayList<Long>();
			for (ChanelItemTeamDO chanelItemTeamDO : list1) {
				itemIds.add(chanelItemTeamDO.getItemId());
			}
			//按照order顺序查询列表
			List<ChanelItemDO> list2 =chanelItemManager.queryOrder(itemIds);
			int size=list2.size();
			for(int i=0;i<size;i++){
				ChanelItemDTO chanelItemDTO=new ChanelItemDTO();
				ChanelItemTeamDO l1=list1.get(i);
				ChanelItemDO l2=list2.get(i);
				chanelItemDTO.setId(l1.getItemId());
				chanelItemDTO.setChlogourl(l2.getChlogourl());
				chanelItemDTO.setChName(l2.getChName());
				chanelItemDTO.setByname(l2.getByname());
				chanelItemDTO.setType(l2.getType());
				chanelItemDTO.setChtype(l2.getChtype());
				chanelItemDTO.setOrderId(l1.getOrderId());
				chanelItemDTO.setChlogoName(l2.getChlogoName());
				list.add(chanelItemDTO);
			}
			}else{
				list=null;
			}
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getByIds error," + query, e);
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
	 * @return ChanelItemTeamDO
	 */
	public Result<List<ChanelItemTeamDO>> getAllByTeamId(ChanelItemTeamQuery query) {
		Result<List<ChanelItemTeamDO>> result = new Result<List<ChanelItemTeamDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelItemTeamDO> list = chanelItemTeamManager.getAllByTeamId(query);
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getByIds error," + query, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}
	
	
}
