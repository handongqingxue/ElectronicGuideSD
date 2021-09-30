package com.electronicGuideSD.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.BusStop;

public interface BusStopMapper {

	Map<String, Object> selectMinDistanceStop(@Param("x")Float x, @Param("y")Float y);

	Map<String, Object> selectMinDistanceStopByNoIds(@Param("x")Float x, @Param("y")Float y, @Param("busNoIdList")List<String> busNoIdList);

	int selectForInt(@Param("name")String name);

	List<BusStop> selectList(@Param("name")String name, @Param("start")int start, @Param("rows")int rows, String sort, String order);

	int add(BusStop busStop);

	int edit(BusStop busStop);

	BusStop selectById(@Param("id")String id);

	List<BusStop> selectOtherList(@Param("id")String id);

	List<BusStop> selectWAList();

	List<BusStop> selectBySortStartToEnd(@Param("startSort")int startSort, @Param("endSort")int endSort);

	List<BusStop> selectCBBData(@Param("busNoId")int busNoId);

	List<BusStop> selectOtherCBBData(@Param("busStopId")int busStopId, @Param("busNoIdList")List<String> busNoIdList);

}
