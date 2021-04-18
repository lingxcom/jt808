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
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传文件</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>js/ajaxfileupload.js"></script>
<script type="text/javascript">
var fromPageId='${param.pageid}';
var cmpId="${param.cmpId}";
$(document).ready(function(){
	var height=$(window.document.body).height()+45;
	var width=$(window.document.body).width()+14;
	Lingx.getRootWindow().resizeWindow({height:$(window.document.body).height()+Lingx.PANEL_HEIGHT});
/* 
	$('#form').form({  
	     
	    success:function(data){  
	       var json=$.parseJSON(data);
	       lgxInfo(json.message);
	       if(json.code==1){
	   			var win=getFromWindow(fromPageId);
	    	   	win.lingxSet({cmpId:cmpId,text:json.path,value:json.path});
	    	   closeWindow();
	       }else{
	       }
	    }  
	});   */
});

function lingxSubmit(){
	 $.ajaxFileUpload({
		url: 'lingx/template/upload/handler.jsp', 
	    type: 'post',
	    secureuri: false, //一般设置为false
	    fileElementId: 'file', // 上传文件的id、name属性名
	    dataType: 'json', //返回值类型，一般设置为json、application/json
	    success: function(json, status){  
	    	lgxInfo(json.message);
		       if(json.code==1){
		   			var win=getFromWindow(fromPageId);
		    	   	win.lingxSet({cmpId:cmpId,text:json.path,value:json.path});
		    	   	$(win.document).find("input[name='file_length']").val(json.length);
		    	   closeWindow();
		       }else{
		       }
	    },
	    error: function(json, status, e){ 
	        alert(e);
	    }
	 });
	//$('#form').submit();
}
//action="lingx/template/upload/handler.jsp"

</script>
<style type="text/css">
*{font-size:12px;}
</style>
</head>
<body style="margin:0px;padding:0px;background-color:#dfe9f6;">
<form id="form"  action="upload" method="post"  ENCTYPE="multipart/form-data">
<div style="background-color:#dfe9f6;">
<div id="id_hidden" style="display:none;width:0px;height:0px;">
</div>
<table  width="100%" style="margin:0px;padding:0px;" border="0" cellpadding="0" cellspacing="0">
<tr style="background-color:#F7F7F7;"><td style="width:110px;"></td><td style="font-size:12px;line-height:24px;" id="vaildMessage" align="left">
</td></tr>
</table>
<div style="height:10px;"></div>
<table id="table" width="100%" style="margin:0px;padding:0px;" border="0" cellpadding="0" cellspacing="0">
<tr>
<td width="10">&nbsp;</td><td>&nbsp;</td><td>&nbsp; </td>
</tr>
<tr>
<td width="10">&nbsp;</td><td align="right">文件：</td><td align="left"><input id="file" style="width:300px;" type="file" name="file"/> </td>
</tr>
<tr>
<td width="10">&nbsp;</td><td>&nbsp;</td><td>&nbsp; </td>
</tr>
</table>
</div>
</form>
</body>

</html>