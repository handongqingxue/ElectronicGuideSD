package com.electronicGuideSD.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class BusStopServiceImpl implements BusStopService {

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
		return busStopDao.selectList(name, (page-1)*rows, rows, sort, order);
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
}
