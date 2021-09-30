package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.*;

public interface BusNosStopService {

	List<BusNosStop> selectList(String name, int busNoId);

	int add(BusNosStop busNosStop);

}
