<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户信息</title>
<%@include file="../../js.jsp"%>
<link rel="stylesheet" href="<%=basePath %>resource/css/background/user/info/info.css"/>
<script type="text/javascript" src="<%=basePath %>resource/js/MD5.js"></script>
<script type="text/javascript">
var path='<%=basePath %>';
var userPath=path+"background/user/";
var pathCQApi='http://www.qrcodesy.com:8080/ElectronicGuideCQ/background/sdApiRequest/';
//var pathCQApi='http://localhost:8080/ElectronicGuideCQ/background/sdApiRequest/';
$(function(){
	$("#yhxx_div").css("width",setFitWidthInParent("body")+"px");
});

//验证原密码
function checkPassword(){
	var flag=false;
	var userName='${sessionScope.user.userName}';
	var password = $("#password").val();
	if(password==null||password==""){
		alert("原密码不能为空");
		flag=false;
	}
	else{
		$.ajaxSetup({async:false});
		$.ajax({
			url:pathCQApi+"checkPassword",
			data:{password:MD5(password).toUpperCase(),userName:userName},
			dataType:"jsonp",
			type: "post",
			jsonpCallback:"jsonpCallback",
			success:function(result){
				var data=JSON.parse(result);
				var status=data.status;
				if(status=="ok"){
					flag=true;
				}
				else if(status=="error"){
					alert(data.message);
					flag=false;
				}
				else if(status=="expire"){
					alert(data.message);
					location.href=data.redirectUrl;
					flag=false;
				}
			}
		});
	}
	return flag;
}

//验证新密码
function checkNewPwd(){
	var password = $("#password").val();
	var newPwd = $("#newPwd").val();
	if(newPwd==null||newPwd==""){
	  	alert("新密码不能为空");
	  	return false;
	}
	if(newPwd==password){
		alert("新密码不能和原密码一致！");
		return false;
	}
	else
		return true;
}

//验证确认密码
function checkNewPwd2(){
	var newPwd = $("#newPwd").val();
	var newPwd2 = $("#newPwd2").val();
	if(newPwd2==null||newPwd2==""){
	  	alert("确认密码不能为空");
	  	return false;
	}
	else if(newPwd!=newPwd2){
		alert("两次密码不一致！");
		return false;
	}
	else
		return true;
}

function checkEditPwd(){
	if(checkPassword()){
		if(checkNewPwd()){
			if(checkNewPwd2()){
				var password = $("#newPwd").val();
				$.ajax({
					url:pathCQApi+"updatePwdById",
					data:{password:MD5(password).toUpperCase()},
					dataType:"jsonp",
					type: "post",
					jsonpCallback:"jsonpCallback",
					success:function(result){
						var data=JSON.parse(result);
						var status=data.status;
						if(status=="ok"){
							$.messager.defaults.ok = "是";
						    $.messager.defaults.cancel = "否";
						    $.messager.defaults.width = 350;//更改消息框宽度
						    $.messager.confirm(
						    	"提示",
						    	data.message
						        ,function(r){    
						            if (r){    
						            	location.href=data.redirectUrl;
						            }
						        }); 
						}
						else if(status=="error"){
							$.messager.alert("提示",data.message,"warning");
						}
						else if(status=="expire"){
							alert(data.message);
							location.href=data.redirectUrl;
							flag=false;
						}
					}
				});
			}
		}
	}
}

function openEditPwdDialog(flag){
	$("#editPwdBg_div").css("display",flag==1?"block":"none");
}
	
function setFitWidthInParent(o){
	var width=$(o).css("width");
	return width.substring(0,width.length-2)-310;
}
</script>
</head>
<body>
<div class="editPwdBg_div" id="editPwdBg_div">
	<div class="editPwd_div">
		<div>
			<span class="close_span" onclick="openEditPwdDialog(0)">×</span>
		</div>
		<h4 class="title">修改密码</h4>
		<div class="ymm_div">
			<input type="password" id="password" placeholder="原密码"/>
		</div>
		<div class="xmm_div">
			<input type="password" id="newPwd" placeholder="新密码"/>
		</div>
		<div class="qrmm_div">
			<input type="password" id="newPwd2" placeholder="确认密码"/>
		</div>
		<div class="confirm_div" onclick="checkEditPwd()">确定</div>
		<div class="warn_div">注意：密码修改后需要重新登录系统</div>
	</div>
</div>

<div class="layui-layout layui-layout-admin">
	<%@include file="../../side.jsp"%>
	<div class="yhxx_div" id="yhxx_div">
		<div class="title_div">用户信息</div>
		<div class="attr_div">
			<span class="key_span">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</span>
			<span class="value_span">${sessionScope.user.userName }</span>
		</div>
		<div class="attr_div">
			<span class="key_span">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</span>
			<span class="value_span">已设置</span>
			<span class="xgmm_span" onclick="openEditPwdDialog(1)">修改密码</span>
		</div>
		<div class="attr_div">
			<span class="key_span">昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</span>
			<span class="value_span">${sessionScope.user.nickName }</span>
		</div>
		<div class="attr_div">
			<span class="key_span">头&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;像：</span>
			<img alt="" src="${sessionScope.user.headImgUrl }">
		</div>
		<div class="attr_div">
			<span class="key_span">创&nbsp;&nbsp;建&nbsp;&nbsp;&nbsp;时&nbsp;&nbsp;间：</span>
			<span class="value_span">${sessionScope.user.createTime }</span>
		</div>
		<div class="attr_div">
			<span class="eu_but_span" onclick="openEditUserDialog(1)">修改用户信息</span>
		</div>
	</div>
	<%@include file="../../foot.jsp"%>
</div>
</body>
</html>