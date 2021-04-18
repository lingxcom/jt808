<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="viewport"
	content="initial-scale=1, width=device-width, maximum-scale=1, user-scalable=no">
<meta name="viewport"
	content="initial-scale=1.0,user-scalable=no,maximum-scale=1"
	media="(device-height: 568px)">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-touch-fullscreen" content="yes">
<meta name="full-screen" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="address=no">
<%@ include file="/lingx/include/include_JavaScriptAndCss2.jsp"%> 
<script type="text/javascript" src="<%=basePath %>js/md5-min.js"></script>

<script type="text/javascript">
function login(){
	encodePassword();
	cookieSet();
	$.post("d",{c:"login",userid:$("#userid").val(),password:$("#password").val(),verifycode:$("#code").val()},function(json){
		if(json.code==1){
			window.location.href="lingx/wap/index.jsp";
		}else{
			alert(json.message);
		}
	},"json");
}

function encodePassword(){
	var password=""+$("#password").val();
	if(password&&password.length!=32){
	var pwd=Lingx.javascriptPasswordEncode($("#password").val(), $("#userid").val());
	$("#password").val(pwd);
	}
}

$(function(){
	var userid=getCookie("userid");
	var password=getCookie("password");
	if(userid||password){
		document.getElementById("checkbox").checked=true;
		$("#userid").val(userid);
		$("#password").val(password);
	}
	$('body').bind('keyup', function(event){
		   if (event.keyCode=="13"){
			   login();
		   }
		});

});
function cookieSet(){
	if(document.getElementById("checkbox").checked){
		setCookie("userid",$("#userid").val(),365);
		setCookie("password",$("#password").val(),365);
	}else{
		setCookie("userid","");
		setCookie("password","");
	}
	
}
function getCookie(c_name)
{
if (document.cookie.length>0)
  {
  c_start=document.cookie.indexOf(c_name + "=");
  if (c_start!=-1)
    { 
    c_start=c_start + c_name.length+1 ;
    c_end=document.cookie.indexOf(";",c_start);
    if (c_end==-1) c_end=document.cookie.length;
    return unescape(document.cookie.substring(c_start,c_end));
    } 
  }
return "";
}

function setCookie(c_name,value,expiredays)
{
var exdate=new Date();
exdate.setDate(exdate.getDate()+expiredays);
document.cookie=c_name+ "=" +escape(value)+
((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
}

</script>
</head>
<body>
<div class="container-fluid">
<div class="row">
<div class="col-md-12"  style="text-align:center;">
<h1>LINGX</h1><br>
</div>
</div>
<div class="row">
<div class="col-md-12">
<form class="form-horizontal">
  <div class="form-group">
    <label for="inputEmail3" class="col-sm-2 control-label">账号</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="userid" placeholder="Email">
    </div>
  </div>
  <div class="form-group">
    <label for="inputPassword3" class="col-sm-2 control-label">密码</label>
    <div class="col-sm-10">
      <input type="password" class="form-control" id="password" placeholder="Password">
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <div class="checkbox">
        <label>
          <input id="checkbox" type="checkbox"><label for="checkbox">记住账号密码</label>
        </label>
      </div>
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10" style="text-align:center;">
      <button onclick="login()" type="button" class="btn btn-primary">登录</button>
    </div>
  </div>
</form>

</div>
</div>
</div>
</body>
</html>