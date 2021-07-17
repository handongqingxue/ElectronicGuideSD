package com.electronicGuideSD.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(UserController.MODULE_NAME)
public class UserController {

	public static final String MODULE_NAME="/background/user";
	
	@RequestMapping(value="/info/info")
	public String goInfoInfo(HttpServletRequest request) {

		return MODULE_NAME+"/info/info";
	}
}
