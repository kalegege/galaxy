package com.wasu.ptyw.galaxy.core.ao;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses.TeredoInfo;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.enums.DbStatus;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.AliasMethod;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.dto.DiscountDTO;
import com.wasu.ptyw.galaxy.core.manager.GalaxyDiscountManager;
import com.wasu.ptyw.galaxy.core.spring.interceptor.TerminalInfo;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountAccessDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyUserDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyDiscountQuery;

/**
 * @author wenguang
 * @date 2015年09月01日
 */
@Service("galaxyDiscountAO")
@Slf4j
public class GalaxyDiscountAO extends BaseAO {
	@Resource
	GalaxyDiscountManager galaxyDiscountManager;
	@Resource
	GalaxyDiscountAccessAO galaxyDiscountAccessAO;
	@Resource
	GalaxyUserAO galaxyUserAO;

	/**
	 * 新增
	 * 
	 * @param DiscountDTO
	 * @return 对象ID
	 */
	public Result<Long> save(GalaxyDiscountDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = galaxyDiscountManager.insert(obj);
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
	 * @param DiscountDTO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(GalaxyDiscountDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = galaxyDiscountManager.update(obj);
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
			int num = galaxyDiscountManager.deleteById(id);
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
	 * @return GalaxyDiscountDO
	 */
	public Result<GalaxyDiscountDO> getById(Long id) {
		Result<GalaxyDiscountDO> result = new Result<GalaxyDiscountDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			GalaxyDiscountDO obj = galaxyDiscountManager.getById(id);
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
	 * @return List<GalaxyDiscountDO>
	 */
	public Result<List<GalaxyDiscountDO>> getByIds(List<Long> ids) {
		Result<List<GalaxyDiscountDO>> result = new Result<List<GalaxyDiscountDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyDiscountDO> list = galaxyDiscountManager.getByIds(ids);
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
	 * @return List<GalaxyDiscountDO>
	 */
	public Result<List<GalaxyDiscountDO>> query(GalaxyDiscountQuery query) {
		Result<List<GalaxyDiscountDO>> result = new Result<List<GalaxyDiscountDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyDiscountDO> list = galaxyDiscountManager.query(query);
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
			int num = galaxyDiscountManager.updateStatusByIds(ids, status);
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

	/**
	 * 获取所有能用的折扣
	 * 
	 * @param query
	 * @return List<GalaxyDiscountDO>
	 */
	public Result<List<GalaxyDiscountDO>> queryOnline() {
		Result<List<GalaxyDiscountDO>> result = new Result<List<GalaxyDiscountDO>>(false);
		try {
			GalaxyDiscountQuery query = new GalaxyDiscountQuery();
			query.setRegionCode(TerminalInfo.getRegion());
			query.setStatus(DbStatus.VALID.getCode());
			query.setOrderBy("priority");
			List<GalaxyDiscountDO> list = galaxyDiscountManager.query(query);
			result.setValue(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryOnline error", e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 抽奖
	 * 
	 * @param id
	 * @return GalaxyDiscountDO
	 */
	public Result<DiscountDTO> random(GalaxyDiscountAccessDO access, GalaxyUserDO user) {
		Result<DiscountDTO> result = new Result<DiscountDTO>(false);
		try {
			if (access == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}

			// 抽奖次数校验，每次购买加1+每日1次
			int permitCount = NumberUtils.toInt(user.getFeature(DbContant.FEA_DISCOUNT_COUNT));
			int count = 0;
			Result<Integer> accessResult = galaxyDiscountAccessAO.queryTodayCount(access.getUserId());
			if (accessResult.isSuccess()) {
				count = accessResult.getValue();
			}
			if (count > 0 && permitCount <= 0) {
				return setErrorMessage(result, ResultCode.DISCOUNT_COUNT_PERMIT);
			}

			Result<List<GalaxyDiscountDO>> queryResult = queryOnline();
			if (!queryResult.isSuccess() || CollectionUtils.isEmpty(queryResult.getValue())) {
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
			// 抽奖
			GalaxyDiscountDO item = random(queryResult.getValue());
			DiscountDTO dto = null;
			if (item != null) {
				dto = new DiscountDTO();
				copyProperties(dto, item);
				// 插入用户中奖记录
				access.setDes(JSON.toJSONString(dto));
				access.setStatus(DbStatus.VALID.getCode());
				galaxyDiscountAccessAO.save(access);
			} else {
				// 插入用户未中奖记录
				access.setStatus(DbStatus.D.getCode());
				galaxyDiscountAccessAO.save(access);
			}
			if (count > 0) {
				user.putFeature(DbContant.FEA_DISCOUNT_COUNT, (permitCount - 1) + "");
				galaxyUserAO.update(user);
			}
			result.setValue(dto);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("random error,access=" + access, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	private GalaxyDiscountDO random(List<GalaxyDiscountDO> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		List<Double> rates = Lists.newArrayList();
		int totalRate = 0;
		for (GalaxyDiscountDO item : list) {
			totalRate += item.getRate();
			rates.add(item.getRate() / 100.0);
		}
		// 补齐100%
		if (totalRate < 100) {
			rates.add((100 - totalRate) / 100.0);
		}

		AliasMethod method = new AliasMethod(rates);
		int index = method.next();
		// 不是补齐的索引
		if (index < list.size()) {
			return list.get(index);
		}
		return null;
	}

}
