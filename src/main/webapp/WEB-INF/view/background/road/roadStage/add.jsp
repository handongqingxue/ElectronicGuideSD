<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加路段</title>
<%@include file="../../js.jsp"%>
<style type="text/css">
.add_rs_canvas_bg_div{
	width: 100%;
	height: 100%;
	background-color: rgba(0,0,0,.45);
	position: fixed;
	z-index: 9016;
	display:none;
}
.add_rs_canvas_div{
	width: 1800px;
	height: 850px;
	margin: 50px auto 0;
	background-color: #fff;
	border-radius:5px;
	position: absolute;
	left: 0;
	right: 0;
}
.add_rs_canvas_div .tjst_div{
	width: 100%;
	height: 50px;
	line-height: 50px;
	border-bottom: #eee solid 1px;
}
.add_rs_canvas_div .tjst_span{
	margin-left: 30px;
}
.add_rs_canvas_div .close_span{
	float: right;margin-right: 30px;cursor: pointer;
}
.add_rs_canvas_dialog_div{
	width: 1775px;
	height: 800px;
	position: absolute;
}
.add_rs_canvas_div .title_div{
	width: 100%;height: 50px;line-height: 50px;
}
.add_rs_canvas_div .title_span{
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
.name_inp,.backX_inp,.backY_inp,.frontX_inp,.frontY_inp{
	width: 150px;
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
.showMapBut_div{
	width: 90px;
	margin-top: -33px;
	margin-left: 173px;
}
</style>
<script type="text/javascript">
var path='<%=basePath %>';
var roadPath='<%=basePath%>'+"background/road/";
var dialogTop=10;
var dialogLeft=20;
var ndNum=0;
var arssdmdNum=1;
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
var roadStage={backX:-1,backY:-1,frontX:-1,frontY:-1};;
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
$(function(){
	jiSuanScale();
	initScenicPlaceJA();
	initOtherRSJA();
	initRoadCBB();
	initBackThroughCBB();
	initFrontThroughCBB();
	initBackIsCrossCBB();
	initBackCrossRSIdsCBB();
	initFrontIsCrossCBB();
	initFrontCrossRSIdsCBB();
	initNewDialog();
	initAddRsSDMapDialogDiv();

	initDialogPosition();//将不同窗体移动到主要内容区域
	
	initSceDisCanvas();
});

function initRoadCBB(){
	$.post(roadPath+"selectRoadCBBData",
		function(result){
			var data=[];
			data.push({id:"",name:"请选择"});
			if(result.status=="ok"){
				var roadList=result.roadList;
				for(var i=0;i<roadList.length;i++){
					var road=roadList[i];
					data.push({id:road.id,name:road.name});
				}
			}
			roadCBB=$("#road_cbb").combobox({
				width:150,
				data:data,
				valueField:"id",
				textField:"name",
				onSelect:function(){
					$("#roadId").val(roadCBB.combobox("getValue"));
				}
			});
		}
	,"json");
}

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

function initBackIsCrossCBB(){
	var data=[];
	data.push({id:"",name:"请选择"},{id:"true",name:"是"},{id:"false",name:"否"});
	backIsCrossCBB=$("#backIsCross_cbb").combobox({
		width:150,
		data:data,
		valueField:"id",
		textField:"name",
		onSelect:function(){
			$("#backIsCross").val(backIsCrossCBB.combobox("getValue"));
		}
	});
}

function initBackCrossRSIdsCBB(){
	var data=[];
	data.push({id:"",name:"请选择"});
	$.post(roadPath+"selectRoadStageCBBData",
		function(result){
			if(result.status=="ok"){
				var roadStageList=result.roadStageList;
				for(var i=0;i<roadStageList.length;i++){
					var roadStage=roadStageList[i];
					data.push({id:roadStage.id,name:roadStage.name});
				}
				backCrossRSIdsCBB=$("#backCrossRSIds_cbb").combobox({
					width:150,
					data:data,
	                multiple:true,
					valueField:"id",
					textField:"name",
					onSelect:function(){
						var backCrossRSIds=backCrossRSIdsCBB.combobox("getValues").toString();
						var backCrossRSIdArr=backCrossRSIds.split(",");
						var bcrsIds="";
						for (var i = 0; i < backCrossRSIdArr.length; i++) {
							var backCrossRSId=backCrossRSIdArr[i];
							if(backCrossRSId=="")
								continue;
							bcrsIds+=","+backCrossRSId;
						}
						$("#backCrossRSIds").val(bcrsIds.substring(1));
					}
				});
			}
		}
	,"json");
}

function initFrontIsCrossCBB(){
	var data=[];
	data.push({id:"",name:"请选择"},{id:"true",name:"是"},{id:"false",name:"否"});
	frontIsCrossCBB=$("#frontIsCross_cbb").combobox({
		width:150,
		data:data,
		valueField:"id",
		textField:"name",
		onSelect:function(){
			$("#frontIsCross").val(frontIsCrossCBB.combobox("getValue"));
		}
	});
}

function initFrontCrossRSIdsCBB(){
	var data=[];
	data.push({id:"",name:"请选择"});
	$.post(roadPath+"selectRoadStageCBBData",
		function(result){
			if(result.status=="ok"){
				var roadStageList=result.roadStageList;
				for(var i=0;i<roadStageList.length;i++){
					var roadStage=roadStageList[i];
					data.push({id:roadStage.id,name:roadStage.name});
				}
				frontCrossRSIdsCBB=$("#frontCrossRSIds_cbb").combobox({
					width:150,
					data:data,
	                multiple:true,
					valueField:"id",
					textField:"name",
					onSelect:function(){
						var frontCrossRSIds=frontCrossRSIdsCBB.combobox("getValues").toString();
						var frontCrossRSIdArr=frontCrossRSIds.split(",");
						var fcrsIds="";
						for (var i = 0; i < frontCrossRSIdArr.length; i++) {
							var frontCrossRSId=frontCrossRSIdArr[i];
							if(frontCrossRSId=="")
								continue;
							fcrsIds+=","+frontCrossRSId;
						}
						$("#frontCrossRSIds").val(fcrsIds.substring(1));
					}
				});
			}
		}
	,"json");
}

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
	           
	           //alert($("#startXY_rad").prop("checked"));
	           if(checkXYRadio()){
	        	   var src=$("#startXY_rad").prop("checked");
	        	   if(src)
	        	   	  startPoint={x:x,y:y};
	        	   var erc=$("#endXY_rad").prop("checked");
	        	   if(erc)
	        	   	  endPoint={x:x,y:y};
	        	   
	        	   if(startPoint!=null&endPoint==null){
	        		   backX=startPoint.x;
        			   backY=sceDisCanvasMinHeight-startPoint.y;
    			       roadStage={backX:backX,backY:startPoint.y,frontX:roadStage.frontX,frontY:roadStage.frontY};
	        	   }
	        	   else if(startPoint==null&endPoint!=null){
        			   frontX=endPoint.x;
        			   frontY=sceDisCanvasMinHeight-endPoint.y;
    			       roadStage={backX:roadStage.backX,backY:roadStage.backY,frontX:frontX,frontY:endPoint.y};
	        	   }
	        	   else if(startPoint!=null&endPoint!=null){
	        		   if(startPoint.x<endPoint.x||startPoint.y<endPoint.y){
	        			   backX=startPoint.x;
	        			   backY=sceDisCanvasMinHeight-startPoint.y;
	        			   frontX=endPoint.x;
	        			   frontY=sceDisCanvasMinHeight-endPoint.y;
	    			       roadStage={backX:backX,backY:startPoint.y,frontX:frontX,frontY:endPoint.y};
	        		   }
	        		   else{
	        			   backX=endPoint.x;
	        			   backY=sceDisCanvasMinHeight-endPoint.y;
	        			   frontX=startPoint.x;
	        			   frontY=sceDisCanvasMinHeight-startPoint.y;
	    			       roadStage={backX:backX,backY:endPoint.y,frontX:frontX,frontY:startPoint.y};
	        		   }
	        	   }
		           //backX=x;
		           //backY=sceDisCanvasMinHeight-y;//将y轴坐标从最初的左上角计算转换为从左下角计算
		           
		           initSceDisCanvas(0);
	           }
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
}

function initRoadStageLocation(){
	/*
	var entityImg = new Image();
	entityImg.src=scenicPlace.picUrl;
	entityImg.onload=function(){
		//不管画布怎么放大、缩小，生成坐标的点位置还是原来的。只是上面鼠标点击后获取的坐标是从坐上为原点计算的，这里画图也是和上面一样的原理，从左上为原点计算位置。只是插入数据库的位置是转换后以左下为原点计算的
		sceDisCanvasContext.drawImage(entityImg, scenicPlace.x/widthScale-scenicPlace.picWidth/2, scenicPlace.y/heightScale-scenicPlace.picHeight/2, scenicPlace.picWidth, scenicPlace.picHeight);
	}
	*/

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
		sceDisCanvasContext.arc(roadStage.backX/widthScale,roadStage.backY/heightScale,arcR/5,0,2*Math.PI);
		sceDisCanvasContext.stroke();
	}
	if(roadStage.frontX!=-1&roadStage.frontY!=-1){
		sceDisCanvasContext.beginPath();
		sceDisCanvasContext.strokeStyle = 'red';//点填充
		sceDisCanvasContext.fillStyle='red';
		sceDisCanvasContext.lineWidth=arcR*1.5;
		sceDisCanvasContext.arc(roadStage.frontX/widthScale,roadStage.frontY/heightScale,arcR/5,0,2*Math.PI);
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
		var backXY="("+otherRSJO.backX+","+otherRSJO.backY+")";
		var backRectWidth=20*backXY.length+20;
		var frontXY="("+otherRSJO.frontX+","+otherRSJO.frontY+")";
		var frontRectWidth=20*frontXY.length+20;
		sceDisCanvasContext.beginPath();
		sceDisCanvasContext.font="25px bold 黑体";
		sceDisCanvasContext.fillStyle = "#f00";
		sceDisCanvasContext.fillText(backXY,otherRSJO.backX/widthScale-backRectWidth/2+fontMarginLeft,otherRSJO.backY/heightScale-atSpace);
		sceDisCanvasContext.fillText(frontXY,otherRSJO.frontX/widthScale-backRectWidth/2+fontMarginLeft,otherRSJO.frontY/heightScale-atSpace);
		sceDisCanvasContext.stroke();
	}
}

