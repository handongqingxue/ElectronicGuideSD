package com.electronicGuideSD.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * ����ฺ�������ó����ܷ��Ľӿ�
 * */
public class APICQUtil {
	
	//public static final String SERVER_PATH="http://192.168.2.166:8080/ElectronicGuideCQ";
	public static final String SERVER_PATH="http://localhost:8080/ElectronicGuideCQ";

	/**
	 * �����û����������ȡ�û���Ϣ
	 * @param userName
	 * @param password
	 * @return
	 */
	public static Map<String, Object> getUser(String userName, String password) {
		
		Map<String, Object> resultMap = null;
		String url=SERVER_PATH+"/background/getUser";
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(0, new BasicNameValuePair("userName", userName));
		params.add(1, new BasicNameValuePair("password", password));
		try {
			resultMap=JSON.parseObject(getRespJson(url, params).get("result").toString(), Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return resultMap;
		}
	}
	
	public static Map<String, Object> getRespJson(String url,List<NameValuePair> params) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		//POST��URL
		//����HttpPost����
		HttpPost httppost=new HttpPost(url);
		//��Ӳ���
		if(params!=null)
			httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		//���ñ���
		HttpResponse response=new DefaultHttpClient().execute(httppost);
		//����Post,������һ��HttpResponse����
		if(response.getStatusLine().getStatusCode()==200){//���״̬��Ϊ200,������������
		String result=EntityUtils.toString(response.getEntity());
		//�õ����ص��ַ���,��ӡ���
	//	System.out.println(result);
		jsonMap.put("result", result);
		}
		return jsonMap;
	}
}
