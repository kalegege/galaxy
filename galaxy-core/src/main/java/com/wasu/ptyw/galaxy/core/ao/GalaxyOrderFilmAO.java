package com.wasu.ptyw.galaxy.core.ao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.core.dto.OrderFilmDTO;
import com.wasu.ptyw.galaxy.core.manager.GalaxyFilmManager;
import com.wasu.ptyw.galaxy.core.manager.GalaxyOrderFilmManager;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.FilmStatus;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;

/**
 * @author wenguang
 * @date 2015年06月24日
 */
@Service("galaxyOrderFilmAO")
@Slf4j
public class GalaxyOrderFilmAO extends BaseAO {
	@Resource
	GalaxyOrderFilmManager galaxyOrderFilmManager;
	@Resource
	GalaxyFilmManager galaxyFilmManager;
	@Resource
	GalaxyWeixinFollowAO galaxyWeixinFollowAO;
	@Resource
	GalaxyDiscountAccessAO galaxyDiscountAccessAO;

	/**
	 * 新增
	 * 
	 * @param GalaxyOrderFilmDO
	 * @return 对象ID
	 */
	public Result<Long> save(GalaxyOrderFilmDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = galaxyOrderFilmManager.insert(obj);
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
	 * @param GalaxyOrderFilmDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(GalaxyOrderFilmDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = galaxyOrderFilmManager.update(obj);
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
	public Result<Integer> deleteById(long id) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (id <= 0) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			int num = galaxyOrderFilmManager.deleteById(id);
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
	 * @return GalaxyOrderFilmDO
	 */
	public Result<GalaxyOrderFilmDO> getById(long id) {
		Result<GalaxyOrderFilmDO> result = new Result<GalaxyOrderFilmDO>(false);
		try {
			if (id <= 0) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			GalaxyOrderFilmDO obj = galaxyOrderFilmManager.getById(id);
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
	 * @return List<GalaxyOrderFilmDO>
	 */
	public Result<List<GalaxyOrderFilmDO>> getByIds(List<Long> ids) {
		Result<List<GalaxyOrderFilmDO>> result = new Result<List<GalaxyOrderFilmDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyOrderFilmDO> list = galaxyOrderFilmManager.getByIds(ids);
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
	 * @return List<GalaxyOrderFilmDO>
	 */
	public Result<List<GalaxyOrderFilmDO>> query(GalaxyOrderFilmQuery query) {
		Result<List<GalaxyOrderFilmDO>> result = new Result<List<GalaxyOrderFilmDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyOrderFilmDO> list = galaxyOrderFilmManager.query(query);
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
			int num = galaxyOrderFilmManager.updateStatusByIds(ids, status);
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
	 * 分页查询，同时判断影片是否在线
	 * 
	 * @param query
	 * @return List<GalaxyOrderFilmDO>
	 */
	public Result<List<OrderFilmDTO>> queryWithStatus(GalaxyOrderFilmQuery query) {
		Result<List<OrderFilmDTO>> result = new Result<List<OrderFilmDTO>>(false);
		try {
			List<OrderFilmDTO> dtoList = Lists.newArrayList();
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyOrderFilmDO> list = galaxyOrderFilmManager.query(query);
			if (CollectionUtils.isEmpty(list)) {
				result.setValue(dtoList);
				return result;
			}

			Map<Long, GalaxyFilmDO> filmMap = getFilms(list);

			for (GalaxyOrderFilmDO item : list) {
				OrderFilmDTO dto = new OrderFilmDTO();
				BeanUtils.copyProperties(dto, item);
				dto.setExpiredDateStr(DateUtil.formatBetweenWithEncode(item.getExpiredDate()));
				dto.setGmtCreateStr(DateUtil.format(item.getGmtCreate(), DateUtil.FORMAT_1));
				dto.setPayTimeEnd(DateUtil.convert(item.getPayTimeEnd(), DateUtil.FORMAT_7, DateUtil.FORMAT_1));

				GalaxyFilmDO film = filmMap.get(item.getFilmId());
				if (film != null && FilmStatus.isOnline(film.getStatus())) {
					dto.setRtspUrl(film.getRtspUrl());
					dto.setAssetUrl(film.getAssetUrl());
					dto.setFilmStatus(FilmStatus.ONLINE.getCode());
				}
				dtoList.add(dto);
			}

			result.setValue(dtoList);
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
	 * 关闭订单
	 * 
	 * @param list 订单状态确保为NEW(0, "新订单")或 PREPAY(1, "待付款"),
	 */
	public boolean closeOrder(List<GalaxyOrderFilmDO> list, int status) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (CollectionUtils.isEmpty(list)) {
				return false;
			}

			List<Long> ids = Lists.newArrayList();
			List<Long> discountIds = Lists.newArrayList();
			List<Long> followIds = Lists.newArrayList();
			for (GalaxyOrderFilmDO order : list) {
				ids.add(order.getId());
				addDiscountId(discountIds, order);
				addFollowId(followIds, order);
			}
			// 更新为取消状态
			galaxyOrderFilmManager.updateStatusByIds(ids, status);

			// 折扣券回滚
			if (CollectionUtils.isNotEmpty(discountIds)) {
				galaxyDiscountAccessAO.updateStatusByIds(discountIds, DbContant.ONE);
			}

			// 微信关注活动回滚
			if (CollectionUtils.isNotEmpty(followIds)) {
				galaxyWeixinFollowAO.updateUsedStatusByIds(followIds, DbContant.ZERO);
			}
			return true;
		} catch (Exception e) {
			log.error("updateStatusByIds error,list=" + list + ", status=" + status, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return false;
	}

	private void addDiscountId(List<Long> discountIds, GalaxyOrderFilmDO order) {
		if (StringUtils.isEmpty(order.getFeature(DbContant.FEA_DISCOUNT_ID))) {
			return;
		}
		long discountId = NumberUtils.toLong(order.getFeature(DbContant.FEA_DISCOUNT_ID));
		if (discountId > 0) {
			discountIds.add(discountId);
		}
	}

	private void addFollowId(List<Long> followIds, GalaxyOrderFilmDO order) {
		if (StringUtils.isEmpty(order.getFeature(DbContant.FEA_WX_FOLLOW))) {
			return;
		}
		long followId = NumberUtils.toLong(order.getFeature(DbContant.FEA_WX_FOLLOW));
		if (followId > 0) {
			followIds.add(followId);
		}
	}

	private Map<Long, GalaxyFilmDO> getFilms(List<GalaxyOrderFilmDO> list) throws MyException {
		List<Long> filmIds = Lists.transform(list, new Function<GalaxyOrderFilmDO, Long>() {
			@Override
			public Long apply(GalaxyOrderFilmDO input) {
				return input.getFilmId();
			}
		});
		List<GalaxyFilmDO> filmList = galaxyFilmManager.getByIds(filmIds);
		Map<Long, GalaxyFilmDO> filmMap = Maps.uniqueIndex(filmList, new Function<GalaxyFilmDO, Long>() {
			@Override
			public Long apply(GalaxyFilmDO input) {
				return input.getId();
			}
		});
		return filmMap;
	}

}
