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
}
