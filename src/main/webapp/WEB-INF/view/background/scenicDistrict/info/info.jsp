<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>景区信息</title>
<%@include file="../../js.jsp"%>
<link rel="stylesheet" href="<%=basePath %>resource/css/background/scenicDistrict/info/info.css"/>
<script type="text/javascript">
var path='<%=basePath %>';
var disdmdNum=0;
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
	initInfoSDSDMapDialogDiv();

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

function initInfoSDSDMapDialogDiv(){
	infoSDSDMDialog=$("#info_sd_sd_map_dialog_div").dialog({
		title:"景区地图",
		width:setFitWidthInParent("body","info_sd_sd_map_dialog_div"),
		height:730,
		top:10,
		left:20,
		buttons:[
           {text:"关闭",id:"cancel_but",iconCls:"icon-cancel",handler:function(){
        	   openInfoSDDialog(0);
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

	$(".panel.window").eq(disdmdNum).css("margin-top","40px");
	$(".panel.window .panel-title").eq(disdmdNum).css("color","#000");
	$(".panel.window .panel-title").eq(disdmdNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(disdmdNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").eq(disdmdNum).css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(disdmdNum).css("margin-top","40px");
	$(".window,.window .window-body").eq(disdmdNum).css("border-color","#ddd");

	$("#info_sd_sd_map_dialog_div #cancel_but").css("left","20%");
	$("#info_sd_sd_map_dialog_div #cancel_but").css("position","absolute");

	$("#info_sd_sd_map_dialog_div #reset_but").css("left","40%");
	$("#info_sd_sd_map_dialog_div #reset_but").css("position","absolute");

	$("#info_sd_sd_map_dialog_div #big_but").css("left","55%");
	$("#info_sd_sd_map_dialog_div #big_but").css("position","absolute");

	$("#info_sd_sd_map_dialog_div #small_but").css("left","80%");
	$("#info_sd_sd_map_dialog_div #small_but").css("position","absolute");
	
	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
	openInfoSDSDMDialog(0);
}

function openInfoSDDialog(flag){
	if(flag==1){
		$("#info_sd_canvas_bg_div").css("display","block");
	}
	else{
		$("#info_sd_canvas_bg_div").css("display","none");
	}
	openInfoSDSDMDialog(flag);
}

function openInfoSDSDMDialog(flag){
	if(flag==1){
		infoSDSDMDialog.dialog("open");
	}
	else{
		infoSDSDMDialog.dialog("close");
	}
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

		for(var i=0;i<scenicPlaceJA.length;i++){
			initScenicPlaceLocation(scenicPlaceJA[i]);//这里的循环必须放在外面，要是在方法里面循环，会默认为一张图片，加载到最后只显示最后一张图片
		}
		initRoadStageLocation();
		initXYLabelLocation();
		for(var i=0;i<textLabelJA.length;i++){
			var textLabelJO=textLabelJA[i];
			initTextLabelLocation(textLabelJO);
		}
		for(var i=0;i<busStopJA.length;i++){
			initBusStopLocation(busStopJA[i]);
		}
		
		var preSceDisCanvas=document.getElementById("sceDisCanvas");
		preSceDisCanvas.parentNode.removeChild(preSceDisCanvas);
		var sceDisCanvasDiv=document.getElementById("sceDisCanvas_div");
		sceDisCanvasDiv.appendChild(sceDisCanvas);
		
		if(reSizeFlag==1)
			loadSceDisCanvas(0);
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

function initTextLabelLocation(textLabelJO){
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
	var disdmdpw=$("body").find(".panel.window").eq(disdmdNum);
	var disdmdws=$("body").find(".window-shadow").eq(disdmdNum);

	var disdmdDiv=$("#info_sd_canvas_dialog_div");
	disdmdDiv.append(disdmdpw);
	disdmdDiv.append(disdmdws);
}

function loadSceDisCanvas(flag){
	var bigButDiv=$("#info_sd_sd_map_dialog_div #big_but");
	var smallButDiv=$("#info_sd_sd_map_dialog_div #small_but");
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
	case "info_sd_sd_map_dialog_div":
		space=170;
		break;
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
	<div class="info_sd_canvas_bg_div" id="info_sd_canvas_bg_div">
		<div class="info_sd_canvas_div" id="info_sd_canvas_div">
			<div class="tjst_div">
				<span class="tjst_span">查看地图</span>
				<span class="close_span" onclick="openInfoSDDialog(0)">X</span>
			</div>
			<div class="info_sd_canvas_dialog_div" id="info_sd_canvas_dialog_div">
				<div class="title_div">
					<span class="title_span">景区管理-景区信息-景区地图</span>
				</div>
				<input type="hidden" id="id"/>
				<div id="info_sd_sd_map_dialog_div">
					<div id="sceDisCanvas_div">
						<canvas id="sceDisCanvas">
						</canvas>
					</div>
				</div>
			</div>
		</div>
	</div>
	
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
			<span class="ckdt_span" onclick="openInfoSDDialog(1);">查看地图</span>
		</div>
	</div>
	<%@include file="../../foot.jsp"%>
</div>
</body>
</html>