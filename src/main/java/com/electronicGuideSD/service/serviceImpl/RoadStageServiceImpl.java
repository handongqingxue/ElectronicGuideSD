package com.electronicGuideSD.service.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;
import com.electronicGuideSD.util.RoadStageUtil;

@Service
public class RoadStageServiceImpl implements RoadStageService {

	@Autowired
	private RoadStageMapper roadStageDao;

	@Override
	public List<RoadStage> getShortRoadLine(Float meX, Float meY, Float scenicPlaceX, Float scenicPlaceY) {
		// TODO Auto-generated method stub
		System.out.println("scenicPlaceX==="+scenicPlaceX);
		System.out.println("scenicPlaceY==="+scenicPlaceY);
		Map<String,Object> meToRoadMap = roadStageDao.selectMinDistanceStage(meX,meY);
		/*
		System.out.println("meRoadId="+Integer.valueOf(meToRoadMap.get("roadId").toString()));
		System.out.println("meFrontThrough="+Boolean.valueOf(meToRoadMap.get("frontThrough").toString()));
		System.out.println("meBackThrough="+Boolean.valueOf(meToRoadMap.get("backThrough").toString()));
		System.out.println("meBfFlag="+meToRoadMap.get("bfFlag").toString());
		*/
		
		Map<String,Object> spToRoadMap = roadStageDao.selectMinDistanceStage(scenicPlaceX,scenicPlaceY);
		/*
		System.out.println("spRoadId="+Integer.valueOf(spToRoadMap.get("roadId").toString()));
		System.out.println("spFrontThrough="+Boolean.valueOf(spToRoadMap.get("frontThrough").toString()));
		System.out.println("spBackThrough="+Boolean.valueOf(spToRoadMap.get("backThrough").toString()));
		*/

		List<RoadStage> roadStageList = roadStageDao.select();
		Map<String, Object> roadStageMap = RoadStageUtil.initAllRoadMap(roadStageList);
		List<Map<String,Object>> allNavList = RoadStageUtil.initAllNavRoadLine(roadStageMap,meToRoadMap,spToRoadMap,meX,meY,scenicPlaceX,scenicPlaceY);
		List<RoadStage> shortNavLine=RoadStageUtil.initGetSPShortNavLine(allNavList);
		//System.out.println("size1==="+((List<RoadStage>)allNavList.get(3).get("navLine")).size());
		System.out.println("size1==="+shortNavLine.size());
		return shortNavLine;
	}

	@Override
	public int selectForInt(String roadName, String name) {
		// TODO Auto-generated method stub
		return roadStageDao.selectForInt(roadName,name);
	}

	@Override
	public List<RoadStage> selectList(String roadName, String name, int page, int rows, String sort, String order) {
		// TODO Auto-generated method stub
		return roadStageDao.selectList(roadName, name, (page-1)*rows, rows, sort, order);
	}
}
