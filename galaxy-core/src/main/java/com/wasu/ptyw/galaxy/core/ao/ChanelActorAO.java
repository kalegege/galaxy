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
import com.wasu.ptyw.galaxy.core.dto.ActorDTO;
import com.wasu.ptyw.galaxy.core.manager.ChanelActorManager;
import com.wasu.ptyw.galaxy.dal.dataobject.Actor1DO;
import com.wasu.ptyw.galaxy.dal.dataobject.Actor1DTO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelActorDO;
import com.wasu.ptyw.galaxy.dal.dataobject.QueryActorAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.QueryActorDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelActorQuery;

/**
 * @author wenguang
 * @date 2016年08月24日
 */
@Service("chanelActorAO")
@Slf4j
public class ChanelActorAO extends BaseAO {
	@Resource
	private ChanelActorManager chanelActorManager;

	/**
	 * 新增
	 * 
	 * @param ChanelActorDO
	 * @return 对象ID
	 */
	public Result<Long> save(ChanelActorDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = chanelActorManager.insert(obj);
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
	 * @param ChanelActorDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(ChanelActorDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = chanelActorManager.update(obj);
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
			ChanelActorDO obj = chanelActorManager.getById(id);
			if(obj==null){
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
			//删除线上的数据
			if(obj.getPublishid()>0){
				int num = chanelActorManager.deleteById(obj.getPublishid());
			}
			int num = chanelActorManager.deleteById(id);
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
	 * @return ChanelActorDO
	 */
	public Result<ChanelActorDO> getById(Long id) {
		Result<ChanelActorDO> result = new Result<ChanelActorDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelActorDO obj = chanelActorManager.getById(id);
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
	 * @return List<ChanelActorDO>
	 */
	public Result<List<ChanelActorDO>> getByIds(List<Long> ids) {
		Result<List<ChanelActorDO>> result = new Result<List<ChanelActorDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelActorDO> list = chanelActorManager.getByIds(ids);
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
	 * @return List<ChanelActorDO>
	 */
	public Result<List<ChanelActorDO>> query(ChanelActorQuery query) {
		Result<List<ChanelActorDO>> result = new Result<List<ChanelActorDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelActorDO> list = chanelActorManager.query(query);
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
	 * @return List<ChanelActorDO>
	 */
	public Result<List<ActorDTO>> queryActor(String keywords) {

		Result<List<ActorDTO>> result = new Result<List<ActorDTO>>(false);
		List<ActorDTO> result1 = new ArrayList<ActorDTO>();

		try {
			QueryActorAllDO list = chanelActorManager.getActors(keywords);
			
			if (list != null) {
				List<QueryActorDO> queryActorDOs = list.getData();
				List<QueryActorDO> aList=new ArrayList<QueryActorDO>();
				List<QueryActorDO> bList=new ArrayList<QueryActorDO>();
				List<QueryActorDO> cList=new ArrayList<QueryActorDO>();
				List<QueryActorDO> dList=new ArrayList<QueryActorDO>();
				
				for (QueryActorDO item :queryActorDOs) {
					switch (Integer.parseInt(item.getType())) {
					// 电视剧
					case 37:
						aList.add(item);
						break;
					// 电影
					case 36:
						bList.add(item);
						break;
					// 综艺
					case 13:
						cList.add(item);
						break;
					// 资讯
					case 15:
						dList.add(item);
						break;
					default:
						break;
					}
				}
				for (int i = 0; i < 10; i++) {
					ActorDTO ActorDTO = new ActorDTO();
					if((aList.size() !=  0)&&(aList.size() > i)){
						ActorDTO.setAName(aList.get(i).getName());
					}
					if((bList.size() != 0)&&(bList.size() > i)){
						ActorDTO.setBName(bList.get(i).getName());
					}
					if((cList.size() != 0)&&(cList.size() > i)){
						ActorDTO.setCName(cList.get(i).getName());
					}
					if((dList.size() != 0)&&(dList.size() > i)){
						ActorDTO.setDName(dList.get(i).getName());
					}
					result1.add(ActorDTO);
				}
			}
			result.setValue(result1);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("query error," + keywords, e);
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
	 * @return List<ChanelActorDO>
	 * @throws MyException 
	 */
	public Result<List<ActorDTO>> queryActor1(String keywords) throws MyException {
		
		Result<List<ActorDTO>> result = new Result<List<ActorDTO>>(false);
		List<ActorDTO> result1 = new ArrayList<ActorDTO>();

		try {
			//Actor1DO list =chanelActorManager.getActor1(keywords);
			Actor1DO list =chanelActorManager.getActor2(keywords);
			//QueryActorAllDO list = chanelActorManager.getActors(keywords);
			
			if (list.getContents() != null) {
				List<Actor1DTO> queryActorDOs = list.getContents();
				List<Actor1DTO> aList=new ArrayList<Actor1DTO>();
				List<Actor1DTO> bList=new ArrayList<Actor1DTO>();
				List<Actor1DTO> cList=new ArrayList<Actor1DTO>();
				List<Actor1DTO> dList=new ArrayList<Actor1DTO>();
				
				for (Actor1DTO item :queryActorDOs) {
					switch (Integer.parseInt(item.getContentType())) {
					// 电视剧
					case 37:
						aList.add(item);
						break;
					// 电影
					case 36:
						bList.add(item);
						break;
					// 综艺
					case 13:
						cList.add(item);
						break;
					// 资讯
					case 15:
						dList.add(item);
						break;
					default:
						break;
					}
				}
				for (int i = 0; i < 10; i++) {
					ActorDTO ActorDTO = new ActorDTO();
					if((aList.size() !=  0)&&(aList.size() > i)){
						ActorDTO.setAName(aList.get(i).getName());
					}
					if((bList.size() != 0)&&(bList.size() > i)){
						ActorDTO.setBName(bList.get(i).getName());
					}
					if((cList.size() != 0)&&(cList.size() > i)){
						ActorDTO.setCName(cList.get(i).getName());
					}
					if((dList.size() != 0)&&(dList.size() > i)){
						ActorDTO.setDName(dList.get(i).getName());
					}
					result1.add(ActorDTO);
				}
			}
			result.setValue(result1);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("query error," + keywords, e);
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
	 * @return List<ChanelActorDO>
	 */
	public Result<List<ChanelActorDO>> getRecommend(ChanelActorQuery query) {
		Result<List<ChanelActorDO>> result = new Result<List<ChanelActorDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelActorDO> listAll =chanelActorManager.getRecommend(query);
			result.setValue(listAll);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getRecommend error," + query, e);
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
	 * @return List<ChanelActorDO>
	 */
	public Result<List<ChanelActorDO>> queryRecommend(ChanelActorQuery query) {
		Result<List<ChanelActorDO>> result = new Result<List<ChanelActorDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			//query.setStatus(1);
			List<ChanelActorDO> result1=chanelActorManager.queryRecommend(query);
			
			result.setValue(result1);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getRecommend error," + query, e);
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
			int num = chanelActorManager.updateStatusByIds(ids, status);
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

	// 去除超过4个的上线艺人
	public Result<Integer> sort() {
		Result<Integer> result = new Result<Integer>(false);
		int num = 0;
		try {
			ChanelActorQuery query = new ChanelActorQuery();
			List<ChanelActorDO> listOnline = chanelActorManager.queryOnline(query);
			if (listOnline.size() > 4) {
				ChanelActorDO chanelActorDO = new ChanelActorDO();
				chanelActorDO.setId(listOnline.get(0).getId());
				chanelActorDO.setStatus(0);
				num = chanelActorManager.update(chanelActorDO);
			}
			result.setValue(num);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("sort error,ids=" + ", status=", e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}
    public Result<Integer> sort(long id){
    	Result<Integer> result = new Result<Integer>(false);
    	int num = 0;
    	try{
    		ChanelActorQuery query = new ChanelActorQuery();
    		List<ChanelActorDO> listOnline = chanelActorManager.queryOnline(query);
    		if(listOnline.size() > 4){
    			ChanelActorDO chanelActorDO = new ChanelActorDO();
    			if(id == listOnline.get(0).getId()){
    				chanelActorDO.setId(listOnline.get(1).getId());
    				chanelActorDO.setStatus(0);
    				
    			}else{
    				chanelActorDO.setId(listOnline.get(0).getId());
    				chanelActorDO.setStatus(0);
    			}	
				num = chanelActorManager.update(chanelActorDO);
    			result.setValue(num);
    			result.setSuccess(true);
    		}
    	}catch(Exception e){
    		log.error("sort error,id=" + ",status=",e);
    		if(e instanceof MyException){
    			setErrorMessage(result,((MyException) e).getResultCode());
    		}else{
    			setErrorMessage(result,ResultCode.SYSTEM_ERROR);
    		}
    	}
    	return result;
    }
	public Result<Integer> publishById(long id) {
		// TODO Auto-generated method stub
		Result<Integer> result = new Result<Integer>(false);
		try{
			if(!NumUtil.isGreaterZero(id)){
				return setErrorMessage(result,ResultCode.PARAM_INPUT_INVALID);
			}
			int num = chanelActorManager.publishById(id);
			if(num > 0){
				result.setValue(num);
				result.setSuccess(true);
			}else{
				setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
		}catch(Exception e){
			log.error("updateById error,"+id,e );
			if(e instanceof MyException){
				setErrorMessage(result,((MyException) e).getResultCode());
			}else{
				setErrorMessage(result,ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	
}
