package com.electronicGuideSD.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.*;

public interface BusNoMapper {

	int selectForInt(@Param("name")String name);

	List<BusNo> selectList(@Param("name")String name, @Param("start")int start, @Param("rows")int rows, String sort, String order);

	int add(BusNo busNo);

	BusNo selectById(@Param("id")String id);

	int edit(BusNo busNo);

	List<BusNo> selectCBBData();

}
