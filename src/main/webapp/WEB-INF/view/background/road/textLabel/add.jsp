<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加标签</title>
<%@include file="../../js.jsp"%>
<style type="text/css">
.add_tl_canvas_bg_div{
	width: 100%;
	height: 100%;
	background-color: rgba(0,0,0,.45);
	position: fixed;
	z-index: 9016;
	display:none;
}
.add_tl_canvas_div{
	width: 1800px;
	height: 850px;
	margin: 50px auto 0;
	background-color: #fff;
	border-radius:5px;
	position: absolute;
	left: 0;
	right: 0;
}
.add_tl_canvas_div .tjst_div{
	width: 100%;
	height: 50px;
	line-height: 50px;
	border-bottom: #eee solid 1px;
}
.add_tl_canvas_div .tjst_span{
	margin-left: 30px;
}
.add_tl_canvas_div .close_span{
	float: right;margin-right: 30px;cursor: pointer;
}
.add_tl_canvas_dialog_div{
	width: 1775px;
	height: 800px;
	position: absolute;
}
.add_tl_canvas_div .title_div{
	width: 100%;height: 50px;line-height: 50px;
}
.add_tl_canvas_div .title_span{
	margin-left: 30px;
}
.add_tl_sd_map_dialog_div .toolbar{
	height:32px;
}
.add_tl_sd_map_dialog_div .toolbar .row_div{
	margin-top: 5px;
}
.add_tl_sd_map_dialog_div .toolbar .xsbq_span{
	margin-left: 13px;
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
.name_inp,.sort_inp,.rotate_inp,.x_inp,.y_inp{
	width: 180px;
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
.showMapBut_div{
	width: 90px;
}
.sceDis_img{
	height:180px;
	margin-top: 10px;
}
</style>
<script type="text/javascript">
var path='<%=basePath %>';
var sceDisPath='<%=basePath%>'+"background/scenicDistrict/";
var roadPath='<%=basePath%>'+"background/road/";
var dialogTop=10;
var dialogLeft=20;
var ndNum=0;
var atlsdmdNum=1;
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
var textLabel;
var textLabelY;
var lineWidth=10;
var fontMarginLeft=45;
var nameMarginTop=30;
var arcR=10;
var atSpace=10;
var scenicPlaceJA;
var roadStageJA;
var textLabelJA;
var busStopJA;
var busStopImageUrl=path+"resource/image/busStop.png";
var busStopWidth=50;
var busStopHeight=50;
$(function(){
	jiSuanScale();
	initScenicPlaceJA();
	initRoadStageJA();
	initTextLabelJA();
	initBusStopJA();
	initEntityTypesCBB();
	initNewDialog();
	initAddTLSDMapDialogDiv();

	initDialogPosition();//将不同窗体移动到主要内容区域
	
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

function initScenicPlaceJA(){
	scenicPlaceJA=JSON.parse('${requestScope.scenicPlaceJAStr}');
	for(var i=0;i<scenicPlaceJA.length;i++){
		var scenicPlaceJO=scenicPlaceJA[i];
		scenicPlaceJO.y=sceDisCanvasMinHeight-scenicPlaceJO.y;
	}
}

function initRoadStageJA(){
	roadStageJA=JSON.parse('${requestScope.roadStageJAStr}');
	for(var i=0;i<roadStageJA.length;i++){
		var roadStageJO=roadStageJA[i];
		roadStageJO.backY=sceDisCanvasMinHeight-roadStageJO.backY;
		roadStageJO.frontY=sceDisCanvasMinHeight-roadStageJO.frontY;
	}
}

//https://www.cnblogs.com/ye-hcj/p/10356397.html
function initTextLabelJA(){
	textLabelJA=JSON.parse('${requestScope.textLabelJAStr}');
	for(var i=0;i<textLabelJA.length;i++){
		var textLabelJO=textLabelJA[i];
		textLabelJO.y=sceDisCanvasMinHeight-textLabelJO.y;
	}
}

function initBusStopJA(){
	busStopJA=JSON.parse('${requestScope.busStopJAStr}');
	for(var i=0;i<busStopJA.length;i++){
		var busStopJO=busStopJA[i];
		busStopJO.y=sceDisCanvasMinHeight-busStopJO.y;
	}
}

function initEntityTypesCBB(){
	var data=[];
	data.push({type:"",name:"请选择"});
	$.post(sceDisPath+"selectEntityTypeCBBData",
		function(result){
			if(result.status=="ok"){
				var entityTypeList=result.entityTypeList;
				for(var i=0;i<entityTypeList.length;i++){
					var entityType=entityTypeList[i];
					data.push({type:entityType.type,name:entityType.name});
				}
				entityTypesCBB=$("#entityTypes_cbb").combobox({
					width:120,
					data:data,
	                multiple:true,
					valueField:"type",
					textField:"name",
					onLoadSuccess:function(){
						var types=""
						for (var i = 1; i < data.length; i++){
							types+=","+data[i].type;
						}
						$(this).combobox("setValues",types.substring(1).split(","));
					},
					onChange:function(){
						initSceDisCanvas(0);
					}
				});
			}
			initSceDisCanvas();
		}
	,"json");
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
	sceDisCanvas.style.width=sceDisCanvasStyleWidth+"px";//通过缩放来改变画布大小，画布大小改变后，上面的定位点位置也就跟着改变了
	sceDisCanvas.style.height=sceDisCanvasStyleHeight+"px";
	sceDisCanvas.width=sceDisCanvasWidth;
	sceDisCanvas.height=sceDisCanvasHeight;
	sceDisCanvasContext = sceDisCanvas.getContext("2d");
	sceDisCanvasImg.onload=function(){
		sceDisCanvasContext.drawImage(sceDisCanvasImg, 0, 0, sceDisCanvasWidth, sceDisCanvasHeight);

		var entityTypes=entityTypesCBB.combobox("getValues").toString();
		if(entityTypes.indexOf("scenicPlace")!=-1){
			for(var i=0;i<scenicPlaceJA.length;i++){
				initScenicPlaceLocation(scenicPlaceJA[i]);//这里的循环必须放在外面，要是在方法里面循环，会默认为一张图片，加载到最后只显示最后一张图片
			}
		}
		if(entityTypes.indexOf("road")!=-1)
			initRoadStageLocation();
		if(entityTypes.indexOf("xy")!=-1)
			initXYLabelLocation();
		if(entityTypes.indexOf("textLabel")!=-1)
			initTextLabelLocation();
		if(textLabel!=undefined)
			setTextLabelLocation();
		if(entityTypes.indexOf("busStop")!=-1){
			for(var i=0;i<busStopJA.length;i++){
				initBusStopLocation(busStopJA[i]);
			}
		}
		
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
	           x=x*(sceDisCanvasMinWidth/sceDisCanvasStyleWidth);//用最初的画布宽度比上当前画布宽度，得出缩放比例，从而将点击获得的坐标还原为画布上的坐标
	           y=y*(sceDisCanvasMinHeight/sceDisCanvasStyleHeight);
	           
	           textLabelY=sceDisCanvasMinHeight-y;//将y轴坐标从最初的左上角计算转换为从左下角计算
	           
		       var name=$("#name").val();
		       var rotate=$("#rotate").val();
		       textLabel={name:name,x:x,y:y,rotate:rotate};
	           initSceDisCanvas(0);
	    }
	}
}

function initScenicPlaceLocation(scenicPlaceJO){
	var entityImg = new Image();
	entityImg.src=scenicPlaceJO.picUrl;
	entityImg.onload=function(){
		//不管画布怎么放大、缩小，生成坐标的点位置还是原来的。只是上面鼠标点击后获取的坐标是从坐上为原点计算的，这里画图也是和上面一样的原理，从左上为原点计算位置。只是插入数据库的位置是转换后以左下为原点计算的
		sceDisCanvasContext.drawImage(entityImg, scenicPlaceJO.x/widthScale-scenicPlaceJO.picWidth/2, scenicPlaceJO.y/heightScale-scenicPlaceJO.picHeight/2, scenicPlaceJO.picWidth, scenicPlaceJO.picHeight);
	}

	var rectWidth=20*scenicPlaceJO.name.length+20;
	sceDisCanvasContext.font="25px bold 黑体";
	sceDisCanvasContext.fillStyle = "#000";
	sceDisCanvasContext.fillText(scenicPlaceJO.name,scenicPlaceJO.x/widthScale-rectWidth/2,scenicPlaceJO.y/heightScale+scenicPlaceJO.picHeight/2+nameMarginTop);
	sceDisCanvasContext.stroke();
}

function initRoadStageLocation(){
	sceDisCanvasContext.strokeStyle = 'blue';//点填充
	sceDisCanvasContext.fillStyle='blue';
	sceDisCanvasContext.lineWidth=lineWidth;
	for(var i=0;i<roadStageJA.length;i++){
		var roadStageJO=roadStageJA[i];
		sceDisCanvasContext.beginPath();
		sceDisCanvasContext.arc(roadStageJO.backX/widthScale,roadStageJO.backY/heightScale,arcR/15,0,2*Math.PI);
		sceDisCanvasContext.moveTo(roadStageJO.backX/widthScale, roadStageJO.backY/heightScale);//起始位置
		sceDisCanvasContext.lineTo(roadStageJO.frontX/widthScale, roadStageJO.frontY/heightScale);//停止位置
		sceDisCanvasContext.arc(roadStageJO.frontX/widthScale,roadStageJO.frontY/heightScale,arcR/15,0,2*Math.PI);
		sceDisCanvasContext.stroke();
	}
}

function initXYLabelLocation(){
	for(var i=0;i<roadStageJA.length;i++){
		var roadStageJO=roadStageJA[i];
		var backXY="("+roadStageJO.backX+","+(sceDisCanvasMinHeight-roadStageJO.backY)+")";
		var backRectWidth=20*backXY.length+20;
		var frontXY="("+roadStageJO.frontX+","+(sceDisCanvasMinHeight-roadStageJO.frontY)+")";
		var frontRectWidth=20*frontXY.length+20;
		sceDisCanvasContext.beginPath();
		sceDisCanvasContext.font="25px bold 黑体";
		sceDisCanvasContext.fillStyle = "#f00";
		sceDisCanvasContext.fillText(backXY,roadStageJO.backX/widthScale-backRectWidth/2+fontMarginLeft,roadStageJO.backY/heightScale-atSpace);
		sceDisCanvasContext.fillText(frontXY,roadStageJO.frontX/widthScale-backRectWidth/2+fontMarginLeft,roadStageJO.frontY/heightScale-atSpace);
		sceDisCanvasContext.stroke();
	}
}

function initTextLabelLocation(){
	for(var i=0;i<textLabelJA.length;i++){
		var textLabelJO=textLabelJA[i];
		var name=textLabelJO.name;
		var rectWidth=20*name.length+20;
		sceDisCanvasContext.beginPath();
		
		sceDisCanvasContext.translate(textLabelJO.x/widthScale-rectWidth/2+fontMarginLeft,textLabelJO.y/heightScale-atSpace);
		sceDisCanvasContext.rotate(textLabelJO.rotate*(Math.PI/180));
		
		
		sceDisCanvasContext.font="25px bold 黑体";
		sceDisCanvasContext.fillStyle = "#000";
		sceDisCanvasContext.fillText(name,0,0);
		
		sceDisCanvasContext.stroke();

		sceDisCanvasContext.rotate(-(textLabelJO.rotate*(Math.PI/180)));
		sceDisCanvasContext.translate(-(textLabelJO.x/widthScale-rectWidth/2+fontMarginLeft),-(textLabelJO.y/heightScale-atSpace));
	}
}

function setTextLabelLocation(){
	var name=textLabel.name;
	var rectWidth=60*name.length;
	var x=textLabel.x;
	var y=textLabel.y;
	var rotate=textLabel.rotate;
	
	sceDisCanvasContext.translate(x/widthScale-rectWidth/2+fontMarginLeft,y/heightScale-atSpace);
	sceDisCanvasContext.rotate(rotate*(Math.PI/180));
	
	sceDisCanvasContext.font="25px bold 黑体";
	sceDisCanvasContext.fillStyle = "#000";
	sceDisCanvasContext.fillText(name,0,0);
	
	sceDisCanvasContext.stroke();

	sceDisCanvasContext.rotate(-(rotate*(Math.PI/180)));
	sceDisCanvasContext.translate(-(x/widthScale-rectWidth/2+fontMarginLeft),-(y/heightScale-atSpace));
}

function initBusStopLocation(busStopJO){
	var entityImg = new Image();
	entityImg.src=busStopImageUrl;
	entityImg.onload=function(){
		//不管画布怎么放大、缩小，生成坐标的点位置还是原来的。只是上面鼠标点击后获取的坐标是从坐上为原点计算的，这里画图也是和上面一样的原理，从左上为原点计算位置。只是插入数据库的位置是转换后以左下为原点计算的
		sceDisCanvasContext.drawImage(entityImg, busStopJO.x/widthScale-busStopWidth/2, busStopJO.y/heightScale-busStopHeight/2, busStopWidth, busStopHeight);
	}

	var rectWidth=20*busStopJO.name.length+20;
	sceDisCanvasContext.font="25px bold 黑体";
	sceDisCanvasContext.fillStyle = "#000";
	sceDisCanvasContext.fillText(busStopJO.name,busStopJO.x/widthScale-rectWidth/2,busStopJO.y/heightScale+busStopHeight/2+nameMarginTop);
	sceDisCanvasContext.stroke();
}

function initDialogPosition(){
	//基本属性组
	var ndpw=$("body").find(".panel.window").eq(ndNum);
	var ndws=$("body").find(".window-shadow").eq(ndNum);
	
	var atlsdmdpw=$("body").find(".panel.window").eq(atlsdmdNum);
	var atlsdmdws=$("body").find(".window-shadow").eq(atlsdmdNum);

	var ccDiv=$("#center_con_div");
	ccDiv.append(ndpw);
	ccDiv.append(ndws);
	ccDiv.css("width",setFitWidthInParent("body","center_con_div")+"px");
	
	var atlsdmdDiv=$("#add_tl_canvas_dialog_div");
	atlsdmdDiv.append(atlsdmdpw);
	atlsdmdDiv.append(atlsdmdws);
}

function initAddTLSDMapDialogDiv(){
	addTLSDMDialog=$("#add_tl_sd_map_dialog_div").dialog({
		title:"景区地图",
		toolbar:"#add_tl_sd_map_dialog_div #toolbar",
		width:setFitWidthInParent("body","add_tl_sd_map_dialog_div"),
		height:730,
		top:10,
		left:20,
		buttons:[
           {text:"取消",id:"cancel_but",iconCls:"icon-cancel",handler:function(){
        	   openAddTLDialog(0);
           }},
           {text:"确定",id:"ok_but",iconCls:"icon-ok",handler:function(){
        	   $("#x").val(textLabel.x);
        	   $("#y").val(textLabelY);
        	   $("#ratate").val(textLabel.ratate);
        	   openAddTLDialog(0);
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

	$(".panel.window").eq(atlsdmdNum).css("margin-top","40px");
	$(".panel.window .panel-title").eq(atlsdmdNum).css("color","#000");
	$(".panel.window .panel-title").eq(atlsdmdNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(atlsdmdNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").eq(atlsdmdNum).css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(atlsdmdNum).css("margin-top","40px");
	$(".window,.window .window-body").eq(atlsdmdNum).css("border-color","#ddd");

	$("#add_tl_sd_map_dialog_div #cancel_but").css("left","20%");
	$("#add_tl_sd_map_dialog_div #cancel_but").css("position","absolute");

	$("#add_tl_sd_map_dialog_div #ok_but").css("left","35%");
	$("#add_tl_sd_map_dialog_div #ok_but").css("position","absolute");

	$("#add_tl_sd_map_dialog_div #reset_but").css("left","50%");
	$("#add_tl_sd_map_dialog_div #reset_but").css("position","absolute");

	$("#add_tl_sd_map_dialog_div #big_but").css("left","65%");
	$("#add_tl_sd_map_dialog_div #big_but").css("position","absolute");

	$("#add_tl_sd_map_dialog_div #small_but").css("left","80%");
	$("#add_tl_sd_map_dialog_div #small_but").css("position","absolute");
	
	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
	openAddTLSDMDialog(0);
}

function initNewDialog(){
	dialogTop+=20;
	$("#new_div").dialog({
		title:"标签信息",
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

function openAddTLDialog(flag){
	if(flag==1){
		$("#add_tl_canvas_bg_div").css("display","block");
	}
	else{
		$("#add_tl_canvas_bg_div").css("display","none");
	}
	openAddTLSDMDialog(flag);
}

function openAddTLSDMDialog(flag){
	if(flag==1){
		addTLSDMDialog.dialog("open");
	}
	else{
		addTLSDMDialog.dialog("close");
	}
}

function checkAdd(){
	if(checkName()){
		if(checkSort()){
			if(checkRotate()){
				if(checkX()){
					if(checkY()){
						addTextLabel();
					}
				}
			}
		}
	}
}

function addTextLabel(){
	var formData = new FormData($("#form1")[0]);
	$.ajax({
		type:"post",
		url:roadPath+"addTextLabel",
		dataType: "json",
		data:formData,
		cache: false,
		processData: false,
		contentType: false,
		success: function (data){
			if(data.status==1){
				alert(data.msg);
				location.href=roadPath+"textLabel/list";
			}
			else{
				alert(data.msg);
			}
		}
	});
}

function checkTextLabelInfo(){
	if(checkName()){
		if(checkRotate()){
			openAddTLDialog(1);
		}
	}
}

function focusName(){
	var name = $("#name").val();
	if(name=="标签名称不能为空"){
		$("#name").val("");
		$("#name").css("color", "#555555");
	}
}

//验证标签名称
function checkName(){
	var name = $("#name").val();
	if(name==null||name==""||name=="标签名称不能为空"){
		$("#name").css("color","#E15748");
    	$("#name").val("标签名称不能为空");
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

//验证旋转角度
function checkRotate(){
	var rotate = $("#rotate").val();
	if(rotate==null||rotate==""){
	  	alert("请输入旋转角度");
	  	return false;
	}
	else
		return true;
}

//验证x轴坐标
function checkX(){
	var x = $("#x").val();
	if(x==null||x==""){
	  	alert("请选择x轴坐标");
	  	return false;
	}
	else
		return true;
}

//验证y轴坐标
function checkY(){
	var y = $("#y").val();
	if(y==null||y==""){
	  	alert("请选择y轴坐标");
	  	return false;
	}
	else
		return true;
}

function loadSceDisCanvas(flag){
	var bigButDiv=$("#add_tl_sd_map_dialog_div #big_but");
	var smallButDiv=$("#add_tl_sd_map_dialog_div #small_but");
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
	case "add_tl_sd_map_dialog_div":
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
	<div class="add_tl_canvas_bg_div" id="add_tl_canvas_bg_div">
		<div class="add_tl_canvas_div" id="add_tl_canvas_div">
			<div class="tjst_div">
				<span class="tjst_span">添加实体</span>
				<span class="close_span" onclick="openAddTLDialog(0)">X</span>
			</div>
			<div class="add_tl_canvas_dialog_div" id="add_tl_canvas_dialog_div">
				<div class="title_div">
					<span class="title_span">道路管理-标签查询-添加</span>
				</div>
				<input type="hidden" id="id"/>
				<div class="add_tl_sd_map_dialog_div" id="add_tl_sd_map_dialog_div">
					<div class="toolbar" id="toolbar">
						<div class="row_div">
							<span class="xsbq_span">显示标签</span>&nbsp;&nbsp;&nbsp;
							<select id="entityTypes_cbb"></select>
						</div>
					</div>
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
	<div class="page_location_div">添加标签</div>
	
	<div id="new_div">
		<form id="form1" name="form1" method="post" action="" enctype="multipart/form-data">
		<table>
		  <tr>
			<td class="td1" align="right">
				名称
			</td>
			<td class="td2">
				<input type="text" class="name_inp" id="name" name="name" placeholder="请输入标签名称" onfocus="focusName()" onblur="checkName()"/>
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
				景区地图
			</td>
			<td class="td2">
				<div class="upBut_div showMapBut_div" onclick="checkTextLabelInfo();">显示地图</div>
				<img class="sceDis_img" id="sceDis_img" alt="" src="${sessionScope.user.scenicDistrict.mapUrl }"/>
			</td>
			<td class="td1" align="right">
				旋转角度
			</td>
			<td class="td2">
				<input type="number" class="rotate_inp" id="rotate" name="rotate" placeholder="请输入旋转角度"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				x轴坐标
			</td>
			<td class="td2">
				<input type="number" class="x_inp" id="x" name="x" placeholder="请输入x轴坐标"/>
			</td>
			<td class="td1" align="right">
				y轴坐标
			</td>
			<td class="td2">
				<input type="number" class="y_inp" id="y" name="y" placeholder="请输入y轴坐标"/>
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