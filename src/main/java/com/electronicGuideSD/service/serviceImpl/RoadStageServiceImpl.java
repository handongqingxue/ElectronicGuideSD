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
		return allNavList;
	}
	
	public List<RoadStage> initAllNavRoadLine(Map<String, Object> roadStageMap,Map<String,Object> meToRoadMap, Float meX, Float meY) {
		List<List<RoadStage>> allNavList=new ArrayList<>();
		List<RoadStage> rsList=null;
		Integer startRoadId = Integer.valueOf(meToRoadMap.get("roadId").toString());
		RoadStage roadStage=new RoadStage();
		roadStage.setBackX(meX);
		roadStage.setBackY(meY);
		if(RoadStage.BACK_FLAG.equals(meToRoadMap.get("bfFlag").toString())) {//判断离你最近的点是前方点还是后方点
			Float frontX = Float.valueOf(meToRoadMap.get("backX").toString());//这里虽然获得后面的点，但方向相反，相当于游客导航路线里前面的点
			Float frontY = Float.valueOf(meToRoadMap.get("backY").toString());
			roadStage.setFrontX(frontX);//将位置从你所在地引到离你最近的那个后方点处，这是就设置成路线的前方点了
			roadStage.setFrontY(frontY);
			System.out.println("oooooooo="+meX+","+meY+","+frontX+","+frontY+","+startRoadId);
			Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
			
			rsList=(List<RoadStage>)roadStageMap.get("roadStage"+meToRoadMap.get("roadId").toString());
			int itemIndex = RoadStageUtil.getListItemIndexBySort(rsList,sort);

			List<RoadStage> upChildNavList=new ArrayList<>();
			upChildNavList.add(roadStage);
			for(int i=itemIndex;i<rsList.size();i++) {
				System.out.println("i上==="+i);
				RoadStage rs = rsList.get(i);
				//RoadStageUtil.addRSNavInList(frontX,frontY,rs,upChildNavList);
				//Integer roadId = Integer.valueOf(meToRoadMap.get("roadId").toString());
				//System.out.println("11111111111="+frontX+","+frontY+","+rsFrontX+","+rsFrontY+","+roadId);
			}
			List<RoadStage> downChildNavList=new ArrayList<>();
			downChildNavList.add(roadStage);
			for(int i=itemIndex-1;i>=0;i--) {
				System.out.println("i下1==="+i);
				RoadStage rs = rsList.get(i);
				//RoadStageUtil.addRSNavInList(frontX,frontY,rs,downChildNavList);
			}
			allNavList.add(upChildNavList);
			allNavList.add(downChildNavList);
		}
		else if(RoadStage.FRONT_FLAG.equals(meToRoadMap.get("bfFlag").toString())) {
			Float frontX = Float.valueOf(meToRoadMap.get("frontX").toString());//这里虽然获得后面的点，但方向相反，相当于游客导航路线里前面的点
			Float frontY = Float.valueOf(meToRoadMap.get("frontY").toString());
			roadStage.setFrontX(frontX);
			roadStage.setFrontY(frontY);

			Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
			System.out.println("sort==="+sort);
			rsList=(List<RoadStage>)roadStageMap.get("roadStage"+meToRoadMap.get("roadId").toString());
			int itemIndex = RoadStageUtil.getListItemIndexBySort(rsList,sort);

			List<RoadStage> childNavList=new ArrayList<>();
			//RoadStage rs = rsList.get(itemIndex);
			String bfFlag = RoadStage.FRONT_FLAG;
			childNavList.add(roadStage);
			if(RoadStage.BACK_FLAG.equals(bfFlag)) {
				List<RoadStage> upChildNavList=new ArrayList<>();
				upChildNavList.add(roadStage);
				//RoadStageUtil.addRSNavInList(frontX,frontY,rs,upChildNavList,bfFlag);
			}
			else if(RoadStage.FRONT_FLAG.equals(bfFlag)) {
				//if(rs.getBackThrough()) {
					List<RoadStage> downChildNavList=new ArrayList<>();
					downChildNavList=childNavList;
					RoadStage rs = rsList.get(itemIndex);
					RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,downChildNavList,bfFlag);
					for(int i=itemIndex-1;i>=0;i--) {
						System.out.println("i下2==="+i);
						RoadStage preRS = rsList.get(i+1);
						rs = rsList.get(i);
						System.out.println("xx=="+preRS.getBackX()+",yy=="+preRS.getBackY()+",xxx=="+preRS.getFrontX());
						String preBfFlag = preRS.getBfFlag();
						if(RoadStage.BACK_FLAG.equals(preBfFlag)) {
							bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getFrontX(),preRS.getFrontY(),rs);
							RoadStageUtil.addRSNavInList(preRS.getFrontX(),preRS.getFrontY(),rs,downChildNavList,bfFlag);
						}
						else if(RoadStage.FRONT_FLAG.equals(preBfFlag)) {
							bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getBackX(),preRS.getBackY(),rs);
							RoadStageUtil.addRSNavInList(preRS.getBackX(),preRS.getBackY(),rs,downChildNavList,bfFlag);
						}
						System.out.println("dcnlSize="+downChildNavList.size());
					}
					allNavList.add(downChildNavList);
				//}
			}
			
			/*
			for(int i=itemIndex;i<rsList.size();i++) {
				System.out.println("i上==="+i);
				rs = rsList.get(i);
				bfFlag = null;
				if(i==itemIndex) {
					bfFlag = RoadStageUtil.checkConnectBackOrFront(frontX,frontY,rs);
					RoadStageUtil.addRSNavInList(frontX,frontY,rs,upChildNavList,bfFlag);
				}
				else {
					RoadStage preRS = rsList.get(i-1);
					bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getFrontX(),preRS.getFrontY(),rs);
					RoadStageUtil.addRSNavInList(preRS.getFrontX(),preRS.getFrontY(),rs,upChildNavList,bfFlag);
				}
			}
			List<RoadStage> downChildNavList=new ArrayList<>();
			downChildNavList.add(roadStage);
			for(int i=itemIndex-1;i>=0;i--) {
				System.out.println("i下2==="+i);
				RoadStage rs = rsList.get(i);
				String bfFlag = null;
				if(i==itemIndex-1) {
					bfFlag = RoadStageUtil.checkConnectBackOrFront(frontX,frontY,rs);
					RoadStageUtil.addRSNavInList(frontX,frontY,rs,downChildNavList,bfFlag);
				}
				else {
					RoadStage preRS = rsList.get(i);
					System.out.println("xx=="+preRS.getBackX()+",yy=="+preRS.getBackY()+",xxx=="+preRS.getFrontX());
					bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getFrontX(),preRS.getFrontY(),rs);
					RoadStageUtil.addRSNavInList(preRS.getFrontX(),preRS.getFrontY(),rs,downChildNavList,bfFlag);
				}
				System.out.println("dcnlSize="+downChildNavList.size());
			}
			allNavList.add(upChildNavList);
			*/
		}
		System.out.println("size1==="+allNavList.get(0).size());
		return allNavList.get(0);
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
