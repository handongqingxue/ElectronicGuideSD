package com.electronicGuideSD.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;
import com.electronicGuideSD.util.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(RoadController.MODULE_NAME)
public class RoadController {

	@Autowired
	private RoadService roadService;
	@Autowired
	private RoadStageService roadStageService;
	@Autowired
	private ScenicPlaceService scenicPlaceService;
	@Autowired
	private TextLabelService textLabelService;
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
	
	@RequestMapping(value="/road/detail")
	public String goRoadDetail(HttpServletRequest request) {

		Road road = roadService.selectById(request.getParameter("id"));
		
		request.setAttribute("road", road);
		
		return MODULE_NAME+"/road/detail";
	}
	
	@RequestMapping(value="/roadStage/add")
	public String goRoadStageAdd(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL),request);

		return MODULE_NAME+"/roadStage/add";
	}
	
	@RequestMapping(value="/roadStage/edit")
	public String goRoadStageEdit(HttpServletRequest request) {
		
		String id = request.getParameter("id");
		RoadStage roadStage = roadStageService.selectById(id);
		request.setAttribute("roadStage", roadStage);
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.OTHER_RS,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL),request);

		return MODULE_NAME+"/roadStage/edit";
	}
	
	@RequestMapping(value="/roadStage/list")
	public String goRoadStageList(HttpServletRequest request) {

		return MODULE_NAME+"/roadStage/list";
	}
	
	@RequestMapping(value="/roadStage/detail")
	public String goRoadStageDetail(HttpServletRequest request) {
		
		String id = request.getParameter("id");
		RoadStage roadStage = roadStageService.selectById(id);
		
		List<RoadStage> allRSList = roadStageService.selectCBBData();
		Map<String, String> rsNameMap = RoadStageUtil.initRSNameMap(allRSList);
		String backCrossRSIds = roadStage.getBackCrossRSIds();
		if(roadStage.getBackIsCross()&&!StringUtils.isEmpty(backCrossRSIds)) {
			String backCrossRSNames = RoadStageUtil.getRSNameFromMapByIds(rsNameMap,backCrossRSIds);
			roadStage.setBackCrossRSNames(backCrossRSNames);
		}
		String frontCrossRSIds = roadStage.getFrontCrossRSIds();
		if(roadStage.getFrontIsCross()&&!StringUtils.isEmpty(frontCrossRSIds)) {
			String frontCrossRSNames = RoadStageUtil.getRSNameFromMapByIds(rsNameMap,frontCrossRSIds);
			roadStage.setFrontCrossRSNames(frontCrossRSNames);
		}
		
		request.setAttribute("roadStage", roadStage);
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.OTHER_RS,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL),request);

		return MODULE_NAME+"/roadStage/detail";
	}
	
	@RequestMapping(value="/textLabel/add")
	public String goTextLabelAdd(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL),request);

		return MODULE_NAME+"/textLabel/add";
	}
	
	@RequestMapping(value="/textLabel/edit")
	public String goTextLabelEdit(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.OTHER_TL),request);

		TextLabel textLabel = textLabelService.selectById(request.getParameter("id"));
		
		request.setAttribute("textLabel", textLabel);
		
		return MODULE_NAME+"/textLabel/edit";
	}
	
	@RequestMapping(value="/textLabel/list")
	public String goTextLabelList(HttpServletRequest request) {

		return MODULE_NAME+"/textLabel/list";
	}
	
	@RequestMapping(value="/textLabel/detail")
	public String goTextLabelDetail(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.OTHER_TL),request);

		TextLabel textLabel = textLabelService.selectById(request.getParameter("id"));
		
		request.setAttribute("textLabel", textLabel);
		
		return MODULE_NAME+"/textLabel/detail";
	}
	
	@RequestMapping(value="/selectRoadList")
	@ResponseBody
	public Map<String, Object> selectRoadList(String name,int page,int rows,String sort,String order) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int count=roadService.selectForInt(name);
		List<Road> roadList=roadService.selectList(name, page, rows, sort, order);

		jsonMap.put("total", count);
		jsonMap.put("rows", roadList);
			
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
				plan.setMsg("���·��ʧ�ܣ�");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("���·���ɹ���");
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
				plan.setMsg("�༭·��ʧ�ܣ�");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("�༭·���ɹ���");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/addRoadStage",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String addRoadStage(RoadStage roadStage,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			int count=roadStageService.add(roadStage);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("���·����ʧ�ܣ�");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("���·�����ɹ���");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/editRoadStage",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String editRoadStage(RoadStage roadStage,
			HttpServletRequest request) {

		String json=null;
		try {
			PlanResult plan=new PlanResult();
			int count=roadStageService.edit(roadStage);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("�༭·����ʧ�ܣ�");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("�༭·�����ɹ���");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/selectRoadStageList")
	@ResponseBody
	public Map<String, Object> selectRoadStageList(String roadName,String name,int page,int rows,String sort,String order) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int count=roadStageService.selectForInt(roadName,name);
		List<RoadStage> roadStageList=roadStageService.selectList(roadName,name, page, rows, sort, order);

		jsonMap.put("total", count);
		jsonMap.put("rows", roadStageList);
			
		return jsonMap;
	}
	
	@RequestMapping(value="/selectRoadCBBData")
	@ResponseBody
	public Map<String, Object> selectRoadCBBData() {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Road> roadList = roadService.selectRoadCBBData();

		if(roadList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "���޵�·");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("roadList", roadList);
		}
		return jsonMap;
	}
	
	@RequestMapping(value="/selectRoadStageCBBData")
	@ResponseBody
	public Map<String, Object> selectRoadStageCBBData() {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<RoadStage> roadStageList = roadStageService.selectCBBData();

		if(roadStageList.size()==0) {
			jsonMap.put("status", "no");
			jsonMap.put("message", "����·��");
		}
		else {
			jsonMap.put("status", "ok");
			jsonMap.put("roadStageList", roadStageList);
		}
		return jsonMap;
	}
	
	@RequestMapping(value="/selectTextLabelList")
	@ResponseBody
	public Map<String, Object> selectTextLabelList(String name,int page,int rows,String sort,String order) {
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int count=textLabelService.selectForInt(name);
		List<TextLabel> textLabelList=textLabelService.selectList(name, page, rows, sort, order);

		jsonMap.put("total", count);
		jsonMap.put("rows", textLabelList);
			
		return jsonMap;
	}

	@RequestMapping(value="/addTextLabel",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String addTextLabel(TextLabel textLabel,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			int count=textLabelService.add(textLabel);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("��ӱ�ǩʧ�ܣ�");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("��ӱ�ǩ�ɹ���");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value="/editTextLabel",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String editTextLabel(TextLabel textLabel,
			HttpServletRequest request) {

		String json=null;
		try {
			PlanResult plan=new PlanResult();
			int count=textLabelService.edit(textLabel);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("�༭��ǩʧ�ܣ�");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("�༭��ǩ�ɹ���");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
