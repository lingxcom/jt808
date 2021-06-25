	function lingxSet(values){
		$("#"+values.cmpId).val(values.value);
		$("#"+values.cmpId+"_text").val(values.text);
		
	}
	//value为 JSON字符串 encode后的
	function dialogModel(title,ecode,mcode,cmpId){
		var txt=$("#"+cmpId+"_text").val(),val=$("#"+cmpId).val();
		if(txt){
		txt=encodeURI(encodeURI(txt));
		}
		openWindow(title,"e?e="+ecode+"&cmpId="+cmpId+"&m="+mcode+"&LingxOpertor=input&text="+txt+"&value="+val);
	}
	
	
	function dialogUploadFile(title,cmpId,param){
		openWindow(title,"lingx/template/upload/upload.jsp?cmpId="+cmpId+"&type="+(param||"JSON"));
	}
	
	function dialogUser1(cmpId){
	var txt=$("#"+cmpId+"_text").val(),val=$("#"+cmpId).val();
		if(txt){
		txt=encodeURI(encodeURI(txt));
		}
	openWindow("用户选择框","lingx/common/select_users_ext.jsp?lingxInputType=1&cmpId="+cmpId+"&text="+txt+"&value="+val);
		    
	}
	function dialogUserN(cmpId){
	var txt=$("#"+cmpId+"_text").val(),val=$("#"+cmpId).val();
		if(txt){
		txt=encodeURI(encodeURI(txt));
		}
	openWindow("用户选择框","lingx/common/select_users_ext.jsp?lingxInputType=n&cmpId="+cmpId+"&text="+txt+"&value="+val);
		    
	}
	
		
	function dialogOrg1(cmpId){
	var txt=$("#"+cmpId+"_text").val(),val=$("#"+cmpId).val();
		if(txt){
		txt=encodeURI(encodeURI(txt));
		}
	openWindow("组织选择框","lingx/common/select_orgs_ext.jsp?lingxInputType=1&cmpId="+cmpId+"&text="+txt+"&value="+val);
		    
	}
	function dialogOrgN(cmpId){
	var txt=$("#"+cmpId+"_text").val(),val=$("#"+cmpId).val();
		if(txt){
		txt=encodeURI(encodeURI(txt));
		}
	openWindow("组织选择框","lingx/common/select_orgs_ext.jsp?lingxInputType=n&cmpId="+cmpId+"&text="+txt+"&value="+val);
		    
	}