function initDialogPosition(){
	//基本属性组
	var ndpw=$("body").find(".panel.window").eq(ndNum);
	var ndws=$("body").find(".window-shadow").eq(ndNum);
	
	var arssdmdpw=$("body").find(".panel.window").eq(arssdmdNum);
	var arssdmdws=$("body").find(".window-shadow").eq(arssdmdNum);

	var ccDiv=$("#center_con_div");
	ccDiv.append(ndpw);
	ccDiv.append(ndws);
	ccDiv.css("width",setFitWidthInParent("body","center_con_div")+"px");
	
	var arssdmdDiv=$("#add_rs_canvas_dialog_div");
	arssdmdDiv.append(arssdmdpw);
	arssdmdDiv.append(arssdmdws);
}

function putXYFromInpToVar(){
	var backX=$("#backX").val();
	if(backX!=""&backX!=null)
		roadStage.backX=backX;
	var backY=$("#backY").val();
	if(backY!=""&backY!=null)
		roadStage.backY=sceDisCanvasMinHeight-backY;
	var frontX=$("#frontX").val();
	if(frontX!=""&frontX!=null)
		roadStage.frontX=frontX;
	var frontY=$("#frontY").val();
	if(frontY!=""&frontY!=null)
		roadStage.frontY=sceDisCanvasMinHeight-frontY;
    initSceDisCanvas(0);
	openAddRsDialog(1);
}

