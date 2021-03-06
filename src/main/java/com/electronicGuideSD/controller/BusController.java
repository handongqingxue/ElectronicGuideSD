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
import com.electronicGuideSD.util.BusUtil;
import com.electronicGuideSD.util.EntityUtil;
import com.electronicGuideSD.util.JsonUtil;
import com.electronicGuideSD.util.PlanResult;
import com.electronicGuideSD.util.RoadStageUtil;

@Controller
@RequestMapping(BusController.MODULE_NAME)
public class BusController {

	@Autowired
	private BusNoService busNoService;
	@Autowired
	private BusStopService busStopService;
	@Autowired
	private BusNosStopService busNosStopService;
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
	
	@RequestMapping(value="/busNo/addStop")
	public String goBusNoAddStop(HttpServletRequest request) {

		return MODULE_NAME+"/busNo/addStop";
	}
	
	@RequestMapping(value="/busNo/editStop")
	public String goBusNoEditStop(HttpServletRequest request) {
			
		String id = request.getParameter("id");
		BusNosStop busNosStop = busNosStopService.selectById(id);
		request.setAttribute("busNosStop", busNosStop);
			
		return MODULE_NAME+"/busNo/editStop";
	}
	
	@RequestMapping(value="/busNo/detailStop")
	public String goBusNoDetailStop(HttpServletRequest request) {
			
		String id = request.getParameter("id");
		BusNosStop busNosStop = busNosStopService.selectById(id);
		request.setAttribute("busNosStop", busNosStop);
			
		return MODULE_NAME+"/busNo/detailStop";
	}
	
	@RequestMapping(value="/busStop/add")
	public String goBusStopAdd(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL,busStopService,EntityUtil.BUS_STOP),request);

		return MODULE_NAME+"/busStop/add";
	}
	
	@RequestMapping(value="/busStop/edit")
	public String goBusStopEdit(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL,busStopService,EntityUtil.OTHER_BS),request);
		
		String id = request.getParameter("id");
		BusStop busStop = busStopService.selectById(id);
		request.setAttribute("busStop", busStop);
		
		return MODULE_NAME+"/busStop/edit";
	}
	
	@RequestMapping(value="/busStop/list")
	public String goBusStopList(HttpServletRequest request) {

		return MODULE_NAME+"/busStop/list";
	}
	
	@RequestMapping(value="/busStop/detail")
	public String goBusStopDetail(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL,busStopService,EntityUtil.OTHER_BS),request);
		
		BusStop busStop = busStopService.selectById(request.getParameter("id"));
		
		List<BusNo> allBNList = busNoService.selectCBBData();
		Map<String, String> bsNameMap = BusUtil.initBSNameMap(allBNList);
		String busNoIds = busStop.getBusNoIds();
		String busNoNames = BusUtil.getBSNameFromMapByIds(bsNameMap,busNoIds);
		busStop.setBusNoNames(busNoNames);
		
		request.setAttribute("busStop", busStop);
		
		return MODULE_NAME+"/busStop/detail";
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
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("??????????????");
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
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("??????????????");
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
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/editBusStop",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String editBusStop(BusStop busStop,
			HttpServletRequest request) {

		String json=null;
		try {
			PlanResult plan=new PlanResult();
			int count=busStopService.edit(busStop);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/addBusNosStop",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String addBusNosStop(BusNosStop busNosStop,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			int count=busNosStopService.add(busNosStop);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	
	@RequestMapping(value="/editBusNosStop",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String editBusNosStop(BusNosStop busNosStop,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			int count=busNosStopService.edit(busNosStop);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("??????????????");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/selectBusNosStopList")
	@ResponseBody
	public Map<String, Object> selectBusNosStopList(String name,int busNoId) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<BusNosStop> busNosStopList=busNosStopService.selectList(name, busNoId);

		jsonMap.put("data", busNosStopList);
			
		return jsonMap;
	}

	@RequestMapping(value="/selectBusNoCBBData")
	@ResponseBody
	public Map<String, Object> selectBusNoCBBData() {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<BusNo> busNoList = busNoService.selectCBBData();

		if(busNoList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "????????");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("busNoList", busNoList);
		}
		return jsonMap;
	}

	@RequestMapping(value="/selectBusStopCBBData")
	@ResponseBody
	public Map<String, Object> selectBusStopCBBData(int busNoId) {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<BusStop> busStopList = busStopService.selectCBBData(busNoId);

		if(busStopList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "????????????");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("busStopList", busStopList);
		}
		return jsonMap;
	}

	@RequestMapping(value="/selectOtherBsCBBData")
	@ResponseBody
	public Map<String, Object> selectOtherBsCBBData(int busStopId,int busNoId) {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<BusStop> busStopList = busStopService.selectOtherCBBData(busStopId,busNoId);
		
		if(busStopList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "????????????");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("busStopList", busStopList);
		}
		return jsonMap;
	}
}
