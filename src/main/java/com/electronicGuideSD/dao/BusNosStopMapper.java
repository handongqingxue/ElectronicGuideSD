package com.electronicGuideSD.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.*;

public interface BusNosStopMapper {

	List<BusNosStop> selectList(@Param("name")String name, @Param("busNoId")int busNoId);

	List<BusNosStop> selectByBusNoId(@Param("busNoId")int busNoId);

	int add(BusNosStop busNosStop);

	int edit(BusNosStop busNosStop);

}
