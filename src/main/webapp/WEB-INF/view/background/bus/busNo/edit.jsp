<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>编辑车辆</title>
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
var busPath='<%=basePath%>'+"background/bus/";
var wechatAppletPath='<%=basePath%>'+"wechatApplet/";
var dialogTop=10;
var dialogLeft=20;
var edNum=0;
$(function(){
	initStartTimeCBB();
	initEndTimeCBB();
	initEditDialog();

	initDialogPosition();//将不同窗体移动到主要内容区域
});

function initStartTimeCBB(){
	var startTime='${requestScope.busNo.startTime }';
	var startTimeArr=startTime.split(":");
	var sth=startTimeArr[0];
	var stm=startTimeArr[1];
	var sts=startTimeArr[2];
	
	var sthData=[];
	sthData.push({id:"",name:"请选择时"});
	for(var i=0;i<24;i++){
		var field=i<10?"0"+i:i;
		sthData.push({id:field,name:field});
	}
	sthCBB=$("#sth_cbb").combobox({
		width:80,
		data:sthData,
		valueField:"id",
		textField:"name",
		onLoadSuccess:function(){
			$(this).combobox("setValue",sth);
		}
	});
	
	var stmData=[];
	stmData.push({id:"",name:"请选择分"});
	var stsData=[];
	stsData.push({id:"",name:"请选择秒"});
	for(var i=0;i<60;i++){
		var field=i<10?"0"+i:i;
		stmData.push({id:field,name:field});
		stsData.push({id:field,name:field});
	}
	stmCBB=$("#stm_cbb").combobox({
		width:80,
		data:stmData,
		valueField:"id",
		textField:"name",
		onLoadSuccess:function(){
			$(this).combobox("setValue",stm);
		}
	});
	stsCBB=$("#sts_cbb").combobox({
		width:80,
		data:stsData,
		valueField:"id",
		textField:"name",
		onLoadSuccess:function(){
			$(this).combobox("setValue",sts);
		}
	});
}

function initEndTimeCBB(){
	var endTime='${requestScope.busNo.endTime }';
	var endTimeArr=endTime.split(":");
	var eth=endTimeArr[0];
	var etm=endTimeArr[1];
	var ets=endTimeArr[2];
	
	var ethData=[];
	ethData.push({id:"",name:"请选择时"});
	for(var i=0;i<24;i++){
		var field=i<10?"0"+i:i;
		ethData.push({id:field,name:field});
	}
	ethCBB=$("#eth_cbb").combobox({
		width:80,
		data:ethData,
		valueField:"id",
		textField:"name",
		onLoadSuccess:function(){
			$(this).combobox("setValue",eth);
		}
	});
	
	var etmData=[];
	etmData.push({id:"",name:"请选择分"});
	var etsData=[];
	etsData.push({id:"",name:"请选择秒"});
	for(var i=0;i<60;i++){
		var field=i<10?"0"+i:i;
		etmData.push({id:field,name:field});
		etsData.push({id:field,name:field});
	}
	etmCBB=$("#etm_cbb").combobox({
		width:80,
		data:etmData,
		valueField:"id",
		textField:"name",
		onLoadSuccess:function(){
			$(this).combobox("setValue",etm);
		}
	});
	etsCBB=$("#ets_cbb").combobox({
		width:80,
		data:etsData,
		valueField:"id",
		textField:"name",
		onLoadSuccess:function(){
			$(this).combobox("setValue",ets);
		}
	});
}

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
		title:"路名信息",
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
	$("#edit_div table tr").css("height","45px");

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
			if(checkStartTime()){
				if(checkEndTime()){
					editBusNo();
				}
			}
		}
	}
}

function editBusNo(){
	var formData = new FormData($("#form1")[0]);
	$.ajax({
		type:"post",
		url:busPath+"editBusNo",
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

function focusName(){
	var name = $("#name").val();
	if(name=="车辆名不能为空"){
		$("#name").val("");
		$("#name").css("color", "#555555");
	}
}

//验证车辆名
function checkName(){
	var name = $("#name").val();
	if(name==null||name==""||name=="车辆名不能为空"){
		$("#name").css("color","#E15748");
    	$("#name").val("车辆名不能为空");
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

//验证首班时间
function checkStartTime(){
	var sth=sthCBB.combobox("getValue");
	var stm=stmCBB.combobox("getValue");
	var sts=stsCBB.combobox("getValue");
	if(sth==null||sth==""||stm==null||stm==""||sts==null||sts==""){
		$("#startTime").val("");
	  	alert("请选择首班时间");
	  	return false;
	}
	else{
		$("#startTime").val(sth+":"+stm+":"+sts);
		return true;
	}
}

//验证末班时间
function checkEndTime(){
	var eth=ethCBB.combobox("getValue");
	var etm=etmCBB.combobox("getValue");
	var ets=etsCBB.combobox("getValue");
	if(eth==null||eth==""||etm==null||etm==""||ets==null||ets==""){
		$("#endTime").val("");
	  	alert("请选择末班时间");
	  	return false;
	}
	else{
		$("#endTime").val(eth+":"+etm+":"+ets);
		return true;
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
	<div class="page_location_div">编辑路名</div>
	
	<div id="edit_div">
		<form id="form1" name="form1" method="post" action="" enctype="multipart/form-data">
		<input type="hidden" name="id" id="id" value="${requestScope.busNo.id }" />
		<table>
		  <tr>
			<td class="td1" align="right">
				路名
			</td>
			<td class="td2">
				<input type="text" class="name_inp" id="name" name="name" value="${requestScope.busNo.name }" placeholder="请输入路名" onfocus="focusName()" onblur="checkName()"/>
			</td>
			<td class="td1" align="right">
				排序
			</td>
			<td class="td2">
				<input type="number" class="sort_inp" id="sort" name="sort" value="${requestScope.busNo.sort }" placeholder="请输入排序"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				首班时间
			</td>
			<td class="td2">
				<select id="sth_cbb">
				</select>:
				<select id="stm_cbb">
				</select>:
				<select id="sts_cbb">
				</select>
				<input type="hidden" id="startTime" name="startTime" value="${requestScope.busNo.startTime }"/>
			</td>
			<td class="td1" align="right">
				末班时间
			</td>
			<td class="td2">
				<select id="eth_cbb">
				</select>:
				<select id="etm_cbb">
				</select>:
				<select id="ets_cbb">
				</select>
				<input type="hidden" id="endTime" name="endTime" value="${requestScope.busNo.endTime }"/>
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