package com.electronicGuideSD.service;

import java.util.List;
import java.util.Map;

import com.electronicGuideSD.entity.*;

public interface RoadStageService {

	List<RoadStage> getShortRoadLine(Float meX, Float meY, Float scenicPlaceX, Float scenicPlaceY);
	Map<String,Object> initAllRoadMap(List<RoadStage> roadStageList);
	boolean checkRoadMapIdExist(int roadId, Map<String, Object> roadMap);

}
