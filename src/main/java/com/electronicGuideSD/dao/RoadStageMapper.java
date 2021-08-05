package com.electronicGuideSD.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.*;

public interface RoadStageMapper {

	List<RoadStage> select();

	Map<String, Object> selectMinDistanceStage(@Param("x")Float x, @Param("y")Float y);

}
