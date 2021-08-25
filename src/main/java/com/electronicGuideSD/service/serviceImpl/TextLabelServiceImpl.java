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

	@Override
	public int selectForInt(String name) {
		// TODO Auto-generated method stub
		return textLabelDao.selectForInt(name);
	}

	@Override
	public List<TextLabel> selectList(String name, int page, int rows, String sort, String order) {
		// TODO Auto-generated method stub
		return textLabelDao.selectList(name, (page-1)*rows, rows, sort, order);
	}

	@Override
	public int add(TextLabel textLabel) {
		// TODO Auto-generated method stub
		return textLabelDao.add(textLabel);
	}

	@Override
	public List<TextLabel> selectOtherList(String id) {
		// TODO Auto-generated method stub
		return textLabelDao.selectOtherList(id);
	}
}
