<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加景点</title>
<%@include file="../../js.jsp"%>
<style type="text/css">
.add_sp_canvas_bg_div{
	width: 100%;
	height: 100%;
	background-color: rgba(0,0,0,.45);
	position: fixed;
	z-index: 9016;
	display:none;
}
.add_sp_canvas_div{
	width: 1800px;
	height: 850px;
	margin: 50px auto 0;
	background-color: #fff;
	border-radius:5px;
	position: absolute;
	left: 0;
	right: 0;
}
.add_sp_canvas_div .tjst_div{
	width: 100%;
	height: 50px;
	line-height: 50px;
	border-bottom: #eee solid 1px;
}
.add_sp_canvas_div .tjst_span{
	margin-left: 30px;
}
.add_sp_canvas_div .close_span{
	float: right;margin-right: 30px;cursor: pointer;
}
.add_sp_canvas_dialog_div{
	width: 1775px;
	height: 800px;
	position: absolute;
}
.add_sp_canvas_div .title_div{
	width: 100%;height: 50px;line-height: 50px;
}
.add_sp_canvas_div .title_span{
	margin-left: 30px;
}

.center_con_div{
	height: 90vh;
	margin-left:205px;
	position: absolute;
}
.page_location_div{
	height: 50px;
	line-height: 50px;
	margin-left: 20px;
	font-size: 18px;
}
.name_inp{
	width: 150px;
	height:30px;
}
.picWidth_inp,.picHeight_inp{
	width: 130px;
	height:30px;
}
.sort_inp{
	width: 100px;
	height:30px;
}
.upBut_div{
	height: 30px;
	line-height:30px;
	text-align:center;
	color:#fff;
	background-color: #1777FF;
	border-radius:5px;
}
.upPicBut_div,.showMapBut_div{
	width: 90px;
}
.upSimpleIntroVoiceBut_div,.upDetailIntroVoiceBut_div{
	width: 110px;
}
.picUrl_img{
	width: 180px;
	height:180px;
	margin-top: 10px;
}
.sceDis_img{
	height:180px;
	margin-top: 10px;
}
.simpleIntroVoiceUrl_embed,.detailIntroVoiceUrl_embed{
	width: 300px;
	height:50px;
}
</style>
<script type="text/javascript">
var path='<%=basePath %>';
var scenicPlacePath='<%=basePath%>'+"background/scenicPlace/";
var dialogTop=10;
var dialogLeft=20;
var ndNum=0;
var aspsdmdNum=1;
var sceDisCanvas;
var sceDisCanvasMinWidth;
var sceDisCanvasMinHeight;
var sceDisCanvasMaxWidth;
var sceDisCanvasMaxHeight;
var sceDisCanvasStyleWidth;//sceDisCanvasMinWidth
var sceDisCanvasStyleHeight;//sceDisCanvasMinHeight
var sceDisCanvasWidth;
var sceDisCanvasHeight;
var widthScale;
var heightScale;
var reSizeTimeout;
var scenicPlace;
$(function(){
	initNewDialog();
	initAddSpSDMapDialogDiv();

	initDialogPosition();//将不同窗体移动到主要内容区域
	
	jiSuanScale();
	initSceDisCanvas();
});

function jiSuanScale(){
	sceDisCanvasMinWidth=parseFloat('${sessionScope.user.scenicDistrict.mapWidth}');
	sceDisCanvasMinHeight=parseFloat('${sessionScope.user.scenicDistrict.mapHeight}');
	sceDisCanvasStyleWidth=sceDisCanvasMinWidth;
	sceDisCanvasStyleHeight=sceDisCanvasMinHeight;
	
	sceDisCanvasMaxWidth=parseFloat('${sessionScope.user.scenicDistrict.picWidth}');
	sceDisCanvasMaxHeight=parseFloat('${sessionScope.user.scenicDistrict.picHeight}');
	sceDisCanvasWidth=sceDisCanvasMaxWidth;
	sceDisCanvasHeight=sceDisCanvasMaxHeight;

	widthScale=sceDisCanvasStyleWidth/sceDisCanvasWidth;
	heightScale=sceDisCanvasStyleHeight/sceDisCanvasHeight;
}

