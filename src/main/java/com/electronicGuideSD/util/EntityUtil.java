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
	
	public static List<Map<String,Object>> initServiceParamList(RoadStageService roadStageService,
			ScenicPlaceService scenicPlaceService,
			TextLabelService textLabelService) {
		List<Map<String,Object>> paramList=new ArrayList<>();
		Map<String,Object> paramMap=null;
		if(roadStageService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", "otherRS");
			paramMap.put("service", roadStageService);
			paramList.add(paramMap);
		}
		if(scenicPlaceService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", "scenicPlace");
			paramMap.put("service", scenicPlaceService);
			paramList.add(paramMap);
		}
		if(textLabelService!=null) {
			paramMap=new HashMap<>();
			paramMap.put("type", "textLabel");
			paramMap.put("service", textLabelService);
			paramList.add(paramMap);
		}
		return paramList;
	}

    public static void putJAStrInRequest(List<Map<String,Object>> paramList, HttpServletRequest request) {
		for (Map<String, Object> paramMap : paramList) {
    		String type = paramMap.get("type").toString();
    		if("otherRS".equals(type)) {
    			RoadStageService roadStageService=(RoadStageService)paramMap.get("service");
    			String id = request.getParameter("id");
    			List<RoadStage> otherRSList = roadStageService.selectOtherList(id);
    			request.setAttribute("otherRSJAStr", JSONArray.fromObject(otherRSList));
    		}
    		else if("scenicPlace".equals(type)) {
    			ScenicPlaceService scenicPlaceService=(ScenicPlaceService)paramMap.get("service");
    			List<ScenicPlace> scenicPlaceList = scenicPlaceService.selectList();
    			request.setAttribute("scenicPlaceJAStr", JSONArray.fromObject(scenicPlaceList));
    		}
    		else if("textLabel".equals(type)) {
    			TextLabelService textLabelService=(TextLabelService)paramMap.get("service");
    			List<TextLabel> textLabelList = textLabelService.selectList();
    			request.setAttribute("textLabelJAStr", JSONArray.fromObject(textLabelList));
    		}
		}
	}
}
