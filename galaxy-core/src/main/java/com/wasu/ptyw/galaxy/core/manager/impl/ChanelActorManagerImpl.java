package com.wasu.ptyw.galaxy.core.manager.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.http.LocalHttpRequest;
import com.wasu.ptyw.galaxy.core.ao.ChanelItemAO;
import com.wasu.ptyw.galaxy.core.manager.ChanelActorManager;
import com.wasu.ptyw.galaxy.dal.dao.ChanelActorDAO;
import com.wasu.ptyw.galaxy.dal.dataobject.Actor1DO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelActorDO;
import com.wasu.ptyw.galaxy.dal.dataobject.QueryActorAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.QueryActorDO;
import com.wasu.ptyw.galaxy.dal.persist.DAOException;
import com.wasu.ptyw.galaxy.dal.query.ChanelActorQuery;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wenguang
 * @date 2016年08月24日
 */
@Service("chanelActorManager")
public class ChanelActorManagerImpl implements ChanelActorManager {
	
	
	private static Log log= LogFactory.getLog(ChanelActorManagerImpl.class);
	
	@Resource
	private ChanelActorDAO chanelActorDao;

	@Override
	public Long insert(ChanelActorDO obj) throws MyException {
		try {
			chanelActorDao.insert(obj);
			return obj.getId();
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int update(ChanelActorDO obj) throws MyException {
		try {
			return chanelActorDao.update(obj);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, obj);
		}
	}

	@Override
	public int deleteById(long id) throws MyException {
		try {
			return chanelActorDao.deleteById(id);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public ChanelActorDO getById(long id) throws MyException {
		try {
				return chanelActorDao.getById(id);
			
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, id);
		}
	}

	@Override
	public List<ChanelActorDO> getByIds(List<Long> ids) throws MyException {
		try {
			return chanelActorDao.getByIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}
	
	@Override
	public List<ChanelActorDO> getByActorIds(List<Integer> ids) throws MyException {
		try {
			return chanelActorDao.getByActorIds(ids);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids);
		}
	}

	@Override
	public List<ChanelActorDO> query(ChanelActorQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelActorDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
				query.setOrderBy(null);
				
			}
			return chanelActorDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}
	@Override
	public List<ChanelActorDO> queryRecommend(ChanelActorQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelActorDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			query.setOrderBy("status desc");
			return chanelActorDao.query(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public List<ChanelActorDO> getRecommend(ChanelActorQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelActorDao.queryCount(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			query.setOrderBy("status desc");
			return chanelActorDao.getRecommend(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}
	
	@Override
	public ChanelActorDO queryFirst(ChanelActorQuery query) throws MyException {
		try {
			query.setCurrentPage(1);
			query.setPageSize(1);
			List<ChanelActorDO> list = chanelActorDao.query(query);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
		return null;
	}

	@Override
	public int updateStatusByIds(List<Long> ids, int status) throws MyException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", status);
			map.put("ids", ids);
			return chanelActorDao.updateStatusByIds(map);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, ids, status);
		}
	}

	@Override
	public List<ChanelActorDO> queryOnline(ChanelActorQuery query) throws MyException {
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelActorDao.queryCountOnline(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			//query.setOrderBy("actor_id DESC");
			return chanelActorDao.queryOnline(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public List<ChanelActorDO> queryOffline(ChanelActorQuery query) throws MyException {
		// TODO Auto-generated method stub
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelActorDao.queryCountOffline(query);
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelActorDao.queryOffline(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	@Override
	public List<ChanelActorDO> queryOffline1(ChanelActorQuery query) throws MyException {
		// TODO Auto-generated method stub
		try {
			if (query.getPageSize() < Integer.MAX_VALUE) {
				int count = chanelActorDao.queryCountOffline(query) + 4;
				if (count == 0) {
					return Lists.newArrayList();
				}
				query.setTotalItem(count);
			}
			return chanelActorDao.queryOffline1(query);
		} catch (DAOException e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e, query);
		}
	}

	// 从接口获取数据的条数
	@Override
	public QueryActorAllDO getActor(String keywords) throws MyException {
		try {
			System.out.println("start");
			String url = "http://www.utc.gscatv.com/templates/iptv_gansu/runtime/default/template/test/search.jsp?actor="
					+ keywords;
			System.out.println(url);
			String json = LocalHttpRequest.post(url);
			QueryActorAllDO result = JSON.parseObject(json, QueryActorAllDO.class);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}
	
	// 从接口获取数据的条数
	@Override
	public Actor1DO getActor1(String keywords) throws MyException {
		try {
			System.out.println("start");
			String url = "http://hd2.hzdtv.tv/dataquery/contentSearch?mode=1&filed=actors&word="
					+ keywords;
			System.out.println(url);
			String json = LocalHttpRequest.post1(url, "UTF-8");
			Actor1DO result = JSON.parseObject(json, Actor1DO.class);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}
	
	// 从接口获取数据的条数
	@Override
	public Actor1DO getActor2(String keywords) throws MyException {
		try {
			System.out.println("start");
			String url = "http://hd2.hzdtv.tv/dataquery/contentSearch?field=actors&sortBy=match:desc,contentType:desc,createTime:desc&folderId=35868&word="
					+ keywords +"&pageItems=40&pageIndex=1";
			System.out.println(url);
			String json = LocalHttpRequest.post1(url, "UTF-8");
			Actor1DO result = JSON.parseObject(json, Actor1DO.class);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
		}
	}
	

	@Override
	public QueryActorAllDO getActors(String keywords) throws MyException {
		
		log.info("start request"+keywords);
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = "http://www.utc.gscatv.cn/templates/iptv_gansu/runtime/default/template/test/search.jsp?actor=" + keywords;
			log.info("urlName:"+urlName);
			URL realUrl = new URL(urlName);
			//打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			//设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//			System.out.println(conn.getContent());
			//建立实际的连接
			conn.connect();
			//获取所有响应头字段
			Map<String,List<String>> map = conn.getHeaderFields();
			//遍历所有的响应头字段
//			for (String key : map.keySet())
//			{
//			System.out.println(key + "--->" + map.get(key));
//			}
			//定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"GB2312"));
			String line;
			while ((line = in.readLine())!= null)
			{
			result += line;
			}
//			log.info(result);
			QueryActorAllDO result1 = JSON.parseObject(result, QueryActorAllDO.class);
			log.info(result1);
			return result1;
		}catch(

	Exception e)
	{
		throw new MyException(ResultCode.DAO_DEF_EXCEPTION, e);
	}
}
	
	
	public static String gbEncoding(final String gbString) {   
        char[] utfBytes = gbString.toCharArray();   
              String unicodeBytes = "";   
               for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {   
                    String hexB = Integer.toHexString(utfBytes[byteIndex]);   
                      if (hexB.length() <= 2) {   
                          hexB = "00" + hexB;   
                     }   
                      unicodeBytes = unicodeBytes + "\\u" + hexB;   
                  }   
                  System.out.println("unicodeBytes is: " + unicodeBytes);   
                  return unicodeBytes;   
             }

	@Override
	public int publishById(long id) throws MyException {
		try{
		    ChanelActorDO obj = chanelActorDao.getById(id);
		    //判断是否第一次发布
		    if(obj.getPublishid()==0){
		    	//复制原始的数据，生成一条线上的数据,-1表示线上的数据	    	
		    	obj.setId(null);
		    	obj.setPublishid(-1L);
		    	chanelActorDao.insert(obj);
		    	long publishid = obj.getId();
		    			
		    	
		    	//判断发布的数据与原始的数据是否是同一条数据,通过publishid判断
		    	ChanelActorDO chanelActorDO = new ChanelActorDO();
		    	chanelActorDO.setPublishid(publishid);
		    	chanelActorDO.setId(id);
		    	return chanelActorDao.update(chanelActorDO);
		    }else{
		    	
		    	//如果多次发布的,复制原始的数据更新线上的数据
		    	obj.setId(obj.getPublishid());
		    	obj.setPublishid(-1L);
		    	return chanelActorDao.update(obj);
		    }
		    
		}catch(Exception e){
			throw new MyException(ResultCode.DAO_DEF_EXCEPTION,e,id);
		}
	}

}
