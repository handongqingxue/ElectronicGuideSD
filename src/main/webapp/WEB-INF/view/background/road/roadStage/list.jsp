<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.tab1_div{
	margin-top:20px;
	margin-left: 220px;
}
.tab1_div .toolbar{
	height:32px;
}
.tab1_div .toolbar .name_span,.tab1_div .toolbar .roadName_span{
	margin-left: 13px;
}
.tab1_div .toolbar .name_inp,.tab1_div .toolbar .roadName_inp{
	width: 120px;
	height: 25px;
}
.tab1_div .toolbar .search_but{
	margin-left: 13px;
}
</style>
<title>路名查询</title>
<%@include file="../../js.jsp"%>
<script type="text/javascript">
var roadPath='<%=basePath%>'+"background/road/";
$(function(){
	initSearchLB();
	initAddLB();
	initTab1();
});

function initSearchLB(){
	$("#search_but").linkbutton({
		iconCls:"icon-search",
		onClick:function(){
			var roadName=$("#toolbar #roadName").val();
			var name=$("#toolbar #name").val();
			tab1.datagrid("load",{roadName:roadName,name:name});
		}
	});
}

function initAddLB(){
	$("#add_but").linkbutton({
		iconCls:"icon-add",
		onClick:function(){
			location.href=roadPath+"roadStage/add";
		}
	});
}

function initTab1(){
	tab1=$("#tab1").datagrid({
		title:"路段查询",
		url:roadPath+"selectRoadStageList",
		toolbar:"#toolbar",
		width:setFitWidthInParent("body"),
		pagination:true,
		pageSize:10,
		columns:[[
			{field:"name",title:"路段名",width:150},
			{field:"backX",title:"后方坐标",width:120,formatter:function(value,row){
				return value+","+row.backY;
			}},
			{field:"frontX",title:"前方坐标",width:120,formatter:function(value,row){
				return value+","+row.frontY;
			}},
			{field:"backThrough",title:"后方是否相通",width:120,formatter:function(value,row){
				return value?"是":"否";
			}},
			{field:"frontThrough",title:"前方是否相通",width:120,formatter:function(value,row){
				return value?"是":"否";
			}},
			{field:"backIsCross",title:"后方是否是交叉点",width:150,formatter:function(value,row){
				return value?"是":"否";
			}},
			{field:"backCrossRSNames",title:"后方交叉点路段名",width:150},
			{field:"frontIsCross",title:"前方是否是交叉点",width:150,formatter:function(value,row){
				return value?"是":"否";
			}},
			{field:"frontCrossRSNames",title:"前方交叉点路段名",width:150},
			{field:"roadName",title:"所属道路",width:150},
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
				$(this).datagrid("mergeCells",{index:0,field:"name",colspan:14});
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
			<span class="name_span">路段名：</span>
			<input type="text" class="name_inp" id="name" placeholder="请输入路段名"/>
			<span class="roadName_span">所属道路：</span>
			<input type="text" class="roadName_inp" id="roadName" placeholder="请输入所属道路"/>
			<a class="search_but" id="search_but">查询</a>
			<a id="add_but">添加</a>
		</div>
		<table id="tab1">
		</table>
	</div>
	<%@include file="../../foot.jsp"%>
</div>
</body>
</html>