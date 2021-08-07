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
		List<RoadStage>[] allNavListArr=new ArrayList[100];
		List<RoadStage> rsList=null;
		Integer startRoadId = Integer.valueOf(meToRoadMap.get("roadId").toString());
		RoadStage roadStage=new RoadStage();
		roadStage.setBackX(meX);
		roadStage.setBackY(meY);
		String mtrBfFlag = meToRoadMap.get("bfFlag").toString();
		if(RoadStage.BACK_FLAG.equals(mtrBfFlag)) {//判断离你最近的点是前方点还是后方点
			Float frontX = Float.valueOf(meToRoadMap.get("backX").toString());//这里虽然获得后面的点，但方向相反，相当于游客导航路线里前面的点
			Float frontY = Float.valueOf(meToRoadMap.get("backY").toString());
			roadStage.setFrontX(frontX);//将位置从你所在地引到离你最近的那个后方点处，这是就设置成路线的前方点了
			roadStage.setFrontY(frontY);
			System.out.println("游客到最近的导航点路线="+meX+","+meY+","+frontX+","+frontY+","+startRoadId);
			Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
			
			rsList=(List<RoadStage>)roadStageMap.get("roadStage"+startRoadId);
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
			allNavListArr[0]=upChildNavList;
			allNavListArr[1]=downChildNavList;
		}
		else if(RoadStage.FRONT_FLAG.equals(mtrBfFlag)) {
			Float frontX = Float.valueOf(meToRoadMap.get("frontX").toString());//这里虽然获得后面的点，但方向相反，相当于游客导航路线里前面的点
			Float frontY = Float.valueOf(meToRoadMap.get("frontY").toString());
			roadStage.setFrontX(frontX);
			roadStage.setFrontY(frontY);

			Integer sort = Integer.valueOf(meToRoadMap.get("sort").toString());
			System.out.println("sort==="+sort);
			rsList=(List<RoadStage>)roadStageMap.get("roadStage"+startRoadId);
			int itemIndex = RoadStageUtil.getListItemIndexBySort(rsList,sort);

			List<RoadStage> childNavList=new ArrayList<>();
			//RoadStage rs = rsList.get(itemIndex);
			//String bfFlag = RoadStage.FRONT_FLAG;
			childNavList.add(roadStage);
			/*
			if(RoadStage.BACK_FLAG.equals(bfFlag)) {
				List<RoadStage> upChildNavList=new ArrayList<>();
				upChildNavList.add(roadStage);
				//RoadStageUtil.addRSNavInList(frontX,frontY,rs,upChildNavList,bfFlag);
			}
			else if(RoadStage.FRONT_FLAG.equals(bfFlag)) {
			*/
				//if(rs.getBackThrough()) {
					List<RoadStage> upChildNavList=new ArrayList<>();
					upChildNavList.addAll(childNavList);
					List<RoadStage> downChildNavList=new ArrayList<>();
					downChildNavList.addAll(childNavList);
					RoadStage preRS = null;
					RoadStage rs = rsList.get(itemIndex);
					String bfFlag=mtrBfFlag;
					
					RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,upChildNavList,bfFlag);
					for(int i=itemIndex+1;i<rsList.size();i++) {
						System.out.println("i上==="+i);
						preRS = rsList.get(i-1);
						rs = rsList.get(i);
						System.out.println("xx=="+preRS.getBackX()+",yy=="+preRS.getBackY()+",xxx=="+preRS.getFrontX());
						String preBfFlag = preRS.getBfFlag();
						if(RoadStage.BACK_FLAG.equals(preBfFlag)) {
							bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getFrontX(),preRS.getFrontY(),rs);
							RoadStageUtil.addRSNavInList(preRS.getFrontX(),preRS.getFrontY(),rs,upChildNavList,bfFlag);
						}
						else if(RoadStage.FRONT_FLAG.equals(preBfFlag)) {
							bfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getBackX(),preRS.getBackY(),rs);
							RoadStageUtil.addRSNavInList(preRS.getBackX(),preRS.getBackY(),rs,upChildNavList,bfFlag);
						}
						System.out.println("ucnlSize="+upChildNavList.size());
					}

					RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,downChildNavList,bfFlag);
					for(int i=itemIndex-1;i>=0;i--) {
						System.out.println("i下2==="+i);
						preRS = rsList.get(i+1);
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
					allNavListArr[0]=upChildNavList;
					allNavListArr[1]=downChildNavList;
				//}
			//}
		}
		System.out.println("size1==="+allNavListArr[1].size());
		return allNavListArr[1];
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