function changeCanvasSize(bigFlag,resetFlag){
	loadSceDisCanvas(true);
    var mcw=sceDisCanvasStyleWidth;
	var mch=sceDisCanvasStyleHeight;
	if(resetFlag){
		sceDisCanvasStyleWidth=sceDisCanvasMinWidth;
	}
	else{
		if(bigFlag==1)
			sceDisCanvasStyleWidth+=sceDisCanvasMinWidth*0.2;
		else
			sceDisCanvasStyleWidth-=sceDisCanvasMinWidth*0.2;
	}
	
	if(sceDisCanvasStyleWidth<sceDisCanvasMinWidth){
		sceDisCanvasStyleWidth=sceDisCanvasMinWidth;
	}
	else if(sceDisCanvasStyleWidth>sceDisCanvasMaxWidth){
		sceDisCanvasStyleWidth=sceDisCanvasMaxWidth;
	}

	if(sceDisCanvasStyleHeight<sceDisCanvasMinHeight){
		sceDisCanvasStyleHeight=sceDisCanvasMinHeight;
	}
	else if(sceDisCanvasStyleHeight>sceDisCanvasMaxHeight){
		sceDisCanvasStyleHeight=sceDisCanvasMaxHeight;
	}
	sceDisCanvasStyleHeight=sceDisCanvasStyleWidth*sceDisCanvasHeight/sceDisCanvasWidth;
	
	//缩放地图改变尺寸时，不改变点的坐标（坐标自动跟着变），改变的只有上面矩形框大小、文字大小
	var cswSFB=mcw/sceDisCanvasStyleWidth;
	var cshSFB=mch/sceDisCanvasStyleHeight;
	
	initSceDisCanvas(1);
}

function initSceDisCanvas(reSizeFlag){
	var sceDisCanvasImg = new Image();
	sceDisCanvasImg.src='${sessionScope.user.scenicDistrict.mapUrl}';
	sceDisCanvas = document.createElement("canvas");
	sceDisCanvas.id="sceDisCanvas";
	console.log(sceDisCanvasStyleWidth);
	sceDisCanvas.style.width=sceDisCanvasStyleWidth+"px";//通过缩放来改变画布大小，画布大小改变后，上面的定位点位置也就跟着改变了
	sceDisCanvas.style.height=sceDisCanvasStyleHeight+"px";
	sceDisCanvas.width=sceDisCanvasWidth;
	sceDisCanvas.height=sceDisCanvasHeight;
	sceDisCanvasContext = sceDisCanvas.getContext("2d");
	sceDisCanvasImg.onload=function(){
		sceDisCanvasContext.drawImage(sceDisCanvasImg, 0, 0, sceDisCanvasWidth, sceDisCanvasHeight);
		
		if(scenicPlace!=undefined)
			setScenicPlaceLocation();
		
		var preSceDisCanvas=document.getElementById("sceDisCanvas");
		preSceDisCanvas.parentNode.removeChild(preSceDisCanvas);
		var sceDisCanvasDiv=document.getElementById("sceDisCanvas_div");
		sceDisCanvasDiv.appendChild(sceDisCanvas);
		
		if(reSizeFlag==1)
			loadSceDisCanvas(0);
	}
	sceDisCanvas.onclick=function(e){
		if (e.offsetX || e.layerX) {
	           var x = e.offsetX == undefined ? e.layerX : e.offsetX;
	           var y = e.offsetY == undefined ? e.layerY : e.offsetY;
	           //alert(scenicPlace.x/widthScale+","+sceDisCanvasHeight-scenicPlace.y/heightScale);
	           //x=x*(sceDisCanvasStyleWidth/sceDisCanvasWidth);
	           x=x*(sceDisCanvasMinWidth/sceDisCanvasStyleWidth);
	           y=y*(sceDisCanvasMinHeight/sceDisCanvasStyleHeight);
	           alert(x+","+(sceDisCanvasMinHeight-y));
	       	   var picUrl=$("#picUrl_img").attr("src");
		       var picWidth=$("#picWidth").val();
		       var picHeight=$("#picHeight").val();
	           scenicPlace={x:x,y:y,picUrl:picUrl,picWidth:picWidth,picHeight:picHeight};
	           initSceDisCanvas(0);
	           //setScenicPlaceLocation();
	    }
	}
}

function setScenicPlaceLocation(){
	var entityImg = new Image();
	entityImg.src=scenicPlace.picUrl;
	entityImg.onload=function(){
		//不管画布怎么放大、缩小，生成坐标的点位置还是原来的。只是上面鼠标点击后获取的坐标是从坐上为原点计算的，这里画图也是和上面一样的原理，从左上为原点计算位置。只是插入数据库的位置是转换后以左下为原点计算的
		sceDisCanvasContext.drawImage(entityImg, scenicPlace.x/widthScale-scenicPlace.picWidth/2, scenicPlace.y/heightScale-scenicPlace.picHeight/2, scenicPlace.picWidth, scenicPlace.picHeight);
	}
}

