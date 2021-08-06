package com.electronicGuideSD.util;

import java.util.List;

import com.electronicGuideSD.entity.RoadStage;

public class RoadStageUtil {

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
