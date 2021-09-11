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
			int count=0;
			JSONObject a=new JSONObject();
			a.put("x", roadStage.getBackX());//���ô���ӵ�·�κ󷽵�x����
			a.put("y", roadStage.getBackY());//���ô���ӵ�·�κ󷽵�y����
			JSONObject b=new JSONObject();
			b.put("x", roadStage.getFrontX());//���ô���ӵ�·��ǰ����x����
			b.put("y", roadStage.getFrontY());//���ô���ӵ�·��ǰ����y����
			List<RoadStage> pprsList = RoadStageUtil.selectPublicPointRSList(roadStageService,a,b,null);
			int pprsListSize=pprsList.size();
			if(pprsListSize==0)//��û��������·���н����·�Σ���ô��ֱ����Ӵ����·��
				count=roadStageService.add(roadStage);
			else {//���еĻ���ִ����������
				List<Integer> pprsRoadIdList=new ArrayList<>();
				String deleteIds="";
				for (int i = 0; i < pprsListSize; i++) {
					RoadStage pprs = pprsList.get(i);
					if(!pprsRoadIdList.contains(pprs.getRoadId()))
						pprsRoadIdList.add(pprs.getRoadId());//�Ѽ��н����·�μ��ϵ�������·id
					System.out.println("pprs:backX="+pprs.getBackX()+",backY="+pprs.getBackY()+",crossX="+pprs.getCrossX()+",crossY="+pprs.getCrossY()+",id="+pprs.getId()+",roadId="+pprs.getRoadId());
					org.json.JSONObject dividePPRSJO = RoadStageUtil.dividePPRoadStage(roadStage,pprs);//���ݽ���ָ�·��Ϊ����
					RoadStage prePPRS = (RoadStage)(dividePPRSJO.get("preRS"));
					RoadStage sufPPRS = (RoadStage)(dividePPRSJO.get("sufRS"));
					System.out.println("prePPRS:backX="+prePPRS.getBackX()+",backY="+prePPRS.getBackY()+",frontX="+prePPRS.getFrontX()+",frontY="+prePPRS.getFrontY()+",id="+pprs.getId());
					System.out.println("sufPPRS:backX="+sufPPRS.getBackX()+",backY="+sufPPRS.getBackY()+",frontX="+sufPPRS.getFrontX()+",frontY="+sufPPRS.getFrontY()+",id="+pprs.getId());
					//ƴ����Ҫɾ����·�ε�id
					deleteIds+=","+pprs.getId();
					//��ӷָ�������·��
					roadStageService.add(prePPRS);//���ָ�����ɵ�ǰ��·����ӵ����ݿ��
					roadStageService.add(sufPPRS);//���ָ�����ɵĺ��·����ӵ����ݿ��
				}
				//ɾ��ԭ��δ�ָ�֮ǰ��·�Σ����ڱ��ָ����·�Σ���·��û���ˣ�
				roadStageService.deleteByIds(deleteIds);
				List<RoadStage> divideRSList=RoadStageUtil.divideRoadStage(roadStage,pprsList);//�����н���ָ�����·�Σ��������ɶ���·��
				System.out.println("divideRSList="+divideRSList);
				System.out.println("pprsRoadIdList="+pprsRoadIdList);
				//ͨ�������ѷָ�����·�β������ݿ��
				for (RoadStage divideRS : divideRSList) {
					roadStageService.add(divideRS);
				}
				
				List<RoadStage> roadStageList = roadStageService.selectOtherList(null);//��ѯ����·��
				Map<String, Object> allRoadStageMap = RoadStageUtil.initAllRoadMap(roadStageList);//���ݵ�·id��������·�η�����
				List<RoadStage> connectRSList = null;
				//ƴ�ӡ����������ཻ·��
				for(int i = 0; i < pprsRoadIdList.size(); i++) {
					int pprsRoadId=pprsRoadIdList.get(i);
					connectRSList = RoadStageUtil.connectRoadStageInRoad(allRoadStageMap,pprsRoadId);//����ͬ��·���·�ΰ�ǰ������ƴ������
					//����ƴ�Ӻõ�ǰ��·�κ�����·�ε�������������¸õ�·�µ�ÿ��·������
					RoadStageUtil.updateRoadStageInRoad(connectRSList,roadStageList);
					System.out.println("connectRSList="+connectRSList);
					roadStageService.updateAttrInRoad(connectRSList);//�����º��ÿ����·�µ�·��ͬ�������ݿ��
				}
				//ƴ�ӡ����¸ղ���ӵ���·��
				connectRSList = RoadStageUtil.connectRoadStageInRoad(allRoadStageMap,roadStage.getRoadId());
				RoadStageUtil.updateRoadStageInRoad(connectRSList,roadStageList);
				System.out.println("connectRSList="+connectRSList);
				count=roadStageService.updateAttrInRoad(connectRSList);
			}
			
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
			int count=0;

			List<RoadStage> roadStageList = null;
			Map<String, Object> allRoadStageMap = null;
			List<RoadStage> connectRSList = null;
			
			JSONObject a=new JSONObject();
			a.put("x", roadStage.getBackX());//���ô���ӵ�·�κ󷽵�x����
			a.put("y", roadStage.getBackY());//���ô���ӵ�·�κ󷽵�y����
			JSONObject b=new JSONObject();
			b.put("x", roadStage.getFrontX());//���ô���ӵ�·��ǰ����x����
			b.put("y", roadStage.getFrontY());//���ô���ӵ�·��ǰ����y����
			List<RoadStage> pprsList = RoadStageUtil.selectPublicPointRSList(roadStageService,a,b,roadStage.getId());
			int pprsListSize=pprsList.size();
			if(pprsListSize==0) {//��û��������·���н����·�Σ���ô��ֱ����Ӵ����·��
				count=roadStageService.edit(roadStage);
				
				roadStageList = roadStageService.selectOtherList(null);//��ѯ����·��
				allRoadStageMap = RoadStageUtil.initAllRoadMap(roadStageList);//���ݵ�·id��������·�η�����
			}
			else {//���еĻ���ִ����������
				roadStageService.deleteByIds(roadStage.getId().toString());
				List<Integer> pprsRoadIdList=new ArrayList<>();
				String deleteIds="";
				for (int i = 0; i < pprsListSize; i++) {
					RoadStage pprs = pprsList.get(i);
					if(!pprsRoadIdList.contains(pprs.getRoadId()))
						pprsRoadIdList.add(pprs.getRoadId());//�Ѽ��н����·�μ��ϵ�������·id
					System.out.println("pprs:backX="+pprs.getBackX()+",backY="+pprs.getBackY()+",crossX="+pprs.getCrossX()+",crossY="+pprs.getCrossY()+",id="+pprs.getId()+",roadId="+pprs.getRoadId());
					org.json.JSONObject dividePPRSJO = RoadStageUtil.dividePPRoadStage(roadStage,pprs);//���ݽ���ָ�·��Ϊ����
					RoadStage prePPRS = (RoadStage)(dividePPRSJO.get("preRS"));
					RoadStage sufPPRS = (RoadStage)(dividePPRSJO.get("sufRS"));
					System.out.println("prePPRS:backX="+prePPRS.getBackX()+",backY="+prePPRS.getBackY()+",frontX="+prePPRS.getFrontX()+",frontY="+prePPRS.getFrontY()+",id="+pprs.getId());
					System.out.println("sufPPRS:backX="+sufPPRS.getBackX()+",backY="+sufPPRS.getBackY()+",frontX="+sufPPRS.getFrontX()+",frontY="+sufPPRS.getFrontY()+",id="+pprs.getId());
					//ƴ����Ҫɾ����·�ε�id
					deleteIds+=","+pprs.getId();
					//��ӷָ�������·��
					roadStageService.add(prePPRS);//���ָ�����ɵ�ǰ��·����ӵ����ݿ��
					roadStageService.add(sufPPRS);//���ָ�����ɵĺ��·����ӵ����ݿ��
				}
				//ɾ��ԭ��δ�ָ�֮ǰ��·�Σ����ڱ��ָ����·�Σ���·��û���ˣ�
				roadStageService.deleteByIds(deleteIds);
				List<RoadStage> divideRSList=RoadStageUtil.divideRoadStage(roadStage,pprsList);//�����н���ָ�����·�Σ��������ɶ���·��
				System.out.println("divideRSList="+divideRSList);
				System.out.println("pprsRoadIdList="+pprsRoadIdList);
				//ͨ�������ѷָ�����·�β������ݿ��
				for (RoadStage divideRS : divideRSList) {
					roadStageService.add(divideRS);
				}

				roadStageList = roadStageService.selectOtherList(null);//��ѯ����·��
				allRoadStageMap = RoadStageUtil.initAllRoadMap(roadStageList);//���ݵ�·id��������·�η�����
				
				//ƴ�ӡ����������ཻ·��
				for(int i = 0; i < pprsRoadIdList.size(); i++) {
					int pprsRoadId=pprsRoadIdList.get(i);
					connectRSList = RoadStageUtil.connectRoadStageInRoad(allRoadStageMap,pprsRoadId);//����ͬ��·���·�ΰ�ǰ������ƴ������
					//����ƴ�Ӻõ�ǰ��·�κ�����·�ε�������������¸õ�·�µ�ÿ��·������
					RoadStageUtil.updateRoadStageInRoad(connectRSList,roadStageList);
					System.out.println("connectRSList="+connectRSList);
					roadStageService.updateAttrInRoad(connectRSList);//�����º��ÿ����·�µ�·��ͬ�������ݿ��
				}
			}
			
			System.out.println("oldRoadId==="+roadStage.getOldRoadId());
			System.out.println("roadId==="+roadStage.getRoadId());
			
			connectRSList = RoadStageUtil.connectRoadStageInRoad(allRoadStageMap,roadStage.getOldRoadId());
			RoadStageUtil.updateRoadStageInRoad(connectRSList,roadStageList);
			System.out.println("connectRSList="+connectRSList);
			count=roadStageService.updateAttrInRoad(connectRSList);
			
			if(roadStage.getRoadId()!=roadStage.getOldRoadId()) {
				connectRSList = RoadStageUtil.connectRoadStageInRoad(allRoadStageMap,roadStage.getRoadId());
				RoadStageUtil.updateRoadStageInRoad(connectRSList,roadStageList);
				System.out.println("connectRSList="+connectRSList);
				count=roadStageService.updateAttrInRoad(connectRSList);
			}
			
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
