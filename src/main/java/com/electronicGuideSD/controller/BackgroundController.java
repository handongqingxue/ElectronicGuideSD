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
	@Autowired
	private ScenicDistrictService scenicDistrictService;
	public static final String MODULE_NAME="/background";
	//public static final String SERVER_PATH_CQ="https://www.qrcodesy.com/ElectronicGuideCQ";
	public static final String SERVER_PATH_CQ="https://localhost/ElectronicGuideCQ";
	public static final String SERVER_PATH_SD="http://120.27.5.36:8080/ElectronicGuideSD";
	//public static final String SERVER_PATH_SD="http://localhost:8080/ElectronicGuideSD";
	
	/**
	 * ��ת����¼ҳ��
	 * @return
	 */
	@RequestMapping(value="/goLogin",method=RequestMethod.GET)
	public String goLogin() {
		return "redirect:"+SERVER_PATH_CQ+MODULE_NAME+"/goLogin";
	}

	/**
	 * �ӳ����̨��¼����ת��������̨
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
			//ʹ��shiro����֤  
			token.setRememberMe(true);  
			currentUser.login(token);//��֤��ɫ��Ȩ��  
			
			HttpSession session=request.getSession();
			User user=(User)SecurityUtils.getSubject().getPrincipal();//�������ȡ�����û���Ϣ������
			session.setAttribute("user", user);
			
			userService.edit(user);//ͬ���������ݿ��û��������Ϣ
			scenicDistrictService.edit(user.getScenicDistrict());//ͬ���������ݿ⾰���������Ϣ
		}
		//return "redirect:"+SERVER_PATH_SD+MODULE_NAME+"/user/info/info";
		return "redirect:"+serverPath+MODULE_NAME+"/user/info/info";//��ת�������������û���Ϣҳ
	}
	
	@RequestMapping(value="/exit")
	public String exit(HttpServletRequest request) {
		System.out.println("�˳��ӿ�");
		Subject currentUser = SecurityUtils.getSubject();       
	    currentUser.logout();    

		return goLogin();
	}
}
