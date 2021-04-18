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
<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/uploadify/jquery.uploadify.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>js/uploadify/uploadify.css">
<link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
<style type="text/css">
.uploadify:hover .uploadify-button {
background-color:blue;
}
</style>
<script type="text/javascript">
var fromPageId='${param.pageid}';
var cmpId="${param.cmpId}";



$(function() {
    $("#file_upload").uploadify({
    	buttonText:"选择上传文件",
    	fileObjName:'file',
    	auto:true,
    	multi:false,
    	uploadLimit  : 1,
    	buttonClass     : "btn btn-primary",
        swf           : '<%=basePath %>js/uploadify/uploadify.swf',
        uploader      : '<%=basePath %>lingx/template/upload/handler.jsp', 
        width         : 120,
        sizeLimit: '99999999999',
        onSelect:function(file){
        	//lgxInfo("点“确定”上传；更换文件前须删除之前文件");
        },
        onUploadSuccess : function(file, data, response) {
        	esuccess($.parseJSON(data));
        }
    });

	Lingx.getRootWindow().resizeWindow({height:$(window.document.body).height()+Lingx.PANEL_HEIGHT+100});
});

function esuccess(json){
	lgxInfo(json.message);
    if(json.code==1){
			var win=getFromWindow(fromPageId);
 	   	win.lingxSet({cmpId:cmpId,text:json.path,value:json.path});
 	   	$(win.document).find("input[name='file_length']").val(json.length);
 	   closeWindow();
    }else{
    }

}

function lingxSubmit(){
	$('#file_upload').uploadify('upload','*');
}
</script>
<style type="text/css">
*{font-size:12px;}
</style>
</head>
<body style="margin:0px;padding:5px;background-color:#dfe9f6;">
<div style="height:30px;line-height:30px">说明：选择文件后，点“确定”上传；更换文件前须删除之前文件</div>
		<div id="queue"></div>
		<input id="file_upload" name="file" type="file" multiple="false">
</body>

</html>