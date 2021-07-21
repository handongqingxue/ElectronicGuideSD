<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%String basePath=request.getScheme()+"://"+request.getServerName()+":"
	+request.getServerPort()+request.getContextPath()+"/";
%>
<script type="text/javascript"
	src="<%=basePath %>resource/js/jquery-3.3.1.js"></script>
<script type="text/javascript"
	src="<%=basePath %>resource/layui/layui.js"></script>
<link rel="stylesheet"
	href="<%=basePath %>/resource/layui/css/layui.css">
	
<link href="<%=basePath %>resource/jquery-easyui-1.3.6/themes/icon.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath %>resource/jquery-easyui-1.3.6/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
	
<script type="text/javascript"
	src="<%=basePath %>resource/jquery-easyui-1.3.6/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=basePath %>resource/jquery-easyui-1.3.6/jquery.namespace.js"></script>
<script type="text/javascript"
	src="<%=basePath %>resource/jquery-easyui-1.3.6/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=basePath %>resource/jquery-easyui-1.3.6/locale/easyui-lang-zh_CN.js"></script>
