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
	
	/**
	 * 初始化所有导航路线
	 * @param roadStageMap
	 * @param meToRoadMap
	 * @param spToRoadMap
	 * @param meX
	 * @param meY
	 * @param scenicPlaceX
	 * @param scenicPlaceY
	 * @return
	 */
	public static List<Map<String,Object>> initAllNavRoadLine(Map<String, Object> allRoadStageMap,Map<String,Object> mtrNearRSMap,Map<String,Object> spToRoadMap, 
			Float meX, Float meY, Float scenicPlaceX, Float scenicPlaceY) {
		List<Map<String,Object>> allNavList=new ArrayList<>();
		List<RoadStage> childNavList=new ArrayList<>();
		List<RoadStage> startRSList=null;
		float distance=0;
		Integer startRoadId = Integer.valueOf(mtrNearRSMap.get("roadId").toString());//游客到最近路段的路线所在道路id
		
		RoadStage meToRoadStage=initMeToRoadNavLine(meX, meY, mtrNearRSMap);
		//将游客到最近路线点的路段加到遍历的集合里
		childNavList.add(meToRoadStage);
		String mtrBfFlag = meToRoadStage.getPreBfFlag();

		RoadStage rtspRoadStage = initRoadToSPNavLine(spToRoadMap,scenicPlaceX,scenicPlaceY);
		Float rtspBackX = rtspRoadStage.getBackX();
		Float rtspBackY = rtspRoadStage.getBackY();
		
		if(meToRoadStage.getFrontX().equals(rtspBackX)&&meToRoadStage.getFrontY().equals(rtspBackY))
			initNavLineDirect(meToRoadStage,rtspRoadStage,allNavList);
		else {
			Integer sort = Integer.valueOf(mtrNearRSMap.get("sort").toString());
			System.out.println("sort==="+sort);
			startRSList=(List<RoadStage>)allRoadStageMap.get("roadStage"+startRoadId);
			int itemIndex = RoadStageUtil.getListItemIndexBySort(startRSList,sort);
			
			initNavLineFromItemIndex(allRoadStageMap,childNavList,meToRoadStage,startRSList,rtspRoadStage,mtrBfFlag,itemIndex,rtspBackX,rtspBackY,allNavList,distance);
		}
		
		return allNavList;
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
		
		if(shortNavMap==null) {
			return null;
		}
		else {
			System.out.println("shortNavMap="+shortNavMap.get("navLong"));
			return (List<RoadStage>)shortNavMap.get("navLine");
		}
	}
	
	public static void initNavLineFromItemIndex(Map<String, Object> allRoadStageMap,List<RoadStage> childNavList,RoadStage roadStage,List<RoadStage> rsList,RoadStage rtspRoadStage,String preBfFlag,int itemIndex,Float rtspBackX,Float rtspBackY,List<Map<String,Object>> allNavList,float preDistance) {
		//if(rs.getBackThrough()) {//上面的查询条件里已经规定后方有路，这里就没必要判断了
		
		RoadStage rs = rsList.get(itemIndex);//获取游客进入导航线的第一个路段
		
		Map<String,Object> frontNavLineMap = initFrontNavLine(allRoadStageMap,childNavList,roadStage,rsList,rs,rtspRoadStage,preBfFlag,itemIndex,rtspBackX,rtspBackY,allNavList,preDistance);

		Map<String,Object> backNavLineMap = initBackNavLine(allRoadStageMap,childNavList,roadStage,rsList,rs,rtspRoadStage,preBfFlag,itemIndex,rtspBackX,rtspBackY,allNavList,preDistance);
		
		allNavList.add(frontNavLineMap);
		allNavList.add(backNavLineMap);
		//}
	}
	
	/**
	 * 若离游客最近的路段点与离景区最近的路段点是同一个点，可以调用此方法直接导航过去
	 * @param meToRoadStage
	 * @param rtspRoadStage
	 * @param allNavList
	 */
	public static void initNavLineDirect(RoadStage meToRoadStage,RoadStage rtspRoadStage,List<Map<String,Object>> allNavList) {
		List<RoadStage> childNavList=new ArrayList<>();
		childNavList.add(meToRoadStage);
		childNavList.add(rtspRoadStage);
		
		Map<String,Object> navLineMap=new HashMap<>();
		navLineMap.put("navLine", childNavList);
		navLineMap.put("getSPFlag", true);
		navLineMap.put("navLong", meToRoadStage.getDistance()+rtspRoadStage.getDistance());
		allNavList.add(navLineMap);
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
			meFrontX = Float.valueOf(meToRoadMap.get("frontX").toString());
			meFrontY = Float.valueOf(meToRoadMap.get("frontY").toString());
		}
		
		roadStage.setFrontX(meFrontX);//将位置从你所在地引到离你最近的那个后方点处，这是就设置成路线的前方点了
		roadStage.setFrontY(meFrontY);
		roadStage.setDistance(RoadStageUtil.jiSuanDistance(meX,meY,meFrontX,meFrontY));
		roadStage.setPreBfFlag(mtrBfFlag);
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
		rtspRoadStage.setDistance(RoadStageUtil.jiSuanDistance(sptrBackX,sptrBackY,scenicPlaceX,scenicPlaceY));
		return rtspRoadStage;
		////
	}
	
	public static Map<String,Object> initFrontNavLine(Map<String, Object> roadStageMap, List<RoadStage> childNavList, RoadStage preRoadStage,List<RoadStage> rsList, RoadStage currentRS, RoadStage rtspRoadStage, String preBfFlag, int itemIndex, Float rtspBackX, Float rtspBackY,List<Map<String,Object>> allNavList,float preDistance) {
		boolean getSPFlag=false;
		float distance=preDistance;
		List<RoadStage> frontChildNavList=new ArrayList<>();
		frontChildNavList.addAll(childNavList);//将待遍历的集合添加到向前遍历的集合里
		
		//顺着入口段的路段往前遍历，先把本路段加进去，为了方便下面的遍历
		if(RoadStage.BACK_FLAG.equals(preBfFlag)) {
			RoadStage nextRS = rsList.get(itemIndex+1);
			String nextBfFlag = RoadStageUtil.checkConnectBackOrFront(currentRS.getBackX(),currentRS.getBackY(),nextRS);
			System.out.println("nextBfFlag==="+nextBfFlag);
			if(RoadStage.BACK_FLAG.equals(nextBfFlag)) {
				RoadStageUtil.addNextConnectRSNavInList(nextRS,frontChildNavList,preBfFlag);//先把第一个路段添加到向前遍历的集合里
				itemIndex++;
			}
			else {
				RoadStageUtil.addRSNavInList(preRoadStage.getFrontX(),preRoadStage.getFrontY(),currentRS,frontChildNavList,preBfFlag);//先把第一个路段添加到向前遍历的集合里
			}
		}
		else if(RoadStage.FRONT_FLAG.equals(preBfFlag)) {
			if(itemIndex+1<rsList.size()) {
				RoadStage nextRS = rsList.get(itemIndex+1);
				String nextBfFlag = RoadStageUtil.checkConnectBackOrFront(currentRS.getFrontX(),currentRS.getFrontY(),nextRS);
				System.out.println("nextBfFlag==="+nextBfFlag);
				if(RoadStage.FRONT_FLAG.equals(nextBfFlag)) {
					RoadStageUtil.addNextConnectRSNavInList(nextRS,frontChildNavList,preBfFlag);//先把第一个路段添加到向前遍历的集合里
					itemIndex++;
				}
				else {
					RoadStageUtil.addRSNavInList(preRoadStage.getFrontX(),preRoadStage.getFrontY(),currentRS,frontChildNavList,preBfFlag);//先把第一个路段添加到向前遍历的集合里
				}
			}
		}
		
		for(int i=itemIndex+1;i<rsList.size();i++) {
			System.out.println("i上==="+i);
			RoadStage preRS = rsList.get(i-1);
			currentRS = rsList.get(i);
			//System.out.println("xx=="+preRS.getBackX()+",yy=="+preRS.getBackY()+",xxx=="+preRS.getFrontX());
			//String preBfFlag = preRS.getPreBfFlag();
			if(RoadStage.BACK_FLAG.equals(preBfFlag)) {
				preBfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getFrontX(),preRS.getFrontY(),currentRS);
				if(rtspBackX.equals(preRS.getFrontX())&&rtspBackY.equals(preRS.getFrontY())) {
					getSPFlag=true;
					frontChildNavList.add(rtspRoadStage);
					break;
				}
				else
					RoadStageUtil.addRSNavInList(preRS.getFrontX(),preRS.getFrontY(),currentRS,frontChildNavList,preBfFlag);
			}
			else if(RoadStage.FRONT_FLAG.equals(preBfFlag)) {
				preBfFlag = RoadStageUtil.checkConnectBackOrFront(preRS.getBackX(),preRS.getBackY(),currentRS);
				if(rtspBackX.equals(preRS.getBackX())&&rtspBackY.equals(preRS.getBackY())) {
					getSPFlag=true;
					frontChildNavList.add(rtspRoadStage);
					break;
				}
				else
					RoadStageUtil.addRSNavInList(preRS.getBackX(),preRS.getBackY(),currentRS,frontChildNavList,preBfFlag);
			}
			System.out.println("fcnlSize="+frontChildNavList.size());
		}
		
		System.out.println("getSPFlag==="+getSPFlag);
		Map<String,Object> navLineMap=new HashMap<>();
		navLineMap.put("navLine", frontChildNavList);
		navLineMap.put("getSPFlag", getSPFlag);
		navLineMap.put("navLong", jiSuanNavLineDistance(frontChildNavList));
		return navLineMap;
	}
	
	public static Map<String,Object> initBackNavLine(Map<String, Object> roadStageMap, List<RoadStage> childNavList, RoadStage roadStage,List<RoadStage> rsList, RoadStage rs, RoadStage rtspRoadStage, String bfFlag, int itemIndex, Float rtspBackX, Float rtspBackY, List<Map<String,Object>> allNavList,float preDistance) {
		boolean getSPFlag=false;
		float distance=preDistance;
		List<RoadStage> backChildNavList=new ArrayList<>();
		backChildNavList.addAll(childNavList);//将待遍历的集合添加到向后遍历的集合里
		
		//顺着入口段的路段往后遍历，先把本路段加进去，为了方便下面的遍历
		RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,backChildNavList,bfFlag);
		for(int i=itemIndex-1;i>=0;i--) {
			System.out.println("i下2==="+i);
			RoadStage preRS = rsList.get(i+1);
			rs = rsList.get(i);
			String preBfFlag = preRS.getPreBfFlag();
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
						System.out.println("这里有交叉口="+rsIds);
						String[] rsIdArr = rsIds.split(",");
						for (String rsId : rsIdArr) {
							List<RoadStage> fenZhiRsList = (List<RoadStage>)roadStageMap.get("roadStage"+rsId);
							int fenZhiItemIndex=getListItemIndexByLocation(fenZhiRsList,rs.getBackX(),rs.getBackY(),RoadStage.BACK_FLAG);
							System.out.println("fenZhiItemIndex="+fenZhiItemIndex);
							//fenZhiRsList.get(0)
							initNavLineFromItemIndex(roadStageMap,backChildNavList,rs,fenZhiRsList,rtspRoadStage,rs.getPreBfFlag(),fenZhiItemIndex,rtspBackX,rtspBackY,allNavList,distance);
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
		navLineMap.put("navLong", jiSuanNavLineDistance(backChildNavList));
		return navLineMap;
	}
	
	public static float jiSuanDistance(Float backX, Float backY, Float frontX, Float frontY) {
		float bfX = Math.abs(frontX-backX);
		float bfY = Math.abs(frontY-backY);
		return Float.valueOf(String.format("%.2f", Math.sqrt(bfX*bfX+bfY*bfY)));
	}
	
	public static float jiSuanNavLineDistance(List<RoadStage> roadStageList) {
		float distance=0;
		for (RoadStage roadStage : roadStageList) {
			distance+=roadStage.getDistance();
		}
		System.out.println("distance==="+distance);
		return distance;
	}
	
	/*
	public static void main(String[] args) {
		System.out.println("==="+RoadStageUtil.jiSuanDistance((float)600.00,(float)730.00,(float)300.00,(float)300.00));
	}
	*/
	
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
				roadStage.setDistance(RoadStageUtil.jiSuanDistance(x,y,rsFrontX,rsFrontY));
				rs.setPreBfFlag(bfFlag);
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
				roadStage.setDistance(RoadStageUtil.jiSuanDistance(x,y,rsFrontX,rsFrontY));
				rs.setPreBfFlag(bfFlag);
				navList.add(roadStage);
			}
		}
	}
	
	public static void addNextConnectRSNavInList(RoadStage rs,List<RoadStage> navList,String bfFlag) {
		RoadStage roadStage=new RoadStage();
		if(RoadStage.BACK_FLAG.equals(bfFlag)) {
			roadStage.setBackX(rs.getBackX());
			roadStage.setBackY(rs.getBackY());
			roadStage.setFrontX(rs.getFrontX());
			roadStage.setFrontY(rs.getFrontY());
			roadStage.setDistance(RoadStageUtil.jiSuanDistance(rs.getBackX(),rs.getBackY(),rs.getFrontX(),rs.getFrontY()));
			rs.setPreBfFlag(bfFlag);
			navList.add(roadStage);
		}
		else if(RoadStage.FRONT_FLAG.equals(bfFlag)) {
			roadStage.setBackX(rs.getFrontX());
			roadStage.setBackY(rs.getFrontY());
			roadStage.setFrontX(rs.getBackX());
			roadStage.setFrontY(rs.getBackY());
			roadStage.setDistance(RoadStageUtil.jiSuanDistance(rs.getBackX(),rs.getBackY(),rs.getFrontX(),rs.getFrontY()));
			rs.setPreBfFlag(bfFlag);
			navList.add(roadStage);
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
	
	public static Map<String,String> initRSNameMap(List<RoadStage> roadStageList) {
		Map<String,String> rsNameMap=new HashMap<>();
		for (RoadStage roadStage : roadStageList) {
			rsNameMap.put("roadStage"+roadStage.getId(), roadStage.getName());
		}
		return rsNameMap;
	}
	
	public static String getRSNameFromMapByIds(Map<String, String> rsNameMap, String ids) {
		String[] idArr = ids.split(",");
		String rsNames="";
		for (String id : idArr) {
			String rsName = rsNameMap.get("roadStage"+id);
			rsNames+=","+rsName;
		}
		return rsNames.substring(1);
	}
}
