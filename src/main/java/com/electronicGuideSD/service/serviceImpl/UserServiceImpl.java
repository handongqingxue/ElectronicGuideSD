package com.electronicGuideSD.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.User;
import com.electronicGuideSD.service.*;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userDao;

	@Override
	public int edit(User user) {
		// TODO Auto-generated method stub
		int count=0;
		user.setHeadImgUrl("http://www.qrcodesy.com:8080"+user.getHeadImgUrl());
		if(userDao.getCountById(user.getId())==0)
			count=userDao.add(user);
		else
			count=userDao.edit(user);
		return count;
	}
}
