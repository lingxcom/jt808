<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	com.lingx.core.service.IPageService pageService=spring.getBean(com.lingx.core.service.IPageService.class);
	String url="";
	if(session.getAttribute(com.lingx.core.Constants.SESSION_USER)!=null){
		url="d?c=i";
	}else{
		url="d?c=l";
	}
	request.setAttribute("page", url);
	
	com.lingx.core.service.II18NService i18n=spring.getBean(com.lingx.core.service.II18NService.class);
	String lanuage=request.getHeader("Accept-Language");
	if(lanuage!=null&&lanuage.indexOf(",")>0){
		lanuage=lanuage.substring(0,lanuage.indexOf(","));
		if(i18n.hasLanuage(lanuage)){
			i18n.setLanuage(lanuage);
			session.setAttribute(com.lingx.core.Constants.SESSION_LANGUAGE, lanuage);
		}
	}
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<script type="text/javascript">
var page="${page}";
window.location.href=page;
/* 
var sUserAgent = navigator.userAgent.toLowerCase();
var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
var bIsMidp = sUserAgent.match(/midp/i) == "midp";
var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
var bIsAndroid = sUserAgent.match(/android/i) == "android";
var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";

if (bIsIpad || bIsIphoneOs || bIsMidp && bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {//如果是上述设备就会以手机域名打开
	if(page=="d?c=i"){
		window.location.href = 'lingx/wap/index.jsp';
	}else{
		window.location.href = 'lingx/wap/login.jsp';
	}
    
   // return;
}else{
	window.location.href=page;
} */
</script>
</head>
<body>
</body>
</html> 