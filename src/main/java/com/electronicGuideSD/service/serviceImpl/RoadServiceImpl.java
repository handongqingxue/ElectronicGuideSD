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
	private RoadMapper roadDao;

	@Override
	public int selectForInt(String name) {
		// TODO Auto-generated method stub
		return roadDao.selectForInt(name);
	}

	@Override
	public List<Road> selectList(String name, int page, int rows, String sort, String order) {
		// TODO Auto-generated method stub
		return roadDao.selectList(name, (page-1)*rows, rows, sort, order);
	}

	@Override
	public int add(Road road) {
		// TODO Auto-generated method stub
		return roadDao.add(road);
	}

	@Override
	public Road selectById(String id) {
		// TODO Auto-generated method stub
		return roadDao.selectById(id);
	}

	@Override
	public int edit(Road road) {
		// TODO Auto-generated method stub
		return roadDao.edit(road);
	}

}
