package com.electronicGuideSD.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.BusStop;

public interface BusStopMapper {

	int selectForInt(@Param("name")String name);

	List<BusStop> selectList(@Param("name")String name, @Param("start")int start, @Param("rows")int rows, String sort, String order);

	int add(BusStop busStop);

	int edit(BusStop busStop);

	BusStop selectById(@Param("id")String id);

}
