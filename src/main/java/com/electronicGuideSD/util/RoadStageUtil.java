package com.electronicGuideSD.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.electronicGuideSD.entity.RoadStage;
import com.electronicGuideSD.service.RoadStageService;

import net.sf.json.JSONObject;

public class RoadStageUtil {
	
	public static final String ASC="asc";
	public static final String DESC="desc";
	public static final String FIRST="first";
	public static final String LAST="last";
	
	@SuppressWarnings("unchecked")
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
	 * ��ʼ�����е���·��
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
		Integer startRoadId = Integer.valueOf(mtrNearRSMap.get("roadId").toString());//�ο͵����·�ε�·�����ڵ�·id
		
		RoadStage meToRoadStage=initMeToRoadNavLine(meX, meY, mtrNearRSMap);
		//���ο͵����·�ߵ��·�μӵ������ļ�����
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
		//if(rs.getBackThrough()) {//����Ĳ�ѯ�������Ѿ��涨����·�������û��Ҫ�ж���
		
		RoadStage rs = rsList.get(itemIndex);//��ȡ�οͽ��뵼���ߵĵ�һ��·��
		
		Map<String,Object> frontNavLineMap = initFrontNavLine(allRoadStageMap,childNavList,roadStage,rsList,rs,rtspRoadStage,preBfFlag,itemIndex,rtspBackX,rtspBackY,allNavList,preDistance);

		Map<String,Object> backNavLineMap = initBackNavLine(allRoadStageMap,childNavList,roadStage,rsList,rs,rtspRoadStage,preBfFlag,itemIndex,rtspBackX,rtspBackY,allNavList,preDistance);
		
