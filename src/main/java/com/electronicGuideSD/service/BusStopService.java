package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.*;

public interface BusStopService {

	int selectForInt(String name);

	List<BusStop> selectList(String name, int page, int rows, String sort, String order);

	int add(BusStop busStop);

	int edit(BusStop busStop);

	BusStop selectById(String id);

}
