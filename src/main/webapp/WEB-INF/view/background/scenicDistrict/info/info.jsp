<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>景区信息</title>
<%@include file="../../js.jsp"%>
<link rel="stylesheet" href="<%=basePath %>resource/css/background/scenicDistrict/info/info.css"/>
</head>
<body>
<div class="layui-layout layui-layout-admin">
	<%@include file="../../side.jsp"%>
	<div class="jqxx_div" id="jqxx_div">
		<div class="title_div">景区信息</div>
		<div class="attr_div name_div">
			<span class="key_span">景&nbsp;&nbsp;&nbsp;&nbsp;区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.name }</span>
		</div>
		<div class="attr_div address_div">
			<span class="key_span">景&nbsp;&nbsp;&nbsp;&nbsp;区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.address }</span>
		</div>
		<div class="attr_div mapWidth_div">
			<span class="key_span">地图区域宽度(px)：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.mapWidth }</span>
		</div>
		<div class="attr_div mapHeight_div">
			<span class="key_span">地图区域高度(px)：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.mapHeight }</span>
		</div>
		<div class="attr_div picWidth_div">
			<span class="key_span">地图图片宽度(px)：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.picWidth }</span>
		</div>
		<div class="attr_div picHeight_div">
			<span class="key_span">地图图片高度(px)：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.picHeight }</span>
		</div>
		<div class="attr_div createTime_div">
			<span class="key_span">创&nbsp;&nbsp;&nbsp;&nbsp;建&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.createTime }</span>
		</div>
		<div class="attr_div modifyTime_div">
			<span class="key_span">修&nbsp;&nbsp;&nbsp;&nbsp;改&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.modifyTime }</span>
		</div>
		
		<div class="attr_div longitude_div">
			<span class="key_span">经&nbsp;&nbsp;&nbsp;&nbsp;度&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;范&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;围：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.longitudeStart }-${sessionScope.user.scenicDistrict.longitudeEnd }</span>
		</div>
		<div class="attr_div latitude_div">
			<span class="key_span">纬&nbsp;&nbsp;&nbsp;&nbsp;度&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;范&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;围：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.latitudeStart }-${sessionScope.user.scenicDistrict.latitudeEnd }</span>
		</div>
		<div class="attr_div serverName_div">
			<span class="key_span">景&nbsp;&nbsp;&nbsp;&nbsp;区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;域&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.serverName }</span>
		</div>
		<div class="introduce_div">
			<span class="key_span">景&nbsp;&nbsp;&nbsp;&nbsp;区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;介&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;绍：</span>
			<span class="value_span">${sessionScope.user.scenicDistrict.introduce }</span>
		</div>
		
		<div class="qrcodeUrl_div">
			<span class="key_span">二&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;维&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</span>
			<img alt="" src="${sessionScope.user.scenicDistrict.qrcodeUrl }">
		</div>
		<div class="mapUrl_div">
			<span class="key_span">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;图：</span>
			<img alt="" src="${sessionScope.user.scenicDistrict.mapUrl }">
			<span class="ckdt_span" onclick="openEditPwdDialog(1)">查看地图</span>
		</div>
	</div>
	<%@include file="../../foot.jsp"%>
</div>
</body>
</html>