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
	@Autowired
	private BusStopService busStopService;
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
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL,busStopService,EntityUtil.BUS_STOP),request);

		return MODULE_NAME+"/roadStage/add";
	}
	
	@RequestMapping(value="/roadStage/edit")
	public String goRoadStageEdit(HttpServletRequest request) {
		
		String id = request.getParameter("id");
		RoadStage roadStage = roadStageService.selectById(id);
		request.setAttribute("roadStage", roadStage);
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.OTHER_RS,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL,busStopService,EntityUtil.BUS_STOP),request);

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
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.OTHER_RS,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL,busStopService,EntityUtil.BUS_STOP),request);

		return MODULE_NAME+"/roadStage/detail";
	}
	
	@RequestMapping(value="/textLabel/add")
	public String goTextLabelAdd(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL,busStopService,EntityUtil.BUS_STOP),request);

		return MODULE_NAME+"/textLabel/add";
	}
	
	@RequestMapping(value="/textLabel/edit")
	public String goTextLabelEdit(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.OTHER_TL,busStopService,EntityUtil.BUS_STOP),request);

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
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.OTHER_TL,busStopService,EntityUtil.BUS_STOP),request);

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
	
	@RequestMapping(value="/addRoadStage",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String addRoadStage(RoadStage roadStage,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			int count=0;
			
			List<RoadStage> roadStageList = null;
			Map<String, Object> allRoadStageMap = null;
			
			JSONObject a=new JSONObject();
			a.put("x", roadStage.getBackX());//设置待添加的路段后方的x坐标
			a.put("y", roadStage.getBackY());//设置待添加的路段后方的y坐标
			JSONObject b=new JSONObject();
			b.put("x", roadStage.getFrontX());//设置待添加的路段前方的x坐标
			b.put("y", roadStage.getFrontY());//设置待添加的路段前方的y坐标
			List<RoadStage> pprsList = RoadStageUtil.selectPublicPointRSList(roadStageService,a,b,null);
			int pprsListSize=pprsList.size();
			if(pprsListSize==0) {//若没有与待添加路段有交点的路段，那么就直接添加待添加路段
				roadStageService.add(roadStage);
				
				roadStageList = roadStageService.selectOtherList(null);//查询所有路段
				allRoadStageMap = RoadStageUtil.initAllRoadMap(roadStageList);//根据道路id，将所有路段分组存放
			}
			else {//若有的话则执行下面代码块
				List<Integer> pprsRoadIdList=new ArrayList<>();
				String deleteIds="";
				for (int i = 0; i < pprsListSize; i++) {
					RoadStage pprs = pprsList.get(i);
					if(!pprsRoadIdList.contains(pprs.getRoadId()))
						pprsRoadIdList.add(pprs.getRoadId());//搜集有交点的路段集合的所属道路id
					System.out.println("pprs:backX="+pprs.getBackX()+",backY="+pprs.getBackY()+",crossX="+pprs.getCrossX()+",crossY="+pprs.getCrossY()+",id="+pprs.getId()+",roadId="+pprs.getRoadId());
					org.json.JSONObject dividePPRSJO = RoadStageUtil.dividePPRoadStage(roadStage,pprs);//根据交点分割路段为两段
					RoadStage prePPRS = (RoadStage)(dividePPRSJO.get("preRS"));
					RoadStage sufPPRS = (RoadStage)(dividePPRSJO.get("sufRS"));
					System.out.println("prePPRS:backX="+prePPRS.getBackX()+",backY="+prePPRS.getBackY()+",frontX="+prePPRS.getFrontX()+",frontY="+prePPRS.getFrontY()+",id="+pprs.getId());
					System.out.println("sufPPRS:backX="+sufPPRS.getBackX()+",backY="+sufPPRS.getBackY()+",frontX="+sufPPRS.getFrontX()+",frontY="+sufPPRS.getFrontY()+",id="+pprs.getId());
					//拼接需要删除的路段的id
					deleteIds+=","+pprs.getId();
					//添加分割后的两端路段
					roadStageService.add(prePPRS);//将分割后生成的前段路段添加到数据库表
					roadStageService.add(sufPPRS);//将分割后生成的后段路段添加到数据库表
				}
				//删除原来未分割之前的路段（现在被分割成新路段，旧路段没用了）
				roadStageService.deleteByIds(deleteIds);
				List<RoadStage> divideRSList=RoadStageUtil.divideRoadStage(roadStage,pprsList);//用所有交点分割待添加路段，生成若干段新路段
				System.out.println("divideRSList="+divideRSList);
				System.out.println("pprsRoadIdList="+pprsRoadIdList);
				//通过遍历把分割后的新路段插入数据库表
				for (RoadStage divideRS : divideRSList) {
					roadStageService.add(divideRS);
				}
				
				roadStageList = roadStageService.selectOtherList(null);//查询所有路段
				allRoadStageMap = RoadStageUtil.initAllRoadMap(roadStageList);//根据道路id，将所有路段分组存放
				
				//拼接、更新其他相交路段
				for(int i = 0; i < pprsRoadIdList.size(); i++) {
					int pprsRoadId=pprsRoadIdList.get(i);
					RoadStageUtil.updateAttrInRoad(roadStageService, allRoadStageMap, pprsRoadId, roadStageList);
				}
			}
			
			//拼接、更新刚才添加的新路段
			count=RoadStageUtil.updateAttrInRoad(roadStageService, allRoadStageMap, roadStage.getRoadId(), roadStageList);
			
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("添加路段名失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("添加路段名成功！");
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
			int count=0;

			List<RoadStage> roadStageList = null;
			Map<String, Object> allRoadStageMap = null;
			
			JSONObject a=new JSONObject();
			a.put("x", roadStage.getBackX());//设置待添加的路段后方的x坐标
			a.put("y", roadStage.getBackY());//设置待添加的路段后方的y坐标
			JSONObject b=new JSONObject();
			b.put("x", roadStage.getFrontX());//设置待添加的路段前方的x坐标
			b.put("y", roadStage.getFrontY());//设置待添加的路段前方的y坐标
			List<RoadStage> pprsList = RoadStageUtil.selectPublicPointRSList(roadStageService,a,b,roadStage.getId());
			int pprsListSize=pprsList.size();
			if(pprsListSize==0) {//若没有与待添加路段有交点的路段，那么就直接添加待添加路段
				count=roadStageService.edit(roadStage);
				
				roadStageList = roadStageService.selectOtherList(null);//查询所有路段
				allRoadStageMap = RoadStageUtil.initAllRoadMap(roadStageList);//根据道路id，将所有路段分组存放
			}
			else {//若有的话则执行下面代码块
				roadStageService.deleteByIds(roadStage.getId().toString());
				List<Integer> pprsRoadIdList=new ArrayList<>();
				String deleteIds="";
				for (int i = 0; i < pprsListSize; i++) {
					RoadStage pprs = pprsList.get(i);
					if(!pprsRoadIdList.contains(pprs.getRoadId()))
						pprsRoadIdList.add(pprs.getRoadId());//搜集有交点的路段集合的所属道路id
					System.out.println("pprs:backX="+pprs.getBackX()+",backY="+pprs.getBackY()+",crossX="+pprs.getCrossX()+",crossY="+pprs.getCrossY()+",id="+pprs.getId()+",roadId="+pprs.getRoadId());
					org.json.JSONObject dividePPRSJO = RoadStageUtil.dividePPRoadStage(roadStage,pprs);//根据交点分割路段为两段
					RoadStage prePPRS = (RoadStage)(dividePPRSJO.get("preRS"));
					RoadStage sufPPRS = (RoadStage)(dividePPRSJO.get("sufRS"));
					System.out.println("prePPRS:backX="+prePPRS.getBackX()+",backY="+prePPRS.getBackY()+",frontX="+prePPRS.getFrontX()+",frontY="+prePPRS.getFrontY()+",id="+pprs.getId());
					System.out.println("sufPPRS:backX="+sufPPRS.getBackX()+",backY="+sufPPRS.getBackY()+",frontX="+sufPPRS.getFrontX()+",frontY="+sufPPRS.getFrontY()+",id="+pprs.getId());
					//拼接需要删除的路段的id
					deleteIds+=","+pprs.getId();
					//添加分割后的两端路段
					roadStageService.add(prePPRS);//将分割后生成的前段路段添加到数据库表
					roadStageService.add(sufPPRS);//将分割后生成的后段路段添加到数据库表
				}
				//删除原来未分割之前的路段（现在被分割成新路段，旧路段没用了）
				roadStageService.deleteByIds(deleteIds);
				List<RoadStage> divideRSList=RoadStageUtil.divideRoadStage(roadStage,pprsList);//用所有交点分割待添加路段，生成若干段新路段
				System.out.println("divideRSList="+divideRSList);
				System.out.println("pprsRoadIdList="+pprsRoadIdList);
				//通过遍历把分割后的新路段插入数据库表
				for (RoadStage divideRS : divideRSList) {
					roadStageService.add(divideRS);
				}

				roadStageList = roadStageService.selectOtherList(null);//查询所有路段
				allRoadStageMap = RoadStageUtil.initAllRoadMap(roadStageList);//根据道路id，将所有路段分组存放
				
				//拼接、更新其他相交路段
				for(int i = 0; i < pprsRoadIdList.size(); i++) {
					int pprsRoadId=pprsRoadIdList.get(i);
					RoadStageUtil.updateAttrInRoad(roadStageService,allRoadStageMap,pprsRoadId,roadStageList);
				}
			}
			
			System.out.println("oldRoadId==="+roadStage.getOldRoadId());
			System.out.println("roadId==="+roadStage.getRoadId());
			
			count=RoadStageUtil.updateAttrInRoad(roadStageService,allRoadStageMap,roadStage.getOldRoadId(),roadStageList);
			
			if(roadStage.getRoadId()!=roadStage.getOldRoadId()) {
				count=RoadStageUtil.updateAttrInRoad(roadStageService,allRoadStageMap,roadStage.getRoadId(),roadStageList);
			}
			
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("编辑路段名失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("编辑路段名成功！");
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
			jsonMap.put("message", "暂无道路");
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
			jsonMap.put("message", "暂无路段");
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
				plan.setMsg("添加标签失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("添加标签成功！");
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
				plan.setMsg("编辑标签失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("编辑标签成功！");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	
	@RequestMapping(value="/checkRSIfConnectInRoad",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String checkRSIfConnectInRoad(float backX,float backY,float frontX,float frontY,int roadId) {
		String json=null;
		PlanResult plan=new PlanResult();
		boolean bool=roadStageService.checkIfConnectInRoad(backX,backY,frontX,frontY,roadId);
		if(bool) {
			plan.setStatus(1);
			plan.setMsg("验证成功！");
			json=JsonUtil.getJsonFromObject(plan);
		}
		else {
			plan.setStatus(0);
			plan.setMsg("路段必须与道路内其他路段相连！");
			json=JsonUtil.getJsonFromObject(plan);
		}
		return json;
	}

}
