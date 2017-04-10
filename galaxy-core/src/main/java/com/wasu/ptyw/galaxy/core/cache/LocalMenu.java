package com.wasu.ptyw.galaxy.core.cache;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.util.DateUtil;
import com.wasu.ptyw.galaxy.core.ao.ChanelTeamAO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuAllDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamQuery;

/**
 * 本地缓存类
 * 
 * @author wenguang
 * @date 2016年7月6日
 */
public class LocalMenu {

	@Autowired
	private ChanelTeamAO chanelTeamAO;
	
	private static Cache<String, MenuAllDO> cache;
	
	private static MenuAllDO menuAllDO;
	
	public MenuAllDO get(String regionId) {
		
		final String key = regionId+ "_" + new Date().getDate();
		
        if (cache == null) {
        	cache = CacheBuilder.newBuilder()
                    .maximumSize(1000)
                    .refreshAfterWrite(1, TimeUnit.HOURS)
                    .expireAfterAccess(2, TimeUnit.HOURS)
                    .build(
                            new CacheLoader<String, MenuAllDO>() {
                                @Override
                                public MenuAllDO load(String s) throws Exception {
//                                	ChanelTeamQuery query = new ChanelTeamQuery();
//                            		query.setRegionId(key);
//                            		Result<MenuAllDO> result = chanelTeamAO.getMenu(query);
//                            		menuAllDO=result.getValue();
//                            		return menuAllDO;
                            		return null;
                                }
                            }
                    );
        }

        try {
        	MenuAllDO result = (MenuAllDO) cache.get(key, 
        			new Callable<MenuAllDO>() {
                @Override
                public MenuAllDO call() throws Exception {
                	ChanelTeamQuery query = new ChanelTeamQuery();
            		query.setRegionId(key);
            		Result<MenuAllDO> result = chanelTeamAO.getMenu(query);
            		menuAllDO=result.getValue();
            		return menuAllDO;
                }
            });

             return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	
	
	
	
	
//	
//	long expireSeconds = 24 * 60 * 60;// 24小时，单位秒
//	static int k = 0;
//	static Date nowtime;
//	static ChanelTeamAO chanelTeamAO=new ChanelTeamAO();
//	static MenuAllDO menuAllDO=new MenuAllDO();
//
//	public void init(String key) throws Exception {
//		Date date1 = new Date();
//		if(nowtime.getDate() != date1.getDate()){
//			fetch(key);
//		}
//	}
//
//	protected MenuAllDO fetch(String key) throws Exception {
//		// TODO Auto-generated method stub
//		ChanelTeamQuery query = new ChanelTeamQuery();
//		query.setRegionId(key);
//		Result<MenuAllDO> result = chanelTeamAO.getMenu(query);
//		menuAllDO=result.getValue();
//		nowtime=new Date();
//		if (result.isSuccess() && result.getValue() != null) {
//			return menuAllDO;
//		}
//		throw new Exception("data is not found in port");
//	}
//	
//	public MenuAllDO get(String key) throws Exception{
//		init(key);
//		return menuAllDO;
//	}
	
}
