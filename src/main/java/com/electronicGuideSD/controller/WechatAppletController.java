package com.electronicGuideSD.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.electronicGuideSD.service.*;
import com.electronicGuideSD.entity.*;

@Controller
@RequestMapping(WechatAppletController.MODULE_NAME)
public class WechatAppletController {

	@Autowired
	private ScenicPlaceService scenicPlaceService;
	@Autowired
	private RoadStageService roadStageService;
	@Autowired
	private RoadStageService roadDotService;
	public static final String MODULE_NAME="/wechatApplet";

	@RequestMapping(value="/selectScenicPlaceList")
	@ResponseBody
	public Map<String, Object> selectScenicPlaceList() {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<ScenicPlace> scenicPlaceList = scenicPlaceService.selectList();

		if(scenicPlaceList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "暂无景点");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("scenicPlaceList", scenicPlaceList);
		}
		return jsonMap;
	}

	@RequestMapping(value="/selectRoadStageList")
	@ResponseBody
	public Map<String, Object> selectRoadStageList() {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<RoadStage> roadStageList = roadStageService.selectOtherList(null);

		if(roadStageList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "暂无路段");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("roadStageList", roadStageList);
		}
		return jsonMap;
	}

	@RequestMapping(value="/navToDestination")
	@ResponseBody
	public Map<String, Object> navToDestination(Float meX,Float meY,Float scenicPlaceX,Float scenicPlaceY) {

		System.out.println("meX="+meX);
		System.out.println("meY="+meY);
		System.out.println("scenicPlaceX="+scenicPlaceX);
		System.out.println("scenicPlaceY="+scenicPlaceY);
		/*
		meX=(float) 1130.0;
		meY=(float) 580.0;
		scenicPlaceX=(float) 909.0;
		scenicPlaceY=(float) 439.0;
		*/
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<RoadStage> destRoadStageList = new ArrayList<>();
		List<RoadStage> roadStageList = roadDotService.getShortRoadLine(meX,meY,scenicPlaceX,scenicPlaceY);
		
		jsonMap.put("roadStageList", roadStageList);
		return jsonMap;
	}
}