function initDialogPosition(){
	//基本属性组
	var ndpw=$("body").find(".panel.window").eq(ndNum);
	var ndws=$("body").find(".window-shadow").eq(ndNum);
	
	var aspsdmdpw=$("body").find(".panel.window").eq(aspsdmdNum);
	var aspsdmdws=$("body").find(".window-shadow").eq(aspsdmdNum);

	var ccDiv=$("#center_con_div");
	ccDiv.append(ndpw);
	ccDiv.append(ndws);
	ccDiv.css("width",setFitWidthInParent("body","center_con_div")+"px");
	
	var aspsdmdDiv=$("#add_sp_canvas_dialog_div");
	aspsdmdDiv.append(aspsdmdpw);
	aspsdmdDiv.append(aspsdmdws);
}

function initAddSpSDMapDialogDiv(){
	addSpSdMDialog=$("#add_sp_sd_map_dialog_div").dialog({
		title:"景区地图",
		width:setFitWidthInParent("body","add_sp_sd_map_dialog_div"),
		height:730,
		top:10,
		left:20,
		buttons:[
           {text:"取消",id:"cancel_but",iconCls:"icon-cancel",handler:function(){
        	   
           }},
           {text:"确定",id:"ok_but",iconCls:"icon-ok",handler:function(){
        	   
           }},
           {text:"还原",id:"reset_but",iconCls:"icon-remove",handler:function(){
        	   changeCanvasSize(null,true);
           }},
           {text:"放大",id:"big_but",iconCls:"icon-add",handler:function(){
        	   changeCanvasSize(true,false);
           }},
           {text:"缩小",id:"small_but",iconCls:"icon-remove",handler:function(){
        	   changeCanvasSize(false,false);
           }}
        ]
	});

	$(".panel.window").eq(aspsdmdNum).css("margin-top","40px");
	$(".panel.window .panel-title").eq(aspsdmdNum).css("color","#000");
	$(".panel.window .panel-title").eq(aspsdmdNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(aspsdmdNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").eq(aspsdmdNum).css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(aspsdmdNum).css("margin-top","40px");
	$(".window,.window .window-body").eq(aspsdmdNum).css("border-color","#ddd");

	$("#add_sp_sd_map_dialog_div #cancel_but").css("left","20%");
	$("#add_sp_sd_map_dialog_div #cancel_but").css("position","absolute");

	$("#add_sp_sd_map_dialog_div #ok_but").css("left","35%");
	$("#add_sp_sd_map_dialog_div #ok_but").css("position","absolute");

	$("#add_sp_sd_map_dialog_div #reset_but").css("left","50%");
	$("#add_sp_sd_map_dialog_div #reset_but").css("position","absolute");

	$("#add_sp_sd_map_dialog_div #big_but").css("left","65%");
	$("#add_sp_sd_map_dialog_div #big_but").css("position","absolute");

	$("#add_sp_sd_map_dialog_div #small_but").css("left","80%");
	$("#add_sp_sd_map_dialog_div #small_but").css("position","absolute");
	
	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
	openAddSpSdMDialog(0);
}

function initNewDialog(){
	dialogTop+=20;
	$("#new_div").dialog({
		title:"景点信息",
		width:setFitWidthInParent("body","new_div"),
		height:730,
		top:dialogTop,
		left:dialogLeft,
		buttons:[
           {text:"保存",id:"ok_but",iconCls:"icon-ok",handler:function(){
        	   checkAdd();
           }}
        ]
	});

	$("#new_div table").css("width",(setFitWidthInParent("body","new_div_table"))+"px");
	$("#new_div table").css("magin","-100px");
	$("#new_div table td").css("padding-left","30px");
	$("#new_div table td").css("padding-right","20px");
	$("#new_div table td").css("font-size","15px");
	$("#new_div table .td1").css("width","10%");
	$("#new_div table .td2").css("width","35%");
	$("#new_div table tr").css("border-bottom","#CAD9EA solid 1px");
	$("#new_div table tr").each(function(i){
		if(i==1)
			$(this).css("height","250px");
		else if(i==4)
			$(this).css("height","200px");
		else if(i==5)
			$(this).css("height","100px");
		else
			$(this).css("height","45px");
	});

	$(".panel.window").eq(ndNum).css("margin-top","20px");
	$(".panel.window .panel-title").eq(ndNum).css("color","#000");
	$(".panel.window .panel-title").eq(ndNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(ndNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(ndNum).css("margin-top","20px");
	$(".window,.window .window-body").eq(ndNum).css("border-color","#ddd");

	$("#new_div #ok_but").css("left","45%");
	$("#new_div #ok_but").css("position","absolute");
	
	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
}

function openAddSpDialog(flag){
	if(flag==1){
		$("#add_sp_canvas_bg_div").css("display","block");
	}
	else{
		$("#add_sp_canvas_bg_div").css("display","none");
	}
	openAddSpSdMDialog(flag);
}

function openAddSpSdMDialog(flag){
	if(flag==1){
		addSpSdMDialog.dialog("open");
	}
	else{
		addSpSdMDialog.dialog("close");
	}
}

function checkAdd(){
	if(checkName()){
		if(checkSort()){
			if(checkX()){
				if(checkY()){
					if(checkPicWidth()){
						if(checkPicHeight()){
							if(checkSimpleIntro()){
								if(checkDetailIntro()){
									addScenicPlace();
								}
							}
						}
					}
				}
			}
		}
	}
}

function addScenicPlace(){
	var formData = new FormData($("#form1")[0]);
	$.ajax({
		type:"post",
		url:scenicPlacePath+"addScenicPlace",
		dataType: "json",
		data:formData,
		cache: false,
		processData: false,
		contentType: false,
		success: function (data){
			if(data.status==1){
				alert(data.msg);
				location.href=scenicPlacePath+"scenicPlace/list";
			}
			else{
				alert(data.msg);
			}
		}
	});
}

function checkScenicPlaceInfo(){
	if(checkPicUrl()){
		if(checkPicWidth()){
			if(checkPicHeight()){
				openAddSpDialog(1);
			}
		}
	}
}

function checkPicUrl(){
	var picUrl=$("#picUrl_img").attr("src");
	if(picUrl==null||picUrl==""){
	  	alert("请选择景区图片");
	  	return false;
	}
	else
		return true;
}

function focusName(){
	var name = $("#name").val();
	if(name=="景区名称不能为空"){
		$("#name").val("");
		$("#name").css("color", "#555555");
	}
}

//验证景区名称
function checkName(){
	var name = $("#name").val();
	if(name==null||name==""||name=="景区名称不能为空"){
		$("#name").css("color","#E15748");
    	$("#name").val("景区名称不能为空");
    	return false;
	}
	else
		return true;
}

//验证排序
function checkSort(){
	var sort = $("#sort").val();
	if(sort==null||sort==""){
	  	alert("请输入排序");
	  	return false;
	}
	else
		return true;
}

//验证x轴坐标
function checkX(){
	var x = $("#x").val();
	if(x==null||x==""){
	  	alert("请输入x轴坐标");
	  	return false;
	}
	else
		return true;
}

//验证y轴坐标
function checkY(){
	var y = $("#y").val();
	if(y==null||y==""){
	  	alert("请输入y轴坐标");
	  	return false;
	}
	else
		return true;
}

//验证图片宽度
function checkPicWidth(){
	var picWidth = $("#picWidth").val();
	if(picWidth==null||picWidth==""){
	  	alert("请输入图片宽度");
	  	return false;
	}
	else
		return true;
}

//验证图片高度
function checkPicHeight(){
	var picHeight = $("#picHeight").val();
	if(picHeight==null||picHeight==""){
	  	alert("请输入图片高度");
	  	return false;
	}
	else
		return true;
}

function focusSimpleIntro(){
	var simpleIntro = $("#simpleIntro").val();
	if(simpleIntro=="简单介绍不能为空"){
		$("#simpleIntro").val("");
		$("#simpleIntro").css("color", "#555555");
	}
}

//验证简单介绍
function checkSimpleIntro(){
	var simpleIntro = $("#simpleIntro").val();
	if(simpleIntro==null||simpleIntro==""||simpleIntro=="简单介绍不能为空"){
		$("#simpleIntro").css("color","#E15748");
    	$("#simpleIntro").val("简单介绍不能为空");
    	return false;
	}
	else
		return true;
}

function focusDetailIntro(){
	var detailIntro = $("#detailIntro").val();
	if(detailIntro=="详细介绍不能为空"){
		$("#detailIntro").val("");
		$("#detailIntro").css("color", "#555555");
	}
}

//验证详细介绍
function checkDetailIntro(){
	var detailIntro = $("#detailIntro").val();
	if(detailIntro==null||detailIntro==""||detailIntro=="详细介绍不能为空"){
		$("#detailIntro").css("color","#E15748");
    	$("#detailIntro").val("详细介绍不能为空");
    	return false;
	}
	else
		return true;
}

function uploadPicUrl(){
	document.getElementById("picUrl_file").click();
}

function uploadSimpleIntroVoiceUrl(){
	document.getElementById("simpleIntroVoiceUrl_file").click();
}

function uploadDetailIntroVoiceUrl(){
	document.getElementById("detailIntroVoiceUrl_file").click();
}

function showPicUrl(obj){
	var file = $(obj);
    var fileObj = file[0];
    var windowURL = window.URL || window.webkitURL;
    var dataURL;
    var $img = $("#picUrl_img");

    if (fileObj && fileObj.files && fileObj.files[0]) {
        dataURL = windowURL.createObjectURL(fileObj.files[0]);
        $img.attr("src", dataURL);
    } else {
        dataURL = $file.val();
        var imgObj = document.getElementById("preview");
        // 两个坑:
        // 1、在设置filter属性时，元素必须已经存在在DOM树中，动态创建的Node，也需要在设置属性前加入到DOM中，先设置属性在加入，无效；
        // 2、src属性需要像下面的方式添加，上面的两种方式添加，无效；
        imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
        imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;

    }
}

function showSimpleIntroVoiceUrl(obj){
	var file = $(obj);
    var fileObj = file[0];
    var windowURL = window.URL || window.webkitURL;
    var dataURL;
    var $embed = $("#simpleIntroVoiceUrl_embed");

    if (fileObj && fileObj.files && fileObj.files[0]) {
        dataURL = windowURL.createObjectURL(fileObj.files[0]);
        $embed.attr("src", dataURL);
    } else {
        dataURL = $file.val();
        var embedObj = document.getElementById("preview");
        // 两个坑:
        // 1、在设置filter属性时，元素必须已经存在在DOM树中，动态创建的Node，也需要在设置属性前加入到DOM中，先设置属性在加入，无效；
        // 2、src属性需要像下面的方式添加，上面的两种方式添加，无效；
        embedObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
        embedObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;

    }
}

function showDetailIntroVoiceUrl(obj){
	var file = $(obj);
    var fileObj = file[0];
    var windowURL = window.URL || window.webkitURL;
    var dataURL;
    var $embed = $("#detailIntroVoiceUrl_embed");

    if (fileObj && fileObj.files && fileObj.files[0]) {
        dataURL = windowURL.createObjectURL(fileObj.files[0]);
        $embed.attr("src", dataURL);
    } else {
        dataURL = $file.val();
        var embedObj = document.getElementById("preview");
        // 两个坑:
        // 1、在设置filter属性时，元素必须已经存在在DOM树中，动态创建的Node，也需要在设置属性前加入到DOM中，先设置属性在加入，无效；
        // 2、src属性需要像下面的方式添加，上面的两种方式添加，无效；
        embedObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
        embedObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;

    }
}

function loadSceDisCanvas(flag){
	var bigButDiv=$("#add_sp_sd_map_dialog_div #big_but");
	var smallButDiv=$("#add_sp_sd_map_dialog_div #small_but");
	if(flag){
		bigButDiv.css("display","none");
		smallButDiv.css("display","none");
	}
	else{
		reSizeTimeout=setTimeout(function(){
			bigButDiv.css("display","block");
			smallButDiv.css("display","block");
			clearTimeout(reSizeTimeout);
		},"1000");
	}
}

function setFitWidthInParent(parent,self){
	var space=0;
	switch (self) {
	case "center_con_div":
		space=205;
		break;
	case "new_div":
		space=340;
		break;
	case "add_sp_sd_map_dialog_div":
		space=170;
		break;
	case "new_div_table":
	case "panel_window":
		space=355;
		break;
	}
	var width=$(parent).css("width");
	return width.substring(0,width.length-2)-space;
}
</script>
</head>
<body>
<div class="layui-layout layui-layout-admin">	
	<div class="add_sp_canvas_bg_div" id="add_sp_canvas_bg_div">
		<div class="add_sp_canvas_div" id="add_sp_canvas_div">
			<div class="tjst_div">
				<span class="tjst_span">添加实体</span>
				<span class="close_span" onclick="openAddSpDialog(0)">X</span>
			</div>
			<div class="add_sp_canvas_dialog_div" id="add_sp_canvas_dialog_div">
				<div class="title_div">
					<span class="title_span">景点管理-景点查询-添加</span>
				</div>
				<input type="hidden" id="id"/>
				<div id="add_sp_sd_map_dialog_div">
					<div id="sceDisCanvas_div">
						<canvas id="sceDisCanvas">
						</canvas>
					</div>
				</div>
			</div>
		</div>
	</div>
	
<%@include file="../../side.jsp"%>
<div class="center_con_div" id="center_con_div">
	<div class="page_location_div">添加景点</div>
	
	<div id="new_div">
		<form id="form1" name="form1" method="post" action="" enctype="multipart/form-data">
		<table>
		  <tr>
			<td class="td1" align="right">
				名称
			</td>
			<td class="td2">
				<input type="text" class="name_inp" id="name" name="name" placeholder="请输入景区名称" onfocus="focusName()" onblur="checkName()"/>
			</td>
			<td class="td1" align="right">
				排序
			</td>
			<td class="td2">
				<input type="number" class="sort_inp" id="sort" name="sort" placeholder="请输入排序"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				图片
			</td>
			<td class="td2">
				<div class="upBut_div upPicBut_div" onclick="uploadPicUrl()">选择图片</div>
				<input type="file" id="picUrl_file" name="picUrl_file" style="display: none;" onchange="showPicUrl(this)"/>
				<img class="picUrl_img" id="picUrl_img" alt="" src=""/>
			</td>
			<td class="td1" align="right">
				景区地图
			</td>
			<td class="td2">
				<div class="upBut_div showMapBut_div" onclick="checkScenicPlaceInfo();">显示地图</div>
				<img class="sceDis_img" id="sceDis_img" alt="" src="${sessionScope.user.scenicDistrict.mapUrl }"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				图片宽度(px)
			</td>
			<td class="td2">
				<input type="number" class="picWidth_inp" id="picWidth" name="picWidth" placeholder="请输入图片宽度"/>
			</td>
			<td class="td1" align="right">
				图片高度(px)
			</td>
			<td class="td2">
				<input type="number" class="picHeight_inp" id="picHeight" name="picHeight" placeholder="请输入图片高度"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				x轴坐标
			</td>
			<td class="td2">
				<span id="x_span"></span>
				<input type="hidden" id="x_inp" name="x"/>
			</td>
			<td class="td1" align="right">
				y轴坐标
			</td>
			<td class="td2">
				<span id="y_span"></span>
				<input type="hidden" id="y_inp" name="y"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				简单介绍
			</td>
			<td class="td2">
				<textarea rows="8" cols="50" id="simpleIntro" name="simpleIntro" placeholder="请输入简单介绍" onfocus="focusSimpleIntro()" onblur="checkSimpleIntro()"></textarea>
			</td>
			<td class="td1" align="right">
				详细介绍
			</td>
			<td class="td2">
				<textarea rows="8" cols="50" id="detailIntro" name="detailIntro" placeholder="请输入详细介绍" onfocus="focusDetailIntro()" onblur="checkDetailIntro()"></textarea>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				简单介绍语音包
			</td>
			<td class="td2">
				<div class="upBut_div upSimpleIntroVoiceBut_div" onclick="uploadSimpleIntroVoiceUrl()">选择语音包</div>
				<input type="file" id="simpleIntroVoiceUrl_file" name="simpleIntroVoiceUrl_file" style="display: none;" onchange="showSimpleIntroVoiceUrl(this)"/>
				<embed class="simpleIntroVoiceUrl_embed" id="simpleIntroVoiceUrl_embed" alt="" src="">
			</td>
			<td class="td1" align="right">
				详细介绍语音包
			</td>
			<td class="td2">
				<div class="upBut_div upDetailIntroVoiceBut_div" onclick="uploadDetailIntroVoiceUrl()">选择语音包</div>
				<input type="file" id="detailIntroVoiceUrl_file" name="detailIntroVoiceUrl_file" style="display: none;" onchange="showDetailIntroVoiceUrl(this)"/>
				<img class="detailIntroVoiceUrl_embed" id="detailIntroVoiceUrl_embed" alt=""/>
			</td>
		  </tr>
		</table>
		</form>
	</div>
	
	<%@include file="../../foot.jsp"%>
	</div>
</div>
</body>
</html>