		allNavList.add(frontNavLineMap);
		allNavList.add(backNavLineMap);
		//}
	}
	
	/**
	 * �����ο������·�ε����뾰�������·�ε���ͬһ���㣬���Ե��ô˷���ֱ�ӵ�����ȥ
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
			meFrontX = Float.valueOf(meToRoadMap.get("frontX").toString());
			meFrontY = Float.valueOf(meToRoadMap.get("frontY").toString());
		}
		
		roadStage.setFrontX(meFrontX);//��λ�ô������ڵ���������������Ǹ��󷽵㴦�����Ǿ����ó�·�ߵ�ǰ������
		roadStage.setFrontY(meFrontY);
		roadStage.setDistance(RoadStageUtil.jiSuanDistance(meX,meY,meFrontX,meFrontY));
		roadStage.setPreBfFlag(mtrBfFlag);
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
		rtspRoadStage.setDistance(RoadStageUtil.jiSuanDistance(sptrBackX,sptrBackY,scenicPlaceX,scenicPlaceY));
		return rtspRoadStage;
		////
	}
	
	public static Map<String,Object> initFrontNavLine(Map<String, Object> roadStageMap, List<RoadStage> childNavList, RoadStage preRoadStage,List<RoadStage> rsList, RoadStage currentRS, RoadStage rtspRoadStage, String preBfFlag, int itemIndex, Float rtspBackX, Float rtspBackY,List<Map<String,Object>> allNavList,float preDistance) {
		boolean getSPFlag=false;
		float distance=preDistance;
		List<RoadStage> frontChildNavList=new ArrayList<>();
		frontChildNavList.addAll(childNavList);//���������ļ�����ӵ���ǰ�����ļ�����
		
		//˳����ڶε�·����ǰ�������Ȱѱ�·�μӽ�ȥ��Ϊ�˷�������ı���
		if(RoadStage.BACK_FLAG.equals(preBfFlag)) {
			if(itemIndex+1<rsList.size()) {
				RoadStage nextRS = rsList.get(itemIndex+1);
				String nextBfFlag = RoadStageUtil.checkConnectBackOrFront(currentRS.getBackX(),currentRS.getBackY(),nextRS);
				System.out.println("nextBfFlag==="+nextBfFlag);
				if(RoadStage.BACK_FLAG.equals(nextBfFlag)) {
					RoadStageUtil.addNextConnectRSNavInList(nextRS,frontChildNavList,preBfFlag);//�Ȱѵ�һ��·����ӵ���ǰ�����ļ�����
					itemIndex++;
				}
				else {
					RoadStageUtil.addRSNavInList(preRoadStage.getFrontX(),preRoadStage.getFrontY(),currentRS,frontChildNavList,preBfFlag);//�Ȱѵ�һ��·����ӵ���ǰ�����ļ�����
				}
			}
		}
		else if(RoadStage.FRONT_FLAG.equals(preBfFlag)) {
			if(itemIndex+1<rsList.size()) {
				RoadStage nextRS = rsList.get(itemIndex+1);
				String nextBfFlag = RoadStageUtil.checkConnectBackOrFront(currentRS.getFrontX(),currentRS.getFrontY(),nextRS);
				System.out.println("nextBfFlag==="+nextBfFlag);
				if(RoadStage.FRONT_FLAG.equals(nextBfFlag)) {
					RoadStageUtil.addNextConnectRSNavInList(nextRS,frontChildNavList,preBfFlag);//�Ȱѵ�һ��·����ӵ���ǰ�����ļ�����
					itemIndex++;
				}
				else {
					RoadStageUtil.addRSNavInList(preRoadStage.getFrontX(),preRoadStage.getFrontY(),currentRS,frontChildNavList,preBfFlag);//�Ȱѵ�һ��·����ӵ���ǰ�����ļ�����
				}
			}
		}
		
		for(int i=itemIndex+1;i<rsList.size();i++) {
			System.out.println("i��==="+i);
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
		backChildNavList.addAll(childNavList);//���������ļ�����ӵ��������ļ�����
		
		//˳����ڶε�·������������Ȱѱ�·�μӽ�ȥ��Ϊ�˷�������ı���
		RoadStageUtil.addRSNavInList(roadStage.getFrontX(),roadStage.getFrontY(),rs,backChildNavList,bfFlag);
		for(int i=itemIndex-1;i>=0;i--) {
			System.out.println("i��2==="+i);
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
						System.out.println("�����н����="+rsIds);
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
			System.out.println("��ǰһ��·�κ󷽵����");
			//���ɵĵ����߷�����ܺ͵�ͼ��·�ߵķ����෴
			if(rs.getFrontThrough()) {//��󷽵���ӣ���Ҫ�ж�ǰ�����Ƿ�ͨ
				Float rsFrontX = Float.valueOf(rs.getFrontX());//������Ȼ���ǰ��ĵ㣬�������෴���൱���ο͵���·�������ĵ�
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
			System.out.println("��ǰһ��·��ǰ�������");
			if(rs.getBackThrough()) {//��ǰ������ӣ���Ҫ�жϺ󷽵��Ƿ�ͨ
				Float rsFrontX = Float.valueOf(rs.getBackX());//����ǰ������ӣ����ﵼ������ǰ���ĵ����·����󷽵ĵ�
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
			System.out.println("��󷽵����");
			flag=RoadStage.BACK_FLAG;
		}
		else if(x.equals(rs.getFrontX())&&y.equals(rs.getFrontY())) {
			System.out.println("��ǰ�������");
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
	
	public static org.json.JSONObject getPublicPointJO(JSONObject a,JSONObject b,JSONObject c,JSONObject d) {
		float aX = Float.valueOf(a.getString("x"));
		float aY = Float.valueOf(a.getString("y"));
		float bX = Float.valueOf(b.getString("x"));
		float bY = Float.valueOf(b.getString("y"));
		float cX = Float.valueOf(c.getString("x"));
		float cY = Float.valueOf(c.getString("y"));
		float dX = Float.valueOf(d.getString("x"));
		float dY = Float.valueOf(d.getString("y"));
		// ������abc �����2��  
	    float area_abc = (aX - cX) * (bY - cY) - (aY - cY) * (bX - cX);  
	  
	    // ������abd �����2��  
	    float area_abd = (aX - dX) * (bY - dY) - (aY - dY) * (bX - dX);
	  
	    // ���������ͬ���������߶�ͬ��,���ཻ (�Ե����߶��ϵ����,�����������ཻ����);  
	    if ( area_abc*area_abd>=0 ) {  
	        return null;  
	    }  
	  
	    // ������cda �����2��  
	    float area_cda = (cX - aX) * (dY - aY) - (cY - aY) * (dX - aX);  
	    // ������cdb �����2��  
	    // ע��: ������һ��С�Ż�.����Ҫ���ù�ʽ�������,����ͨ����֪����������Ӽ��ó�.  
	    float area_cdb = area_cda + area_abc - area_abd ;  
	    if (  area_cda * area_cdb >= 0 ) {  
	        return null;
	    }  
	  
	    //���㽻������  
	    float t = area_cda / ( area_abd- area_abc );
	    float dx= t*(bX - aX);
	    float dy= t*(bY - aY);
	    return new org.json.JSONObject("{\"x\":"+(aX+dx)+",\"y\":"+(aY+dy)+"}");
	    //return { x: a.x + dx , y: a.y + dy };  
	}
	
	/**
	 * ��ѯ�������·�������˳���Ľ����·�μ���
	 * @param roadStageService
	 * @param a
	 * @param b
	 * @param currentRSId 
	 * @return
	 */
	public static List<RoadStage> selectPublicPointRSList(RoadStageService roadStageService, JSONObject a, JSONObject b, Integer currentRSId) {
		List<RoadStage> ppRSList = new ArrayList<>();
		List<RoadStage> rsList = roadStageService.selectOtherList(null);
		for (int i = 0; i < rsList.size(); i++) {
			RoadStage rs = rsList.get(i);
			if(currentRSId!=null) {
				if(rs.getId()==currentRSId)
					continue;
			}
			JSONObject c=new JSONObject();
			c.put("x", rs.getBackX());
			c.put("y", rs.getBackY());
			JSONObject d=new JSONObject();
			d.put("x", rs.getFrontX());
			d.put("y", rs.getFrontY());
			org.json.JSONObject jo = RoadStageUtil.getPublicPointJO(a,b,c,d);
			if(jo!=null) {
				rs.setCrossX(Float.valueOf(jo.get("x").toString()));
				rs.setCrossY(Float.valueOf(jo.get("y").toString()));
				ppRSList.add(rs);
			}
		}
		return ppRSList;
	}
	
	public static org.json.JSONObject dividePPRoadStage(RoadStage roadStage, RoadStage pprs) {
		org.json.JSONObject dividePPRSJO=new org.json.JSONObject();

		float crossX = pprs.getCrossX();
		float crossY = pprs.getCrossY();
		RoadStage preRS = new RoadStage();
		float rsBackX = pprs.getBackX();
		float rsBackY = pprs.getBackY();
		preRS.setBackX(rsBackX);
		preRS.setBackY(rsBackY);
		preRS.setFrontX(crossX);
		preRS.setFrontY(crossY);
		preRS.setBackThrough(pprs.getBackThrough());
		preRS.setFrontThrough(true);
		preRS.setBackIsCross(pprs.getBackIsCross());
		preRS.setBackCrossRSIds(pprs.getBackCrossRSIds());
		preRS.setFrontIsCross(true);
		//preRS.setFrontCrossRSIds(roadStage.getId().toString());
		preRS.setRoadId(pprs.getRoadId());
		dividePPRSJO.put("preRS", preRS);
		
		RoadStage sufRS = new RoadStage();
		Float rsFrontX = pprs.getFrontX();
		Float rsFrontY = pprs.getFrontY();
		sufRS.setBackX(crossX);
		sufRS.setBackY(crossY);
		sufRS.setFrontX(rsFrontX);
		sufRS.setFrontY(rsFrontY);
		sufRS.setBackThrough(true);
		sufRS.setFrontThrough(pprs.getFrontThrough());
		sufRS.setBackIsCross(true);
		//sufRS.setBackCrossRSIds(roadStage.getId().toString());
		sufRS.setFrontIsCross(pprs.getFrontIsCross());
		sufRS.setFrontCrossRSIds(pprs.getFrontCrossRSIds());
		sufRS.setRoadId(pprs.getRoadId());
		dividePPRSJO.put("sufRS", sufRS);
		
		return dividePPRSJO;
	}
	
	/**
	 * ����ο�����:https://blog.csdn.net/qq_40618664/article/details/110110718
	 * @param roadStage
	 * @param pprsList
	 */
	public static List<RoadStage> divideRoadStage(RoadStage roadStage, List<RoadStage> pprsList) {
		float backX = roadStage.getBackX();
		float backY = roadStage.getBackY();
		for (int i = 0; i < pprsList.size(); i++) {
			RoadStage pprs = pprsList.get(i);
			float crossX = pprs.getCrossX();
			float crossY = pprs.getCrossY();
			float distance = jiSuanDistance(backX,backY,crossX,crossY);
			pprs.setDistance(distance);
			/*
			if(i==0)
				pprs.setDistance((float)5);
			else if(i==1)
				pprs.setDistance((float)2);
			else if(i==2)
				pprs.setDistance((float)1);
			*/
		}
		sortByDistance(pprsList,ASC);
		System.out.println("pprsList==="+pprsList);
		List<RoadStage> divideRSList=new ArrayList<>();
		RoadStage divideRS=new RoadStage();
		divideRS.setBackX(roadStage.getBackX());
		divideRS.setBackY(roadStage.getBackY());
		divideRS.setFrontX(pprsList.get(0).getCrossX());
		divideRS.setFrontY(pprsList.get(0).getCrossY());
		divideRS.setFrontThrough(true);
		divideRS.setRoadId(roadStage.getRoadId());
		divideRSList.add(divideRS);
		for (int i = 0; i < pprsList.size()-1; i++) {
			RoadStage currentPPRS = pprsList.get(i);
			RoadStage nextPPRS = pprsList.get(i+1);
			divideRS=new RoadStage();
			divideRS.setBackX(currentPPRS.getCrossX());
			divideRS.setBackY(currentPPRS.getCrossY());
			divideRS.setFrontX(nextPPRS.getCrossX());
			divideRS.setFrontY(nextPPRS.getCrossY());
			divideRS.setBackThrough(true);
			divideRS.setFrontThrough(true);
			divideRS.setRoadId(roadStage.getRoadId());
			divideRSList.add(divideRS);
		}
		divideRS=new RoadStage();
		divideRS.setBackX(pprsList.get(pprsList.size()-1).getCrossX());
		divideRS.setBackY(pprsList.get(pprsList.size()-1).getCrossY());
		divideRS.setFrontX(roadStage.getFrontX());
		divideRS.setFrontY(roadStage.getFrontY());
		divideRS.setBackThrough(true);
		divideRS.setRoadId(roadStage.getRoadId());
		divideRSList.add(divideRS);
		return divideRSList;
	}
	
	public static void sortByDistance(List<RoadStage> pprsList, String sortFlag) {
		if(ASC.equals(sortFlag)) {
			Collections.sort(pprsList, new comparatorAsc());
		}
		else if(DESC.equals(sortFlag)) {
			Collections.sort(pprsList, new comparatorDesc());
		}
	}
	
	/**
     * ����
     */
	public static class comparatorDesc implements Comparator<RoadStage> {
        @Override
        public int compare(RoadStage roadStage1, RoadStage roadStage2) {
            Float distance1 = roadStage1.getDistance();
            Float distance2 = roadStage2.getDistance();
            if (distance2 != null) {
                return distance2.compareTo(distance1);
            }
            return 0;
        }
    }

    /**
     * ����
     */
	public static class comparatorAsc implements Comparator<RoadStage> {
        @Override
        public int compare(RoadStage roadStage1, RoadStage roadStage2) {
            Float distance1 = roadStage1.getDistance();
            Float distance2 = roadStage2.getDistance();
            if (distance1 != null) {
                return distance1.compareTo(distance2);
            }
            return 0;
        }
    }
	
	public static List<RoadStage> connectRoadStageInRoad(Map<String, Object> allRoadStageMap, int roadId) {
		List<RoadStage> connectRSList = new ArrayList<>();
		List<RoadStage> roadStageList = (List<RoadStage>)allRoadStageMap.get("roadStage"+roadId);
		RoadStage roadStage = roadStageList.get(0);
		addRSInList(roadStage,connectRSList,roadStageList,LAST,true);
		return connectRSList;
	}
	
	public static void addConnectRoadStageInList(List<RoadStage> roadStageList,List<RoadStage> connectRSList) {
		for(int i=0;i<roadStageList.size();i++) {
			RoadStage roadStage = roadStageList.get(i);
			int connectRSListSize = connectRSList.size();
			if(connectRSListSize==1) {
				RoadStage connectRS = connectRSList.get(0);
				float backX = connectRS.getBackX();
				float backY = connectRS.getBackY();
				float frontX = connectRS.getFrontX();
				float frontY = connectRS.getFrontY();
				if(backX==roadStage.getBackX()&&backY==roadStage.getBackY()||backX==roadStage.getFrontX()&&backY==roadStage.getFrontY()) {
					System.out.println("1backX="+backX+",backY="+backY+",frontX="+frontX+",frontY="+frontY);
					addRSInList(roadStage,connectRSList,roadStageList,FIRST,true);
				}
				else if(frontX==roadStage.getBackX()&&frontY==roadStage.getBackY()||frontX==roadStage.getFrontX()&&frontY==roadStage.getFrontY()) {
					System.out.println("2backX="+backX+",backY="+backY+",frontX="+frontX+",frontY="+frontY);
					addRSInList(roadStage,connectRSList,roadStageList,LAST,true);
				}
			}
			else {
				RoadStage connectRS = connectRSList.get(0);
				float backX = connectRS.getBackX();
				float backY = connectRS.getBackY();
				float frontX = connectRS.getFrontX();
				float frontY = connectRS.getFrontY();
				if(backX==roadStage.getBackX()&&backY==roadStage.getBackY()||backX==roadStage.getFrontX()&&backY==roadStage.getFrontY()||
				   frontX==roadStage.getBackX()&&frontY==roadStage.getBackY()||frontX==roadStage.getFrontX()&&frontY==roadStage.getFrontY()) {
					System.out.println("backX="+backX+",backY="+backY+",frontX="+frontX+",frontY="+frontY);
					addRSInList(roadStage,connectRSList,roadStageList,FIRST,true);
				}
				connectRS = connectRSList.get(connectRSList.size()-1);
				backX = connectRS.getBackX();
				backY = connectRS.getBackY();
				frontX = connectRS.getFrontX();
				frontY = connectRS.getFrontY();
				if(backX==roadStage.getBackX()&&backY==roadStage.getBackY()||backX==roadStage.getFrontX()&&backY==roadStage.getFrontY()||
				   frontX==roadStage.getBackX()&&frontY==roadStage.getBackY()||frontX==roadStage.getFrontX()&&frontY==roadStage.getFrontY()) {
					System.out.println("backX="+backX+",backY="+backY+",frontX="+frontX+",frontY="+frontY);
					addRSInList(roadStage,connectRSList,roadStageList,LAST,true);
				}
			}
		}
	}
	
	public static void addRSInList(RoadStage roadStage,List<RoadStage> connectRSList,List<RoadStage> roadStageList,String localFlag,boolean restAttrFlag) {
		if(restAttrFlag) {
			roadStage.setBackThrough(false);
			roadStage.setFrontThrough(false);
			roadStage.setBackIsCross(false);
			roadStage.setBackCrossRSIds(null);
			roadStage.setFrontIsCross(false);
			roadStage.setFrontCrossRSIds(null);
		}
		
		if(FIRST.equals(localFlag))
			connectRSList.add(0, roadStage);
		if(LAST.equals(localFlag))
			connectRSList.add(roadStage);
		
		roadStageList.remove(roadStage);
		addConnectRoadStageInList(roadStageList,connectRSList);
	}
	
	public static void updateRoadStageInRoad(List<RoadStage> roadStageList, List<RoadStage> allRSList) {
		for(int i=0;i<roadStageList.size();i++) {
			RoadStage roadStage = roadStageList.get(i);
			roadStage.setSort(i);
			roadStage.setName(roadStage.getRoadName()+(i+1));
			for(int j=0;j<allRSList.size();j++) {
				RoadStage rs = allRSList.get(j);
				if(roadStage.getId()==rs.getId())
					continue;
				float roadStageBackX = roadStage.getBackX();
				float roadStageBackY = roadStage.getBackY();
				float roadStageFrontX = roadStage.getFrontX();
				float roadStageFrontY = roadStage.getFrontY();
				int roadStageRoadId = roadStage.getRoadId();
				
				float rsBackX = rs.getBackX();
				float rsBackY = rs.getBackY();
				float rsFrontX = rs.getFrontX();
				float rsFrontY = rs.getFrontY();
				int rsRoadId = rs.getRoadId();
				if(roadStageBackX==rsBackX&&roadStageBackY==rsBackY||roadStageBackX==rsFrontX&&roadStageBackY==rsFrontY) {
					roadStage.setBackThrough(true);
					if(roadStageRoadId!=rsRoadId) {
						roadStage.setBackIsCross(true);
						String backCrossRSIds = roadStage.getBackCrossRSIds();
						if(StringUtils.isEmpty(backCrossRSIds))
							roadStage.setBackCrossRSIds(rs.getId().toString());
						else {
							backCrossRSIds = roadStage.getBackCrossRSIds();
							if(!(backCrossRSIds+",").contains(rs.getId()+",")) {
								roadStage.setBackCrossRSIds(backCrossRSIds+","+rs.getId().toString());
							}
						}
					}
				}
				if(roadStageFrontX==rsBackX&&roadStageFrontY==rsBackY||roadStageFrontX==rsFrontX&&roadStageFrontY==rsFrontY) {
					roadStage.setFrontThrough(true);
					if(roadStageRoadId!=rsRoadId) {
						roadStage.setFrontIsCross(true);
						String frontCrossRSIds = roadStage.getFrontCrossRSIds();
						if(StringUtils.isEmpty(frontCrossRSIds)) {
							roadStage.setFrontCrossRSIds(rs.getId().toString());
						}
						else {
							frontCrossRSIds = roadStage.getFrontCrossRSIds();
							if(!(frontCrossRSIds+",").contains(rs.getId()+","))
								roadStage.setFrontCrossRSIds(frontCrossRSIds+","+rs.getId().toString());
						}
						
					}
				}
			}
		}
	}
	
	public static int updateAttrInRoad(RoadStageService roadStageService, Map<String, Object> allRoadStageMap, int roadId, List<RoadStage> roadStageList) {
		List<RoadStage>  connectRSList = RoadStageUtil.connectRoadStageInRoad(allRoadStageMap,roadId);//����ͬ��·���·�ΰ�ǰ������ƴ������
		//����ƴ�Ӻõ�ǰ��·�κ�����·�ε�������������¸õ�·�µ�ÿ��·������
		RoadStageUtil.updateRoadStageInRoad(connectRSList,roadStageList);
		System.out.println("connectRSList="+connectRSList);
		return roadStageService.updateAttrInRoad(connectRSList);//�����º��ÿ����·�µ�·��ͬ�������ݿ��
	}
	
	/**
	 * ��֤�������ǲ�������֮��Ľ���
	 * @param startPoint
	 * @param endPoint
	 * @param publicPoint
	 * @return
	 */
	/*
	public static boolean checkIfCrossOverPoint(JSONObject startPoint,JSONObject endPoint,org.json.JSONObject publicPoint) {
		boolean flag=true;
		float startPointX = Float.valueOf(startPoint.get("x").toString());
		float startPointY = Float.valueOf(startPoint.get("y").toString());
		float endPointX = Float.valueOf(endPoint.get("x").toString());
		float endPointY = Float.valueOf(endPoint.get("y").toString());
		float publicPointX = Float.valueOf(publicPoint.get("x").toString());
		float publicPointY = Float.valueOf(publicPoint.get("y").toString());
		if(publicPointX==startPointX&&publicPointY==startPointY)
			flag=false;
		else if(publicPointX==endPointX&&publicPointY==endPointY)
			flag=false;
		return flag;
	}
	*/
	
	public static void main(String[] args) {
		/*
		JSONObject a=new JSONObject();
		a.put("x", (float)100);
		a.put("y", (float)100);
		
		JSONObject b=new JSONObject();
		b.put("x", (float)500);
		b.put("y", (float)600);
		
		JSONObject c=new JSONObject();
		c.put("x", (float)100);
		c.put("y", (float)600);

		JSONObject d=new JSONObject();
		d.put("x", (float)500);
		d.put("y", (float)600);
		
		org.json.JSONObject pointJO = RoadStageUtil.getPublicPointJO(a,b,c,d);
		System.out.println("x="+pointJO.get("x").toString()+",y="+pointJO.get("y").toString());
		*/
		
		List<RoadStage> rsList=new ArrayList<>();
		RoadStage rs=new RoadStage();
		rs.setBackX((float)100);
		rsList.add(rs);
		RoadStage rs1=new RoadStage();
		rs1.setBackX((float)200);
		rsList.add(rs1);
		RoadStage rs2=new RoadStage();
		rs2.setBackX((float)300);
		rsList.add(0, rs2);
		System.out.println("1==="+rsList.get(0).getBackX());
	}
}
