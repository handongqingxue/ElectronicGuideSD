package com.electronicGuideSD.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public static final String MODULE_NAME="/background/scenicDistrict";
	
	@RequestMapping(value="/info/info")
	public String goInfoInfo(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL,busStopService,EntityUtil.BUS_STOP),request);

		return MODULE_NAME+"/info/info";
	}
}
