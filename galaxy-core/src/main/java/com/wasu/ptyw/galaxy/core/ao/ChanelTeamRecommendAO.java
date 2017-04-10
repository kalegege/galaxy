package com.wasu.ptyw.galaxy.core.ao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.common.util.NumUtil;
import com.wasu.ptyw.galaxy.core.dto.ChanelTeamRecommendDTO;
import com.wasu.ptyw.galaxy.core.manager.ChanelActorManager;
import com.wasu.ptyw.galaxy.core.manager.ChanelItemManager;
import com.wasu.ptyw.galaxy.core.manager.ChanelTeamManager;
import com.wasu.ptyw.galaxy.core.manager.ChanelTeamRecommendManager;
import com.wasu.ptyw.galaxy.core.manager.ChanelTeamSelfcontrolManager;
import com.wasu.ptyw.galaxy.dal.dataobject.AssertMenuAllDO;
import com.wasu.ptyw.galaxy.dal.dataobject.CastDTO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelActorDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelItemDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamRecommendDO;
import com.wasu.ptyw.galaxy.dal.dataobject.ChanelTeamSelfcontrolDO;
import com.wasu.ptyw.galaxy.dal.dataobject.IndexDO;
import com.wasu.ptyw.galaxy.dal.dataobject.IndexDataDO;
import com.wasu.ptyw.galaxy.dal.dataobject.IndexRecommendDO;
import com.wasu.ptyw.galaxy.dal.dataobject.IndexSelfcontrolDO;
import com.wasu.ptyw.galaxy.dal.dataobject.MenuAllDO;
import com.wasu.ptyw.galaxy.dal.query.ChanelActorQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelItemQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamRecommendQuery;
import com.wasu.ptyw.galaxy.dal.query.ChanelTeamSelfcontrolQuery;

/**
 * @author wenguang
 * @date 2016年07月13日
 */
@Service("chanelTeamRecommendAO")
@Slf4j
public class ChanelTeamRecommendAO extends BaseAO {
	@Resource
	private ChanelTeamRecommendManager chanelTeamRecommendManager;
	@Resource
	private ChanelTeamSelfcontrolManager chanelTeamSelfcontrolManager;
	@Resource
	private ChanelItemManager chanelItemManager;
	@Resource
	private ChanelTeamManager chanelTeamManager;
	@Resource
	private ChanelActorManager chanelActorManager;

