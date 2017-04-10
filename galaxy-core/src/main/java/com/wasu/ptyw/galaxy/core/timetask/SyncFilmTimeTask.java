package com.wasu.ptyw.galaxy.core.timetask;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.core.ao.GalaxyFilmAO;
import com.wasu.ptyw.galaxy.core.api.ClpsService;
import com.wasu.ptyw.galaxy.core.cache.LocalCache;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.FilmStatus;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyFilmQuery;

/**
 * 电影数据同步定时任务
 * 
 * @author wenguang
 * @date 2015年9月23日
 */
@Service("syncFilmTimeTask")
@Slf4j
public class SyncFilmTimeTask {
	@Resource
	GalaxyFilmAO galaxyFilmAO;
	@Resource
	ClpsService clpsService;

	JSONArray jsonArr = null;

	public void sync() {
		try {
			initSyncUrl();
			if (jsonArr == null)
				return;
			String region = null;
			List<String> urls = Lists.newArrayList();
			for (Object o : jsonArr) {
				JSONObject json = (JSONObject) o;
				region = json.getString("region");
				JSONArray urlArr = json.getJSONArray("urls");

				urls.clear();
				for (Object url : urlArr) {
					JSONObject jsonUrl = (JSONObject) url;
					urls.add(jsonUrl.getString("url"));
				}
				syncFilm(region, urls);
			}
		} catch (Exception e) {
			log.error("syncFilm error", e);
		}
	}

	/**
	 * 同步影片信息
	 */
	public void syncFilm(String region, List<String> urls) {
		log.info("SyncFilmTimeTask syncFilm start");
		Result<List<GalaxyFilmDO>> result = galaxyFilmAO.query(buildQuery(region));
		if (!result.isSuccess()) {
			return;
		}
		Map<String, GalaxyFilmDO> map = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(result.getValue())) {
			map = Maps.uniqueIndex(result.getValue(), new Function<GalaxyFilmDO, String>() {
				@Override
				public String apply(GalaxyFilmDO input) {
					return input.getFolderCode() + "-" + input.getAssetId();
				}
			});
		}

		// 在3个不同的栏目下
		// List<GalaxyFilmDO> clpsList = clpsService.getFilms();
		// syncDbWithClps(clpsService.getFilms(), map, "同步正片");
		// syncDbWithClps(clpsService.getFilmClips(), map, "同步正片片花");
		// syncDbWithClps(clpsService.getFilmTickets(), map, "同步购票片花");

		for (String url : urls) {
			syncDbWithClps(clpsService.getFilms(url), map, region);
		}

		log.info("SyncFilmTimeTask syncFilm end");
	}

	void syncDbWithClps(List<GalaxyFilmDO> clpsList, Map<String, GalaxyFilmDO> map, String region) {
		// 异常情况取不到数据，直接退出
		if (CollectionUtils.isEmpty(clpsList)) {
			log.warn(region + ": clpsList is empty");
			return;
		}
		int updateCount = 0;
		// 更新或插入新影片
		Set<Long> existIds = Sets.newHashSet();
		String folderCode = null;
		for (GalaxyFilmDO item : clpsList) {
			// if (StringUtils.isNotEmpty(item.getSub2Name())) {
			// item.setName(item.getSub2Name());
			// }
			folderCode = item.getFolderCode();
			String key = folderCode + "-" + item.getAssetId();
			if (map.containsKey(key)) {
				GalaxyFilmDO dbItem = map.get(key);
				existIds.add(dbItem.getId());
				copyProperties(dbItem, item);
				dbItem.setClpsStatus(DbContant.ONE);
				dbItem.setUpdateCount(dbItem.getUpdateCount() + 1);
				galaxyFilmAO.update(dbItem);
			} else {
				item.setStatus(FilmStatus.ONLINE.getCode());
				item.setClpsStatus(DbContant.ONE);
				item.setRegionCode(region);
				item.setAliasPrice(500);// 新增默认5元
				galaxyFilmAO.save(item);
			}
			updateCount++;
		}

		// 下线clps不存在的影片
		for (Entry<String, GalaxyFilmDO> set : map.entrySet()) {
			GalaxyFilmDO dbItem = set.getValue();
			if (!StringUtils.equalsIgnoreCase(dbItem.getFolderCode(), folderCode) || existIds.contains(dbItem.getId()))
				continue;
			dbItem.setStatus(FilmStatus.OFFLINESYS.getCode());
			dbItem.setClpsStatus(DbContant.ZERO);
			galaxyFilmAO.update(dbItem);
			updateCount++;
		}
		log.info(region + ": updateCount:" + updateCount);
	}

	private void copyProperties(final GalaxyFilmDO dest, final GalaxyFilmDO orig) {
		try {
			// BeanUtils.copyProperties(dest, orig);

			PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
			PropertyDescriptor[] origDescriptors = propertyUtils.getPropertyDescriptors(orig);
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if ("class".equals(name)) {
					continue; // No point in trying to set an object's class
				}
				if (propertyUtils.isReadable(orig, name) && propertyUtils.isWriteable(dest, name)) {
					try {
						Object value = propertyUtils.getSimpleProperty(orig, name);
						if (value != null)
							BeanUtilsBean.getInstance().copyProperty(dest, name, value);
					} catch (NoSuchMethodException e) {
						// Should not happen
					}
				}
			}
		} catch (Exception e) {
			log.error("copyProperties error", e);
		}
	}

	private GalaxyFilmQuery buildQuery(String region) {
		GalaxyFilmQuery query = new GalaxyFilmQuery();
		query.setRegionCode(region);
		query.setPageSize(Integer.MAX_VALUE);
		query.setCurrentPage(1);// 一直获取第一页数据，直到取不到为止
		return query;
	}

	private void initSyncUrl() {
		String s = LocalCache.get("set_sync_clps");
		if (StringUtils.isEmpty(s)) {
			jsonArr = null;
		} else {
			jsonArr = JSON.parseArray(s);
		}
	}

	public static void main(String[] args) {
		String s = "[{'region':'taiyuan','urls':[{'url':'http://taiyuan-utc.wasu.cn/a?f=mov_hot_69_5&profile=TAIYUAN22SCENARIO'},{'url':'http://taiyuan-utc.wasu.cn/a?f=mov_hot_69_5_1&profile=TAIYUAN22SCENARIO'},{'url':'http://taiyuan-utc.wasu.cn/a?f=mov_hot_69_5_2&profile=TAIYUAN22SCENARIO'}]}]";
		JSONArray jsonArr = JSON.parseArray(s);
		System.out.println(jsonArr.toJSONString());
	}
}
