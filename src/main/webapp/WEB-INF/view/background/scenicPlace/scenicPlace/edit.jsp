<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>编辑景点</title>
<%@include file="../../js.jsp"%>
<style type="text/css">
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
.x_inp,.y_inp{
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
.upPicBut_div{
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
var edNum=0;
$(function(){
	initEditDialog();

	initDialogPosition();//将不同窗体移动到主要内容区域
});

function initDialogPosition(){
	//基本属性组
	var edpw=$("body").find(".panel.window").eq(edNum);
	var edws=$("body").find(".window-shadow").eq(edNum);

	var ccDiv=$("#center_con_div");
	ccDiv.append(edpw);
	ccDiv.append(edws);
	ccDiv.css("width",setFitWidthInParent("body","center_con_div")+"px");
}

function initEditDialog(){
	dialogTop+=20;
	$("#edit_div").dialog({
		title:"景点信息",
		width:setFitWidthInParent("body","edit_div"),
		height:730,
		top:dialogTop,
		left:dialogLeft,
		buttons:[
           {text:"保存",id:"ok_but",iconCls:"icon-ok",handler:function(){
        	   checkEdit();
           }}
        ]
	});

	$("#edit_div table").css("width",(setFitWidthInParent("body","edit_div_table"))+"px");
	$("#edit_div table").css("magin","-100px");
	$("#edit_div table td").css("padding-left","30px");
	$("#edit_div table td").css("padding-right","20px");
	$("#edit_div table td").css("font-size","15px");
	$("#edit_div table .td1").css("width","10%");
	$("#edit_div table .td2").css("width","35%");
	$("#edit_div table tr").css("border-bottom","#CAD9EA solid 1px");
	$("#edit_div table tr").each(function(i){
		if(i==1)
			$(this).css("height","250px");
		else if(i==3)
			$(this).css("height","200px");
		else if(i==4)
			$(this).css("height","100px");
		else
			$(this).css("height","45px");
	});

	$(".panel.window").eq(edNum).css("margin-top","20px");
	$(".panel.window .panel-title").eq(edNum).css("color","#000");
	$(".panel.window .panel-title").eq(edNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(edNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(edNum).css("margin-top","20px");
	$(".window,.window .window-body").eq(edNum).css("border-color","#ddd");

	$("#edit_div #ok_but").css("left","45%");
	$("#edit_div #ok_but").css("position","absolute");
	
	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
}

function checkEdit(){
	if(checkName()){
		if(checkSort()){
			if(checkX()){
				if(checkY()){
					if(checkSimpleIntro()){
						if(checkDetailIntro()){
							editScenicPlace();
						}
					}
				}
			}
		}
	}
}

function editScenicPlace(){
	var formData = new FormData($("#form1")[0]);
	$.ajax({
		type:"post",
		url:scenicPlacePath+"editScenicPlace",
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

function setFitWidthInParent(parent,self){
	var space=0;
	switch (self) {
	case "center_con_div":
		space=205;
		break;
	case "edit_div":
		space=340;
		break;
	case "edit_div_table":
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
<%@include file="../../side.jsp"%>
<div class="center_con_div" id="center_con_div">
	<div class="page_location_div">添加景点</div>
	
	<div id="edit_div">
		<form id="form1" name="form1" method="post" action="" enctype="multipart/form-data">
		<input type="hidden" name="id" id="id" value="${requestScope.scenicPlace.id }" />
		<table>
		  <tr>
			<td class="td1" align="right">
				名称
			</td>
			<td class="td2">
				<input type="text" class="name_inp" id="name" name="name" value="${requestScope.scenicPlace.name }" placeholder="请输入景区名称" onfocus="focusName()" onblur="checkName()"/>
			</td>
			<td class="td1" align="right">
				排序
			</td>
			<td class="td2">
				<input type="number" class="sort_inp" id="sort" name="sort" value="${requestScope.scenicPlace.sort }" placeholder="请输入排序"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				图片
			</td>
			<td class="td2">
				<div class="upBut_div upPicBut_div" onclick="uploadPicUrl()">选择图片</div>
				<input type="file" id="picUrl_file" name="picUrl_file" style="display: none;" onchange="showPicUrl(this)"/>
				<img class="picUrl_img" id="picUrl_img" alt="" src="${requestScope.scenicPlace.picUrl }"/>
			</td>
			<td class="td1" align="right">
			</td>
			<td class="td2">
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				x轴坐标
			</td>
			<td class="td2">
				<input type="number" class="x_inp" id="x" name="x" value="${requestScope.scenicPlace.x }" placeholder="请输入x轴坐标"/>
			</td>
			<td class="td1" align="right">
				y轴坐标
			</td>
			<td class="td2">
				<input type="number" class="y_inp" id="y" name="y" value="${requestScope.scenicPlace.y }" placeholder="请输入y轴坐标"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				简单介绍
			</td>
			<td class="td2">
				<textarea rows="8" cols="50" id="simpleIntro" name="simpleIntro" placeholder="请输入简单介绍" onfocus="focusSimpleIntro()" onblur="checkSimpleIntro()">${requestScope.scenicPlace.simpleIntro }</textarea>
			</td>
			<td class="td1" align="right">
				详细介绍
			</td>
			<td class="td2">
				<textarea rows="8" cols="50" id="detailIntro" name="detailIntro" placeholder="请输入详细介绍" onfocus="focusDetailIntro()" onblur="checkDetailIntro()">${requestScope.scenicPlace.detailIntro }</textarea>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				简单介绍语音包
			</td>
			<td class="td2">
				<div class="upBut_div upSimpleIntroVoiceBut_div" onclick="uploadSimpleIntroVoiceUrl()">选择语音包</div>
				<input type="file" id="simpleIntroVoiceUrl_file" name="simpleIntroVoiceUrl_file" style="display: none;" onchange="showSimpleIntroVoiceUrl(this)"/>
				<embed class="simpleIntroVoiceUrl_embed" id="simpleIntroVoiceUrl_embed" alt="" src="${requestScope.scenicPlace.simpleIntroVoiceUrl }">
			</td>
			<td class="td1" align="right">
				详细介绍语音包
			</td>
			<td class="td2">
				<div class="upBut_div upDetailIntroVoiceBut_div" onclick="uploadDetailIntroVoiceUrl()">选择语音包</div>
				<input type="file" id="detailIntroVoiceUrl_file" name="detailIntroVoiceUrl_file" style="display: none;" onchange="showDetailIntroVoiceUrl(this)"/>
				<img class="detailIntroVoiceUrl_embed" id="detailIntroVoiceUrl_embed" alt="" src="${requestScope.scenicPlace.detailIntroVoiceUrl }"/>
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