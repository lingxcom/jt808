<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
String ts=lingx.getConfigValue("lingx.update.version.ts", "20121221152030");
request.setAttribute("ts", ts);
if(!lingx.isSuperman(request))return;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更新列表</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript" src="lingx/model/update/pager.js"></script>
<SCRIPT type="text/javascript">
var downloadUrl="<%="http://mdd.lingx.com/bi/action/notice.jsp" %>"+"?callback=?";

var pager=new Pager({id:"footer",rows:20,callback:reloadGrid});

var model=avalon.define({
	$id:"body",
	list:[],
	view:function(id,title){
		openWindow2(title,"lingx/common/notice_view.jsp?id="+id);
	}
});
$(function(){
	reloadGrid();
});

function reloadGrid(p,r){
	p=p||1;
	r=r||20;
	$.getJSON(downloadUrl+"&page="+p+"&rows="+r+"&appid=${SESSION_USER.app.id}&ts=${ts}",function(json){
		model.list=json.rows;
		pager.getModel().reset({total:json.total});

	});
}
function ft(time){
	return time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12);
}
</SCRIPT>
</head>
<body ms-controller="body">
<div class="container-fluid" style="margin-top:10px;">
<div class="row">
<div class="col-md-6">
说明：平台的重要通知，如：BUG修复，新功能上线。

</div>
<div class="col-md-6" style="text-align:right;"><!-- 
<a class="btn btn-success" onclick="uploadPackage();" href="javascript:;" role="button">本地上传</a> -->
</div>
</div>
<div class="row">
<div class="col-md-12">
<table class="table table-striped table-hover" style="margin-top:10px;">
<tr>
<th>编号</th>
<th>标题</th>
<!-- <th>更新内容</th> -->
<th>发布时间</th>
<th>操作</th>
</tr>
<tr ms-repeat="list">
<td width="50">{{el.id}}</td>
<td><a href="javascript:;" ms-click="view(el.id,el.title)">{{el.title}}</a> </td>
<!-- <td>{{el.content}}</td> -->
<td width="160">{{ft(el.ts)}}</td>

<td width="100">
<a href="javascript:;" ms-click="view(el.id,el.title)">查看</a>   <!-- |
<a target="_blank" ms-attr-href="el.path">下载</a>  -->

</td>
</tr>
</table>
</div>
</div>
  <div class="row">
  <div class="col-md-12">
   		<div class="m-page-footer" ms-controller="footer">
            <table width="100%">
                <tr>
                <td>
                    <div class="pages_info pull-left">显示 {{(currentPage-1)*rows+1}} 到 {{currentPage*rows>totalRecord?totalRecord:currentPage*rows}} 项，共 {{totalRecord}} 项 </div>
                </td>
                <td style="text-align:right;">
                    <div class="dataTables_paginate paging_simple_numbers pages_num">
                        <ul class="pagination" style="margin:0;">
                            <li class="paginate_button previous" ms-class="disabled:currentPage<=1" aria-controls="editable" tabindex="0" id="editable_previous"><a href="javascript:;" ms-click="prev">上一页</a></li>
                            <li class="paginate_button " aria-controls="editable" tabindex="0" ms-repeat="list" ms-class="active:el==currentPage"><a href="javascript:;" ms-click="jump(el)">{{el}}</a></li>
                            <li class="paginate_button next" ms-class="disabled:currentPage>=totalPage" aria-controls="editable" tabindex="0" id="editable_next"><a href="javascript:;" ms-click="next">下一页</a></li>
                        </ul>
                    </div>
                </td>
                </tr>
            </table>
        </div>
  </div>
  </div>

</div>
</body>

</html>