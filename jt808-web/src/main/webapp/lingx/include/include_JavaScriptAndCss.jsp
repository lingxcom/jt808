
<link rel="stylesheet" type="text/css" href="js/ext-theme-triton/theme-triton-all.css">
<link rel="stylesheet" type="text/css" href="js/ext-theme-classic/icon.css">
<link rel="stylesheet" type="text/css" href="js/lingx-ext-icon.css">
<script type="text/javascript" src="js/ext-all.gzjs"></script>
<script type="text/javascript" src="js/ext-theme-triton/theme-triton.js?1"></script>
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
<script type="text/javascript" src="lingx/js/lingx.js?1234"></script>
<script type="text/javascript" src="lingx/js/commonApi.js?1"></script> 
<script type="text/javascript" src="lingx/js/lingx-ext.js?123"></script> 