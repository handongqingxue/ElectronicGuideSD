package com.electronicGuideSD.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class BusNoServiceImpl implements BusNoService {

	@Autowired
	private BusNoMapper busNoDao;

	@Override
	public int selectForInt(String name) {
		// TODO Auto-generated method stub
		return busNoDao.selectForInt(name);
	}

	@Override
	public List<BusNo> selectList(String name, int page, int rows, String sort, String order) {
		// TODO Auto-generated method stub
		return busNoDao.selectList(name, (page-1)*rows, rows, sort, order);
	}

	@Override
	public int add(BusNo busNo) {
		// TODO Auto-generated method stub
		return busNoDao.add(busNo);
	}

	@Override
	public BusNo selectById(String id) {
		// TODO Auto-generated method stub
		return busNoDao.selectById(id);
	}

	@Override
	public int edit(BusNo busNo) {
		// TODO Auto-generated method stub
		return busNoDao.edit(busNo);
	}

	@Override
	public List<BusNo> selectCBBData() {
		// TODO Auto-generated method stub
		return busNoDao.selectCBBData();
	}
}
