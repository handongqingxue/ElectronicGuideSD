<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加路名</title>
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
.sort_inp{
	width: 100px;
	height:30px;
}
</style>
<script type="text/javascript">
var path='<%=basePath %>';
var roadPath='<%=basePath%>'+"background/road/";
var wechatAppletPath='<%=basePath%>'+"wechatApplet/";
var dialogTop=10;
var dialogLeft=20;
var ndNum=0;
$(function(){
	initBackThroughCBB();
	initFrontThroughCBB();
	initNewDialog();

	initDialogPosition();//将不同窗体移动到主要内容区域
});

function initBackThroughCBB(){
	var data=[];
	data.push({id:"",name:"请选择"},{id:"true",name:"是"},{id:"false",name:"否"});
	backThroughCBB=$("#backThrough_cbb").combobox({
		width:150,
		data:data,
		valueField:"id",
		textField:"name",
		onSelect:function(){
			$("#backThrough").val(backThroughCBB.combobox("getValue"));
		}
	});
}

function initFrontThroughCBB(){
	var data=[];
	data.push({id:"",name:"请选择"},{id:"true",name:"是"},{id:"false",name:"否"});
	frontThroughCBB=$("#frontThrough_cbb").combobox({
		width:150,
		data:data,
		valueField:"id",
		textField:"name",
		onSelect:function(){
			$("#frontThrough").val(frontThroughCBB.combobox("getValue"));
		}
	});
}

function initDialogPosition(){
	//基本属性组
	var ndpw=$("body").find(".panel.window").eq(ndNum);
	var ndws=$("body").find(".window-shadow").eq(ndNum);

	var ccDiv=$("#center_con_div");
	ccDiv.append(ndpw);
	ccDiv.append(ndws);
	ccDiv.css("width",setFitWidthInParent("body","center_con_div")+"px");
}

function initNewDialog(){
	dialogTop+=20;
	$("#new_div").dialog({
		title:"路名信息",
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
	$("#new_div table tr").css("height","45px");

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

function checkAdd(){
	if(checkName()){
		if(checkSort()){
			if(checkBackThrough()){
				if(checkFrontThrough()){
					addRoad();
				}
			}
		}
	}
}

function addRoad(){
	var formData = new FormData($("#form1")[0]);
	$.ajax({
		type:"post",
		url:roadPath+"addRoad",
		dataType: "json",
		data:formData,
		cache: false,
		processData: false,
		contentType: false,
		success: function (data){
			if(data.status==1){
				alert(data.msg);
				location.href=roadPath+"road/list";
			}
			else{
				alert(data.msg);
			}
		}
	});
}

function focusName(){
	var name = $("#name").val();
	if(name=="道路名称不能为空"){
		$("#name").val("");
		$("#name").css("color", "#555555");
	}
}

//验证道路名称
function checkName(){
	var name = $("#name").val();
	if(name==null||name==""||name=="道路名称不能为空"){
		$("#name").css("color","#E15748");
    	$("#name").val("道路名称不能为空");
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

//验证后方是否相通
function checkBackThrough(){
	var backThrough=backThroughCBB.combobox("getValue");
	if(backThrough==null||backThrough==""){
    	alert("请选择是否相通");
    	return false;
	}
	else
		return true;
}

//验证前方是否相通
function checkFrontThrough(){
	var frontThrough=backThroughCBB.combobox("getValue");
	if(backThrough==null||backThrough==""){
	  	alert("请选择是否相通");
	  	return false;
	}
	else
		return true;
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
<%@include file="../../side.jsp"%>
<div class="center_con_div" id="center_con_div">
	<div class="page_location_div">添加路名</div>
	
	<div id="new_div">
		<form id="form1" name="form1" method="post" action="" enctype="multipart/form-data">
		<table>
		  <tr>
			<td class="td1" align="right">
				路名
			</td>
			<td class="td2">
				<input type="text" class="name_inp" id="name" name="name" placeholder="请输入路名" onfocus="focusName()" onblur="checkName()"/>
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
				后方是否相通
			</td>
			<td class="td2">
				<select id="backThrough_cbb"></select>
				<input type="hidden" id="backThrough" name="backThrough"/>
			</td>
			<td class="td1" align="right">
				前方是否相通
			</td>
			<td class="td2">
				<select id="frontThrough_cbb"></select>
				<input type="hidden" id="frontThrough" name="frontThrough"/>
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