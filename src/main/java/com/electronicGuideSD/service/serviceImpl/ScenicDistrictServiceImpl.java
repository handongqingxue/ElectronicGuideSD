package com.electronicGuideSD.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class ScenicDistrictServiceImpl implements ScenicDistrictService {
	
	@Autowired
	private ScenicDistrictMapper scenicDistrictDao;

	@Override
	public int edit(ScenicDistrict scenicDistrict) {
		// TODO Auto-generated method stub
		int count=0;
		if(scenicDistrictDao.getCount()==0)
			count=scenicDistrictDao.add(scenicDistrict);
		else
			count=scenicDistrictDao.edit(scenicDistrict);
		return count;
	}

}
