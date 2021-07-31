package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.*;

public interface ToSpRouteDotService {

	int selectForInt(String name);

	List<ToSpRouteDot> selectList(String name, int page, int rows, String sort, String order);

}
