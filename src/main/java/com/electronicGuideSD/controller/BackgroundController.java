package com.electronicGuideSD.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.util.*;
import com.electronicGuideSD.controller.BackgroundController;

@Controller
@RequestMapping(BackgroundController.MODULE_NAME)
public class BackgroundController {

	public static final String MODULE_NAME="/background";
	//public static final String SERVER_PATH_CQ="http://192.168.2.166:8080/ElectronicGuideCQ";
	public static final String SERVER_PATH_CQ="http://localhost:8080/ElectronicGuideCQ";
	public static final String SERVER_PATH_SD="http://localhost:8080/ElectronicGuideSD";
	
	/**
	 * ��ת����¼ҳ��
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login() {
		return "redirect:"+SERVER_PATH_CQ+MODULE_NAME+"/login";
	}

	@RequestMapping(value="/loginFromCQ")
	public String loginFromCQ(String userName,String password,HttpServletRequest request) {

		UsernamePasswordToken token = new UsernamePasswordToken(userName,password);  
		Subject currentUser = SecurityUtils.getSubject();  
		if (!currentUser.isAuthenticated()){
			//ʹ��shiro����֤  
			token.setRememberMe(true);  
			currentUser.login(token);//��֤��ɫ��Ȩ��  
			
			HttpSession session=request.getSession();
			User user=(User)SecurityUtils.getSubject().getPrincipal();
			session.setAttribute("user", user);
		}
		return "redirect:"+SERVER_PATH_SD+MODULE_NAME+"/user/info/info";
	}
}
