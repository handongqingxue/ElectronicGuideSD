package com.electronicGuideSD.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.ToSpRoute;

public interface ToSpRouteMapper {

	int selectForInt(@Param("name")String name);

	List<ToSpRoute> selectList(@Param("name")String name, @Param("start")int start, @Param("rows")int rows, String sort, String order);

	int add(ToSpRoute toSpRoute);

}
