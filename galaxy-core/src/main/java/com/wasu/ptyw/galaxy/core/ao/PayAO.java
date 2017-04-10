/**
 * 
 */
package com.wasu.ptyw.galaxy.core.ao;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wasu.ptyw.galaxy.common.dataobject.Result;
import com.wasu.ptyw.galaxy.common.dataobject.ResultCode;
import com.wasu.ptyw.galaxy.common.exception.MyException;
import com.wasu.ptyw.galaxy.core.api.AlipayService;
import com.wasu.ptyw.galaxy.core.api.WasupayService;
import com.wasu.ptyw.galaxy.core.api.WxpayService;
import com.wasu.ptyw.galaxy.core.cache.LocalCache;
import com.wasu.ptyw.galaxy.core.dto.DiscountDTO;
import com.wasu.ptyw.galaxy.core.manager.GalaxyFilmManager;
import com.wasu.ptyw.galaxy.core.manager.GalaxyOrderFilmManager;
import com.wasu.ptyw.galaxy.core.manager.GalaxyUserManager;
import com.wasu.ptyw.galaxy.dal.constant.DbContant;
import com.wasu.ptyw.galaxy.dal.constant.FilmStatus;
import com.wasu.ptyw.galaxy.dal.constant.FollowStatus;
import com.wasu.ptyw.galaxy.dal.constant.OrderFilmStatus;
import com.wasu.ptyw.galaxy.dal.constant.PayChannel;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyDiscountAccessDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyOrderFilmDO;
import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyWeixinFollowDO;
import com.wasu.ptyw.galaxy.dal.query.GalaxyOrderFilmQuery;

/**
 * 本地支付相关类
 * 
 * @author wenguang
 * @date 2015年6月24日
 */
@Service("payAO")
@Slf4j
public class PayAO extends BaseAO {
	@Resource
	WxpayService wxpayService;
	@Resource
	AlipayService alipayService;
	@Resource
	WasupayService wasupayService;

	@Resource
	GalaxyFilmManager galaxyFilmManager;
	@Resource
	GalaxyUserManager galaxyUserManager;
	@Resource
	GalaxyOrderFilmManager galaxyOrderFilmManager;

	@Resource
	GalaxyDiscountAccessAO galaxyDiscountAccessAO;
	@Resource
	GalaxyWeixinFollowAO galaxyWeixinFollowAO;
	@Resource
	GalaxyOrderFilmAO galaxyOrderFilmAO;
	@Resource
	GalaxyHistoryAO galaxyHistoryAO;
	@Resource
	GalaxyUserAO galaxyUserAO;

	/**
	 * 本地预下单
	 * 
	 * @param GalaxyUserDO
	 * @return 对象ID
	 */
	public Result<Long> preOrder(GalaxyOrderFilmDO order) {
		Result<Long> result = new Result<Long>(false);
		try {
			if (order == null) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}

			GalaxyFilmDO film = galaxyFilmManager.getById(order.getFilmId());
			if (film == null || FilmStatus.isNew(film.getStatus())) {
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);
			}

			order.setContImage(film.getHaibaoUrl());
			order.setContCode(film.getAssetId());
			order.setContName(film.getFilmName());

			GalaxyDiscountAccessDO discountAccess = setOrderPrice(film, order);

			// 更新老的订单
			GalaxyOrderFilmDO orderDb = getOrderFilmDODb(order);
			if (orderDb != null) {
				updateOrderByStatus(orderDb);
			}

			Long id = galaxyOrderFilmManager.insert(order);
			
//			String qrcodeUrl = wasupayService.prePay(id, order);
//			if(StringUtils.isEmpty(qrcodeUrl)){
//				galaxyOrderFilmManager.updateStatusByIds(Lists.newArrayList(id), OrderFilmStatus.CANCELED.getCode());
//				return setErrorMessage(result, ResultCode.ORDER_WASU_CREATE_FAIL);
//			}

			// 微信关注活动
			long followId = NumberUtils.toLong(order.getFeature(DbContant.FEA_WX_FOLLOW));
			if (followId > 0) {
				followed(followId);
			}

			// 折扣券相关
			if (discountAccess != null) {
				discountAccess.putDes(DbContant.FEA_DISCOUNT_ORDER, id.toString());
				galaxyDiscountAccessAO.update(discountAccess);
			}
			
			result.setValue(id);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("preOrder error, order=" + order, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 获取二维码内容
	 */
	public Result<String> getQrcodeUrl(long orderId, int payChannel) {
		Result<String> result = new Result<String>(false);
		try {
			if (orderId <= 0) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}
			if (PayChannel.isWeixin(payChannel)) {
				// result.setValue(wxpayService.generateQrcode(OrderContant.TRADE_NO_PRE
				// + orderId));
				String qrcodeUrl = wxpayService.prePay(orderId);
				if (StringUtils.isNotEmpty(qrcodeUrl)) {
					result.setValue(qrcodeUrl);
				}
			} else {
				String qrcodeUrl = alipayService.prePay(orderId);
				if (StringUtils.isNotEmpty(qrcodeUrl)) {
					result.setValue(qrcodeUrl);
				}
			}
		} catch (Exception e) {
			log.error("getQrcodeUrl error, orderId=" + orderId + ", payChannel=" + payChannel, e);
			setErrorMessage(result, ResultCode.SYSTEM_ERROR);
		}
		return result;
	}

