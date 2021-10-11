package com.electronicGuideSD.util.shiro.realm;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.electronicGuideSD.dao.UserMapper;
import com.electronicGuideSD.entity.User;
import com.electronicGuideSD.util.APICQUtil;

public class MyRealm  extends AuthorizingRealm {
	@Autowired
	private UserMapper userMapper;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		// TODO Auto-generated method stub
		User msg=(User)SecurityUtils.getSubject().getPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(); 
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		// TODO Auto-generated method stub
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		//User msg=new User(token.getUsername(),String.valueOf(token.getPassword()));
		//User resultMsg=userMapper.get(msg);
		Map<String, Object> userMap = (Map<String, Object>)APICQUtil.getUser(token.getUsername(),String.valueOf(token.getPassword()));
		JSONObject userJO = (JSONObject)userMap.get("user");
		User resultMsg=JSONObject.parseObject(userJO.toString(), User.class);
		if(token.getUsername().equals(resultMsg.getUserName())
				&&
				String.valueOf(token.getPassword()).equals(resultMsg.getPassword())){
			return new SimpleAuthenticationInfo(resultMsg,resultMsg.getPassword(),resultMsg.getUserName());
		}else{
			throw new AuthenticationException();  
		}
	}

}
