package com.electronicGuideSD.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.electronicGuideSD.controller.BackgroundController;

@Controller
@RequestMapping(BackgroundController.MODULE_NAME)
public class BackgroundController {

	public static final String MODULE_NAME="/background";
	public static final String SERVER_PATH_CQ="http://192.168.2.166:8080/ElectronicGuideCQ";
	
	/**
	 * Ìø×ªÖÁµÇÂ¼Ò³Ãæ
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login() {
		return "redirect:"+SERVER_PATH_CQ+MODULE_NAME+"/login";
	}
}
