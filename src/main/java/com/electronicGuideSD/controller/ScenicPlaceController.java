package com.electronicGuideSD.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.electronicGuideSD.service.*;
import com.electronicGuideSD.entity.*;

@Controller
@RequestMapping(ScenicPlaceController.MODULE_NAME)
public class ScenicPlaceController {

	@Autowired
	private ScenicPlaceService scenicPlaceService;
	public static final String MODULE_NAME="/background/scenicPlace";
	
	@RequestMapping(value="/scenicPlace/add")
	public String goScenicPlaceAdd(HttpServletRequest request) {

		return MODULE_NAME+"/scenicPlace/add";
	}
	
	@RequestMapping(value="/scenicPlace/list")
	public String goScenicPlaceList(HttpServletRequest request) {

		return MODULE_NAME+"/scenicPlace/list";
	}
	
	@RequestMapping(value="/selectList")
	@ResponseBody
	public Map<String, Object> selectList(String name,int page,int rows,String sort,String order) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int count=scenicPlaceService.selectForInt(name);
		List<ScenicPlace> sdList=scenicPlaceService.selectList(name, page, rows, sort, order);

		jsonMap.put("total", count);
		jsonMap.put("rows", sdList);
			
		return jsonMap;
	}
}
