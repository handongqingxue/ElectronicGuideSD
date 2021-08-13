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

	List<RoadStage> selectCBBData();

	int add(RoadStage roadStage);

}
