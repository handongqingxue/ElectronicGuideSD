package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.*;

public interface TextLabelService {

	List<TextLabel> selectList();

	int selectForInt(String name);

	List<TextLabel> selectList(String name, int page, int rows, String sort, String order);

	int add(TextLabel textLabel);

}
