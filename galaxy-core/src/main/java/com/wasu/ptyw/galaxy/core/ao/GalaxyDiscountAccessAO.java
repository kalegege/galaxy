package com.wasu.ptyw.galaxy.core.ao;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.enums.DbStatus;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.dto.DiscountDTO;
import com.wasu.ptyw.galaxy.core.manager.GalaxyDiscountAccessManager;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountAccessDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyDiscountAccessQuery;

/**
 * @author wenguang
 * @date 2015年09月25日
 */
@Service("galaxyDiscountAccessAO")
@Slf4j
public class GalaxyDiscountAccessAO extends BaseAO {
	@Resource
	private GalaxyDiscountAccessManager galaxyDiscountAccessManager;
	/**
	 * 默认价格,不要改成太小
	 */
	int defaultPrice = 500;

	/**
	 * 新增
	 * 
	 * @param GalaxyDiscountAccessDO
	 * @return 对象ID
	 */
	public Result<Long> save(GalaxyDiscountAccessDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = galaxyDiscountAccessManager.insert(obj);
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
	 * @param GalaxyDiscountAccessDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(GalaxyDiscountAccessDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = galaxyDiscountAccessManager.update(obj);
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
			int num = galaxyDiscountAccessManager.deleteById(id);
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
	 * @return GalaxyDiscountAccessDO
	 */
	public Result<GalaxyDiscountAccessDO> getById(Long id) {
		Result<GalaxyDiscountAccessDO> result = new Result<GalaxyDiscountAccessDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			GalaxyDiscountAccessDO obj = galaxyDiscountAccessManager.getById(id);
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
	 * @return List<GalaxyDiscountAccessDO>
	 */
	public Result<List<GalaxyDiscountAccessDO>> getByIds(List<Long> ids) {
		Result<List<GalaxyDiscountAccessDO>> result = new Result<List<GalaxyDiscountAccessDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyDiscountAccessDO> list = galaxyDiscountAccessManager.getByIds(ids);
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
	 * @return List<GalaxyDiscountAccessDO>
	 */
	public Result<List<GalaxyDiscountAccessDO>> query(GalaxyDiscountAccessQuery query) {
		Result<List<GalaxyDiscountAccessDO>> result = new Result<List<GalaxyDiscountAccessDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyDiscountAccessDO> list = galaxyDiscountAccessManager.query(query);
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
	 * 查询总数
	 * 
	 * @param query
	 * @return Integer
	 */
	public Result<Integer> queryCount(GalaxyDiscountAccessQuery query) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			result.setValue(galaxyDiscountAccessManager.queryCount(query));
		} catch (Exception e) {
			log.error("queryCount error," + query, e);
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
			int num = galaxyDiscountAccessManager.updateStatusByIds(ids, status);
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
	 * 获取一个最小的折扣价
	 * 
	 * @param query
	 * @return List<GalaxyDiscountAccessDO>
	 */
	public Result<DiscountDTO> queryMyDiscount(long userId) {
		Result<DiscountDTO> result = new Result<DiscountDTO>(false);
		try {
			if (userId < 1) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			GalaxyDiscountAccessQuery query = new GalaxyDiscountAccessQuery();
			query.setUserId(userId);
			query.setPageSize(Integer.MAX_VALUE);
			query.setStatus(DbStatus.VALID.getCode());
			List<GalaxyDiscountAccessDO> list = galaxyDiscountAccessManager.query(query);
			DiscountDTO dto = null;
			for (GalaxyDiscountAccessDO item : list) {
				if (StringUtils.isEmpty(item.getDes()) || DateUtil.expire(item.getGmtCreate(), DateUtil.DAY_7))//7天有效期
					continue;
				DiscountDTO discount = JSON.parseObject(item.getDes(), DiscountDTO.class);
				discount.setDiscountId(item.getId());
				dto = comparePrice(defaultPrice, dto, discount);
				if (dto.minPrice(defaultPrice) == 0)
					break;
			}
			result.setValue(dto);
		} catch (Exception e) {
			log.error("query error,userId=" + userId, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 查询今日抽奖总数
	 * 
	 * @param query
	 * @return Integer
	 */
	public Result<Integer> queryTodayCount(Long userId) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (!NumUtil.isGreaterZero(userId)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			GalaxyDiscountAccessQuery query = new GalaxyDiscountAccessQuery();
			query.setUserId(userId);
			query.setGmtDay(DateUtil.now());
			query.setPageSize(Integer.MAX_VALUE);
			query.setOrderBy("id desc");

			result.setValue(galaxyDiscountAccessManager.queryCount(query));
		} catch (Exception e) {
			log.error("queryTodayCount error,userId=" + userId, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	private DiscountDTO comparePrice(int price, DiscountDTO dest, DiscountDTO org) {
		if (dest == null)
			return org;
		int destPrice = dest.minPrice(price);
		int orgPrice = org.minPrice(price);
		return destPrice > orgPrice ? org : dest;
	}
}
