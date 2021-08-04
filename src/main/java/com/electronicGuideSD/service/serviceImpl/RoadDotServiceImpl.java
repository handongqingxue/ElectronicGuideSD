package com.electronicGuideSD.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class RoadDotServiceImpl implements RoadDotService {

	@Autowired
	private RoadDotMapper roadDotDao;

	@Override
	public List<RoadDot> select() {
		// TODO Auto-generated method stub
		return roadDotDao.select();
	}
}
