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
		Map<String, Object> roadStageMap = initAllRoadMap(roadStageList);
		List<RoadStage> allNavList = initAllNavRoadLine(roadStageMap,meToRoadMap,meX,meY);
		System.out.println("size==="+allNavList.size());
		return allNavList;
	}
	
	public List<RoadStage> initAllNavRoadLine(Map<String, Object> roadStageMap,Map<String,Object> meToRoadMap, Float meX, Float meY) {
		List<RoadStage> allNavList=new ArrayList<>();
		List<RoadStage> rsList=null;
		Integer startRoadId = Integer.valueOf(meToRoadMap.get("roadId").toString());
		RoadStage roadStage=new RoadStage();
		roadStage.setBackX(meX);
		roadStage.setBackY(meY);
		if(RoadStage.BACK_FLAG.equals(meToRoadMap.get("bfFlag").toString())) {//�ж���������ĵ���ǰ���㻹�Ǻ󷽵�
			Float frontX = Float.valueOf(meToRoadMap.get("backX").toString());//������Ȼ��ú���ĵ㣬�������෴���൱���ο͵���·����ǰ��ĵ�
			Float frontY = Float.valueOf(meToRoadMap.get("backY").toString());
			roadStage.setFrontX(frontX);//��λ�ô������ڵ���������������Ǹ��󷽵㴦�����Ǿ����ó�·�ߵ�ǰ������
			roadStage.setFrontY(frontY);
			allNavList.add(roadStage);
			System.out.println("oooooooo="+meX+","+meY+","+frontX+","+frontY+","+startRoadId);
			Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
			
			rsList=(List<RoadStage>)roadStageMap.get("roadStage"+meToRoadMap.get("roadId").toString());
			int itemIndex = RoadStageUtil.getListItemIndexBySort(rsList,sort);
			for(int i=itemIndex;i<rsList.size();i++) {
				System.out.println("i��==="+i);
				RoadStage rs = rsList.get(i);
				RoadStageUtil.addRSNavInList(frontX,frontY,rs,allNavList);
				System.out.println("size==="+allNavList.size());
				//Integer roadId = Integer.valueOf(meToRoadMap.get("roadId").toString());
				//System.out.println("11111111111="+frontX+","+frontY+","+rsFrontX+","+rsFrontY+","+roadId);
			}
			for(int i=itemIndex-1;i>=0;i--) {
				System.out.println("i��==="+i);
			}
			
		}
		else if(RoadStage.FRONT_FLAG.equals(meToRoadMap.get("bfFlag").toString())) {
			Float frontX = Float.valueOf(meToRoadMap.get("frontX").toString());//������Ȼ��ú���ĵ㣬�������෴���൱���ο͵���·����ǰ��ĵ�
			Float frontY = Float.valueOf(meToRoadMap.get("frontY").toString());
			roadStage.setFrontX(frontX);
			roadStage.setFrontY(frontY);
			allNavList.add(roadStage);

			Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
			System.out.println("sort==="+sort);
			rsList=(List<RoadStage>)roadStageMap.get("roadStage"+meToRoadMap.get("roadId").toString());
			int itemIndex = RoadStageUtil.getListItemIndexBySort(rsList,sort);
			
			for(int i=itemIndex;i<rsList.size();i++) {
				System.out.println("i��==="+i);
				RoadStage rs = rsList.get(i);
				System.out.println("front222==="+frontX+","+frontY);
				System.out.println("bx==="+rs.getBackX());
				System.out.println("by==="+rs.getBackY());
				if(frontX.equals(rs.getBackX())&&frontY.equals(rs.getBackY())) {
					System.out.println("��󷽵����");
					//���ɵĵ����߷�����ܺ͵�ͼ��·�ߵķ����෴
					Float rsFrontX = Float.valueOf(rs.getFrontX());//������Ȼ���ǰ��ĵ㣬�������෴���൱���ο͵���·�������ĵ�
					Float rsFrontY = Float.valueOf(rs.getFrontY());
					roadStage=new RoadStage();
					roadStage.setBackX(frontX);
					roadStage.setBackY(frontY);
					roadStage.setFrontX(rsFrontX);
					roadStage.setFrontY(rsFrontY);
					allNavList.add(roadStage);
					System.out.println("===="+rsList.size());
					Integer roadId = Integer.valueOf(meToRoadMap.get("roadId").toString());
					System.out.println("2222222222="+frontX+","+frontY+","+rsFrontX+","+rsFrontY+","+roadId);
				}
				else if(frontX.equals(rs.getFrontX())&&frontY.equals(rs.getFrontY())) {
					System.out.println("��ǰ�������");
					Float rsFrontX = Float.valueOf(rs.getBackX());//����ǰ������ӣ����ﵼ������ǰ���ĵ����·����󷽵ĵ�
					Float rsFrontY = Float.valueOf(rs.getBackY());
					roadStage=new RoadStage();
					roadStage.setBackX(frontX);
					roadStage.setBackY(frontY);
					roadStage.setFrontX(rsFrontX);
					roadStage.setFrontY(rsFrontY);
					allNavList.add(roadStage);
					System.out.println("===="+rsList.size());
					Integer roadId = Integer.valueOf(meToRoadMap.get("roadId").toString());
					System.out.println("2222222222="+frontX+","+frontY+","+rsFrontX+","+rsFrontY+","+roadId);
				}
			}
			for(int i=itemIndex-1;i>=0;i--) {
				System.out.println("i��==="+i);
			}
		}
		return allNavList;
	}

	@Override
	public Map<String, Object> initAllRoadMap(List<RoadStage> roadStageList) {
		// TODO Auto-generated method stub
		Map<String, Object> roadMap=new HashMap<>();
		for (RoadStage roadStage : roadStageList) {
			if(checkRoadMapIdExist(roadStage.getRoadId(),roadMap)) {
				List<RoadStage> rsList = (List<RoadStage>)roadMap.get("roadStage"+roadStage.getRoadId());
				rsList.add(roadStage);
			}
			else {
				List<RoadStage> rsList = new ArrayList<>();
				rsList.add(roadStage);
				roadMap.put("roadStage"+roadStage.getRoadId(), rsList);
			}
		}
		return roadMap;
	}

	@Override
	public boolean checkRoadMapIdExist(int roadId, Map<String, Object> roadMap) {
		// TODO Auto-generated method stub
		boolean flag=false;
		Set<String> keySet = roadMap.keySet();
		for (String key : keySet) {
			//System.out.println("key==="+key);
			if(("roadStage"+roadId).equals(key))
				flag=true;
		}
		return flag;
	}
}