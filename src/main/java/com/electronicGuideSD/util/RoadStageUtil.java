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
		//将游客到最近路线点的路段加到遍历的集合里
		childNavList.add(roadStage);
		String mtrBfFlag = roadStage.getBfFlag();
		
		Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
		System.out.println("sort==="+sort);
		rsList=(List<RoadStage>)roadStageMap.get("roadStage"+startRoadId);
		int itemIndex = RoadStageUtil.getListItemIndexBySort(rsList,sort);

		RoadStage rtspRoadStage = initRoadToSPNavLine(spToRoadMap,scenicPlaceX,scenicPlaceY);
		Float rtspBackX = rtspRoadStage.getBackX();
		Float rtspBackY = rtspRoadStage.getBackY();
		
		//if(rs.getBackThrough()) {//上面的查询条件里已经规定后方有路，这里就没必要判断了
		
		RoadStage rs = rsList.get(itemIndex);//获取游客进入导航线的第一个路段
		
		String bfFlag=mtrBfFlag;

		List<RoadStage> frontChildNavList = initFrontNavLine(childNavList,roadStage,rsList,rs,rtspRoadStage,bfFlag,itemIndex,rtspBackX,rtspBackY);

		List<RoadStage> backChildNavList = initBackNavLine(childNavList,roadStage,rsList,rs,rtspRoadStage,bfFlag,itemIndex,rtspBackX,rtspBackY);
		
		
		Map<String,Object> navLineMap=new HashMap<>();
		navLineMap.put("navLine", frontChildNavList);
		navLineMap.put("navLong", 1000);
		allNavList.add(navLineMap);

		navLineMap=new HashMap<>();
		navLineMap.put("navLine", backChildNavList);
		navLineMap.put("navLong", 800);
		allNavList.add(navLineMap);
		//}
		
		System.out.println("size1==="+((List<RoadStage>)allNavList.get(1).get("navLine")).size());
		return (List<RoadStage>)allNavList.get(1).get("navLine");
	}
	
	public static RoadStage initMeToRoadNavLine(Float meX, Float meY, Map<String,Object> meToRoadMap) {
		//以下代码是初始化从游客位置到最近路段点的
		RoadStage roadStage=new RoadStage();
		roadStage.setBackX(meX);
		roadStage.setBackY(meY);
		Float meFrontX = null;
		Float meFrontY = null;
		String mtrBfFlag = meToRoadMap.get("bfFlag").toString();
		if(RoadStage.BACK_FLAG.equals(mtrBfFlag)) {//判断离你最近的点是前方点还是后方点
			meFrontX = Float.valueOf(meToRoadMap.get("backX").toString());//这里虽然获得后面的点，但方向相反，相当于游客导航路线里前面的点
			meFrontY = Float.valueOf(meToRoadMap.get("backY").toString());
		}
		else if(RoadStage.FRONT_FLAG.equals(mtrBfFlag)) {
			meFrontX = Float.valueOf(meToRoadMap.get("frontX").toString());//这里虽然获得后面的点，但方向相反，相当于游客导航路线里前面的点
			meFrontY = Float.valueOf(meToRoadMap.get("frontY").toString());
		}
		
		roadStage.setFrontX(meFrontX);//将位置从你所在地引到离你最近的那个后方点处，这是就设置成路线的前方点了
		roadStage.setFrontY(meFrontY);
		roadStage.setBfFlag(mtrBfFlag);
		System.out.println("游客到最近的导航点路线="+meX+","+meY+","+meFrontX+","+meFrontY);
		return roadStage;
	}
	
	public static RoadStage initRoadToSPNavLine(Map<String,Object> spToRoadMap, Float scenicPlaceX, Float scenicPlaceY) {
		//以下代码是初始化从最近路段点到景点的路段
		RoadStage rtspRoadStage=new RoadStage();
		Float sptrBackX = null;
		Float sptrBackY = null;
		String sptrBfFlag = spToRoadMap.get("bfFlag").toString();
		if(RoadStage.BACK_FLAG.equals(sptrBfFlag)) {//判断离你最近的点是前方点还是后方点
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
	
	public static List<RoadStage> initFrontNavLine(List<RoadStage> childNavList, RoadStage roadStage,List<RoadStage> rsList, RoadStage rs, RoadStage rtspRoadStage, String bfFlag, int itemIndex, Float rtspBackX, Float rtspBackY) {
		boolean getSPFlag=false;
		List<RoadStage> frontChildNavList=new ArrayList<>();
		frontChildNavList.addAll(childNavList);//将待遍历的集合添加到向前遍历的集合里
		
		//顺着入口段的路段往前遍历，先把本路段加进去，为了方便下面的遍历
		RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,frontChildNavList,bfFlag);//先把第一个路段添加到向前遍历的集合里
		for(int i=itemIndex+1;i<rsList.size();i++) {
			System.out.println("i上==="+i);
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
		return frontChildNavList;
	}
	
	public static List<RoadStage> initBackNavLine(List<RoadStage> childNavList, RoadStage roadStage,List<RoadStage> rsList, RoadStage rs, RoadStage rtspRoadStage, String bfFlag, int itemIndex, Float rtspBackX, Float rtspBackY) {
		boolean getSPFlag=false;
		List<RoadStage> backChildNavList=new ArrayList<>();
		backChildNavList.addAll(childNavList);//将待遍历的集合添加到向后遍历的集合里
		
		//顺着入口段的路段往后遍历，先把本路段加进去，为了方便下面的遍历
		RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,backChildNavList,bfFlag);
		for(int i=itemIndex-1;i>=0;i--) {
			System.out.println("i下2==="+i);
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
				else
					RoadStageUtil.addRSNavInList(preRS.getBackX(),preRS.getBackY(),rs,backChildNavList,bfFlag);
			}
			System.out.println("bcnlSize="+backChildNavList.size());
		}
		return backChildNavList;
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
	 * 根据路段的排序号，获取该路段在该道路集合里的位置。排序号可能不连贯，不能保证排序号一定和位置一样，必须得调用此方法遍历获取一下
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
			System.out.println("与后方点相接");
			//生成的导航线方向可能和地图上路线的方向相反
			if(rs.getFrontThrough()) {//与后方点相接，就要判断前方点是否通
				Float rsFrontX = Float.valueOf(rs.getFrontX());//这里虽然获得前面的点，但方向相反，相当于游客导航路线里后面的点
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
			System.out.println("与前方点相接");
			if(rs.getBackThrough()) {//与前方点相接，就要判断后方点是否通
				Float rsFrontX = Float.valueOf(rs.getBackX());//若与前方点相接，这里导航线里前方的点就是路线里后方的点
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
			System.out.println("与后方点相接");
			flag=RoadStage.BACK_FLAG;
		}
		else if(x.equals(rs.getFrontX())&&y.equals(rs.getFrontY())) {
			System.out.println("与前方点相接");
			flag=RoadStage.FRONT_FLAG;
		}
		return flag;
	}
}
