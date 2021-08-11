package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.*;

public interface RoadService {

	int selectForInt(String name);

	List<Road> selectList(String name, int page, int rows, String sort, String order);

	int add(Road road);

	Road selectById(String id);

	int edit(Road road);

}
