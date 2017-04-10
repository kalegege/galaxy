package com.wasu.ptyw.galaxy.core.api.wxprotocol;

import java.util.List;

import lombok.Data;

import com.wasu.ptyw.galaxy.dal.dataobject.GalaxyFilmDO;

@Data
public class ClpsReqData {
	List<GalaxyFilmDO> nav0;
	String folderCode;
	String folderName;
	String tvid;
	String uid;
}
