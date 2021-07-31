package com.electronicGuideSD.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class ToSpRouteDotServiceImpl implements ToSpRouteDotService {
	
	@Autowired
	private ToSpRouteDotMapper toSpRouteDotDao;

	@Override
	public int selectForInt(String name) {
		// TODO Auto-generated method stub
		return toSpRouteDotDao.selectForInt(name);
	}

	@Override
	public List<ToSpRouteDot> selectList(String name, int page, int rows, String sort, String order) {
		// TODO Auto-generated method stub
		return toSpRouteDotDao.selectList(name, (page-1)*rows, rows, sort, order);
	}

}
