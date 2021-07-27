package com.electronicGuideSD.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class ScenicPlaceServiceImpl implements ScenicPlaceService {

	@Autowired
	private ScenicPlaceMapper scenicPlaceDao;

	@Override
	public int selectForInt(String name) {
		// TODO Auto-generated method stub
		return scenicPlaceDao.selectForInt(name);
	}

	@Override
	public List<ScenicPlace> selectList(String name, int page, int rows, String sort, String order) {
		// TODO Auto-generated method stub
		return scenicPlaceDao.selectList(name, (page-1)*rows, rows, sort, order);
	}

	@Override
	public int add(ScenicPlace scenicPlace) {
		// TODO Auto-generated method stub
		return scenicPlaceDao.add(scenicPlace);
	}

	@Override
	public ScenicPlace selectById(String id) {
		// TODO Auto-generated method stub
		return scenicPlaceDao.selectById(id);
	}

	@Override
	public int edit(ScenicPlace scenicPlace) {
		// TODO Auto-generated method stub
		return scenicPlaceDao.edit(scenicPlace);
	}

	@Override
	public List<ScenicPlace> selectList() {
		// TODO Auto-generated method stub
		return scenicPlaceDao.selectWAList();
	}
}
