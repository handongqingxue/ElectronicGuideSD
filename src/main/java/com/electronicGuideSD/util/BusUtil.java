package com.electronicGuideSD.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.electronicGuideSD.entity.*;

public class BusUtil {

	public static Map<String, String> initBSNameMap(List<BusNo> busNoList) {
		// TODO Auto-generated method stub
		Map<String,String> bsNameMap=new HashMap<>();
		for (BusNo busNo : busNoList) {
			bsNameMap.put("busNo"+busNo.getId(), busNo.getName());
		}
		return bsNameMap;
	}

	public static String getBSNameFromMapByIds(Map<String, String> bsNameMap, String ids) {
		// TODO Auto-generated method stub
		String[] idArr = ids.split(",");
		String bsNames="";
		for (String id : idArr) {
			String bsName = bsNameMap.get("busNo"+id);
			bsNames+=","+bsName;
		}
		return bsNames.substring(1);
	}

}
