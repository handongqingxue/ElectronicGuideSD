package com.electronicGuideSD.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.*;

public interface RoadDotMapper {

	List<RoadDot> select();

	Map<String, Object> selectMinDistanceDot(@Param("x")Float x, @Param("y")Float y);

}
