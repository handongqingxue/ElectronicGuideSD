package com.electronicGuideSD.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.ScenicPlace;

public interface ScenicPlaceMapper {

	int selectForInt(@Param("name")String name);

	List<ScenicPlace> selectList(@Param("name")String name, @Param("start")int start, @Param("rows")int rows, String sort, String order);

	int add(ScenicPlace scenicPlace);

	ScenicPlace selectById(@Param("id")String id);

	int edit(ScenicPlace scenicPlace);

}
