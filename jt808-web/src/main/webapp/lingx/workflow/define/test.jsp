<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html xmlns="http://www.w3.org/1999/xhtml">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
<title>无标题文档</title>  
  
<style type="text/css">  
.linkinfo  
{  
    z-index:102;  
}  
.point  
{  
    position:absolute;background-color:blue;overflow:hidden;  
}  
.point-critical  
{  
    background-color:Red;  
}  
.arrowhead-down  
{  
    background:url(../images/arrow_blue_down.gif) top left no-repeat;     
    overflow:hidden;  
    width:10px;  
    height:5px;  
    position:absolute;  
}  
.arrowhead-down-critical  
{  
    background-image:url(../images/arrow_red_down.gif);//这是一个宽9px，高5px的向下的红色箭头  
}  
</style>  
</head>  
  
<body>  
    <div class="linkinfo point point-critical" style="left: 31px; top: 31px; width: 9px; height: 1px;"></div>  
    <div class="linkinfo point point-critical" style="left: 40px; top: 31px; height: 23px; width: 1px;"></div>  
    <div class="linkinfo arrowhead-down arrowhead-down-critical" style="left: 36px; top: 49px;"></div>  
</body>  
</html> 