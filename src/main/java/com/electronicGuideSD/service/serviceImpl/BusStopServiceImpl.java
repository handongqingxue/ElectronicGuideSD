package com.electronicGuideSD.service.serviceImpl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;
import com.electronicGuideSD.util.BusUtil;

@Service
public class BusStopServiceImpl implements BusStopService {

	@Autowired
	private BusNoMapper busNoDao;
	@Autowired
	private BusStopMapper busStopDao;

	@Override
	public int selectForInt(String name) {
		// TODO Auto-generated method stub
		return busStopDao.selectForInt(name);
	}

	@Override
	public List<BusStop> selectList(String name, int page, int rows, String sort, String order) {
		// TODO Auto-generated method stub
		List<BusStop> busStopList = busStopDao.selectList(name, (page-1)*rows, rows, sort, order);
		List<BusNo> allBNList = busNoDao.selectCBBData();
		Map<String, String> bsNameMap = BusUtil.initBSNameMap(allBNList);
		for (BusStop busStop : busStopList) {
			String busNoIds = busStop.getBusNoIds();
			String busNoNames = BusUtil.getBSNameFromMapByIds(bsNameMap,busNoIds);
			busStop.setBusNoNames(busNoNames);
		}
		return busStopList;
	}

	@Override
	public List<BusStop> selectList() {
		// TODO Auto-generated method stub
		return busStopDao.selectWAList();
	}

	@Override
	public int add(BusStop busStop) {
		// TODO Auto-generated method stub
		return busStopDao.add(busStop);
	}

	@Override
	public int edit(BusStop busStop) {
		// TODO Auto-generated method stub
		return busStopDao.edit(busStop);
	}

	@Override
	public BusStop selectById(String id) {
		// TODO Auto-generated method stub
		return busStopDao.selectById(id);
	}

	@Override
	public List<BusStop> selectOtherList(String id) {
		// TODO Auto-generated method stub
		return busStopDao.selectOtherList(id);
	}

	@Override
	public List<BusStop> selectBusNosStopCBBData() {
		// TODO Auto-generated method stub
		return busStopDao.selectBusNosStopCBBData();
	}
}
