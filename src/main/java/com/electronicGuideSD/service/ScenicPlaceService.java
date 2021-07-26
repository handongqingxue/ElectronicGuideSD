package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.ScenicPlace;

public interface ScenicPlaceService {

	int selectForInt(String name);

	List<ScenicPlace> selectList(String name, int page, int rows, String sort, String order);

}
