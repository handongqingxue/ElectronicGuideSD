package com.electronicGuideSD.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class EntityTypeServiceImpl implements EntityTypeService {

	@Autowired
	private EntityTypeMapper entityTypeDao;

	@Override
	public List<EntityType> selectCBBData() {
		// TODO Auto-generated method stub
		return entityTypeDao.selectCBBData();
	}
}
