package com.electronicGuideSD.service;

import java.util.List;
import java.util.Map;

import com.electronicGuideSD.entity.*;

public interface RoadStageService {

	Map<String, Object> getShortRoadLine(Float meX, Float meY, Float scenicPlaceX, Float scenicPlaceY, String navType);

	int selectForInt(String roadName, String name);

	List<RoadStage> selectList(String roadName, String name, int page, int rows, String sort, String order);

	List<RoadStage> selectCBBData();

	int add(RoadStage roadStage);

	int edit(RoadStage roadStage);

	RoadStage selectById(String id);

	List<RoadStage> selectOtherList(String id);

	int deleteByIds(String ids);

	int updateAttrInRoad(List<RoadStage> road);

	boolean checkIfConnectInRoad(float backX, float backY, float frontX, float frontY, int roadId);

}
