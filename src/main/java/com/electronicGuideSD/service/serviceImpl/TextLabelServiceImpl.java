package com.electronicGuideSD.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class TextLabelServiceImpl implements TextLabelService {

	@Autowired
	private TextLabelMapper textLabelDao;

	@Override
	public List<TextLabel> selectList() {
		// TODO Auto-generated method stub
		return textLabelDao.selectWAList();
	}
}
