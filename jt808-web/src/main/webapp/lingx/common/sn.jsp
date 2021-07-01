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
<title>应用授权</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 

<script type="text/javascript">
Ext.onReady(function() {
	Lingx.post("e?e=be516eac-aa22-4e16-9d46-cb34dc5713e5&m=af4c7be7-8d10-4b89-b120-d4d9ae05&t=3", {}, function(json){
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
            fieldLabel: require+'注册码'
            ,value:json.message
        }, {
        	id:"p2",
            fieldLabel:  require+'授权码'
          
        }, {
            fieldLabel: '说明',
            xtype: 'displayfield',
            value:"您的系统需要授权后才能使用，<br/> <a href='http://www.lingx.com/bi/authcode.jsp' target='_blank'>点击前往->授权申请</a><br/>"//(注：发邮件时注明要授权码，并带上QQ号或手机号码)
          
        }]
    });

    var win = Ext.create('Ext.window.Window', {
        autoShow: true,
        title: '应用授权',
        width: 500,
        height: 300,
        minWidth: 300,
        minHeight: 200,
        y:100,
        layout: 'fit',
        plain:true,
        items: form,
	
        buttons: [{
            text: '确定',
            handler:function(){
            	var p1=Ext.getCmp("p1").getValue();
            	var p2=Ext.getCmp("p2").getValue();
            	if(!p2){
            		Ext.MessageBox.alert("系统消息","授权码不可为空！");
            		return;
            	}
            	if(p2.length!=64){
            		Ext.MessageBox.alert("系统消息","授权码有误！");
            		return;
            	}
            	Lingx.post("lingx/common/handler.jsp",{c:"authcode",p1:p1,p2:p2},function(json){
            		Ext.MessageBox.alert("系统消息",json.message,function(){
            			if(json.code==1)window.location.href="d?c=logout";
            		});
            	});
            }
        }]
    });
	});
});

</script>
</head>
<body>
</body>

</html>