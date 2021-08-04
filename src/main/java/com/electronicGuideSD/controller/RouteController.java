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
@RequestMapping(RouteController.MODULE_NAME)
public class RouteController {

	@Autowired
	private ToSpRouteService toSpRouteDotService;
	public static final String MODULE_NAME="/background/route";
	
	@RequestMapping(value="/toSp/add")
	public String goToSpAdd(HttpServletRequest request) {

		return MODULE_NAME+"/toSp/add";
	}
	
	@RequestMapping(value="/toSp/list")
	public String goToSpList(HttpServletRequest request) {

		return MODULE_NAME+"/toSp/list";
	}
	
	@RequestMapping(value="/selectToSpList")
	@ResponseBody
	public Map<String, Object> selectToSpList(String name,int page,int rows,String sort,String order) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int count=toSpRouteDotService.selectForInt(name);
		List<ToSpRoute> sdList=toSpRouteDotService.selectList(name, page, rows, sort, order);

		jsonMap.put("total", count);
		jsonMap.put("rows", sdList);
			
		return jsonMap;
	}
	
	@RequestMapping(value="/addTSPRD",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String addTSPRD(ToSpRoute toSpRouteDot,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			int count=toSpRouteDotService.add(toSpRouteDot);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("添加路线点失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("添加路线点成功！");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
