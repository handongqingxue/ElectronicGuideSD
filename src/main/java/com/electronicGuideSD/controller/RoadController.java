package com.electronicGuideSD.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;
import com.electronicGuideSD.util.FileUploadUtils;
import com.electronicGuideSD.util.JsonUtil;
import com.electronicGuideSD.util.PlanResult;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(RoadController.MODULE_NAME)
public class RoadController {

	@Autowired
	private RoadService roadService;
	public static final String MODULE_NAME="/background/road";
	
	@RequestMapping(value="/road/add")
	public String goRoadAdd(HttpServletRequest request) {

		return MODULE_NAME+"/road/add";
	}
	
	@RequestMapping(value="/road/edit")
	public String goRoadEdit(HttpServletRequest request) {

		Road road = roadService.selectById(request.getParameter("id"));
		
		request.setAttribute("road", road);
		
		return MODULE_NAME+"/road/edit";
	}
	
	@RequestMapping(value="/road/list")
	public String goRoadList(HttpServletRequest request) {

		return MODULE_NAME+"/road/list";
	}
	
	@RequestMapping(value="/selectRoadList")
	@ResponseBody
	public Map<String, Object> selectRoadList(String name,int page,int rows,String sort,String order) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int count=roadService.selectForInt(name);
		List<Road> sdList=roadService.selectList(name, page, rows, sort, order);

		jsonMap.put("total", count);
		jsonMap.put("rows", sdList);
			
		return jsonMap;
	}
	
	@RequestMapping(value="/addRoad",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String addRoad(Road road,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			int count=roadService.add(road);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("添加路名失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("添加路名成功！");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/editRoad",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String editRoad(Road road,
			HttpServletRequest request) {

		String json=null;
		try {
			PlanResult plan=new PlanResult();
			int count=roadService.edit(road);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("编辑路名失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("编辑路名成功！");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
