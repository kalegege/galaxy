package com.wasu.ptyw.galaxy.dal.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.Alias;

import com.wasu.ptyw.galaxy.common.util.NumUtil;

/**
 * @author wenguang
 * @date 2015年12月11日
 */
@Alias("GalaxyFilmDO")
@Data
@EqualsAndHashCode(callSuper = true)
public class GalaxyFilmDO extends FeaturesDO {
	private static final long serialVersionUID = 1L;

	public GalaxyFilmDO() {

	}

	/**
	 * 地区编码
	 */
	private String regionCode;

	/**
	 * 栏目编码
	 */
	private String folderCode;

	/**
	 * 资产ID
	 */
	private String assetId;

	/**
	 * 资产类型:13是视频，36是电影
	 */
	private Integer assetType;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 拆条名字
	 */
	private String sub2Name;

	/**
	 * 高标清:0标清，1高清
	 */
	private Integer ishd;

	/**
	 * 价格:单位分
	 */
	private Integer price;

	/**
	 * 发行年份
	 */
	private String year;

	/**
	 * 演员
	 */
	private String actors;

	/**
	 * 导演
	 */
	private String directors;

	/**
	 * 看点
	 */
	private String viewpoint;

	/**
	 * 描述
	 */
	private String des;

	/**
	 * RtspUrl
	 */
	private String rtspUrl;

	/**
	 * 播放地址
	 */
	private String assetUrl;

	/**
	 * 星级:0-5
	 */
	private Integer starlevel;

	/**
	 * 类别
	 */
	private String leixing;

	/**
	 * 海报
	 */
	private String haibaoUrl;

	/**
	 * 草图
	 */
	private String caotuUrl;

	/**
	 * 剧照
	 */
	private String juzhaoUrl;

	/**
	 * 背景
	 */
	private String beijingUrl;

	/**
	 * 标题图
	 */
	private String biaotiUrl;

	/**
	 * 别名
	 */
	private String aliasName;

	/**
	 * 价格:单位分
	 */
	private Integer aliasPrice;

	/**
	 * 微信购票编码
	 */
	private String weixinCode;

	/**
	 * 淘宝购票编码
	 */
	private String taobaoCode;

	/**
	 * 关联影片ID
	 */
	private String linkFilmIds;

	/**
	 * 更新次数
	 */
	private Integer updateCount;

	/**
	 * 状态：0新导入，1上线,2手动下线，3系统下线
	 */
	private Integer status;

	/**
	 * clps状态：0下线，1上线
	 */
	private Integer clpsStatus;

	/**
	 * 优先级
	 */
	private Integer priority;

	public String getFilmName() {
		if (StringUtils.isNotEmpty(aliasName))
			return aliasName;
		if (StringUtils.isNotEmpty(sub2Name))
			return sub2Name;
		return name;
	}

	public int getFilmPrice() {
		if (NumUtil.isGreaterZero(aliasPrice))
			return aliasPrice.intValue();
		return price == null ? 0 : price.intValue();
	}
}
