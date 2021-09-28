package com.electronicGuideSD.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class BusNosStopServiceImpl implements BusNosStopService {

	@Autowired
	private BusNosStopMapper busNosStopDao;

	@Override
	public List<BusNosStop> selectList(String name, int busNoId) {
		// TODO Auto-generated method stub
		return busNosStopDao.selectList(name, busNoId);
	}

	@Override
	public List<BusNosStop> selectPreBnsCBBData() {
		// TODO Auto-generated method stub
		return null;
	}
}
