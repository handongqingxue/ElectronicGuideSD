package com.electronicGuideSD.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.*;

public interface RoadStageMapper {

	List<RoadStage> select();

	Map<String, Object> selectMinDistanceStage(@Param("x")Float x, @Param("y")Float y);

	int selectForInt(@Param("roadName")String roadName, @Param("name")String name);

	List<RoadStage> selectList(@Param("roadName")String roadName, @Param("name")String name, @Param("start")int start, @Param("rows")int rows, String sort, String order);

	List<RoadStage> getBackTwoInRoad(@Param("roadId")int roadId);

	List<RoadStage> getFrontTwoInRoad(@Param("roadId")int roadId);

	List<RoadStage> selectCBBData();

	int add(RoadStage roadStage);

	int edit(RoadStage roadStage);

	RoadStage selectById(@Param("id")String id);

	List<RoadStage> selectOtherList(@Param("id")String id);

	int deleteByIds(List<String> idList);

	int updateAttr(RoadStage roadStage);

}