function initAddRsSDMapDialogDiv(){
	addRsSdMDialog=$("#add_rs_sd_map_dialog_div").dialog({
		title:"景区地图",
		width:setFitWidthInParent("body","add_rs_sd_map_dialog_div"),
		height:730,
		top:10,
		left:20,
		buttons:[
           {text:"取消",id:"cancel_but",iconCls:"icon-cancel",handler:function(){
        	   openAddRsDialog(0);
           }},
           {text:"确定",id:"ok_but",iconCls:"icon-ok",handler:function(){
        	   var backX=roadStage.backX;
        	   var backY=sceDisCanvasMinHeight-roadStage.backY;
        	   var frontX=roadStage.frontX;
        	   var frontY=sceDisCanvasMinHeight-roadStage.frontY;
        	   if(backX<frontX||backY<frontY){
	        	   $("#backX").val(backX);
	        	   $("#backY").val(backY);
	        	   $("#frontX").val(frontX);
	        	   $("#frontY").val(frontY);
        	   }
        	   else{
        		   $("#backX").val(frontX);
	        	   $("#backY").val(frontY);
	        	   $("#frontX").val(backX);
	        	   $("#frontY").val(backY);
        	   }
        	   openAddRsDialog(0);
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

	$(".panel.window").eq(arssdmdNum).css("margin-top","40px");
	$(".panel.window .panel-title").eq(arssdmdNum).css("color","#000");
	$(".panel.window .panel-title").eq(arssdmdNum).css("font-size","15px");
	$(".panel.window .panel-title").eq(arssdmdNum).css("padding-left","10px");
	
	$(".panel-header, .panel-body").eq(arssdmdNum).css("border-color","#ddd");
	
	//以下的是表格下面的面板
	$(".window-shadow").eq(arssdmdNum).css("margin-top","40px");
	$(".window,.window .window-body").eq(arssdmdNum).css("border-color","#ddd");

	$("#add_rs_sd_map_dialog_div #cancel_but").css("left","20%");
	$("#add_rs_sd_map_dialog_div #cancel_but").css("position","absolute");

	$("#add_rs_sd_map_dialog_div #ok_but").css("left","35%");
	$("#add_rs_sd_map_dialog_div #ok_but").css("position","absolute");

	$("#add_rs_sd_map_dialog_div #reset_but").css("left","50%");
	$("#add_rs_sd_map_dialog_div #reset_but").css("position","absolute");

	$("#add_rs_sd_map_dialog_div #big_but").css("left","65%");
	$("#add_rs_sd_map_dialog_div #big_but").css("position","absolute");

	$("#add_rs_sd_map_dialog_div #small_but").css("left","80%");
	$("#add_rs_sd_map_dialog_div #small_but").css("position","absolute");
	
	$(".dialog-button").css("background-color","#fff");
	$(".dialog-button .l-btn-text").css("font-size","20px");
	openAddRsSdMDialog(0);
}

function initNewDialog(){
	dialogTop+=20;
	$("#new_div").dialog({
		title:"路段信息",
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

function openAddRsDialog(flag){
	if(flag==1){
		$("#add_rs_canvas_bg_div").css("display","block");
	}
	else{
		$("#add_rs_canvas_bg_div").css("display","none");
	}
	openAddRsSdMDialog(flag);
}

function openAddRsSdMDialog(flag){
	if(flag==1){
		addRsSdMDialog.dialog("open");
	}
	else{
		addRsSdMDialog.dialog("close");
	}
}

function checkAdd(){
	if(checkName()){
		if(checkRoadId()){
			if(checkBackX()){
				if(checkBackY()){
					if(checkFrontX()){
						if(checkFrontY()){
							if(checkBackThrough()){
								if(checkFrontThrough()){
									if(checkBackIsCross()){
										if(checkBackCrossRSIds()){
											if(checkFrontIsCross()){
												if(checkFrontCrossRSIds()){
													if(checkSort()){
														addRoadStage();
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}

function checkXYRadio(){
	var src=$("#startXY_rad").prop("checked");
	var erc=$("#endXY_rad").prop("checked");
	if(src||erc)
		return true;
	else{
		alert("请选择开始点或结束点");
		return false;
	}
}

function addRoadStage(){
	var formData = new FormData($("#form1")[0]);
	$.ajax({
		type:"post",
		url:roadPath+"addRoadStage",
		dataType: "json",
		data:formData,
		cache: false,
		processData: false,
		contentType: false,
		success: function (data){
			if(data.status==1){
				alert(data.msg);
				location.href=roadPath+"roadStage/list";
			}
			else{
				alert(data.msg);
			}
		}
	});
}

function focusName(){
	var name = $("#name").val();
	if(name=="路段名不能为空"){
		$("#name").val("");
		$("#name").css("color", "#555555");
	}
}

//验证路段名
function checkName(){
	var name = $("#name").val();
	if(name==null||name==""||name=="路段名不能为空"){
		$("#name").css("color","#E15748");
    	$("#name").val("路段名不能为空");
    	return false;
	}
	else
		return true;
}

//验证选择路名
function checkRoadId(){
	var roadId=roadCBB.combobox("getValue");
	if(roadId==null||roadId==""){
    	alert("请选择路名");
    	return false;
	}
	else
		return true;
}

//验证后方x轴坐标
function checkBackX(){
	var backX = $("#backX").val();
	if(backX==null||backX==""){
	  	alert("请输入后方x轴坐标");
	  	return false;
	}
	else
		return true;
}

//验证后方y轴坐标
function checkBackY(){
	var backY = $("#backY").val();
	if(backY==null||backY==""){
	  	alert("请输入前方y轴坐标");
	  	return false;
	}
	else
		return true;
}

//验证前方x轴坐标
function checkFrontX(){
	var frontX = $("#frontX").val();
	if(frontX==null||frontX==""){
	  	alert("请输入前方x轴坐标");
	  	return false;
	}
	else
		return true;
}

//验证前方y轴坐标
function checkFrontY(){
	var frontY = $("#frontY").val();
	if(frontY==null||frontY==""){
	  	alert("请输入前方y轴坐标");
	  	return false;
	}
	else
		return true;
}

//验证后方是否相通
function checkBackThrough(){
	var backThrough=backThroughCBB.combobox("getValue");
	if(backThrough==null||backThrough==""){
	  	alert("请选择后方是否相通");
	  	return false;
	}
	else
		return true;
}

//验证前方是否相通
function checkFrontThrough(){
	var frontThrough=frontThroughCBB.combobox("getValue");
	if(frontThrough==null||frontThrough==""){
	  	alert("请选择前方是否相通");
	  	return false;
	}
	else
		return true;
}

//验证后方是否是交叉点
function checkBackIsCross(){
	var backIsCross=backIsCrossCBB.combobox("getValue");
	if(backIsCross==null||backIsCross==""){
	  	alert("请选择后方是否是交叉点");
	  	return false;
	}
	else
		return true;
}

//验证后方交叉点路段名
function checkBackCrossRSIds(){
	var backIsCross=backIsCrossCBB.combobox("getValue");
	if(!convertStringToBoolean(backIsCross))
		return true;
	var backCrossRSIds=backCrossRSIdsCBB.combobox("getValue");
	if(backCrossRSIds==null||backCrossRSIds==""){
	  	alert("请选择后方交叉点路段名");
	  	return false;
	}
	else
		return true;
}

//验证前方是否是交叉点
function checkFrontIsCross(){
	var frontIsCross=frontIsCrossCBB.combobox("getValue");
	if(frontIsCross==null||frontIsCross==""){
	  	alert("请选择前方是否是交叉点");
	  	return false;
	}
	else
		return true;
}

//验证前方交叉点路段名
function checkFrontCrossRSIds(){
	var frontIsCross=frontIsCrossCBB.combobox("getValue");
	if(!convertStringToBoolean(frontIsCross))
		return true;
	var frontCrossRSIds=frontCrossRSIdsCBB.combobox("getValue");
	if(frontCrossRSIds==null||frontCrossRSIds==""){
	  	alert("请选择前方交叉点路段名");
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

function convertStringToBoolean(str){
	if(str=="true")
		return true;
	else if(str=="false")
		return false;
}

function loadSceDisCanvas(flag){
	var bigButDiv=$("#add_rs_sd_map_dialog_div #big_but");
	var smallButDiv=$("#add_rs_sd_map_dialog_div #small_but");
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
	case "add_rs_sd_map_dialog_div":
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
	<div class="add_rs_canvas_bg_div" id="add_rs_canvas_bg_div">
		<div class="add_rs_canvas_div" id="add_rs_canvas_div">
			<div class="tjst_div">
				<span class="tjst_span">添加实体</span>
				<span class="close_span" onclick="openAddRsDialog(0)">X</span>
			</div>
			<div class="add_rs_canvas_dialog_div" id="add_rs_canvas_dialog_div">
				<div class="title_div">
					<span class="title_span">道路管理-路段查询-添加</span>
				</div>
				<input type="hidden" id="id"/>
				<div id="add_rs_sd_map_dialog_div">
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
	<div class="page_location_div">添加路段</div>
	
	<div id="new_div">
		<form id="form1" name="form1" method="post" action="" enctype="multipart/form-data">
		<table>
		  <tr>
			<td class="td1" align="right">
				路段名
			</td>
			<td class="td2">
				<input type="text" class="name_inp" id="name" name="name" placeholder="请输入路段名" onfocus="focusName()" onblur="checkName()"/>
				<div class="upBut_div showMapBut_div" onclick="putXYFromInpToVar();">地图打点</div>
			</td>
			<td class="td1" align="right">
				所属道路
			</td>
			<td class="td2">
				<select id="road_cbb"></select>
				<input type="hidden" id="roadId" name="roadId"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				后方x轴坐标
			</td>
			<td class="td2">
				<input type="number" class="backX_inp" id="backX" name="backX" placeholder="请输入后方x轴坐标"/>
			</td>
			<td class="td1" align="right">
				后方y轴坐标
			</td>
			<td class="td2">
				<input type="number" class="backY_inp" id="backY" name="backY" placeholder="请输入后方y轴坐标"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				前方x轴坐标
			</td>
			<td class="td2">
				<input type="number" class="frontX_inp" id="frontX" name="frontX" placeholder="请输入前方x轴坐标"/>
			</td>
			<td class="td1" align="right">
				前方y轴坐标
			</td>
			<td class="td2">
				<input type="number" class="frontY_inp" id="frontY" name="frontY" placeholder="请输入前方y轴坐标"/>
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
		  <tr>
			<td class="td1" align="right">
				后方是否是交叉点
			</td>
			<td class="td2">
				<select id="backIsCross_cbb"></select>
				<input type="hidden" id="backIsCross" name="backIsCross"/>
			</td>
			<td class="td1" align="right">
				后方交叉点路段名
			</td>
			<td class="td2">
				<select id="backCrossRSIds_cbb"></select>
				<input type="hidden" id="backCrossRSIds" name="backCrossRSIds"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				前方是否是交叉点
			</td>
			<td class="td2">
				<select id="frontIsCross_cbb"></select>
				<input type="hidden" id="frontIsCross" name="frontIsCross"/>
			</td>
			<td class="td1" align="right">
				前方交叉点路段名
			</td>
			<td class="td2">
				<select id="frontCrossRSIds_cbb"></select>
				<input type="hidden" id="frontCrossRSIds" name="frontCrossRSIds"/>
			</td>
		  </tr>
		  <tr>
			<td class="td1" align="right">
				排序
			</td>
			<td class="td2">
				<input type="number" class="sort_inp" id="sort" name="sort" placeholder="请输入排序"/>
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