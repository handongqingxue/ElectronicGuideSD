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
		Float rtspBackX = rtspRoadStage.getBackX();
		Float rtspBackY = rtspRoadStage.getBackY();
		
		initNavLineFromItemIndex(roadStageMap,childNavList,roadStage,rsList,rtspRoadStage,mtrBfFlag,itemIndex,rtspBackX,rtspBackY,allNavList);
		
		List<RoadStage> shortNavLine=initGetSPShortNavLine(allNavList);
		//System.out.println("size1==="+((List<RoadStage>)allNavList.get(3).get("navLine")).size());
		System.out.println("size1==="+shortNavLine.size());
		return shortNavLine;
	}
	
	public static List<RoadStage> initGetSPShortNavLine(List<Map<String,Object>> allNavList) {
		Float shortDistance=(float) 9999999;
		Map<String,Object> shortNavMap=null;
		for (Map<String,Object> allNavMap : allNavList) {
			Boolean getSPFlag = Boolean.valueOf(allNavMap.get("getSPFlag").toString());
			System.out.println("getSPFlag="+getSPFlag);
			if(getSPFlag) {
				Float navLong = Float.valueOf(allNavMap.get("navLong").toString());
				if(navLong<shortDistance) {
					shortDistance=navLong;
					shortNavMap=allNavMap;
				}
			}
		}
		System.out.println("shortNavMap="+shortNavMap.get("navLong"));
		return (List<RoadStage>)shortNavMap.get("navLine");
	}
	
	public static void initNavLineFromItemIndex(Map<String, Object> roadStageMap,List<RoadStage> childNavList,RoadStage roadStage,List<RoadStage> rsList,RoadStage rtspRoadStage,String bfFlag,int itemIndex,Float rtspBackX,Float rtspBackY,List<Map<String,Object>> allNavList) {
		//if(rs.getBackThrough()) {//����Ĳ�ѯ�������Ѿ��涨����·�������û��Ҫ�ж���
		
		RoadStage rs = rsList.get(itemIndex);//��ȡ�οͽ��뵼���ߵĵ�һ��·��
		
		Map<String,Object> frontNavLineMap = initFrontNavLine(roadStageMap,childNavList,roadStage,rsList,rs,rtspRoadStage,bfFlag,itemIndex,rtspBackX,rtspBackY,allNavList);

		Map<String,Object> backNavLineMap = initBackNavLine(roadStageMap,childNavList,roadStage,rsList,rs,rtspRoadStage,bfFlag,itemIndex,rtspBackX,rtspBackY,allNavList);
		
		allNavList.add(frontNavLineMap);
		allNavList.add(backNavLineMap);
		//}
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
	
	public static Map<String,Object> initFrontNavLine(Map<String, Object> roadStageMap, List<RoadStage> childNavList, RoadStage roadStage,List<RoadStage> rsList, RoadStage rs, RoadStage rtspRoadStage, String bfFlag, int itemIndex, Float rtspBackX, Float rtspBackY,List<Map<String,Object>> allNavList) {
		boolean getSPFlag=false;
		List<RoadStage> frontChildNavList=new ArrayList<>();
		frontChildNavList.addAll(childNavList);//���������ļ�����ӵ���ǰ�����ļ�����
		
		//˳����ڶε�·����ǰ�������Ȱѱ�·�μӽ�ȥ��Ϊ�˷�������ı���
		RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,frontChildNavList,bfFlag);//�Ȱѵ�һ��·����ӵ���ǰ�����ļ�����
		for(int i=itemIndex+1;i<rsList.size();i++) {
			System.out.println("i��==="+i);
			RoadStage preRS = rsList.get(i-1);
			rs = rsList.get(i);
			//System.out.println("xx=="+preRS.getBackX()+",yy=="+preRS.getBackY()+",xxx=="+preRS.getFrontX());
			String preBfFlag = preRS.getBfFlag();
			if(RoadStage.BACK_FLAG.equals(preBfFlag)) {
				bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getFrontX(),preRS.getFrontY(),rs);
				if(rtspBackX.equals(preRS.getFrontX())&&rtspBackY.equals(preRS.getFrontY())) {
					getSPFlag=true;
					frontChildNavList.add(rtspRoadStage);
					break;
				}
				else
					RoadStageUtil.addRSNavInList(preRS.getFrontX(),preRS.getFrontY(),rs,frontChildNavList,bfFlag);
			}
			else if(RoadStage.FRONT_FLAG.equals(preBfFlag)) {
				bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getBackX(),preRS.getBackY(),rs);
				if(rtspBackX.equals(preRS.getBackX())&&rtspBackY.equals(preRS.getBackY())) {
					getSPFlag=true;
					frontChildNavList.add(rtspRoadStage);
					break;
				}
				else
					RoadStageUtil.addRSNavInList(preRS.getBackX(),preRS.getBackY(),rs,frontChildNavList,bfFlag);
			}
			System.out.println("fcnlSize="+frontChildNavList.size());
		}
		
		Map<String,Object> navLineMap=new HashMap<>();
		navLineMap.put("navLine", frontChildNavList);
		navLineMap.put("getSPFlag", getSPFlag);
		navLineMap.put("navLong", 1000);
		return navLineMap;
	}
	
	public static Map<String,Object> initBackNavLine(Map<String, Object> roadStageMap, List<RoadStage> childNavList, RoadStage roadStage,List<RoadStage> rsList, RoadStage rs, RoadStage rtspRoadStage, String bfFlag, int itemIndex, Float rtspBackX, Float rtspBackY, List<Map<String,Object>> allNavList) {
		boolean getSPFlag=false;
		List<RoadStage> backChildNavList=new ArrayList<>();
		backChildNavList.addAll(childNavList);//���������ļ�����ӵ��������ļ�����
		
		//˳����ڶε�·������������Ȱѱ�·�μӽ�ȥ��Ϊ�˷�������ı���
		RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,backChildNavList,bfFlag);
		for(int i=itemIndex-1;i>=0;i--) {
			System.out.println("i��2==="+i);
			RoadStage preRS = rsList.get(i+1);
			rs = rsList.get(i);
			System.out.println("xx=="+preRS.getBackX()+",yy=="+preRS.getBackY()+",xxx=="+preRS.getFrontX());
			String preBfFlag = preRS.getBfFlag();
			if(RoadStage.BACK_FLAG.equals(preBfFlag)) {
				bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getFrontX(),preRS.getFrontY(),rs);
				if(rtspBackX.equals(preRS.getFrontX())&&rtspBackY.equals(preRS.getFrontY())) {
					getSPFlag=true;
					backChildNavList.add(rtspRoadStage);
					break;
				}
				else
					RoadStageUtil.addRSNavInList(preRS.getFrontX(),preRS.getFrontY(),rs,backChildNavList,bfFlag);
			}
			else if(RoadStage.FRONT_FLAG.equals(preBfFlag)) {
				bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getBackX(),preRS.getBackY(),rs);
				if(rtspBackX.equals(preRS.getBackX())&&rtspBackY.equals(preRS.getBackY())) {
					getSPFlag=true;
					backChildNavList.add(rtspRoadStage);
					break;
				}
				else {
					if(rs.getBackIsCross()) {
						String rsIds = rs.getBackCrossRSIds();
						System.out.println("�����н����="+rsIds);
						String[] rsIdArr = rsIds.split(",");
						for (String rsId : rsIdArr) {
							List<RoadStage> fenZhiRsList = (List<RoadStage>)roadStageMap.get("roadStage"+rsId);
							int fenZhiItemIndex=getListItemIndexByLocation(fenZhiRsList,rs.getBackX(),rs.getBackY(),RoadStage.BACK_FLAG);
							System.out.println("fenZhiItemIndex="+fenZhiItemIndex);
							//fenZhiRsList.get(0)
							initNavLineFromItemIndex(roadStageMap,backChildNavList,rs,fenZhiRsList,rtspRoadStage,rs.getBfFlag(),fenZhiItemIndex,rtspBackX,rtspBackY,allNavList);
						}
					}
					RoadStageUtil.addRSNavInList(preRS.getBackX(),preRS.getBackY(),rs,backChildNavList,bfFlag);
				}
			}
			System.out.println("bcnlSize="+backChildNavList.size());
		}

		Map<String,Object> navLineMap=new HashMap<>();
		navLineMap.put("navLine", backChildNavList);
		navLineMap.put("getSPFlag", getSPFlag);
		navLineMap.put("navLong", 800);
		return navLineMap;
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
	
	public static int getListItemIndexByLocation(List<RoadStage> rsList, Float x, Float y, String bfFlag) {
		int index=-1;
		for(int i=0;i<rsList.size();i++) {
			RoadStage rs = rsList.get(i);
			if(RoadStage.BACK_FLAG.equals(bfFlag)) {
				Float frontX = rs.getFrontX();
				Float frontY = rs.getFrontY();
				if(x.equals(frontX)&&y.equals(frontY)) {
					index=i;
					break;
				}
			}
			else if(RoadStage.FRONT_FLAG.equals(bfFlag)) {
				Float backX = rs.getBackX();
				Float backY = rs.getBackY();
				if(x.equals(backX)&&y.equals(backY)) {
					index=i;
					break;
				}
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
