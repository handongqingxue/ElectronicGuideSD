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
	@Autowired
	private BusStopMapper busStopDao;

	@Override
	public List<RoadStage> getShortRoadLine(Float meX, Float meY, Float scenicPlaceX, Float scenicPlaceY, String navType) {
		// TODO Auto-generated method stub
		List<RoadStage> shortNavLine = null;
		List<RoadStage> allRSList = roadStageDao.select();
		Map<String, Object> allRoadMap = RoadStageUtil.initAllRoadMap(allRSList);
		if("walk".equals(navType)) {
			Map<String,Object> meToRoadNearRSMap = roadStageDao.selectMinDistanceStage(meX,meY);
			
			Map<String,Object> roadToSpMap = roadStageDao.selectMinDistanceStage(scenicPlaceX,scenicPlaceY);
	
			List<Map<String,Object>> allNavList = RoadStageUtil.initAllWalkNavRoadLine(allRSList,allRoadMap,meToRoadNearRSMap,roadToSpMap,meX,meY,scenicPlaceX,scenicPlaceY);
			
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
			
			shortNavLine=RoadStageUtil.initGetSPShortNavLine(allNavList);
			if(shortNavLine!=null)
				System.out.println("size1==="+shortNavLine.size());
		}
		else if("bus".equals(navType)) {
			Map<String,Object> bsNearSpNearMap = busStopDao.selectMinDistanceStop(scenicPlaceX,scenicPlaceY);//获得离景点最近的站点
			System.out.println("bsNearSpNearMap="+bsNearSpNearMap);
			float bsNearSpNearX = Float.valueOf(bsNearSpNearMap.get("x").toString());//获得离景点最近的站点的x坐标
			float bsNearSpNearY = Float.valueOf(bsNearSpNearMap.get("y").toString());//获得离景点最近的站点的y坐标
			String[] busNoIdArr = bsNearSpNearMap.get("busNoIds").toString().split(",");
			List<String> busNoIdList = Arrays.asList(busNoIdArr);
			
			Map<String,Object> meNearBsMap = busStopDao.selectMinDistanceStopByNoIds(meX,meY,busNoIdList);//获得离游客最近的能到达景点的最近站点
			System.out.println("meNearBsMap="+meNearBsMap);
			float meNearBsX = Float.valueOf(meNearBsMap.get("x").toString());//获得离游客最近的能到达景点的最近站点的x坐标
			float meNearBsY = Float.valueOf(meNearBsMap.get("y").toString());//获得离游客最近的能到达景点的最近站点的y坐标
			
			//以下代码是从游客位置到附近站点的导航
			Map<String,Object> meToRoadNearRSMap = roadStageDao.selectMinDistanceStage(meX,meY);//获得离游客最近的路段
			Map<String,Object> roadToBsNearBsMap = roadStageDao.selectMinDistanceStage(meNearBsX,meNearBsY);//获得离游客最近的站点最近的路段
			List<Map<String,Object>> meToBsAllNavList = RoadStageUtil.initAllWalkNavRoadLine(allRSList,allRoadMap,meToRoadNearRSMap,roadToBsNearBsMap,meX,meY,meNearBsX,meNearBsY);
			List<RoadStage> meToBsShortNavLine = RoadStageUtil.initGetSPShortNavLine(meToBsAllNavList);
			shortNavLine=meToBsShortNavLine;
			
			//以下代码是从附近站点到景点位置的导航
			Map<String, Object> bsToRoadNearBs = roadStageDao.selectMinDistanceStage(bsNearSpNearX,bsNearSpNearY);//获得离景点最近的站点最近的路段
			Map<String,Object> roadToSpMap = roadStageDao.selectMinDistanceStage(scenicPlaceX,scenicPlaceY);//获得离景点最近的路段
			List<Map<String,Object>> bsToSpAllNavList = RoadStageUtil.initAllWalkNavRoadLine(allRSList,allRoadMap,bsToRoadNearBs,roadToSpMap,bsNearSpNearX,bsNearSpNearY,scenicPlaceX,scenicPlaceY);
			List<RoadStage> bsToSpShortNavLine = RoadStageUtil.initGetSPShortNavLine(bsToSpAllNavList);
			shortNavLine.addAll(bsToSpShortNavLine);
		}
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
