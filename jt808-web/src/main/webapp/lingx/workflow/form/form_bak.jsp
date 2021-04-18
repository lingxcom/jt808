<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>对象编辑工具</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="lingx/workflow/form/form.js"></script>

    <script type="text/javascript" charset="utf-8" src="<%=basePath%>ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=basePath%>ueditor/ueditor.all.min.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="<%=basePath%>ueditor/lang/zh-cn/zh-cn.js"></script>
<style type="text/css">
.redBorder{
color:green;
border:1px solid red;
}
.lingx-wf{
color:red;
-webkit-user-select:none;
-moz-user-select:none;
-ms-user-select:none;
user-select:none;
}
</style>

<script type="text/javascript">


var fromPageId='${param.pageid}';
var handlerJsp="<%=basePath%>lingx/workflow/form/handler.jsp";
var formId=0;
var inputId='';
function reloadGrid(){
	Ext.getCmp("entityGrid_1").getStore().reload();
	//WF.reload();
}
var ue=null;
$(function(){
	
});

function initUEditor(){
	ue = UE.getEditor('editor');
	ue.addListener("ready", function () {
		Lingx.post(handlerJsp,{c:"getForm",id:formId},function(json){
			ue.setContent(json.content);
			$("#ueditor_0").contents().find(".lingx-wf").bind("click",edit);
		});
	});
}

function edit(){
	var _this=$(this);
	inputId=_this.prop("id");
	$("#ueditor_0").contents().find(".lingx-wf").css("border","1px solid #a9a9a9");
	_this.css("border","1px solid red");
	Ext.getCmp('propertyGrid').setSource({
		lname:_this.attr("lname"),
		lnotnull:_this.attr("lnotnull")=='true',
		lmaxlength:_this.attr("lmaxlength")||100,
		lparam:_this.attr("lparam")||"",
		leditor:_this.attr("leditor")||"text",
		lrefEntity:_this.attr("lrefEntity")||"",
		ldefaultValue:_this.attr("ldefalutValue")||"",
		lwidth:_this.attr("lwidth")||0,
		lheight:_this.attr("lheight")||0
		});
}
</script>
</head>
<body>
</body>
</html> 