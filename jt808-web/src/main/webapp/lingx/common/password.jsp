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
<title>修改密码</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript">
Ext.onReady(function() {
	var require="<span style='color:red;'>*</span>";
    var form = Ext.create('Ext.form.Panel', {
        layout: 'form',
        defaultType: 'textfield',
        border: false,
        bodyPadding: 5,
		bodyStyle:"background:#dfe9f6;",
		 fieldDefaults: {
	            labelAlign: 'right',
	            labelWidth: 100
	           , anchor: '100%'
	        },
        items: [{
        	id:"p1",
            fieldLabel: require+'旧密码',
            xtype: 'password'
            
        }, {
        	id:"p2",
            fieldLabel:  require+'新密码',
            xtype: 'password'
          
        }, {
        	id:"p3",
            fieldLabel:  require+'密码确认',
            xtype: 'password'
          
        }, {
            fieldLabel: '说明',
            xtype: 'displayfield',
            value:"您当前的密码过于简单，存在数据安全、信息泄漏等隐患。需要修改密码后才能进入系统；<br>1、密码长度不小于六个字符。<br>2、密码必须同时包含字母与数字。"
          
        }]
    });

    var win = Ext.create('Ext.window.Window', {
        autoShow: true,
        title: '修改密码',
        width: 500,
        height: 300,
        minWidth: 300,
        minHeight: 200,
        y:100,
        layout: 'fit',
        plain:true,
        items: form,
	
        buttons: [{
            text: '修改密码',
            handler:function(){
            	var p1=Ext.getCmp("p1").getValue();
            	var p2=Ext.getCmp("p2").getValue();
            	var p3=Ext.getCmp("p3").getValue();
            	if(!p1||!p2||!p3){
            		Ext.MessageBox.alert("系统消息","密码不可为空");
            		return;
            	}
            	if(!check(p2)){
            		Ext.MessageBox.alert("系统消息","新密码必须同时包含字母和数字");
            		return;
            	}
            	if(p2!=p3){
            		Ext.MessageBox.alert("系统消息","密码确认有误！");
            		return;
            	}
            	Lingx.post("e?e=tlingx_user&m=editPassword",{lgxsn:1,oldpass:p1,password1:p2,password2:p3},function(json){
            		Ext.MessageBox.alert("系统消息",json.message,function(){
            			if(json.code==1)window.location.href="d?c=i";
            		});
            	});
            }
        },{
            text: '返回登录',
            handler:function(){
            	window.location.href="d?c=logout";
            }
        }]
    });
});

function check(temp){
	temp=temp+"";
	var c3=0,c4=0;
	var c1="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	var c2="0123456789";
	for(var i=0;i<temp.length;i++){
		if(c1.indexOf(temp.charAt(i))>=0)c3++;
		if(c2.indexOf(temp.charAt(i))>=0)c4++;
	}
	return c3>0&&c4>0;
}
</script>
</head>
<body>
</body>

</html>