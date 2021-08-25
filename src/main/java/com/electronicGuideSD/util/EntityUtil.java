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
	
	public static final String OTHER_RS="otherRS";
	public static final String SCENIC_PLACE="scenicPlace";
	public static final String TEXT_LABEL="textLabel";
	
	public static List<Map<String,Object>> initServiceParamList(RoadStageService roadStageService,
			ScenicPlaceService scenicPlaceService,
			TextLabelService textLabelService) {
		List<Map<String,Object>> paramList=new ArrayList<>();
		Map<String,Object> paramMap=null;
		if(roadStageService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", OTHER_RS);
			paramMap.put("service", roadStageService);
			paramList.add(paramMap);
		}
		if(scenicPlaceService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", SCENIC_PLACE);
			paramMap.put("service", scenicPlaceService);
			paramList.add(paramMap);
		}
		if(textLabelService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", TEXT_LABEL);
			paramMap.put("service", textLabelService);
			paramList.add(paramMap);
		}
		return paramList;
	}

    public static void putJAStrInRequest(List<Map<String,Object>> paramList, HttpServletRequest request) {
		for (Map<String, Object> paramMap : paramList) {
    		String type = paramMap.get("type").toString();
    		if(OTHER_RS.equals(type)) {
    			RoadStageService roadStageService=(RoadStageService)paramMap.get("service");
    			String id = request.getParameter("id");
    			List<RoadStage> otherRSList = roadStageService.selectOtherList(id);
    			request.setAttribute("otherRSJAStr", JSONArray.fromObject(otherRSList));
    		}
    		else if(SCENIC_PLACE.equals(type)) {
    			ScenicPlaceService scenicPlaceService=(ScenicPlaceService)paramMap.get("service");
    			List<ScenicPlace> scenicPlaceList = scenicPlaceService.selectList();
    			request.setAttribute("scenicPlaceJAStr", JSONArray.fromObject(scenicPlaceList));
    		}
    		else if(TEXT_LABEL.equals(type)) {
    			TextLabelService textLabelService=(TextLabelService)paramMap.get("service");
    			List<TextLabel> textLabelList = textLabelService.selectList();
    			request.setAttribute("textLabelJAStr", JSONArray.fromObject(textLabelList));
    		}
		}
	}
}
