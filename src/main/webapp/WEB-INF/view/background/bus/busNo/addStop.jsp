<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加站点</title>
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
</style>
<script type="text/javascript">
var path='<%=basePath %>';
var busPath='<%=basePath%>'+"background/bus/";
var dialogTop=10;
var dialogLeft=20;
var ndNum=0;
$(function(){
	initBusNoCBB();
	initBusStopCBB();
	initPreBsCBB();
	initNextBsCBB();
	initNewDialog();

	initDialogPosition();//将不同窗体移动到主要内容区域
});

function initBusNoCBB(){
	var data=[];
	data.push({id:"",name:"请选择"});
	$.post(busPath+"selectBusNoCBBData",
		function(result){
			if(result.status=="ok"){
				var busNoList=result.busNoList;
				for(var i=0;i<busNoList.length;i++){
					var busNo=busNoList[i];
					data.push({id:busNo.id,name:busNo.name});
				}
				busNoCBB=$("#busNo_cbb").combobox({
					width:150,
					data:data,
					valueField:"id",
					textField:"name",
					onSelect:function(){
						var busNoId=busNoCBB.combobox("getValue");
						$("#busNoId").val(busNoId);
						selectBusStopCBBData();
					}
				});
			}
		}
	,"json");
}

function initBusStopCBB(){
	var data=[];
	data.push({id:"",name:"请选择"});
	busStopCBB=$("#busStop_cbb").combobox({
		width:150,
		data:data,
		valueField:"id",
		textField:"name",
		onSelect:function(){
			var busStopId=busStopCBB.combobox("getValue");
			$("#busStopId").val(busStopId);
			selectPreBsCBBData();
			selectNextBsCBBData();
		}
	});
}

function selectBusStopCBBData(){
	var busNoId=busNoCBB.combobox("getValue");
	$.post(busPath+"selectBusStopCBBData",
		{busNoId:busNoId},
		function(result){
			var data=[];
			data.push({id:"",name:"请选择"});
			if(result.status=="ok"){
				var busStopList=result.busStopList;
				for(var i=0;i<busStopList.length;i++){
					data.push(busStopList[i]);
				}
				busStopCBB.combobox("loadData",data);
			}
		}
	,"json");
}

function initPreBsCBB(){
	var data=[];
	data.push({id:"",name:"请选择"});
	preBsCBB=$("#preBs_cbb").combobox({
		width:150,
		data:data,
		valueField:"id",
		textField:"name",
		onSelect:function(){
			var preBsId=preBsCBB.combobox("getValue");
			$("#preBsId").val(preBsId);
		}
	});
}

function selectPreBsCBBData(){
	var busStopId=$("#busStopId").val();
	var busNoId=$("#busNoId").val();
	$.post(busPath+"selectOtherBsCBBData",
		{busStopId:busStopId,busNoId:busNoId},
		function(result){
			var data=[];
			data.push({id:"",name:"请选择"});
			if(result.status=="ok"){
				var busStopList=result.busStopList;
				for(var i=0;i<busStopList.length;i++){
					var busStop=busStopList[i];
					data.push({id:busStop.id,name:busStop.name});
				}
				preBsCBB.combobox("loadData",data);
			}
		}
	,"json");
}

function initNextBsCBB(){
	var data=[];
	data.push({id:"",name:"请选择"});
	nextBsCBB=$("#nextBs_cbb").combobox({
		width:150,
		data:data,
		valueField:"id",
		textField:"name",
		onSelect:function(){
			var nextBsId=nextBsCBB.combobox("getValue");
			$("#nextBsId").val(nextBsId);
		}
	});
}

function selectNextBsCBBData(){
	var busStopId=$("#busStopId").val();
	var busNoId=$("#busNoId").val();
	$.post(busPath+"selectOtherBsCBBData",
		{busStopId:busStopId,busNoId:busNoId},
		function(result){
			var data=[];
			data.push({id:"",name:"请选择"});
			if(result.status=="ok"){
				var busStopList=result.busStopList;
				for(var i=0;i<busStopList.length;i++){
					var busStop=busStopList[i];
					data.push({id:busStop.id,name:busStop.name});
				}
				nextBsCBB.combobox("loadData",data);
			}
		}
	,"json");
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
		title:"站点信息",
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
	if(checkBusNoId()){
		if(checkBusStopId()){
			addBusNosStop();
		}
	}
}

function addBusNosStop(){
	var formData = new FormData($("#form1")[0]);
	$.ajax({
		type:"post",
		url:busPath+"addBusNosStop",
		dataType: "json",
		data:formData,
		cache: false,
		processData: false,
		contentType: false,
		success: function (data){
			if(data.status==1){
				alert(data.msg);
				location.href=busPath+"busNo/list";
			}
			else{
				alert(data.msg);
			}
		}
	});
}

//验证车辆
function checkBusNoId(){
	var busNoId=busNoCBB.combobox("getValue");
	if(busNoId==null||busNoId==""){
	  	alert("请选择车辆");
	  	return false;
	}
	else
		return true;
}

function checkBusStopId(){
	var busStopId=busStopCBB.combobox("getValue");
	if(busStopId==null||busStopId==""){
	  	alert("请选择车辆站点");
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
		<div class="page_location_div">添加站点</div>
		
		<div id="new_div">
			<form id="form1" name="form1" method="post" action="" enctype="multipart/form-data">
			<table>
			  <tr>
				<td class="td1" align="right">
					车辆
				</td>
				<td class="td2">
					<select id="busNo_cbb"></select>
					<input type="hidden" id="busNoId" name="busNoId"/>
				</td>
				<td class="td1" align="right">
					车辆站点
				</td>
				<td class="td2">
					<select id="busStop_cbb"></select>
					<input type="hidden" id="busStopId" name="busStopId"/>
				</td>
			  </tr>
			  <tr>
				<td class="td1" align="right">
					上个站点
				</td>
				<td class="td2">
					<select id="preBs_cbb"></select>
					<input type="hidden" id="preBsId" name="preBsId"/>
				</td>
				<td class="td1" align="right">
					下个站点
				</td>
				<td class="td2">
					<select id="nextBs_cbb"></select>
					<input type="hidden" id="nextBsId" name="nextBsId"/>
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