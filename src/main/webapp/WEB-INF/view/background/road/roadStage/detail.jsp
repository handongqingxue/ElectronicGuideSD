<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>路段详情</title>
<%@include file="../../js.jsp"%>
<style type="text/css">
.detail_rs_canvas_bg_div{
	width: 100%;
	height: 100%;
	background-color: rgba(0,0,0,.45);
	position: fixed;
	z-index: 9016;
	display:none;
}
.detail_rs_canvas_div{
	width: 1800px;
	height: 850px;
	margin: 50px auto 0;
	background-color: #fff;
	border-radius:5px;
	position: absolute;
	left: 0;
	right: 0;
}
.detail_rs_canvas_div .tjst_div{
	width: 100%;
	height: 50px;
	line-height: 50px;
	border-bottom: #eee solid 1px;
}
.detail_rs_canvas_div .tjst_span{
	margin-left: 30px;
}
.detail_rs_canvas_div .close_span{
	float: right;margin-right: 30px;cursor: pointer;
}
.detail_rs_canvas_dialog_div{
	width: 1775px;
	height: 800px;
	position: absolute;
}
.detail_rs_canvas_div .title_div{
	width: 100%;height: 50px;line-height: 50px;
}
.detail_rs_canvas_div .title_span{
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
	margin-top: -23px;
	margin-left: 173px;
}
</style>
<script type="text/javascript">
var path='<%=basePath %>';
var roadPath='<%=basePath%>'+"background/road/";
var dialogTop=10;
var dialogLeft=20;
var ddNum=0;
var drssdmdNum=1;
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
var roadStage;
var startPoint=null;
var endPoint=null;
var backX;
var backY;
var frontX;
var frontY;
var arcR=10;
var lineWidth=10;
var fontMarginLeft=45;
var atSpace=10;
var scenicPlaceJA;
var otherRSJA;
var textLabelJA;
$(function(){
	jiSuanScale();
	initScenicPlaceJA();
	initOtherRSJA();
	initTextLabelJA();
	initRoadStage();
	initDetailDialog();
	initDetailRsSDMapDialogDiv();

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

function initOtherRSJA(){
	otherRSJA=JSON.parse('${requestScope.otherRSJAStr}');
	for(var i=0;i<otherRSJA.length;i++){
		var otherRSJO=otherRSJA[i];
		otherRSJO.backY=sceDisCanvasMinHeight-otherRSJO.backY;
		otherRSJO.frontY=sceDisCanvasMinHeight-otherRSJO.frontY;
	}
}

function initTextLabelJA(){
	textLabelJA=JSON.parse('${requestScope.textLabelJAStr}');
	for(var i=0;i<textLabelJA.length;i++){
		var textLabelJO=textLabelJA[i];
		textLabelJO.y=sceDisCanvasMinHeight-textLabelJO.y;
	}
}

function initRoadStage(){
	roadStage={backX:'${requestScope.roadStage.backX }',backY:sceDisCanvasMinHeight-'${requestScope.roadStage.backY }',frontX:'${requestScope.roadStage.frontX }',frontY:sceDisCanvasMinHeight-'${requestScope.roadStage.frontY }'};;
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

		for(var i=0;i<scenicPlaceJA.length;i++){
			initScenicPlaceLocation(scenicPlaceJA[i]);//这里的循环必须放在外面，要是在方法里面循环，会默认为一张图片，加载到最后只显示最后一张图片
		}
		initRoadStageLocation();
		initXYLabelLocation();
		initTextLabelLocation();
		
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
}

function initRoadStageLocation(){
	sceDisCanvasContext.strokeStyle = 'blue';//点填充
	sceDisCanvasContext.fillStyle='blue';
	sceDisCanvasContext.lineWidth=lineWidth;
	for(var i=0;i<otherRSJA.length;i++){
		var otherRSJO=otherRSJA[i];
		sceDisCanvasContext.beginPath();
		sceDisCanvasContext.arc(otherRSJO.backX/widthScale,otherRSJO.backY/heightScale,arcR/15,0,2*Math.PI);
		sceDisCanvasContext.moveTo(otherRSJO.backX/widthScale, otherRSJO.backY/heightScale);//起始位置
		sceDisCanvasContext.lineTo(otherRSJO.frontX/widthScale, otherRSJO.frontY/heightScale);//停止位置
		sceDisCanvasContext.arc(otherRSJO.frontX/widthScale,otherRSJO.frontY/heightScale,arcR/15,0,2*Math.PI);
		sceDisCanvasContext.stroke();
	}
	
	if(roadStage.backX!=-1&roadStage.backY!=-1){
		sceDisCanvasContext.beginPath();
		sceDisCanvasContext.strokeStyle = 'red';//点填充
		sceDisCanvasContext.fillStyle='red';
		sceDisCanvasContext.lineWidth=arcR*1.5;
		sceDisCanvasContext.arc(roadStage.backX/widthScale,roadStage.backY/heightScale,arcR,0,2*Math.PI);
		sceDisCanvasContext.stroke();
	}
	if(roadStage.frontX!=-1&roadStage.frontY!=-1){
		sceDisCanvasContext.beginPath();
		sceDisCanvasContext.strokeStyle = 'red';//点填充
		sceDisCanvasContext.fillStyle='red';
		sceDisCanvasContext.lineWidth=arcR*1.5;
		sceDisCanvasContext.arc(roadStage.frontX/widthScale,roadStage.frontY/heightScale,arcR,0,2*Math.PI);
		sceDisCanvasContext.stroke();
	}
	if(roadStage.backX!=-1&roadStage.backY!=-1&roadStage.frontX!=-1&roadStage.frontY!=-1){
		sceDisCanvasContext.strokeStyle = 'red';//点填充
		sceDisCanvasContext.fillStyle='red';
		sceDisCanvasContext.lineWidth=lineWidth;
		sceDisCanvasContext.beginPath();
		sceDisCanvasContext.moveTo(roadStage.backX/widthScale, roadStage.backY/heightScale);//起始位置
		sceDisCanvasContext.lineTo(roadStage.frontX/widthScale, roadStage.frontY/heightScale);//停止位置
		sceDisCanvasContext.stroke();
	}
}

function initXYLabelLocation(){
	for(var i=0;i<otherRSJA.length;i++){
		var otherRSJO=otherRSJA[i];
		var backXY="("+otherRSJO.backX+","+(sceDisCanvasMinHeight-otherRSJO.backY)+")";
		var backRectWidth=20*backXY.length+20;
		var frontXY="("+otherRSJO.frontX+","+(sceDisCanvasMinHeight-otherRSJO.frontY)+")";
		var frontRectWidth=20*frontXY.length+20;
		sceDisCanvasContext.beginPath();
		sceDisCanvasContext.font="25px bold 黑体";
		sceDisCanvasContext.fillStyle = "#f00";
		sceDisCanvasContext.fillText(backXY,otherRSJO.backX/widthScale-backRectWidth/2+fontMarginLeft,otherRSJO.backY/heightScale-atSpace);
		sceDisCanvasContext.fillText(frontXY,otherRSJO.frontX/widthScale-backRectWidth/2+fontMarginLeft,otherRSJO.frontY/heightScale-atSpace);
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
		
		//sceDisCanvasContext.fillText(backXY,otherRSJO.backX/widthScale-backRectWidth/2+fontMarginLeft,otherRSJO.backY/heightScale-atSpace);
		//sceDisCanvasContext.fillText(frontXY,otherRSJO.frontX/widthScale-backRectWidth/2+fontMarginLeft,otherRSJO.frontY/heightScale-atSpace);
		sceDisCanvasContext.stroke();

		sceDisCanvasContext.rotate(-(textLabelJO.rotate*(Math.PI/180)));
		sceDisCanvasContext.translate(-(textLabelJO.x/widthScale-rectWidth/2+fontMarginLeft),-(textLabelJO.y/heightScale-atSpace));
	}
}

function initDialogPosition(){
	//基本属性组
	var ddpw=$("body").find(".panel.window").eq(ddNum);
	var ddws=$("body").find(".window-shadow").eq(ddNum);
	
	var drssdmdpw=$("body").find(".panel.window").eq(drssdmdNum);
	var drssdmdws=$("body").find(".window-shadow").eq(drssdmdNum);

	var ccDiv=$("#center_con_div");
	ccDiv.append(ddpw);
	ccDiv.append(ddws);
	ccDiv.css("width",setFitWidthInParent("body","center_con_div")+"px");
	
	var drscdDiv=$("#detail_rs_canvas_dialog_div");
	drscdDiv.append(drssdmdpw);
	drscdDiv.append(drssdmdws);
}

function initDetailRsSDMapDialogDiv(){
	detailRsSdMDialog=$("#detail_rs_sd_map_dialog_div").dialog({
		title:"景区地图",
		width:setFitWidthInParent("body","detail_rs_sd_map_dialog_div"),
		height:730,
		top:10,
		left:20,
		buttons:[
           {text:"关闭",id:"cancel_but",iconCls:"icon-cancel",handler:function(){
        	   openDetailRsDialog(0);
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

	$(".panel.window").eq(drssdmdNum).css("margin-top","40px");
	$(".panel.window .panel-title").eq(drssdmdNum).css("color","#000");
	$(".panel.window .panel-title").eq(drssdmdNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(drssdmdNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").eq(drssdmdNum).css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(drssdmdNum).css("margin-top","40px");
	$(".window,.window .window-body").eq(drssdmdNum).css("border-color","#ddd");

	$("#detail_rs_sd_map_dialog_div #cancel_but").css("left","20%");
	$("#detail_rs_sd_map_dialog_div #cancel_but").css("position","absolute");

	$("#detail_rs_sd_map_dialog_div #reset_but").css("left","40%");
	$("#detail_rs_sd_map_dialog_div #reset_but").css("position","absolute");

	$("#detail_rs_sd_map_dialog_div #big_but").css("left","55%");
	$("#detail_rs_sd_map_dialog_div #big_but").css("position","absolute");

	$("#detail_rs_sd_map_dialog_div #small_but").css("left","80%");
	$("#detail_rs_sd_map_dialog_div #small_but").css("position","absolute");
	
	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
	openDetailRsSdMDialog(0);
}

function initDetailDialog(){
	dialogTop+=20;
	$("#detail_div").dialog({
		title:"路段信息",
		width:setFitWidthInParent("body","detail_div"),
		height:730,
		top:dialogTop,
		left:dialogLeft
	});

	$("#detail_div table").css("width",(setFitWidthInParent("body","detail_div_table"))+"px");
	$("#detail_div table").css("magin","-100px");
	$("#detail_div table td").css("padding-left","30px");
	$("#detail_div table td").css("padding-right","20px");
	$("#detail_div table td").css("font-size","15px");
	$("#detail_div table .td1").css("width","10%");
	$("#detail_div table .td2").css("width","35%");
	$("#detail_div table tr").css("border-bottom","#CAD9EA solid 1px");
	$("#detail_div table tr").css("height","45px");

	$(".panel.window").eq(ddNum).css("margin-top","20px");
	$(".panel.window .panel-title").eq(ddNum).css("color","#000");
	$(".panel.window .panel-title").eq(ddNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(ddNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(ddNum).css("margin-top","20px");
	$(".window,.window .window-body").eq(ddNum).css("border-color","#ddd");

	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
}

function openDetailRsDialog(flag){
	if(flag==1){
		$("#detail_rs_canvas_bg_div").css("display","block");
	}
	else{
		$("#detail_rs_canvas_bg_div").css("display","none");
	}
	openDetailRsSdMDialog(flag);
}

function openDetailRsSdMDialog(flag){
	if(flag==1){
		detailRsSdMDialog.dialog("open");
	}
	else{
		detailRsSdMDialog.dialog("close");
	}
}

function loadSceDisCanvas(flag){
	var bigButDiv=$("#detail_rs_sd_map_dialog_div #big_but");
	var smallButDiv=$("#detail_rs_sd_map_dialog_div #small_but");
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
	case "detail_div":
		space=340;
		break;
	case "detail_rs_sd_map_dialog_div":
		space=170;
		break;
	case "detail_div_table":
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
	<div class="detail_rs_canvas_bg_div" id="detail_rs_canvas_bg_div">
		<div class="detail_rs_canvas_div" id="detail_rs_canvas_div">
			<div class="tjst_div">
				<span class="tjst_span">查看实体</span>
				<span class="close_span" onclick="openDetailRsDialog(0)">X</span>
			</div>
			<div class="detail_rs_canvas_dialog_div" id="detail_rs_canvas_dialog_div">
				<div class="title_div">
					<span class="title_span">道路管理-路段查询-详情</span>
				</div>
				<input type="hidden" id="id"/>
				<div id="detail_rs_sd_map_dialog_div">
					<div id="sceDisCanvas_div">
						<canvas id="sceDisCanvas">
						</canvas>
					</div>
					<div>
						<input type="radio" id="startXY_rad" name="xy_radio"/>开始点
						<input type="radio" id="endXY_rad" name="xy_radio"/>结束点
					</div>
				</div>
			</div>
		</div>
	</div>
	
<%@include file="../../side.jsp"%>
<div class="center_con_div" id="center_con_div">
	<div class="page_location_div">路段详情</div>
	
	<div id="detail_div">
		<form id="form1" name="form1" method="post" action="" enctype="multipart/form-data">
		<input type="hidden" name="id" id="id" value="${requestScope.roadStage.id }" />
		<table>
		  <tr>
			<td class="td1" align="right">
				路段名
			</td>
			<td class="td2">
				<span>${requestScope.roadStage.name }</span>
				<div class="upBut_div showMapBut_div" onclick="openDetailRsDialog(1);">地图打点</div>
			</td>
			<td class="td1" align="right">
				所属道路
			</td>
			<td class="td2">
				<span>${requestScope.roadStage.roadName }</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				后方x轴坐标
			</td>
			<td class="td2">
				<span>${requestScope.roadStage.backX }</span>
			</td>
			<td class="td1" align="right">
				后方y轴坐标
			</td>
			<td class="td2">
				<span>${requestScope.roadStage.backY }</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				前方x轴坐标
			</td>
			<td class="td2">
				<span>${requestScope.roadStage.frontX }</span>
			</td>
			<td class="td1" align="right">
				前方y轴坐标
			</td>
			<td class="td2">
				<span>${requestScope.roadStage.frontY }</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				后方是否相通
			</td>
			<td class="td2">
				<span>
					<c:choose>
						<c:when test="${requestScope.roadStage.backThrough }">是</c:when>
						<c:otherwise>否</c:otherwise>
					</c:choose>
				</span>
			</td>
			<td class="td1" align="right">
				前方是否相通
			</td>
			<td class="td2">
				<span>
					<c:choose>
						<c:when test="${requestScope.roadStage.frontThrough }">是</c:when>
						<c:otherwise>否</c:otherwise>
					</c:choose>
				</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				后方是否是交叉点
			</td>
			<td class="td2">
				<span>
					<c:choose>
						<c:when test="${requestScope.roadStage.backIsCross }">是</c:when>
						<c:otherwise>否</c:otherwise>
					</c:choose>
				</span>
			</td>
			<td class="td1" align="right">
				后方交叉点路段名
			</td>
			<td class="td2">
				<span>${requestScope.roadStage.backCrossRSNames }</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				前方是否是交叉点
			</td>
			<td class="td2">
				<span>
					<c:choose>
						<c:when test="${requestScope.roadStage.frontIsCross }">是</c:when>
						<c:otherwise>否</c:otherwise>
					</c:choose>
				</span>
			</td>
			<td class="td1" align="right">
				前方交叉点路段名
			</td>
			<td class="td2">
				<span>${requestScope.roadStage.frontCrossRSNames }</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				排序
			</td>
			<td class="td2">
				<span>${requestScope.roadStage.sort }</span>
			</td>
			<td class="td1" align="right">
			</td>
			<td class="td2">
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