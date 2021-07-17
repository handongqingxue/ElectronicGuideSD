package com.electronicGuideSD.dao;

import org.apache.ibatis.annotations.Param;

import com.electronicGuideSD.entity.*;

public interface UserMapper {

	public User get(User user);

	public int add(User user);

	public int edit(User user);

	public int getCountById(@Param("id")Integer id);
}
