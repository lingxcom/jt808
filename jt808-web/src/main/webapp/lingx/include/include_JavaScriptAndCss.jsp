
<link rel="stylesheet" type="text/css" href="js/ext-theme-classic/ext-theme-classic-all.css">
<link rel="stylesheet" type="text/css" href="js/ext-theme-classic/icon.css">
<link rel="stylesheet" type="text/css" href="js/lingx-ext-icon.css">
<script type="text/javascript" src="js/ext-all.gzjs"></script>
<!-- //cdn.bootcss.com/extjs/4.2.1/ext-all.min.js -->
<%
Object lanuageJS=session.getAttribute("SESSION_LANGUAGE");
if(lanuageJS==null){
	%>
<script type="text/javascript" src="js/ext-lang-zh_CN.js"></script>
	<%
}else{
	%>
<script type="text/javascript" src="js/ext-lang-<%=lanuageJS %>.js"></script>
	<%
}
%>

<script type="text/javascript" src="js/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="lingx/js/resources/css/lingx.css">
<script type="text/javascript" src="lingx/js/lingx.js"></script>
<script type="text/javascript" src="lingx/js/commonApi.js"></script>
<script type="text/javascript" src="lingx/js/lingx-ext.js"></script>