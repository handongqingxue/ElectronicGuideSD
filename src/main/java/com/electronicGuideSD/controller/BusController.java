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
import com.electronicGuideSD.util.EntityUtil;
import com.electronicGuideSD.util.JsonUtil;
import com.electronicGuideSD.util.PlanResult;

@Controller
@RequestMapping(BusController.MODULE_NAME)
public class BusController {

	@Autowired
	private BusNoService busNoService;
	@Autowired
	private BusStopService busStopService;
	@Autowired
	private RoadStageService roadStageService;
	@Autowired
	private ScenicPlaceService scenicPlaceService;
	@Autowired
	private TextLabelService textLabelService;
	public static final String MODULE_NAME="/background/bus";
	
	@RequestMapping(value="/busNo/add")
	public String goBusNoAdd(HttpServletRequest request) {

		return MODULE_NAME+"/busNo/add";
	}
	
	@RequestMapping(value="/busNo/edit")
	public String goBusNoEdit(HttpServletRequest request) {
		
		String id = request.getParameter("id");
		BusNo busNo = busNoService.selectById(id);
		request.setAttribute("busNo", busNo);
		
		return MODULE_NAME+"/busNo/edit";
	}
	
	@RequestMapping(value="/busNo/list")
	public String goBusNoList(HttpServletRequest request) {

		return MODULE_NAME+"/busNo/list";
	}
	
	@RequestMapping(value="/busNo/detail")
	public String goBusNoDetail(HttpServletRequest request) {
		
		BusNo busNo = busNoService.selectById(request.getParameter("id"));
		
		request.setAttribute("busNo", busNo);
		
		return MODULE_NAME+"/busNo/detail";
	}
	
	@RequestMapping(value="/busStop/add")
	public String goBusStopAdd(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL),request);

		return MODULE_NAME+"/busStop/add";
	}
	
	@RequestMapping(value="/busStop/list")
	public String goBusStopList(HttpServletRequest request) {

		return MODULE_NAME+"/busStop/list";
	}
	
	@RequestMapping(value="/selectBusNoList")
	@ResponseBody
	public Map<String, Object> selectBusNoList(String name,int page,int rows,String sort,String order) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int count=busNoService.selectForInt(name);
		List<BusNo> busNoList=busNoService.selectList(name, page, rows, sort, order);

		jsonMap.put("total", count);
		jsonMap.put("rows", busNoList);
			
		return jsonMap;
	}
	
	@RequestMapping(value="/addBusNo",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String addBusNo(BusNo busNo,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			int count=busNoService.add(busNo);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("添加车辆失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("添加车辆成功！");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/editBusNo",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String editBusNo(BusNo busNo,
			HttpServletRequest request) {

		String json=null;
		try {
			PlanResult plan=new PlanResult();
			int count=busNoService.edit(busNo);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("编辑车辆失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("编辑车辆成功！");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/selectBusStopList")
	@ResponseBody
	public Map<String, Object> selectBusStopList(String name,int page,int rows,String sort,String order) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int count=busStopService.selectForInt(name);
		List<BusStop> busStopList=busStopService.selectList(name, page, rows, sort, order);

		jsonMap.put("total", count);
		jsonMap.put("rows", busStopList);
			
		return jsonMap;
	}
	
	@RequestMapping(value="/addBusStop",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String addBusStop(BusStop busStop,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			int count=busStopService.add(busStop);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("添加站点失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("添加站点成功！");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value="/selectBusNoCBBData")
	@ResponseBody
	public Map<String, Object> selectBusNoCBBData() {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<BusNo> busNoList = busNoService.selectCBBData();

		if(busNoList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "暂无车辆");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("busNoList", busNoList);
		}
		return jsonMap;
	}
}
