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
	
	public static List<RoadStage> initAllNavRoadLine(Map<String, Object> roadStageMap,Map<String,Object> meToRoadMap,Map<String,Object> spToRoadMap, 
			Float meX, Float meY, Float scenicPlaceX, Float scenicPlaceY) {
		List<Map<String,Object>> allNavList=new ArrayList<>();
		List<RoadStage> childNavList=new ArrayList<>();
		List<RoadStage> rsList=null;
		boolean getSPFlag=false;
		Integer startRoadId = Integer.valueOf(meToRoadMap.get("roadId").toString());
		
		RoadStage roadStage=initMeToRoadNavLine(meX, meY, meToRoadMap);
		//���ο͵����·�ߵ��·�μӵ������ļ�����
		childNavList.add(roadStage);
		String mtrBfFlag = roadStage.getBfFlag();
		
		Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
		System.out.println("sort==="+sort);
		rsList=(List<RoadStage>)roadStageMap.get("roadStage"+startRoadId);
		int itemIndex = RoadStageUtil.getListItemIndexBySort(rsList,sort);

		RoadStage rtspRoadStage = initRoadToSPNavLine(spToRoadMap,scenicPlaceX,scenicPlaceY);
		Float sptrBackX = rtspRoadStage.getBackX();
		Float sptrBackY = rtspRoadStage.getBackY();
		
		//if(rs.getBackThrough()) {//����Ĳ�ѯ�������Ѿ��涨����·�������û��Ҫ�ж���
		List<RoadStage> upChildNavList=new ArrayList<>();
		upChildNavList.addAll(childNavList);//���������ļ�����ӵ���ǰ�����ļ�����
		List<RoadStage> downChildNavList=new ArrayList<>();
		downChildNavList.addAll(childNavList);//���������ļ�����ӵ��������ļ�����
		RoadStage preRS = null;
		RoadStage rs = rsList.get(itemIndex);//��ȡ�οͽ��뵼���ߵĵ�һ��·��
		
		String bfFlag=mtrBfFlag;

		//˳����ڶε�·����ǰ�������Ȱѱ�·�μӽ�ȥ��Ϊ�˷�������ı���
		RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,upChildNavList,bfFlag);//�Ȱѵ�һ��·����ӵ���ǰ�����ļ�����
		for(int i=itemIndex+1;i<rsList.size();i++) {
			System.out.println("i��==="+i);
			preRS = rsList.get(i-1);
			rs = rsList.get(i);
			//System.out.println("xx=="+preRS.getBackX()+",yy=="+preRS.getBackY()+",xxx=="+preRS.getFrontX());
			String preBfFlag = preRS.getBfFlag();
			if(RoadStage.BACK_FLAG.equals(preBfFlag)) {
				bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getFrontX(),preRS.getFrontY(),rs);
				if(sptrBackX.equals(preRS.getFrontX())&&sptrBackY.equals(preRS.getFrontY())) {
					getSPFlag=true;
					break;
				}
				else
					RoadStageUtil.addRSNavInList(preRS.getFrontX(),preRS.getFrontY(),rs,upChildNavList,bfFlag);
			}
			else if(RoadStage.FRONT_FLAG.equals(preBfFlag)) {
				bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getBackX(),preRS.getBackY(),rs);
				RoadStageUtil.addRSNavInList(preRS.getBackX(),preRS.getBackY(),rs,upChildNavList,bfFlag);
			}
			System.out.println("ucnlSize="+upChildNavList.size());
		}

		//˳����ڶε�·������������Ȱѱ�·�μӽ�ȥ��Ϊ�˷�������ı���
		RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,downChildNavList,bfFlag);
		for(int i=itemIndex-1;i>=0;i--) {
			System.out.println("i��2==="+i);
			preRS = rsList.get(i+1);
			rs = rsList.get(i);
			System.out.println("xx=="+preRS.getBackX()+",yy=="+preRS.getBackY()+",xxx=="+preRS.getFrontX());
			String preBfFlag = preRS.getBfFlag();
			if(RoadStage.BACK_FLAG.equals(preBfFlag)) {
				bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getFrontX(),preRS.getFrontY(),rs);
				if(sptrBackX.equals(preRS.getFrontX())&&sptrBackY.equals(preRS.getFrontY())) {
					getSPFlag=true;
					break;
				}
				else
					RoadStageUtil.addRSNavInList(preRS.getFrontX(),preRS.getFrontY(),rs,downChildNavList,bfFlag);
			}
			else if(RoadStage.FRONT_FLAG.equals(preBfFlag)) {
				bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getBackX(),preRS.getBackY(),rs);
				if(sptrBackX.equals(preRS.getBackX())&&sptrBackY.equals(preRS.getBackY())) {
					getSPFlag=true;
					downChildNavList.add(rtspRoadStage);
					break;
				}
				else
					RoadStageUtil.addRSNavInList(preRS.getBackX(),preRS.getBackY(),rs,downChildNavList,bfFlag);
			}
			System.out.println("dcnlSize="+downChildNavList.size());
		}
		
		Map<String,Object> navLineMap=new HashMap<>();
		navLineMap.put("navLine", upChildNavList);
		navLineMap.put("navLong", 1000);
		allNavList.add(navLineMap);

		navLineMap=new HashMap<>();
		navLineMap.put("navLine", downChildNavList);
		navLineMap.put("navLong", 800);
		allNavList.add(navLineMap);
		//}
		
		System.out.println("size1==="+((List<RoadStage>)allNavList.get(1).get("navLine")).size());
		return (List<RoadStage>)allNavList.get(1).get("navLine");
	}
	
	public static RoadStage initMeToRoadNavLine(Float meX, Float meY, Map<String,Object> meToRoadMap) {
		//���´����ǳ�ʼ�����ο�λ�õ����·�ε��
		RoadStage roadStage=new RoadStage();
		roadStage.setBackX(meX);
		roadStage.setBackY(meY);
		Float meFrontX = null;
		Float meFrontY = null;
		String mtrBfFlag = meToRoadMap.get("bfFlag").toString();
		if(RoadStage.BACK_FLAG.equals(mtrBfFlag)) {//�ж���������ĵ���ǰ���㻹�Ǻ󷽵�
			meFrontX = Float.valueOf(meToRoadMap.get("backX").toString());//������Ȼ��ú���ĵ㣬�������෴���൱���ο͵���·����ǰ��ĵ�
			meFrontY = Float.valueOf(meToRoadMap.get("backY").toString());
		}
		else if(RoadStage.FRONT_FLAG.equals(mtrBfFlag)) {
			meFrontX = Float.valueOf(meToRoadMap.get("frontX").toString());//������Ȼ��ú���ĵ㣬�������෴���൱���ο͵���·����ǰ��ĵ�
			meFrontY = Float.valueOf(meToRoadMap.get("frontY").toString());
		}
		
		roadStage.setFrontX(meFrontX);//��λ�ô������ڵ���������������Ǹ��󷽵㴦�����Ǿ����ó�·�ߵ�ǰ������
		roadStage.setFrontY(meFrontY);
		roadStage.setBfFlag(mtrBfFlag);
		System.out.println("�ο͵�����ĵ�����·��="+meX+","+meY+","+meFrontX+","+meFrontY);
		return roadStage;
	}
	
	public static RoadStage initRoadToSPNavLine(Map<String,Object> spToRoadMap, Float scenicPlaceX, Float scenicPlaceY) {
		//���´����ǳ�ʼ�������·�ε㵽�����·��
		RoadStage rtspRoadStage=new RoadStage();
		Float sptrBackX = null;
		Float sptrBackY = null;
		String sptrBfFlag = spToRoadMap.get("bfFlag").toString();
		if(RoadStage.BACK_FLAG.equals(sptrBfFlag)) {//�ж���������ĵ���ǰ���㻹�Ǻ󷽵�
			sptrBackX = Float.valueOf(spToRoadMap.get("backX").toString());
			sptrBackY = Float.valueOf(spToRoadMap.get("backY").toString());
		}
		else if(RoadStage.FRONT_FLAG.equals(sptrBfFlag)) {
			sptrBackX = Float.valueOf(spToRoadMap.get("frontX").toString());
			sptrBackY = Float.valueOf(spToRoadMap.get("frontY").toString());
		}
		System.out.println("sptrBackX==="+sptrBackX);
		System.out.println("sptrBackY==="+sptrBackY);
		rtspRoadStage.setBackX(sptrBackX);
		rtspRoadStage.setBackY(sptrBackY);
		rtspRoadStage.setFrontX(scenicPlaceX);
		rtspRoadStage.setFrontY(scenicPlaceY);
		return rtspRoadStage;
		////
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

	/**
	 * ����·�ε�����ţ���ȡ��·���ڸõ�·�������λ�á�����ſ��ܲ����ᣬ���ܱ�֤�����һ����λ��һ��������õ��ô˷���������ȡһ��
	 * @param rsList
	 * @param sort
	 * @return
	 */
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
