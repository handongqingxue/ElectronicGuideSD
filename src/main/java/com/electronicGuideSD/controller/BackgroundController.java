package com.electronicGuideSD.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.util.*;
import com.electronicGuideSD.service.*;
import com.electronicGuideSD.controller.BackgroundController;

@Controller
@RequestMapping(BackgroundController.MODULE_NAME)
public class BackgroundController {

	@Autowired
	private UserService userService;
	public static final String MODULE_NAME="/background";
	//public static final String SERVER_PATH_CQ="http://www.qrcodesy.com:8080/ElectronicGuideCQ";
	public static final String SERVER_PATH_CQ="http://localhost:8080/ElectronicGuideCQ";
	public static final String SERVER_PATH_SD="http://localhost:8080/ElectronicGuideSD";
	
	/**
	 * 跳转至登录页面
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login() {
		return "redirect:"+SERVER_PATH_CQ+MODULE_NAME+"/login";
	}

	/**
	 * 从辰麒后台登录后跳转到景区后台
	 * @param serverPath
	 * @param userName
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/loginFromCQ")
	public String loginFromCQ(String serverPath,String userName,String password,HttpServletRequest request) {

		UsernamePasswordToken token = new UsernamePasswordToken(userName,password);  
		Subject currentUser = SecurityUtils.getSubject();  
		if (!currentUser.isAuthenticated()){
			//使用shiro来验证  
			token.setRememberMe(true);  
			currentUser.login(token);//验证角色和权限  
			
			HttpSession session=request.getSession();
			User user=(User)SecurityUtils.getSubject().getPrincipal();//将跨域获取到的用户信息存下来
			session.setAttribute("user", user);
			
			userService.edit(user);//同步景区数据库用户表里的信息
		}
		//return "redirect:"+SERVER_PATH_SD+MODULE_NAME+"/user/info/info";
		return "redirect:"+serverPath+MODULE_NAME+"/user/info/info";//跳转到景区服务器用户信息页
	}
}
