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
<title>选择用户</title>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/bootstrap/plugins/ztree/jquery.ztree.core-3.5.js"></script>
<link href="js/bootstrap/plugins/ztree/zTreeStyle.css" rel="stylesheet">
<script type="text/javascript">
var handlerJsp="lingx/common/handler.jsp";
var isMulti='${param.multi}'||false;
var exp1=true,exp2=true;
var setting1 = {
		async: {
			enable: true,
			url:handlerJsp+"?c=orgtree",
			autoParam:["id=fid", "name=n", "level=lv"],
			otherParam:{"otherParam":"zTreeAsyncTest"},
			dataFilter: filter
		},
	callback: {
		onAsyncSuccess: function(event, treeId, treeNode, msg){
			var treeObj = $.fn.zTree.getZTreeObj("treeOrg");
			var nodes=treeObj.getNodes();
			if(nodes.length>0&&exp1){
				exp1=false;
				treeObj.expandNode(nodes[0], true, true, true);
			}
			
		},
	beforeClick: function(treeId, treeNode) {
		$.post(handlerJsp,{c:"getUsersByOrg",id:treeNode.id},reloadUsers,"json");
	}
}
	};
var setting2 = {
		async: {
			enable: true,
			url:handlerJsp+"?c=roletree",
			autoParam:["id=fid", "name=n", "level=lv"],
			otherParam:{"otherParam":"zTreeAsyncTest"},
			dataFilter: filter
		},
	callback: {
		onAsyncSuccess:function(event, treeId, treeNode, msg){
			var treeObj = $.fn.zTree.getZTreeObj("treeRole");
			var nodes=treeObj.getNodes();
			if(nodes.length>0&&exp2){
				exp2=false;
				treeObj.expandNode(nodes[0], true, true, true);
			}
		},
	beforeClick: function(treeId, treeNode) {
		$.post(handlerJsp,{c:"getUsersByRole",id:treeNode.id},reloadUsers,"json");
	}
}
	};

	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
		return childNodes;
	}

	$(document).ready(function(){
		$.fn.zTree.init($("#treeOrg"), setting1);

		$.fn.zTree.init($("#treeRole"), setting2);
	});
	
	function treeON(id){
		$(".ztree").hide();
		$("#"+id).show();
	}
	
	function reloadUsers(array){
		var ul=$("#cont1").find("ul");
		ul.empty();
		$.each(array,function(ind,obj){
			ul.append("<li ref='"+obj.id+"'>"+obj.name+"</li>");
		});
		
		ul.find("li").bind("click",function(ev){
			var el=$(this);
			if(!isMulti)$("#cont2").find("ul").empty();
			if($("#cont2").find("li[ref='"+el.attr("ref")+"']").length==0)
			$("#cont2").find("ul").append(el.clone(true).unbind("click").removeClass("li-hover").css("color","red").bind("click",function(){
				$(this).remove();
			}));
		}).hover(function(){
			$(this).addClass("li-hover");
		},function(){
			$(this).removeClass("li-hover");
		});
	}
	
	function getValue(){
		var array=$("#cont2").find("li");
		var val="";
		for(var i=0;i<array.length;i++){
			val=val+$(array[i]).attr("ref")+",";
		}
		if(val.length>0)val=val.substring(0,val.length-1);
		return val;
	}
	function getText(){
		var array=$("#cont2").find("li");
		var txt="";
		for(var i=0;i<array.length;i++){
			txt=txt+$(array[i]).text()+",";
		}
		if(txt.length>0)txt=txt.substring(0,txt.length-1);
		return txt;
	}
</script>
<style type="text/css">
*{padding:0px;margin:0px;}
li{list-style-type:none;cursor:pointer;}
.cont{
overflow:auto;
height:300px;
background-color:#FFF;
font-size:12px;line-height:24px;
}
.li-hover{
background-color:#fafafa;
}
</style>
</head>
<body>
<table style="border-collapse:collapse;background-color:#fafafa;" bordercolor="#aaa" border="1" cellSpacing="0" cellPadding="0">
<tr align="center" style="font-size:14px;line-height:28px;">
<td  style="width:200px;"> <a href="javascript:;" onclick="treeON('treeOrg')">按组织筛选</a> | <a href="javascript:;"  onclick="treeON('treeRole')">按角色筛选</a></td>
<td  style="width:200px;"><b>可选用户</b></td>
<td  style="width:200px;"><b>已选用户</b></td>
</tr>
<tr valign="top" >
<td class="cont">
<ul id="treeOrg" class="ztree"></ul>
<ul id="treeRole" class="ztree" style="display:none;"></ul>
</td>
<td align="center">
<div id="cont1" class="cont">
<ul></ul>
</div>
</td>
<td align="center">
<div id="cont2" class="cont">
<ul></ul>
</div>
</td>
</tr>
</table>
</body>

</html>