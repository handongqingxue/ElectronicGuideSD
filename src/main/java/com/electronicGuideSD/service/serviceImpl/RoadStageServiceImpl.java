package com.electronicGuideSD.service.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;
import com.electronicGuideSD.util.*;

@Service
public class RoadStageServiceImpl implements RoadStageService {

	@Autowired
	private RoadStageMapper roadStageDao;

	@Override
	public List<RoadStage> getShortRoadLine(Float meX, Float meY, Float scenicPlaceX, Float scenicPlaceY) {
		// TODO Auto-generated method stub
		System.out.println("scenicPlaceX==="+scenicPlaceX);
		System.out.println("scenicPlaceY==="+scenicPlaceY);
		Map<String,Object> meToRoadNearRSMap = roadStageDao.selectMinDistanceStage(meX,meY);
		/*
		System.out.println("meRoadId="+Integer.valueOf(mtrNearRSMap.get("roadId").toString()));
		System.out.println("meFrontThrough="+Boolean.valueOf(mtrNearRSMap.get("frontThrough").toString()));
		System.out.println("meBackThrough="+Boolean.valueOf(mtrNearRSMap.get("backThrough").toString()));
		System.out.println("meBfFlag="+mtrNearRSMap.get("bfFlag").toString());
		*/
		
		Map<String,Object> roadToSpMap = roadStageDao.selectMinDistanceStage(scenicPlaceX,scenicPlaceY);
		/*
		System.out.println("spRoadId="+Integer.valueOf(spToRoadMap.get("roadId").toString()));
		System.out.println("spFrontThrough="+Boolean.valueOf(spToRoadMap.get("frontThrough").toString()));
		System.out.println("spBackThrough="+Boolean.valueOf(spToRoadMap.get("backThrough").toString()));
		*/

		List<RoadStage> allRSList = roadStageDao.select();
		Map<String, Object> allRoadMap = RoadStageUtil.initAllRoadMap(allRSList);
		List<Map<String,Object>> allNavList = RoadStageUtil.initAllNavRoadLine(allRSList,allRoadMap,meToRoadNearRSMap,roadToSpMap,meX,meY,scenicPlaceX,scenicPlaceY);
		
		for (int i = 0; i < allNavList.size(); i++) {
			Map<String, Object> allNav = allNavList.get(i);
			String navLong = allNav.get("navLong").toString();
			System.out.println("navLong"+i+"="+navLong);
			List<RoadStage> navRoad = (List<RoadStage>)allNav.get("navLine");
			for (int j = 0; j < navRoad.size(); j++) {
				RoadStage roadStage = navRoad.get(j);
				float backX = roadStage.getBackX();
				float backY = roadStage.getBackY();
				float frontX = roadStage.getFrontX();
				float frontY = roadStage.getFrontY();
				System.out.println("backX="+backX+",backY="+backY+",frontX="+frontX+",frontY="+frontY);
			}
			System.out.println("");
		}
		
		List<RoadStage> shortNavLine=RoadStageUtil.initGetSPShortNavLine(allNavList);
		if(shortNavLine!=null)
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
		List<RoadStage> roadStageList = roadStageDao.selectList(roadName, name, (page-1)*rows, rows, sort, order);
		List<RoadStage> allRSList = roadStageDao.select();
		Map<String, String> rsNameMap = RoadStageUtil.initRSNameMap(allRSList);
		for (RoadStage roadStage : roadStageList) {
			String backCrossRSIds = roadStage.getBackCrossRSIds();
			if(roadStage.getBackIsCross()&&!StringUtils.isEmpty(backCrossRSIds)) {
				String backCrossRSNames = RoadStageUtil.getRSNameFromMapByIds(rsNameMap,backCrossRSIds);
				roadStage.setBackCrossRSNames(backCrossRSNames);
			}
			String frontCrossRSIds = roadStage.getFrontCrossRSIds();
			if(roadStage.getFrontIsCross()&&!StringUtils.isEmpty(frontCrossRSIds)) {
				String frontCrossRSNames = RoadStageUtil.getRSNameFromMapByIds(rsNameMap,frontCrossRSIds);
				roadStage.setFrontCrossRSNames(frontCrossRSNames);
			}
		}
		return roadStageList;
	}

	@Override
	public List<RoadStage> selectCBBData() {
		// TODO Auto-generated method stub
		return roadStageDao.selectCBBData();
	}

	@Override
	public int add(RoadStage roadStage) {
		// TODO Auto-generated method stub
		return roadStageDao.add(roadStage);
	}

	@Override
	public int edit(RoadStage roadStage) {
		// TODO Auto-generated method stub
		return roadStageDao.edit(roadStage);
	}

	@Override
	public RoadStage selectById(String id) {
		// TODO Auto-generated method stub
		return roadStageDao.selectById(id);
	}

	@Override
	public List<RoadStage> selectOtherList(String id) {
		// TODO Auto-generated method stub
		return roadStageDao.selectOtherList(id);
	}

	@Override
	public int deleteByIds(String ids) {
		// TODO Auto-generated method stub
		List<String> idList = Arrays.asList(ids.split(","));
		return roadStageDao.deleteByIds(idList);
	}

	@Override
	public int updateAttrInRoad(List<RoadStage> road) {
		// TODO Auto-generated method stub
		int count=0;
		for (RoadStage roadStage : road) {
			count+=roadStageDao.updateAttr(roadStage);
		}
		return count;
	}
}
