package com.electronicGuideSD.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.*;

public interface TextLabelMapper {

	List<TextLabel> selectWAList();

	int selectForInt(@Param("name")String name);

	List<TextLabel> selectList(@Param("name")String name, @Param("start")int start, @Param("rows")int rows, String sort, String order);

	int add(TextLabel textLabel);

	List<TextLabel> selectOtherList(@Param("id")String id);

	TextLabel selectById(@Param("id")String id);

}
