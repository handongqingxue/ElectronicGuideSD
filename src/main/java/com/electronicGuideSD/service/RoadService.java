package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.*;

public interface RoadService {

	int selectForInt(String name);

	List<ToSpRoute> selectList(String name, int page, int rows, String sort, String order);

	int add(ToSpRoute toSpRoute);

}
