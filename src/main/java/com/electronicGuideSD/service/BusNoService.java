package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.*;

public interface BusNoService {

	int selectForInt(String name);

	List<BusNo> selectList(String name, int page, int rows, String sort, String order);

	int add(BusNo busNo);

	BusNo selectById(String id);

	int edit(BusNo busNo);

	List<BusNo> selectCBBData();

}
