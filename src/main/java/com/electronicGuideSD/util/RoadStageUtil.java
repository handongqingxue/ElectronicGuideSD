package com.electronicGuideSD.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.electronicGuideSD.entity.RoadStage;

public class RoadStageUtil {
	
	public static Map<String, Object> initAllRoadMap(List<RoadStage> roadStageList) {
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
	
	public static List<RoadStage> initAllNavRoadLine(Map<String, Object> roadStageMap,Map<String,Object> meToRoadMap, Float meX, Float meY) {
		List<List<RoadStage>> allNavList=new ArrayList<>();
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
			System.out.println("�ο͵�����ĵ�����·��="+meX+","+meY+","+frontX+","+frontY+","+startRoadId);
			Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
			
			rsList=(List<RoadStage>)roadStageMap.get("roadStage"+startRoadId);
			int itemIndex = RoadStageUtil.getListItemIndexBySort(rsList,sort);

			List<RoadStage> upChildNavList=new ArrayList<>();
			upChildNavList.add(roadStage);
			for(int i=itemIndex;i<rsList.size();i++) {
				System.out.println("i��==="+i);
				RoadStage rs = rsList.get(i);
				//RoadStageUtil.addRSNavInList(frontX,frontY,rs,upChildNavList);
				//Integer roadId = Integer.valueOf(meToRoadMap.get("roadId").toString());
				//System.out.println("11111111111="+frontX+","+frontY+","+rsFrontX+","+rsFrontY+","+roadId);
			}
			List<RoadStage> downChildNavList=new ArrayList<>();
			downChildNavList.add(roadStage);
			for(int i=itemIndex-1;i>=0;i--) {
				System.out.println("i��1==="+i);
				RoadStage rs = rsList.get(i);
				//RoadStageUtil.addRSNavInList(frontX,frontY,rs,downChildNavList);
			}
			allNavList.add(upChildNavList);
			allNavList.add(downChildNavList);
		}
		else if(RoadStage.FRONT_FLAG.equals(meToRoadMap.get("bfFlag").toString())) {
			Float frontX = Float.valueOf(meToRoadMap.get("frontX").toString());//������Ȼ��ú���ĵ㣬�������෴���൱���ο͵���·����ǰ��ĵ�
			Float frontY = Float.valueOf(meToRoadMap.get("frontY").toString());
			roadStage.setFrontX(frontX);
			roadStage.setFrontY(frontY);

			Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
			System.out.println("sort==="+sort);
			rsList=(List<RoadStage>)roadStageMap.get("roadStage"+startRoadId);
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
						System.out.println("i��2==="+i);
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
				System.out.println("i��==="+i);
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
				System.out.println("i��2==="+i);
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
	
	public static boolean checkRoadMapIdExist(int roadId, Map<String, Object> roadMap) {
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

	public static int getListItemIndexBySort(List<RoadStage> rsList, Integer sort) {
		// TODO Auto-generated method stub
		int index=-1;
		for(int i=0;i<rsList.size();i++) {
			RoadStage rs = rsList.get(i);
			if(sort==rs.getSort()) {
				System.out.println("i==="+i);
				index=i;
				break;
			}
		}
		return index;
	}
	
	public static void addRSNavInList(Float x,Float y,RoadStage rs,List<RoadStage> navList,String bfFlag) {
		System.out.println("front111==="+x+","+y);
		System.out.println("bx==="+rs.getBackX());
		System.out.println("by==="+rs.getBackY());
		RoadStage roadStage=new RoadStage();
		if(RoadStage.BACK_FLAG.equals(bfFlag)) {
			System.out.println("��󷽵����");
			//���ɵĵ����߷�����ܺ͵�ͼ��·�ߵķ����෴
			if(rs.getFrontThrough()) {//��󷽵���ӣ���Ҫ�ж�ǰ�����Ƿ�ͨ
				Float rsFrontX = Float.valueOf(rs.getFrontX());//������Ȼ���ǰ��ĵ㣬�������෴���൱���ο͵���·�������ĵ�
				Float rsFrontY = Float.valueOf(rs.getFrontY());
				System.out.println("x="+x+",y="+y+",rsFrontX="+rsFrontX+",rsFrontY="+rsFrontY);
				roadStage.setBackX(x);
				roadStage.setBackY(y);
				roadStage.setFrontX(rsFrontX);
				roadStage.setFrontY(rsFrontY);
				rs.setBfFlag(bfFlag);
				navList.add(roadStage);
			}
		}
		else if(RoadStage.FRONT_FLAG.equals(bfFlag)) {
			System.out.println("��ǰ�������");
			if(rs.getBackThrough()) {//��ǰ������ӣ���Ҫ�жϺ󷽵��Ƿ�ͨ
				Float rsFrontX = Float.valueOf(rs.getBackX());//����ǰ������ӣ����ﵼ������ǰ���ĵ����·����󷽵ĵ�
				Float rsFrontY = Float.valueOf(rs.getBackY());
				System.out.println("x="+x+",y="+y+",rsFrontX="+rsFrontX+",rsFrontY="+rsFrontY);
				roadStage.setBackX(x);
				roadStage.setBackY(y);
				roadStage.setFrontX(rsFrontX);
				roadStage.setFrontY(rsFrontY);
				rs.setBfFlag(bfFlag);
				navList.add(roadStage);
			}
		}
	}
	
	public static String checkConnectBackOrFront(Float x,Float y,RoadStage rs) {
		String flag=null;
		if(x.equals(rs.getBackX())&&y.equals(rs.getBackY())) {
			System.out.println("��󷽵����");
			flag=RoadStage.BACK_FLAG;
		}
		else if(x.equals(rs.getFrontX())&&y.equals(rs.getFrontY())) {
			System.out.println("��ǰ�������");
			flag=RoadStage.FRONT_FLAG;
		}
		return flag;
	}
}
