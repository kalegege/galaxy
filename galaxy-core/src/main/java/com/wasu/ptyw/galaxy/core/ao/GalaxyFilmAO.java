package com.wasu.ptyw.galaxy.core.ao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.dto.FilmListDTO;
import com.wasu.ptyw.galaxy.core.dto.FilmSimpleDTO;
import com.wasu.ptyw.galaxy.core.manager.GalaxyFilmManager;
import com.wasu.ptyw.galaxy.core.manager.GalaxyFilmSectionManager;
import com.wasu.ptyw.galaxy.core.spring.interceptor.TerminalInfo;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.FilmRecStatus;
import com.wasu.ptyw.galaxy.dal.constant.FilmStatus;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmSectionDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyFilmQuery;
import com.wasu.ptyw.galaxy.dal.query.GalaxyFilmSectionQuery;

/**
 * @author wenguang
 * @date 2015年09月22日
 */
@Service("galaxyFilmAO")
@Slf4j
public class GalaxyFilmAO extends BaseAO {
	@Resource
	GalaxyFilmManager galaxyFilmManager;
	@Resource
	GalaxyFilmSectionManager galaxyFilmSectionManager;

	/**
	 * 新增
	 * 
	 * @param FilmDTO
	 * @return 对象ID
	 */
	public Result<Long> save(GalaxyFilmDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = galaxyFilmManager.insert(obj);
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
	 * @param FilmDTO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(GalaxyFilmDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = galaxyFilmManager.update(obj);
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
			int num = galaxyFilmManager.deleteById(id);
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
	 * @return GalaxyFilmDO
	 */
	public Result<GalaxyFilmDO> getById(Long id) {
		Result<GalaxyFilmDO> result = new Result<GalaxyFilmDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			GalaxyFilmDO obj = galaxyFilmManager.getById(id);
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
	 * @return List<GalaxyFilmDO>
	 */
	public Result<List<GalaxyFilmDO>> getByIds(List<Long> ids) {
		Result<List<GalaxyFilmDO>> result = new Result<List<GalaxyFilmDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyFilmDO> list = galaxyFilmManager.getByIds(ids);
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
	 * @return List<GalaxyFilmDO>
	 */
	public Result<List<GalaxyFilmDO>> query(GalaxyFilmQuery query) {
		Result<List<GalaxyFilmDO>> result = new Result<List<GalaxyFilmDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<GalaxyFilmDO> list = galaxyFilmManager.query(query);
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
			int num = galaxyFilmManager.updateStatusByIds(ids, status);
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
	 * 获取首页数据
	 */
	public Result<Map<String, Object>> queryIndex() {
		Map<String, Object> map = Maps.newHashMap();
		Result<Map<String, Object>> result = new Result<Map<String, Object>>(false);
		try {
			Map<Long, GalaxyFilmDO> onlineFilms = queryOnlineFilms();
			if (onlineFilms.isEmpty())
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			map.put("today", getToday(onlineFilms));
			// map.put("hot", getHot(onlineFilms));
			// map.put("ticket", getTicket(onlineFilms));
			result.setValue(map);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("queryIndex error", e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 获取院线热映
	 */
	public Result<List<FilmListDTO>> queryHot() {
		Result<List<FilmListDTO>> result = new Result<List<FilmListDTO>>(false);
		try {
			GalaxyFilmSectionQuery query = new GalaxyFilmSectionQuery();
			query.setSecId(FilmRecStatus.HOT.getCode());
			query.setOrderBy("priority");
			query.setStatus(DbContant.ONE);
			query.setRegionCode(TerminalInfo.getRegion());
			List<GalaxyFilmSectionDO> secList = galaxyFilmSectionManager.query(query);
			if (CollectionUtils.isEmpty(secList)) {
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}
			Map<Long, GalaxyFilmDO> onlineFilms = queryOnlineFilms();
			if (onlineFilms.isEmpty())
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			result.setValue(toHotDTO(secList, onlineFilms));
		} catch (Exception e) {
			log.error("queryHot error", e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 获取在线购票
	 */
	public Result<List<FilmListDTO>> queryTicket() {
		Result<List<FilmListDTO>> result = new Result<List<FilmListDTO>>(false);
		try {
			GalaxyFilmSectionQuery query = new GalaxyFilmSectionQuery();
			query.setSecId(FilmRecStatus.TICKET.getCode());
			query.setOrderBy("priority");
			query.setStatus(DbContant.ONE);
			query.setRegionCode(TerminalInfo.getRegion());
			List<GalaxyFilmSectionDO> secList = galaxyFilmSectionManager.query(query);
			if (CollectionUtils.isEmpty(secList)) {
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}

			List<Long> filmIds = Lists.transform(secList, new Function<GalaxyFilmSectionDO, Long>() {
				@Override
				public Long apply(GalaxyFilmSectionDO input) {
					return input.getFilmId();
				}
			});

			List<GalaxyFilmDO> list = galaxyFilmManager.getByIds(filmIds);
			if (CollectionUtils.isEmpty(list)) {
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}

			result.setValue(toTicketDTO(secList, list));
		} catch (Exception e) {
			log.error("queryHot error", e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 获取今日推荐
	 * 
	 * @throws MyException
	 */
	List<FilmSimpleDTO> getToday(Map<Long, GalaxyFilmDO> onlineFilms) throws MyException {
		GalaxyFilmSectionQuery query = new GalaxyFilmSectionQuery();
		query.setSecId(FilmRecStatus.TODAY.getCode());
		query.setOrderBy("priority");
		query.setPageSize(Integer.MAX_VALUE);
		query.setStatus(DbContant.ONE);
		query.setRegionCode(TerminalInfo.getRegion());
		List<GalaxyFilmSectionDO> secList = galaxyFilmSectionManager.query(query);
		if (CollectionUtils.isEmpty(secList))
			return null;
		List<FilmSimpleDTO> filmList = Lists.newArrayList();
		for (GalaxyFilmSectionDO item : secList) {
			GalaxyFilmDO film = onlineFilms.get(item.getFilmId());
			if (film != null) {
				FilmSimpleDTO dto = toSimpleDTO(film);
				// dto.setBgUrl(film.getCaotuUrl());// 首页背景图片
				dto.setRtspUrl(getTodayFilmRtspUrl(film, onlineFilms));// 第一个片花播放地址
				filmList.add(dto);
			}
		}
		return filmList;
	}

	/**
	 * 获取院线热映
	 * 
	 * @throws MyException
	 */
	FilmSimpleDTO getHot(Map<Long, GalaxyFilmDO> onlineFilms) throws MyException {
		GalaxyFilmSectionQuery query = new GalaxyFilmSectionQuery();
		query.setSecId(FilmRecStatus.HOT.getCode());
		query.setOrderBy("priority");
		query.setStatus(DbContant.ONE);
		GalaxyFilmSectionDO sec = galaxyFilmSectionManager.queryFirst(query);
		if (sec == null)
			return null;

		GalaxyFilmDO item = onlineFilms.get(sec.getFilmId());
		if (item != null) {
			FilmSimpleDTO dto = toSimpleDTO(item);
			dto.setRtspUrl(null);
			return dto;
		}
		return null;
	}

	/**
	 * 获取在线购票
	 * 
	 * @throws MyException
	 */
	FilmSimpleDTO getTicket(Map<Long, GalaxyFilmDO> onlineFilms) throws MyException {
		GalaxyFilmSectionQuery query = new GalaxyFilmSectionQuery();
		query.setSecId(FilmRecStatus.TICKET.getCode());
		query.setOrderBy("priority");
		query.setStatus(DbContant.ONE);
		GalaxyFilmSectionDO sec = galaxyFilmSectionManager.queryFirst(query);
		if (sec == null)
			return null;

		GalaxyFilmDO item = onlineFilms.get(sec.getFilmId());
		if (item != null) {
			FilmSimpleDTO dto = toSimpleDTO(item);
			dto.setRtspUrl(null);
			return dto;
		}
		return null;
	}

	private Map<Long, GalaxyFilmDO> queryOnlineFilms() {
		Map<Long, GalaxyFilmDO> subMap = Maps.newHashMap();

		try {
			GalaxyFilmQuery query = new GalaxyFilmQuery();
			query.setStatus(FilmStatus.ONLINE.getCode());
			query.setPageSize(1000);
			query.setQueryCount(false);
			query.setOrderBy(null);
			query.setRegionCode(TerminalInfo.getRegion());
			List<GalaxyFilmDO> list = galaxyFilmManager.query(query);
			if (CollectionUtils.isNotEmpty(list)) {
				subMap = Maps.uniqueIndex(list, new Function<GalaxyFilmDO, Long>() {
					@Override
					public Long apply(GalaxyFilmDO input) {
						return input.getId();
					}
				});
			}
		} catch (MyException e) {
			log.error("queryOnlineFilms error", e);
		}
		return subMap;
	}

	/**
	 * 转为院线热映对象
	 */
	private List<FilmListDTO> toHotDTO(List<GalaxyFilmSectionDO> secList, Map<Long, GalaxyFilmDO> onlineFilms) {
		List<FilmListDTO> dtoList = Lists.newArrayList();
		try {
			for (GalaxyFilmSectionDO item : secList) {
				GalaxyFilmDO film = onlineFilms.get(item.getFilmId());
				if (film == null)
					continue;
				FilmListDTO dto = toFilmListDTO(film);
				dto.setPicUrl(film.getHaibaoUrl());
				// 设置片花
				buildClips(dto, onlineFilms, film.getLinkFilmIds());
				dtoList.add(dto);
			}
		} catch (Exception e) {
			log.error("toHotDTO error", e);
		}
		return dtoList;
	}

	/**
	 * 构造片花
	 */
	void buildClips(FilmListDTO dto, Map<Long, GalaxyFilmDO> subMap, String linkFilmIds) {
		if (StringUtils.isEmpty(linkFilmIds)) {
			return;
		}
		List<Long> idsList = NumUtil.toLongs(StringUtils.split(linkFilmIds, ','));
		for (Long id : idsList) {
			GalaxyFilmDO film = subMap.get(id);
			if (film == null || !FilmStatus.isOnline(film.getStatus())) {
				continue;
			}
			dto.getLinkFilms().add(toClipDTO(film));
		}
	}

	/**
	 * 转换为片花对象
	 */
	FilmListDTO toClipDTO(GalaxyFilmDO item) {
		FilmListDTO dto = toFilmListDTO(item);
		dto.setPicUrl(item.getJuzhaoUrl());
		return dto;
	}

	/**
	 * 转为在线购票对象
	 */
	private List<FilmListDTO> toTicketDTO(List<GalaxyFilmSectionDO> secList, List<GalaxyFilmDO> list) {
		List<FilmListDTO> dtoList = Lists.newArrayList();

		Map<Long, GalaxyFilmDO> subMap = Maps.uniqueIndex(list, new Function<GalaxyFilmDO, Long>() {
			@Override
			public Long apply(GalaxyFilmDO input) {
				return input.getId();
			}
		});
		for (GalaxyFilmSectionDO sec : secList) {
			GalaxyFilmDO film = subMap.get(sec.getFilmId());
			if (film == null || !FilmStatus.isOnline(film.getStatus())) {
				continue;
			}
			FilmListDTO dto = toFilmListDTO(film);
			dto.setPicUrl(film.getJuzhaoUrl());
			// dto.setTicketPicUrl(item.getBiaotiUrl());
			dtoList.add(dto);
		}
		return dtoList;
	}

	private FilmListDTO toFilmListDTO(GalaxyFilmDO i) {
		FilmListDTO d = new FilmListDTO();
		d.setId(i.getId());
		d.setAssetId(i.getAssetId());
		d.setName(i.getFilmName());
		d.setPrice(i.getFilmPrice());
		d.setRtspUrl(i.getRtspUrl());
		d.setAssetUrl(i.getAssetUrl());
		d.setStarlevel(i.getStarlevel());
		d.setWeixinCode(i.getWeixinCode());
		d.setTaobaoCode(i.getTaobaoCode());
		return d;
	}

	private FilmSimpleDTO toSimpleDTO(GalaxyFilmDO i) {
		FilmSimpleDTO d = new FilmSimpleDTO();
		d.setId(i.getId());
		d.setAssetId(i.getAssetId());
		d.setName(i.getFilmName());
		d.setRtspUrl(i.getRtspUrl());
		d.setBiaotiUrl(i.getBiaotiUrl());
		d.setCaotuUrl(i.getCaotuUrl());
		return d;
	}

	/**
	 * 获取今日推荐播放地址，第一个片花的视频地址
	 */
	String getTodayFilmRtspUrl(GalaxyFilmDO film, Map<Long, GalaxyFilmDO> onlineFilms) {
		if (StringUtils.isEmpty(film.getLinkFilmIds()))
			return null;
		long id = NumUtil.toLongs(StringUtils.split(film.getLinkFilmIds(), ',')).get(0);
		GalaxyFilmDO filmItem = onlineFilms.get(id);
		if (filmItem != null) {
			return filmItem.getRtspUrl();
		}
		return null;
	}

	public static void main(String[] args) {
		String s = "http://125.210.138.180:8080/data/pic/clpspic/clps178_236/data/pic/data/pic/201506/20150429144013_haibao410x543modaihuangdichuanqi_11285949671433469744291.jpg";
		try {
			for (int i = 0; i < 20; i++) {
				System.out.println(i + "-->" + System.currentTimeMillis() + "-->" + s);
				java.net.URL url = new java.net.URL(s);
				java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
				con.connect();
				// con.setConnectTimeout(500);
				// con.setReadTimeout(500);
				int state = con.getResponseCode();
				if (state == 200 || state == 304) {
					System.out.println(state);
				}
				con.disconnect();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
