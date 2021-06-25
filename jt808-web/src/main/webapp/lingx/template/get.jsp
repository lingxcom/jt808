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
<title>实体列表</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<style type="text/css">
.edit_word{font-size:12px;line-height:24px;}
</style>
<script type="text/javascript">
var methodFields=${fields};
var vaildMessage='';
vaildMessage=${vaildMessage};
var servletRet=${LingxJsonData};
$(function(){
	var table=$("#table");
	$.each(methodFields,function(index,obj){
		//table.append("<tr><td>s").append(obj.name).append("s</td><td>aa</td></tr>");
		var val='';
		if(obj.value)val=obj.value;
		if('hidden'==obj.inputType){
			$("#id_hidden").append("<input name='"+obj.code+"' value='"+val+"' type='hidden'/>");
		}else{
			var input="<input name='"+obj.code+"' value='"+val+"' maxlength='"+(obj.length/2)+"' style='width:"+width+"px;' />";
			input=getInput(obj);
			table.append("<tr  class='edit_word' ><td class='edit_word' style='width:10px;'>&nbsp;</td><td  class='edit_word' >"+obj.name+"：</td><td>"+input+"</td></tr>");
			$.parser.parse();
		}
	});
	$.each(vaildMessage,function(index,obj){$("#vaildMessage").append("<li>"+obj+"</li>");});
	var height=$(window.document.body).height()+40;
	var width=$(window.document.body).width()+14;
	window.parent.resizeWin(width,height);
	
	if(servletRet&&servletRet.exeRet){
		closeWin();
		reloadGrid();
	}
});

function closeWin(){
	window.parent.closeWin();//window.parent.document
}
function reloadGrid(){
	window.parent.reloadGrid();
}
</script>
</head>
<body style="margin:0px;padding:0px;background-color:#f0f0f0;">
<div id='loading-mask'></div>
<div id='loading'>
<div class='loading'></div>
</div>
<form action="<%=basePath %>e" method="post">
<div id="id_hidden" style="display:none;width:0px;height:0px;">
<input name="lgxsn" value="1" type="hidden"/>
<input name="EntityCode" value="${entity }" type="hidden"/>
<input name="MethodCode" value="${method }" type="hidden"/>
</div>
<table  width="100%" style="margin:0px;padding:0px;" border="0" cellpadding="0" cellspacing="0">
<tr style="background-color:#ffffff;"><td style="width:25px;"></td><td  class='edit_word'  id="vaildMessage" align="left">
</td></tr>
</table>
<table id="table" width="100%" style="margin:0px;padding:0px;" border="0" cellpadding="0" cellspacing="0">

</table>
<hr/>
<table id="table2" width="100%" style="margin:0px;padding:0px;"  border="0" cellpadding="0" cellspacing="0">
<tr><td align="right">
<input type="submit" value="确定" />
<input type="button" value="取消"  onclick="closeWin();"/>&nbsp;&nbsp;
 </td></tr>
</table>
</form>
</body>

</html>