	/**
	 * 新增
	 * 
	 * @param ChanelTeamRecommendDO
	 * @return 对象ID
	 */
	public Result<Long> save(ChanelTeamRecommendDO obj) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			Long id = chanelTeamRecommendManager.insert(obj);
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
	 * @param ChanelTeamRecommendDO
	 * @return 更新成功的记录数
	 */
	public Result<Integer> update(ChanelTeamRecommendDO obj) {
		Result<Integer> result = new Result<Integer>(false);
		try {
			if (obj == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			int num = chanelTeamRecommendManager.update(obj);
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
			int num = chanelTeamRecommendManager.deleteById(id);
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
	 * @return ChanelTeamRecommendDO
	 */
	public Result<ChanelTeamRecommendDO> getById(Long id) {
		Result<ChanelTeamRecommendDO> result = new Result<ChanelTeamRecommendDO>(false);
		try {
			if (!NumUtil.isGreaterZero(id)) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelTeamRecommendDO obj = chanelTeamRecommendManager.getById(id);
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
	 * @return List<ChanelTeamRecommendDO>
	 */
	public Result<List<ChanelTeamRecommendDO>> getByIds(List<Long> ids) {
		Result<List<ChanelTeamRecommendDO>> result = new Result<List<ChanelTeamRecommendDO>>(false);
		try {
			if (ids == null || ids.isEmpty()) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamRecommendDO> list = chanelTeamRecommendManager.getByIds(ids);
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
	 * @return List<ChanelTeamRecommendDO>
	 */
	public Result<List<ChanelTeamRecommendDO>> query(ChanelTeamRecommendQuery query) {
		Result<List<ChanelTeamRecommendDO>> result = new Result<List<ChanelTeamRecommendDO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamRecommendDO> list = chanelTeamRecommendManager.query(query);
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
	 * 推荐频道查询
	 * 
	 * @param query
	 * @return List<ChanelTeamRecommendDO>
	 */
	public Result<List<ChanelTeamRecommendDTO>> getrecommend(ChanelTeamRecommendQuery query) {
		Result<List<ChanelTeamRecommendDTO>> result = new Result<List<ChanelTeamRecommendDTO>>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			List<ChanelTeamRecommendDTO> listDTO=new ArrayList<ChanelTeamRecommendDTO>();
			List<ChanelTeamRecommendDO> listDO = chanelTeamRecommendManager.getrecommend(query);
			Date now=new Date();
			for(ChanelTeamRecommendDO recommendDO:listDO){
				ChanelTeamRecommendDTO recommendDTO=new ChanelTeamRecommendDTO();
				SimpleDateFormat myFmt = new SimpleDateFormat("MM/dd/HH:mm");
				Date stDate = myFmt.parse(recommendDO.getStartDate()+"/"+recommendDO.getStartTime());
				Date enDate = myFmt.parse(recommendDO.getStopDate()+"/"+recommendDO.getStopTime());
				if(now.before(stDate)){
					//直播前
					recommendDTO.setBAliasName(recommendDO.getBAliasName());
					copyDTO(recommendDO,recommendDTO);
					
				}else if(now.after(enDate)){
					//直播后
					
				}else {
					//直播中
					
				}
				listDTO.add(recommendDTO);
			}
			result.setValue(listDTO);
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
	 * 推荐主页接口
	 * 
	 * @param query
	 * @return List<ChanelTeamRecommendDO>
	 */
	public Result<IndexDO> getindex(ChanelTeamRecommendQuery query) {
		Result<IndexDO> result = new Result<IndexDO>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_INVALID);
			}
			ChanelTeamSelfcontrolQuery chanelTeamSelfcontrolQuery = new ChanelTeamSelfcontrolQuery();
			chanelTeamSelfcontrolQuery.setRegionId(query.getRegionId());
			ChanelActorQuery chanelActorQuery=new ChanelActorQuery();
			// List<ChanelTeamRecommendDO> rlist =
			// chanelTeamRecommendManager.getrecommend(query);
			List<ChanelTeamSelfcontrolDO> slist = chanelTeamSelfcontrolManager.getrecommend(chanelTeamSelfcontrolQuery);
			List<ChanelActorDO> alist =chanelActorManager.getRecommend(chanelActorQuery);
			IndexDO indexDO = new IndexDO();
			//赋值艺人推荐
			indexDO.setActor(alist);
			// 赋值自主运营推荐
			IndexSelfcontrolDO indexSelfcontrolDO = new IndexSelfcontrolDO();
			indexSelfcontrolDO.setChNo(slist.get(0).getChNo().toString());
			indexSelfcontrolDO.setChName(slist.get(0).getChName());
			indexSelfcontrolDO.setChPicture(slist.get(0).getChPicture());
			indexDO.setSelfcontrol(indexSelfcontrolDO);

			// 赋值运营推荐
			List<IndexDataDO> indexDataList = new ArrayList<IndexDataDO>();
			//List<IndexRecommendDO> indexList = new ArrayList<IndexRecommendDO>();
			for (int i = 1; i < 6; i++) {
				IndexDataDO indexDataDO=new IndexDataDO();
				ChanelTeamRecommendDO aa = chanelTeamRecommendManager.getById(i);
				IndexRecommendDO indexRecommendDO = new IndexRecommendDO();
				// ChanelTeamRecommendDO aa=rlist.get(i);
				
				
				Date now = new Date();
				//将当前时间延后10分钟
				Date nowPlus = new Date();
				Calendar calendar = Calendar.getInstance();  
				calendar.setTime(nowPlus); 
				calendar.add(Calendar.MINUTE, 10); 
				nowPlus=calendar.getTime();
				
				
				

				// 获取当前节目单时间
				String starttime = aa.getStartDate() + "/" + aa.getStartTime();
				Date start = new SimpleDateFormat("MM/dd/HH:mm").parse(starttime);
				start.setYear(now.getYear());
				String stoptime =aa.getStopDate() + "/" + aa.getStopTime();
				Date stop = new SimpleDateFormat("MM/dd/HH:mm").parse(stoptime);
				stop.setYear(now.getYear());
				
				//基本赋值
				//indexRecommendDO.setChId(aa.getChId());
				//indexRecommendDO.setName(aa.getChName());
				//获取频道chNo
				ChanelItemQuery chanelItemQuery=new ChanelItemQuery();
				chanelItemQuery.setChId(aa.getChId());
				chanelItemQuery.setRegionId(query.getRegionId());
				ChanelItemDO item=chanelItemManager.queryByChid(chanelItemQuery);
				indexRecommendDO.setChNo(item.getChNo().toString());
				if (i == 1) {
					indexRecommendDO.setPicture(aa.getPicture());
				}
				if (nowPlus.before(stop) && now.after(start)) {
					// 当前时间为直播节目
					indexRecommendDO.setStatus("0");
					indexRecommendDO.setName(aa.getChName());
					indexRecommendDO.setMenu(aa.getAliasName());
					indexRecommendDO.setDate(aa.getStartDate());
					indexRecommendDO.setTime(aa.getStartTime());
					
				} else if (now.before(start)) {
					// 当前时间为直播前节目
					CastDTO castDTO=new CastDTO();
					castDTO.setName(aa.getChName());
					castDTO.setDate(aa.getStartDate());
					castDTO.setMenu(aa.getAliasName());
					castDTO.setTime(aa.getStartTime());
					indexDataDO.setCast(castDTO);
					
					if(aa.getBStatus() == 2){
						//0直播1点播2回放
//						ChanelTeamQuery chanelTeamQuery=new ChanelTeamQuery();
//						chanelTeamQuery.setRegionId(query.getRegionId());
//						AssertMenuAllDO assertMenuAllDO = chanelTeamManager.getReplay(chanelTeamQuery);
						indexRecommendDO.setStatus("2");
						indexRecommendDO.setName(aa.getChName());
						indexRecommendDO.setDate(aa.getBDate());
						indexRecommendDO.setTime(aa.getBTime());
						indexRecommendDO.setFolder(aa.getBFolder());
						indexRecommendDO.setAssertId(aa.getBAssertid());
						indexRecommendDO.setMenu(aa.getBAliasName());
					}else{
						indexRecommendDO.setStatus("1");
						indexRecommendDO.setMenu(aa.getBVodName());
						indexRecommendDO.setUrl(aa.getBUrl());
					}
					
				} else if (now.after(stop)) {
					// 当前时间为直播后节目
				
					if(aa.getAStatus() == 2){
//						ChanelTeamQuery chanelTeamQuery=new ChanelTeamQuery();
//						chanelTeamQuery.setRegionId(query.getRegionId());
//						AssertMenuAllDO assertMenuAllDO = chanelTeamManager.getReplay(chanelTeamQuery);
						indexRecommendDO.setStatus("2");
						indexRecommendDO.setName(aa.getChName());
						indexRecommendDO.setDate(aa.getADate());
						indexRecommendDO.setTime(aa.getATime());
						indexRecommendDO.setFolder(aa.getAFolder());
						indexRecommendDO.setAssertId(aa.getAAssertid());
						indexRecommendDO.setMenu(aa.getAAliasName());
					}else{
						indexRecommendDO.setStatus("1");
						indexRecommendDO.setUrl(aa.getAUrl());
						indexRecommendDO.setMenu(aa.getAVodName());
					}
				}
				// Calendar c = Calendar.getInstance();
				// String date=c.get(Calendar.MONTH) +"/" +c.get(Calendar.DATE);
				indexDataDO.setData(indexRecommendDO);
				indexDataList.add(indexDataDO);
			}
			indexDO.setRecommend(indexDataList);
			result.setValue(indexDO);
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
	
	static void copyDTO(ChanelTeamRecommendDO recommendDO,ChanelTeamRecommendDTO recommendDTO){
		
	}

}
