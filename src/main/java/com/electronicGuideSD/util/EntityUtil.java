package com.electronicGuideSD.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

import net.sf.json.JSONArray;

public class EntityUtil {

	public static final String ROAD_STAGE="roadStage";
	public static final String OTHER_RS="otherRS";
	public static final String SCENIC_PLACE="scenicPlace";
	public static final String OTHER_SP="otherSP";
	public static final String TEXT_LABEL="textLabel";
	public static final String OTHER_TL="otherTL";
	public static final String BUS_STOP="busStop";
	public static final String OTHER_BS="otherBS";
	
	public static List<Map<String,Object>> initServiceParamList(RoadStageService roadStageService,
			String roadStageType,
			ScenicPlaceService scenicPlaceService,
			String scenicPlaceType,
			TextLabelService textLabelService,
			String textLabelType,
			BusStopService busStopService,
			String busStopType) {
		List<Map<String,Object>> paramList=new ArrayList<>();
		Map<String,Object> paramMap=null;
		if(roadStageService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", roadStageType);
			paramMap.put("service", roadStageService);
			paramList.add(paramMap);
		}
		if(scenicPlaceService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", scenicPlaceType);
			paramMap.put("service", scenicPlaceService);
			paramList.add(paramMap);
		}
		if(textLabelService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", textLabelType);
			paramMap.put("service", textLabelService);
			paramList.add(paramMap);
		}
		if(busStopService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", busStopType);
			paramMap.put("service", busStopService);
			paramList.add(paramMap);
		}
		return paramList;
	}

    public static void putJAStrInRequest(List<Map<String,Object>> paramList, HttpServletRequest request) {
		for (Map<String, Object> paramMap : paramList) {
    		String type = paramMap.get("type").toString();
    		if(ROAD_STAGE.equals(type)) {
    			RoadStageService roadStageService=(RoadStageService)paramMap.get("service");
    			List<RoadStage> roadStageList = roadStageService.selectOtherList(null);
    			request.setAttribute("roadStageJAStr", JSONArray.fromObject(roadStageList));
    		}
    		else if(OTHER_RS.equals(type)) {
    			RoadStageService roadStageService=(RoadStageService)paramMap.get("service");
    			String id = request.getParameter("id");
    			List<RoadStage> otherRSList = roadStageService.selectOtherList(id);
    			request.setAttribute("otherRSJAStr", JSONArray.fromObject(otherRSList));
    		}
    		else if(SCENIC_PLACE.equals(type)) {
    			ScenicPlaceService scenicPlaceService=(ScenicPlaceService)paramMap.get("service");
    			List<ScenicPlace> scenicPlaceList = scenicPlaceService.selectOtherList(null);
    			request.setAttribute("scenicPlaceJAStr", JSONArray.fromObject(scenicPlaceList));
    		}
    		else if(OTHER_SP.equals(type)) {
    			ScenicPlaceService scenicPlaceService=(ScenicPlaceService)paramMap.get("service");
    			String id = request.getParameter("id");
    			List<ScenicPlace> otherSPList = scenicPlaceService.selectOtherList(id);
    			request.setAttribute("otherSPJAStr", JSONArray.fromObject(otherSPList));
    		}
    		else if(TEXT_LABEL.equals(type)) {
    			TextLabelService textLabelService=(TextLabelService)paramMap.get("service");
    			List<TextLabel> textLabelList = textLabelService.selectOtherList(null);
    			request.setAttribute("textLabelJAStr", JSONArray.fromObject(textLabelList));
    		}
    		else if(OTHER_TL.equals(type)) {
    			TextLabelService textLabelService=(TextLabelService)paramMap.get("service");
    			String id = request.getParameter("id");
    			List<TextLabel> otherTLList = textLabelService.selectOtherList(id);
    			request.setAttribute("otherTLJAStr", JSONArray.fromObject(otherTLList));
    		}
    		else if(BUS_STOP.equals(type)) {
    			BusStopService busStopService=(BusStopService)paramMap.get("service");
    			List<BusStop> busStopList = busStopService.selectOtherList(null);
    			request.setAttribute("busStopJAStr", JSONArray.fromObject(busStopList));
    		}
    		else if(OTHER_BS.equals(type)) {
    			BusStopService busStopService=(BusStopService)paramMap.get("service");
    			String id = request.getParameter("id");
    			List<BusStop> otherBSList = busStopService.selectOtherList(id);
    			request.setAttribute("otherBSJAStr", JSONArray.fromObject(otherBSList));
    		}
		}
	}
}
