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

@Controller
@RequestMapping(RouteDotController.MODULE_NAME)
public class RouteDotController {

	@Autowired
	private ToSpRouteDotService toSpRouteDotService;
	public static final String MODULE_NAME="/background/routeDot";
	
	@RequestMapping(value="/toSp/list")
	public String goToSpList(HttpServletRequest request) {

		return MODULE_NAME+"/toSp/list";
	}
	
	@RequestMapping(value="/selectList")
	@ResponseBody
	public Map<String, Object> selectList(String name,int page,int rows,String sort,String order) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int count=toSpRouteDotService.selectForInt(name);
		List<ToSpRouteDot> sdList=toSpRouteDotService.selectList(name, page, rows, sort, order);

		jsonMap.put("total", count);
		jsonMap.put("rows", sdList);
			
		return jsonMap;
	}

}
