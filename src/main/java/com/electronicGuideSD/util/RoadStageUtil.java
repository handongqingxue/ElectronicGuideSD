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
	
	public static void addRSNavInList(Float frontX,Float frontY,RoadStage rs,List<RoadStage> allNavList) {
		System.out.println("front111==="+frontX+","+frontY);
		System.out.println("bx==="+rs.getBackX());
		System.out.println("by==="+rs.getBackY());
		if(frontX.equals(rs.getBackX())&&frontY.equals(rs.getBackY())) {
			System.out.println("��󷽵����");
			Float rsFrontX = Float.valueOf(rs.getFrontX());//������Ȼ���ǰ��ĵ㣬�������෴���൱���ο͵���·�������ĵ�
			Float rsFrontY = Float.valueOf(rs.getFrontY());
			RoadStage roadStage=new RoadStage();
			roadStage.setBackX(frontX);
			roadStage.setBackY(frontY);
			roadStage.setFrontX(rsFrontX);
			roadStage.setFrontY(rsFrontY);
			allNavList.add(roadStage);
		}
		else if(frontX.equals(rs.getFrontX())&&frontY.equals(rs.getFrontY())) {
			System.out.println("��ǰ�������");
		}
	}
}