	/**
	 * 查询订单状态
	 */
	public Result<Boolean> isPaySuccess(long orderId, int payChannel) {
		Result<Boolean> result = new Result<Boolean>(false);
		try {
			if (orderId <= 0) {
				return setErrorMessage(result, ResultCode.PARAM_INPUT_NULL);
			}

			GalaxyOrderFilmDO order = galaxyOrderFilmManager.getById(orderId);

			if (order == null)
				return setErrorMessage(result, ResultCode.DB_DATA_NOEXIST);

			boolean isPaySuccess = OrderFilmStatus.isSuccess(order.getStatus());
			result.setValue(isPaySuccess);
			if (isPaySuccess) {
				// 刚扫描成功时记录播放历史
				// galaxyHistoryAO.save(order);
			}
		} catch (Exception e) {
			log.error("queryStatus error, orderId=" + orderId, e);
			if (e instanceof MyException) {
				setErrorMessage(result, ((MyException) e).getResultCode());
			} else {
				setErrorMessage(result, ResultCode.SYSTEM_ERROR);
			}
		}
		return result;
	}

	/**
	 * 获取最新一个订单
	 */
	private GalaxyOrderFilmDO getOrderFilmDODb(GalaxyOrderFilmDO order) throws MyException {
		GalaxyOrderFilmQuery orderQuery = new GalaxyOrderFilmQuery();
		orderQuery.setFilmId(order.getFilmId());
		orderQuery.setOutUserId(order.getOutUserId());
		orderQuery.setRegionCode(order.getRegionCode());
		orderQuery.setOrderBy("id desc");
		return galaxyOrderFilmManager.queryFirst(orderQuery);
	}

	/**
	 * 根据数据库订单状态判断是否生成新订单
	 */
	private void updateOrderByStatus(GalaxyOrderFilmDO orderDb) throws MyException {
		int status = orderDb.getStatus();

		if (OrderFilmStatus.isPrepay(status)) {
			// 与下单时需要关闭外部订单
			if (DbContant.YES.equals(orderDb.getFeature(DbContant.FEA_PREPAY_ALI))) {
				boolean closeFlg = alipayService.cancel(orderDb);
				log.warn("close alipay order, order_id=" + orderDb.getId() + "result:" + closeFlg);
			}
			if (DbContant.YES.equals(orderDb.getFeature(DbContant.FEA_PREPAY_WX))) {
				boolean closeFlg = wxpayService.cancel(orderDb);
				log.warn("close weixin order, order_id=" + orderDb.getId() + "result:" + closeFlg);
			}
		}
		if (!OrderFilmStatus.isEnd(status)) {
			galaxyOrderFilmAO.closeOrder(Lists.newArrayList(orderDb), OrderFilmStatus.CANCELED.getCode());
		}
	}

	private GalaxyDiscountAccessDO setOrderPrice(GalaxyFilmDO film, GalaxyOrderFilmDO order) {
		GalaxyDiscountAccessDO discountAccess = null;
		int price = film.getAliasPrice();
		long followId = NumberUtils.toLong(order.getFeature(DbContant.FEA_WX_FOLLOW));
		if (followId > 0) {
			price = NumberUtils.toInt(LocalCache.get(DbContant.KEY_WX_FOLLOW_PRICE), DbContant.WX_FOLLOW_PRICE);
		} else {
			long discountId = NumberUtils.toLong(order.getFeature(DbContant.FEA_DISCOUNT_ID));
			if (discountId > 0) {
				Result<GalaxyDiscountAccessDO> result = galaxyDiscountAccessAO.getById(discountId);
				if (result.isSuccess()) {
					if (StringUtils.isNotEmpty(result.getValue().getDes())) {

						DiscountDTO discount = JSON.parseObject(result.getValue().getDes(), DiscountDTO.class);
						price = discount.minPrice(price);
						discountAccess = result.getValue();
						discountAccess.setStatus(DbContant.TWO);
					}
				}
			}
		}
		order.setTotalPrice(price);
		return discountAccess;
	}

	private void followed(long followId) {
		Result<GalaxyWeixinFollowDO> result = galaxyWeixinFollowAO.getById(followId);
		if (result.isSuccess()) {
			GalaxyWeixinFollowDO follow = result.getValue();
			if (FollowStatus.isFollowed(follow.getStatus())) {
				follow.setUsedStatus(DbContant.ONE);
				galaxyWeixinFollowAO.update(follow);
			}
		}
	}
}
