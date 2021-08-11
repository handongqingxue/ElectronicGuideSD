<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>景点详情</title>
<%@include file="../../js.jsp"%>
<style type="text/css">
.detail_sp_canvas_bg_div{
	width: 100%;
	height: 100%;
	background-color: rgba(0,0,0,.45);
	position: fixed;
	z-index: 9016;
	display:none;
}
.detail_sp_canvas_div{
	width: 1800px;
	height: 850px;
	margin: 50px auto 0;
	background-color: #fff;
	border-radius:5px;
	position: absolute;
	left: 0;
	right: 0;
}
.detail_sp_canvas_div .tjst_div{
	width: 100%;
	height: 50px;
	line-height: 50px;
	border-bottom: #eee solid 1px;
}
.detail_sp_canvas_div .tjst_span{
	margin-left: 30px;
}
.detail_sp_canvas_div .close_span{
	float: right;margin-right: 30px;cursor: pointer;
}
.detail_sp_canvas_dialog_div{
	width: 1775px;
	height: 800px;
	position: absolute;
}
.detail_sp_canvas_div .title_div{
	width: 100%;height: 50px;line-height: 50px;
}
.detail_sp_canvas_div .title_span{
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
var ddNum=0;
var dspsdmdNum=1;
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
var scenicPlaceX;
var scenicPlaceY;
$(function(){
	jiSuanScale();
	initDetailDialog();
	initDetailSpSDMapDialogDiv();

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
}

function resetScenicPlace(x,y){
	var picUrl=$("#picUrl_img").attr("src");
    var picWidth='${requestScope.scenicPlace.picWidth }';
    var picHeight='${requestScope.scenicPlace.picHeight }';
    scenicPlace={x:x,y:y,picUrl:picUrl,picWidth:picWidth,picHeight:picHeight};
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
	var ddpw=$("body").find(".panel.window").eq(ddNum);
	var ddws=$("body").find(".window-shadow").eq(ddNum);
	
	var dspsdmdpw=$("body").find(".panel.window").eq(dspsdmdNum);
	var dspsdmdws=$("body").find(".window-shadow").eq(dspsdmdNum);

	var ccDiv=$("#center_con_div");
	ccDiv.append(ddpw);
	ccDiv.append(ddws);
	ccDiv.css("width",setFitWidthInParent("body","center_con_div")+"px");
	
	var dspsdmdDiv=$("#detail_sp_canvas_dialog_div");
	dspsdmdDiv.append(dspsdmdpw);
	dspsdmdDiv.append(dspsdmdws);
}

function initDetailSpSDMapDialogDiv(){
	detailSpSdMDialog=$("#detail_sp_sd_map_dialog_div").dialog({
		title:"景区地图",
		width:setFitWidthInParent("body","detail_sp_sd_map_dialog_div"),
		height:730,
		top:10,
		left:20,
		buttons:[
           {text:"关闭",id:"cancel_but",iconCls:"icon-cancel",handler:function(){
        	   openDetailSpDialog(0);
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

	$(".panel.window").eq(dspsdmdNum).css("margin-top","40px");
	$(".panel.window .panel-title").eq(dspsdmdNum).css("color","#000");
	$(".panel.window .panel-title").eq(dspsdmdNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(dspsdmdNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").eq(dspsdmdNum).css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(dspsdmdNum).css("margin-top","40px");
	$(".window,.window .window-body").eq(dspsdmdNum).css("border-color","#ddd");

	$("#detail_sp_sd_map_dialog_div #cancel_but").css("left","20%");
	$("#detail_sp_sd_map_dialog_div #cancel_but").css("position","absolute");

	$("#detail_sp_sd_map_dialog_div #reset_but").css("left","40%");
	$("#detail_sp_sd_map_dialog_div #reset_but").css("position","absolute");

	$("#detail_sp_sd_map_dialog_div #big_but").css("left","55%");
	$("#detail_sp_sd_map_dialog_div #big_but").css("position","absolute");

	$("#detail_sp_sd_map_dialog_div #small_but").css("left","80%");
	$("#detail_sp_sd_map_dialog_div #small_but").css("position","absolute");
	
	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
	openDetailSpSdMDialog(0);
	resetScenicPlace('${requestScope.scenicPlace.x }',sceDisCanvasMinHeight-parseInt('${requestScope.scenicPlace.y }'));
}

function initDetailDialog(){
	dialogTop+=20;
	$("#detail_div").dialog({
		title:"景点信息",
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
	$("#detail_div table tr").each(function(i){
		if(i==1)
			$(this).css("height","250px");
		else if(i==4)
			$(this).css("height","200px");
		else if(i==5)
			$(this).css("height","100px");
		else
			$(this).css("height","45px");
	});

	$(".panel.window").eq(ddNum).css("margin-top","20px");
	$(".panel.window .panel-title").eq(ddNum).css("color","#000");
	$(".panel.window .panel-title").eq(ddNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(ddNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(ddNum).css("margin-top","20px");
	$(".window,.window .window-body").eq(ddNum).css("border-color","#ddd");

	$("#detail_div #ok_but").css("left","45%");
	$("#detail_div #ok_but").css("position","absolute");
	
	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
}

function openDetailSpDialog(flag){
	if(flag==1){
		$("#detail_sp_canvas_bg_div").css("display","block");
	}
	else{
		$("#detail_sp_canvas_bg_div").css("display","none");
	}
	openDetailSpSdMDialog(flag);
}

function openDetailSpSdMDialog(flag){
	if(flag==1){
		detailSpSdMDialog.dialog("open");
	}
	else{
		detailSpSdMDialog.dialog("close");
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
	case "detail_div":
		space=340;
		break;
	case "detail_sp_sd_map_dialog_div":
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
	<div class="detail_sp_canvas_bg_div" id="detail_sp_canvas_bg_div">
		<div class="detail_sp_canvas_div" id="detail_sp_canvas_div">
			<div class="tjst_div">
				<span class="tjst_span">查看实体</span>
				<span class="close_span" onclick="openDetailSpDialog(0)">X</span>
			</div>
			<div class="detail_sp_canvas_dialog_div" id="detail_sp_canvas_dialog_div">
				<div class="title_div">
					<span class="title_span">景点管理-景点查询-详情</span>
				</div>
				<input type="hidden" id="id"/>
				<div id="detail_sp_sd_map_dialog_div">
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
	
	<div id="detail_div">
		<input type="hidden" name="id" id="id" value="${requestScope.scenicPlace.id }" />
		<table>
		  <tr>
			<td class="td1" align="right">
				名称
			</td>
			<td class="td2">
				<span>${requestScope.scenicPlace.name }</span>
			</td>
			<td class="td1" align="right">
				排序
			</td>
			<td class="td2">
				<span>${requestScope.scenicPlace.sort }</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				图片
			</td>
			<td class="td2">
				<img class="picUrl_img" id="picUrl_img" alt="" src="${requestScope.scenicPlace.picUrl }"/>
			</td>
			<td class="td1" align="right">
				景区地图
			</td>
			<td class="td2">
				<div class="upBut_div showMapBut_div" onclick="openDetailSpDialog(1);">显示地图</div>
				<img class="sceDis_img" id="sceDis_img" alt="" src="${sessionScope.user.scenicDistrict.mapUrl }"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				图片宽度(px)
			</td>
			<td class="td2">
				<span>${requestScope.scenicPlace.picWidth }</span>
			</td>
			<td class="td1" align="right">
				图片高度(px)
			</td>
			<td class="td2">
				<span>${requestScope.scenicPlace.picHeight }</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				x轴坐标
			</td>
			<td class="td2">
				<span>${requestScope.scenicPlace.x }</span>
			</td>
			<td class="td1" align="right">
				y轴坐标
			</td>
			<td class="td2">
				<span>${requestScope.scenicPlace.y }</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				简单介绍
			</td>
			<td class="td2">
				<span>${requestScope.scenicPlace.simpleIntro }</span>
			</td>
			<td class="td1" align="right">
				详细介绍
			</td>
			<td class="td2">
				<span>${requestScope.scenicPlace.detailIntro }</span>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				简单介绍语音包
			</td>
			<td class="td2">
				<embed class="simpleIntroVoiceUrl_embed" id="simpleIntroVoiceUrl_embed" alt="" src="${requestScope.scenicPlace.simpleIntroVoiceUrl }">
			</td>
			<td class="td1" align="right">
				详细介绍语音包
			</td>
			<td class="td2">
				<img class="detailIntroVoiceUrl_embed" id="detailIntroVoiceUrl_embed" alt="" src="${requestScope.scenicPlace.detailIntroVoiceUrl }"/>
			</td>
		  </tr>
		</table>
	</div>
	<%@include file="../../foot.jsp"%>
	</div>
</div>
</body>
</html>