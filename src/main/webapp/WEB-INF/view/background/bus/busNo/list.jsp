<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.tab1_div,.tab2_div{
	margin-top:20px;
	margin-left: 220px;
}
.tab1_div .toolbar,.tab2_div .toolbar{
	height:32px;
}
.tab1_div .toolbar .name_span,.tab2_div .toolbar .name_span{
	margin-left: 13px;
}
.tab1_div .toolbar .name_inp,.tab2_div .toolbar .name_inp{
	width: 120px;height: 25px;
}
.tab1_div .toolbar .search_but,.tab2_div .toolbar .search_but{
	margin-left: 13px;
}
</style>
<title>路名查询</title>
<%@include file="../../js.jsp"%>
<script type="text/javascript">
var busPath='<%=basePath%>'+"background/bus/";
$(function(){
	initTab1SearchLB();
	initTab1AddLB();
	initTab1();
	
	initTab2SearchLB();
	initTab2AddLB();
	initTab2();
});

function initTab1SearchLB(){
	$("#tab1_search_but").linkbutton({
		iconCls:"icon-search",
		onClick:function(){
			var name=$("#toolbar #name").val();
			tab1.datagrid("load",{name:name});
		}
	});
}

function initTab2SearchLB(){
	$("#tab2_search_but").linkbutton({
		iconCls:"icon-search",
		onClick:function(){
			var name=$("#toolbar #name").val();
			tab2.datagrid("load",{name:name});
		}
	});
}

function initTab1AddLB(){
	$("#tab1_add_but").linkbutton({
		iconCls:"icon-add",
		onClick:function(){
			location.href=busPath+"busNo/add";
		}
	});
}

function initTab2AddLB(){
	$("#tab2_add_but").linkbutton({
		iconCls:"icon-add",
		onClick:function(){
			location.href=busPath+"busNo/addStop";
		}
	});
}

function initTab1(){
	tab1=$("#tab1").datagrid({
		title:"车辆查询",
		url:busPath+"selectBusNoList",
		toolbar:"#tab1_div #toolbar",
		width:setFitWidthInParent("body"),
		pagination:true,
		pageSize:10,
		columns:[[
			{field:"name",title:"几路车",width:150},
			{field:"startTime",title:"首班时间",width:150},
			{field:"endTime",title:"末班时间",width:150},
            {field:"createTime",title:"创建时间",width:150},
            {field:"modifyTime",title:"修改时间",width:150},
            {field:"sort",title:"排序",width:80},
            {field:"id",title:"操作",width:110,formatter:function(value,row){
            	var str="<a href=\"edit?id="+value+"\">编辑</a>&nbsp;&nbsp;"
            		+"<a href=\"detail?id="+value+"\">详情</a>";
            	return str;
            }}
	    ]],
	    onClickRow:function(index,data){
	    	loadTab2Data(data.id);
	    },
        onLoadSuccess:function(data){
			if(data.total==0){
				$(this).datagrid("appendRow",{name:"<div style=\"text-align:center;\">暂无信息<div>"});
				$(this).datagrid("mergeCells",{index:0,field:"name",colspan:7});
				data.total=0;
			}
			
			$(".panel-header").css("background","linear-gradient(to bottom,#F4F4F4 0,#F4F4F4 20%)");
			$(".panel-header .panel-title").css("color","#000");
			$(".panel-header .panel-title").css("font-size","15px");
			$(".panel-header .panel-title").css("padding-left","10px");
			$(".panel-header, .panel-body").css("border-color","#ddd");
		}
	});
}

function initTab2(){
	tab2=$("#tab2").datagrid({
		title:"站点查询",
		toolbar:"#tab2_div #toolbar",
		width:setFitWidthInParent("body"),
		pagination:true,
		pageSize:10,
		columns:[[
			{field:"name",title:"名称",width:150},
			{field:"x",title:"x轴坐标",width:100},
			{field:"y",title:"y轴坐标",width:100},
			{field:"busNoNames",title:"站点车辆",width:200},
            {field:"createTime",title:"创建时间",width:150},
            {field:"modifyTime",title:"修改时间",width:150},
            {field:"sort",title:"排序",width:80},
            {field:"id",title:"操作",width:110,formatter:function(value,row){
            	var str="<a href=\"edit?id="+value+"\">编辑</a>&nbsp;&nbsp;"
            		+"<a href=\"detail?id="+value+"\">详情</a>";
            	return str;
            }}
	    ]],
        onLoadSuccess:function(data){
			if(data.total==0){
				$(this).datagrid("appendRow",{name:"<div style=\"text-align:center;\">暂无信息<div>"});
				$(this).datagrid("mergeCells",{index:0,field:"name",colspan:8});
				data.total=0;
			}
			
			$(".panel-header").css("background","linear-gradient(to bottom,#F4F4F4 0,#F4F4F4 20%)");
			$(".panel-header .panel-title").css("color","#000");
			$(".panel-header .panel-title").css("font-size","15px");
			$(".panel-header .panel-title").css("padding-left","10px");
			$(".panel-header, .panel-body").css("border-color","#ddd");
		}
	});
	tab2.datagrid("appendRow",{name:"<div style=\"text-align:center;\">暂无信息<div>"});
	tab2.datagrid("mergeCells",{index:0,field:"name",colspan:8});
}

function loadTab2Data(busNoId){
	$.post(busPath+"selectBusNosStopList",
		{busNoId:busNoId},
		function(result){
			tab2.datagrid('loadData',result.data);
		}
	,"json");
}

function setFitWidthInParent(o){
	var width=$(o).css("width");
	return width.substring(0,width.length-2)-250;
}
</script>
</head>
<body>
<div class="layui-layout layui-layout-admin">
	<%@include file="../../side.jsp"%>
	<div class="tab1_div" id="tab1_div">
		<div class="toolbar" id="toolbar">
			<span class="name_span">几路车：</span>
			<input type="text" class="name_inp" id="name" placeholder="请输入几路车"/>
			<a class="search_but" id="tab1_search_but">查询</a>
			<a id="tab1_add_but">添加</a>
		</div>
		<table id="tab1">
		</table>
	</div>
	<div class="tab2_div" id="tab2_div">
		<div class="toolbar" id="toolbar">
			<span class="name_span">站点名称：</span>
			<input type="text" class="name_inp" id="name" placeholder="请输入站点名称"/>
			<a class="search_but" id="tab2_search_but">查询</a>
			<a id="tab2_add_but">添加</a>
		</div>
		<table id="tab2">
		</table>
	</div>
	<%@include file="../../foot.jsp"%>
</div>
</body>
</html>