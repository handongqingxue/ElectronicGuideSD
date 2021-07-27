package com.electronicGuideSD.controller;

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
	public static final String MODULE_NAME="/wechatApplet";

	@RequestMapping(value="/selectScenicPlaceList")
	@ResponseBody
	public Map<String, Object> selectScenicPlaceList() {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<ScenicPlace> scenicPlaceList = scenicPlaceService.selectList();

		if(scenicPlaceList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "ÔÝÎÞ¾°µã");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("scenicPlaceList", scenicPlaceList);
		}
		return jsonMap;
	}
}
