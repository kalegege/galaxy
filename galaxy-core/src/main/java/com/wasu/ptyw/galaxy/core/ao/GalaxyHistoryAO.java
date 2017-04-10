package com.wasu.ptyw.galaxy.core.ao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.core.dto.HistoryDTO;
import com.wasu.ptyw.galaxy.core.manager.GalaxyFilmManager;
import com.wasu.ptyw.galaxy.core.manager.GalaxyHistoryManager;
import com.wasu.ptyw.galaxy.core.manager.GalaxyOrderFilmManager;
import com.wasu.ptyw.galaxy.dal.constant.FilmStatus;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyHistoryDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyHistoryQuery;

/**
 * @author wenguang
 * @date 2015年07月01日
 */
@Service("galaxyHistoryAO")
@Slf4j
public class GalaxyHistoryAO extends BaseAO {
	@Resource
	GalaxyHistoryManager galaxyHistoryManager;
	@Resource
	GalaxyOrderFilmManager galaxyOrderFilmManager;
	@Resource
	GalaxyFilmManager galaxyFilmManager;

	/**
	 * 记录播放历史
	 */
	public Result<Long> save(long orderId, long hisId) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (orderId < 1 && hisId < 1) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			if (orderId > 0) {
				GalaxyOrderFilmDO order = galaxyOrderFilmManager.getById(orderId);
				if (order == null)
					return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
				return save(order);
			}

			if (hisId > 0) {
				GalaxyHistoryDO baseDO = new GalaxyHistoryDO();
				baseDO.setId(hisId);
				int count = galaxyHistoryManager.update(baseDO);
				if (count < 1) {
					return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
				} else {
					result.setValue(hisId);
				}
			}
		} catch (Exception e) {
			log.error("save error, orderId=" + orderId + ",hisId=" + hisId, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 记录播放历史
	 * 
	 * @param order
	 *            订单对象
	 * @return 对象ID
	 */
	public Result<Long> save(GalaxyOrderFilmDO order) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (order == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			GalaxyHistoryQuery query = new GalaxyHistoryQuery();
			query.setFilmId(order.getFilmId());
			query.setUserId(order.getUserId());
			query.setRegionCode(order.getRegionCode());
			GalaxyHistoryDO history = galaxyHistoryManager.queryFirst(query);
			if (history == null) {
				history = new GalaxyHistoryDO();
				history.setFilmId(order.getFilmId());
				history.setContCode(order.getContCode());
				history.setContName(order.getContName());
				history.setContImage(order.getContImage());
				history.setOutUserId(order.getOutUserId());
				history.setRegionCode(order.getRegionCode());
				history.setUserId(order.getUserId());
				history.setExpiredDate(order.getExpiredDate());
				history.setPlayTime(0);
				galaxyHistoryManager.insert(history);
			} else {
				// 只是为了更新时间
				history.setExpiredDate(order.getExpiredDate());
				galaxyHistoryManager.update(history);
			}

			result.setValue(history.getId());
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("save error, order=" + order, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 新增
	 * 
	 * @param HistoryDTO
	 * @return 对象ID
	 */
	public Result<Long> save(GalaxyHistoryDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = galaxyHistoryManager.insert(obj);
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
	 * @param HistoryDTO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(GalaxyHistoryDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = galaxyHistoryManager.update(obj);
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
			int num = galaxyHistoryManager.deleteById(id);
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
	 * @return GalaxyHistoryDO
	 */
	public Result<GalaxyHistoryDO> getById(long id) {
		Result<GalaxyHistoryDO> result = new Result<GalaxyHistoryDO>(false);
		try {
			if (id <= 0) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			GalaxyHistoryDO obj = galaxyHistoryManager.getById(id);
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
	 * @return List<GalaxyHistoryDO>
	 */
	public Result<List<GalaxyHistoryDO>> getByIds(List<Long> ids) {
		Result<List<GalaxyHistoryDO>> result = new Result<List<GalaxyHistoryDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyHistoryDO> list = galaxyHistoryManager.getByIds(ids);
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
	 * @return List<GalaxyHistoryDO>
	 */
	public Result<List<GalaxyHistoryDO>> query(GalaxyHistoryQuery query) {
		Result<List<GalaxyHistoryDO>> result = new Result<List<GalaxyHistoryDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyHistoryDO> list = galaxyHistoryManager.query(query);
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
	 * 根据query条件删除
	 * 
	 * @param query
	 * @return 删除成功的记录数
	 */
	public Result<Integer> deleteByQuery(GalaxyHistoryQuery query) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			int num = galaxyHistoryManager.deleteByQuery(query);
			if (num > 0) {
				result.setValue(num);
				result.setSuccess(true);
			} else {
				setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
		} catch (Exception e) {
			log.error("deleteByQuery error," + query, e);
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
	 * @return List<GalaxyHistoryDO>
	 */
	public Result<List<HistoryDTO>> queryHistory(GalaxyHistoryQuery query) {
		Result<List<HistoryDTO>> result = new Result<List<HistoryDTO>>(false);

		try {
			List<HistoryDTO> dtoList = Lists.newArrayList();
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyHistoryDO> list = galaxyHistoryManager.query(query);
			if (CollectionUtils.isEmpty(list)) {
				result.setValue(dtoList);
				return result;
			}

			Map<Long, GalaxyFilmDO> filmMap = getFilms(list);

			for (GalaxyHistoryDO item : list) {
				HistoryDTO dto = new HistoryDTO();
				BeanUtils.copyProperties(dto, item);
				dto.setExpiredDateStr(DateUtil.formatBetweenWithEncode(item.getExpiredDate()));

				GalaxyFilmDO film = filmMap.get(item.getFilmId());
				if (film != null && FilmStatus.isOnline(film.getStatus())) {
					dto.setRtspUrl(film.getRtspUrl());
					dto.setAssetUrl(film.getAssetUrl());
					dto.setFilmStatus(FilmStatus.ONLINE.getCode());
				}
				dtoList.add(dto);
			}

			result.setValue(dtoList);
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

	private Map<Long, GalaxyFilmDO> getFilms(List<GalaxyHistoryDO> list) throws MyException {
		List<Long> filmIds = Lists.transform(list, new Function<GalaxyHistoryDO, Long>() {
			@Override
			public Long apply(GalaxyHistoryDO input) {
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
