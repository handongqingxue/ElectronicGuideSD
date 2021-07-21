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
checkBgIfLogin();

function checkBgIfLogin(){
	$.ajax({
		//url:"http://www.qrcodesy.com:8080/ElectronicGuideCQ/background/sdApiRequest/checkUserLogin",
		url:"http://localhost:8080/ElectronicGuideCQ/background/sdApiRequest/checkUserLogin",
		dataType:"jsonp",
		type: "post",
		jsonpCallback:"jsonpCallback",
		success:function(result){
			var data=JSON.parse(result);
			alert(result)
		}
	});
}
	
function setFitWidthInParent(o){
	var width=$(o).css("width");
	return width.substring(0,width.length-2)-310;
}
</script>
</head>
<body>

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