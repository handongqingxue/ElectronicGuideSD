package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.ScenicPlace;

public interface ScenicPlaceService {

	int selectForInt(String name);

	List<ScenicPlace> selectList(String name, int page, int rows, String sort, String order);

	int add(ScenicPlace scenicPlace);

	ScenicPlace selectById(String id);

	int edit(ScenicPlace scenicPlace);

	List<ScenicPlace> selectList();

}
