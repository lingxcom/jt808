<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String code=request.getParameter("code");
	ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
	IFormService formservice=applicationContext.getBean(IFormService.class);
	IContextService contextService=applicationContext.getBean(IContextService.class);
	String content=jdbc.queryForObject("select content from tlingx_wf_define_form where code=?", String.class,code);
	
	content=formservice.formWorkflowRendering(content, null, contextService.getContext(request));
	request.setAttribute("content",content);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>表单</title>
<link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="js/jquery.js"  charset="UTF-8"></script>
<script type="text/javascript" src="lingx/js/lingx.js"></script>

<script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/bootstrap/plugins/datetimepicker/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script type="text/javascript" src="lingx/workflow/form/form-input.js"  charset="UTF-8"></script>


<script type="text/javascript">
$(function(){
	 $('.form_datetime').datetimepicker({
		 	language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
	        showMeridian: 1
	    });
		$('.form_date').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
	    });
		$('.form_time').datetimepicker({
			language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 1,
			minView: 0,
			maxView: 1,
			forceParse: 0
	    });
});
</script>
</head>
<body style="background-color:#FFF;">
<form id="form" action="w" method="post">
<div style="dispaly:none;">
</div>
<div width="100%" style="padding:auto;">
<br>
<div  class='container' style="width:990px">
${content }
</div>
</div>
</form>
</body>

</html>