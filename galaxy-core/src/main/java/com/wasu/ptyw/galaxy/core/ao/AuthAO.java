/**
 * 
 */
package com.wasu.ptyw.galaxy.core.ao;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.manager.GalaxyOrderFilmManager;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;

/**
 * 权限校验类
 * 
 * @author wenguang
 * @date 2015年6月24日
 */
@Service("authAO")
@Slf4j
public class AuthAO extends BaseAO {
	@Resource
	private GalaxyOrderFilmManager galaxyOrderFilmManager;

	@Resource
	GalaxyHistoryAO galaxyHistoryAO;

	/**
	 * 校验用户是否购买过电影
	 * 
	 * @param GalaxyUserDO
	 * @return 对象ID
	 */
	public Result<GalaxyOrderFilmDO> authFilm(GalaxyOrderFilmQuery query) {
		Result<GalaxyOrderFilmDO> result = new Result<GalaxyOrderFilmDO>(false);
		try {
			if (query == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			GalaxyOrderFilmDO orderDb = galaxyOrderFilmManager.queryFirst(query);

			if (orderDb == null || !orderDb.isSuccess()) {
				result.setValue(null);
			} else {
				result.setValue(orderDb);
				// 刚鉴权通过时记录播放历史
				// galaxyHistoryAO.save(orderDb);
			}
		} catch (Exception e) {
			log.error("preOrder error, query=" + query, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}
}
