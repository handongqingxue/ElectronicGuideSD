package com.electronicGuideSD.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class RoadServiceImpl implements RoadService {
	
	@Autowired
	private RoadMapper toSpRouteDao;

	@Override
	public int selectForInt(String name) {
		// TODO Auto-generated method stub
		return toSpRouteDao.selectForInt(name);
	}

	@Override
	public List<ToSpRoute> selectList(String name, int page, int rows, String sort, String order) {
		// TODO Auto-generated method stub
		return toSpRouteDao.selectList(name, (page-1)*rows, rows, sort, order);
	}

	@Override
	public int add(ToSpRoute toSpRouteDot) {
		// TODO Auto-generated method stub
		return toSpRouteDao.add(toSpRouteDot);
	}

}
