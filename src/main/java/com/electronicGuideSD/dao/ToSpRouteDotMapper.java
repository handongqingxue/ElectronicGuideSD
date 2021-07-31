package com.electronicGuideSD.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.ToSpRouteDot;

public interface ToSpRouteDotMapper {

	int selectForInt(@Param("name")String name);

	List<ToSpRouteDot> selectList(@Param("name")String name, @Param("start")int start, @Param("rows")int rows, String sort, String order);

}
