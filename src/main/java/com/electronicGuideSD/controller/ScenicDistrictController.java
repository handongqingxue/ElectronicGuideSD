package com.electronicGuideSD.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;
import com.electronicGuideSD.util.*;

@Controller
@RequestMapping(ScenicDistrictController.MODULE_NAME)
public class ScenicDistrictController {

	@Autowired
	private RoadStageService roadStageService;
	@Autowired
	private ScenicPlaceService scenicPlaceService;
	@Autowired
	private TextLabelService textLabelService;
	@Autowired
	private BusStopService busStopService;
	@Autowired
	private EntityTypeService entityTypeService;
	public static final String MODULE_NAME="/background/scenicDistrict";
	
	@RequestMapping(value="/info/info")
	public String goInfoInfo(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL,busStopService,EntityUtil.BUS_STOP),request);

		return MODULE_NAME+"/info/info";
	}

	@RequestMapping(value="/selectEntityTypeCBBData")
	@ResponseBody
	public Map<String, Object> selectEntityTypeCBBData() {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<EntityType> entityTypeList = entityTypeService.selectCBBData();

		if(entityTypeList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "暂无实体类型");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("entityTypeList", entityTypeList);
		}
		return jsonMap;
	}
}
