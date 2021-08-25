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

import com.electronicGuideSD.service.*;
import com.electronicGuideSD.util.*;

import net.sf.json.JSONObject;

import com.electronicGuideSD.entity.*;

@Controller
@RequestMapping(ScenicPlaceController.MODULE_NAME)
public class ScenicPlaceController {

	@Autowired
	private ScenicPlaceService scenicPlaceService;
	@Autowired
	private RoadStageService roadStageService;
	@Autowired
	private TextLabelService textLabelService;
	public static final String MODULE_NAME="/background/scenicPlace";
	
	@RequestMapping(value="/scenicPlace/add")
	public String goScenicPlaceAdd(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.SCENIC_PLACE,textLabelService,EntityUtil.TEXT_LABEL),request);

		return MODULE_NAME+"/scenicPlace/add";
	}

	@RequestMapping(value="/scenicPlace/edit")
	public String goScenicPlaceEdit(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.OTHER_SP,textLabelService,EntityUtil.TEXT_LABEL),request);
		
		ScenicPlace sp = scenicPlaceService.selectById(request.getParameter("id"));
		request.setAttribute("scenicPlace", sp);
		
		return MODULE_NAME+"/scenicPlace/edit";
	}
	
	@RequestMapping(value="/scenicPlace/list")
	public String goScenicPlaceList(HttpServletRequest request) {

		return MODULE_NAME+"/scenicPlace/list";
	}

	@RequestMapping(value="/scenicPlace/detail")
	public String goScenicPlaceDetail(HttpServletRequest request) {
		
		EntityUtil.putJAStrInRequest(EntityUtil.initServiceParamList(roadStageService,EntityUtil.ROAD_STAGE,scenicPlaceService,EntityUtil.OTHER_SP,textLabelService,EntityUtil.TEXT_LABEL),request);
		
		ScenicPlace sp = scenicPlaceService.selectById(request.getParameter("id"));
		request.setAttribute("scenicPlace", sp);
		
		return MODULE_NAME+"/scenicPlace/detail";
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

	@RequestMapping(value="/addScenicPlace",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String addScenicPlace(ScenicPlace scenicPlace,
			@RequestParam(value="picUrl_file",required=false) MultipartFile picUrl_file,
			@RequestParam(value="simpleIntroVoiceUrl_file",required=false) MultipartFile simpleIntroVoiceUrl_file,
			@RequestParam(value="detailIntroVoiceUrl_file",required=false) MultipartFile detailIntroVoiceUrl_file,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			MultipartFile[] fileArr=new MultipartFile[3];
			fileArr[0]=picUrl_file;
			fileArr[1]=simpleIntroVoiceUrl_file;
			fileArr[2]=detailIntroVoiceUrl_file;
			for (int i = 0; i < fileArr.length; i++) {
				String jsonStr = null;
				if(fileArr[i].getSize()>0) {
					String folder=null;
					switch (i) {
					case 0:
						folder="ScenicPlacePic";
						break;
					case 1:
					case 2:
						folder="ScenicPlaceVoice";
						break;
					}
					jsonStr = FileUploadUtils.appUploadContentImg(request,fileArr[i],folder);
					JSONObject fileJson = JSONObject.fromObject(jsonStr);
					if("成功".equals(fileJson.get("msg"))) {
						JSONObject dataJO = (JSONObject)fileJson.get("data");
						switch (i) {
						case 0:
							scenicPlace.setPicUrl(dataJO.get("src").toString());
							break;
						case 1:
							scenicPlace.setSimpleIntroVoiceUrl(dataJO.get("src").toString());
							break;
						case 2:
							scenicPlace.setDetailIntroVoiceUrl(dataJO.get("src").toString());
							break;
						}
					}
				}
			}
			int count=scenicPlaceService.add(scenicPlace);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("添加景点失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("添加景点成功！");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value="/editScenicPlace",produces="plain/text; charset=UTF-8")
	@ResponseBody
	public String editScenicPlace(ScenicPlace scenicPlace,
			@RequestParam(value="picUrl_file",required=false) MultipartFile picUrl_file,
			@RequestParam(value="simpleIntroVoiceUrl_file",required=false) MultipartFile simpleIntroVoiceUrl_file,
			@RequestParam(value="detailIntroVoiceUrl_file",required=false) MultipartFile detailIntroVoiceUrl_file,
			HttpServletRequest request) {

		String json=null;;
		try {
			PlanResult plan=new PlanResult();
			MultipartFile[] fileArr=new MultipartFile[3];
			fileArr[0]=picUrl_file;
			fileArr[1]=simpleIntroVoiceUrl_file;
			fileArr[2]=detailIntroVoiceUrl_file;
			for (int i = 0; i < fileArr.length; i++) {
				String jsonStr = null;
				if(fileArr[i].getSize()>0) {
					String folder=null;
					switch (i) {
					case 0:
						folder="ScenicPlacePic";
						break;
					case 1:
					case 2:
						folder="ScenicPlaceVoice";
						break;
					}
					jsonStr = FileUploadUtils.appUploadContentImg(request,fileArr[i],folder);
					JSONObject fileJson = JSONObject.fromObject(jsonStr);
					if("成功".equals(fileJson.get("msg"))) {
						JSONObject dataJO = (JSONObject)fileJson.get("data");
						switch (i) {
						case 0:
							scenicPlace.setPicUrl(dataJO.get("src").toString());
							break;
						case 1:
							scenicPlace.setSimpleIntroVoiceUrl(dataJO.get("src").toString());
							break;
						case 2:
							scenicPlace.setDetailIntroVoiceUrl(dataJO.get("src").toString());
							break;
						}
					}
				}
			}
			int count=scenicPlaceService.edit(scenicPlace);
			if(count==0) {
				plan.setStatus(0);
				plan.setMsg("编辑景点失败！");
				json=JsonUtil.getJsonFromObject(plan);
			}
			else {
				plan.setStatus(1);
				plan.setMsg("编辑景点成功！");
				json=JsonUtil.getJsonFromObject(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
