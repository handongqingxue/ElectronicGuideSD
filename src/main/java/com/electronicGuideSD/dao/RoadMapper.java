package com.electronicGuideSD.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.*;

public interface RoadMapper {

	int selectForInt(@Param("name")String name);

	List<Road> selectList(@Param("name")String name, @Param("start")int start, @Param("rows")int rows, String sort, String order);

	int add(Road road);

	Road selectById(@Param("id")String id);

	int edit(Road road);

}
