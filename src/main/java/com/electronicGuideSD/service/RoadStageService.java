package com.electronicGuideSD.service;

import java.util.List;
import java.util.Map;

import com.electronicGuideSD.entity.*;

public interface RoadStageService {

	List<RoadStage> getShortRoadLine(Float meX, Float meY, Float scenicPlaceX, Float scenicPlaceY);

	int selectForInt(String roadName, String name);

	List<RoadStage> selectList(String roadName, String name, int page, int rows, String sort, String order);

	List<RoadStage> selectCBBData();

	int add(RoadStage roadStage);

	int edit(RoadStage roadStage);

	RoadStage selectById(String id);

	List<RoadStage> selectOtherList(String id);

}
