function setFormValue(){
	$.each(values,function(index,obj){
		$("input[name='"+obj.name+"']").val(obj.value);
		$("select[name='"+obj.name+"']").val(obj.value);
		$("textarea[name='"+obj.name+"']").val(obj.value);
		$("#"+obj.name).attr("src",obj.value);//签章功能有用到
	});
}
$(function(){
	//控件初始化
	init();
	//设置初始值
	 setFormValue();
	 //处理下载标签
	 handlerDownloadTag();
	//设置审批意见
	var opinionEl=$(".opinion");
	var opinionGroup=opinionEl.find(".list-group");
	var opinionTitle=opinionEl.find(".panel-heading");
	$.each(opinion,function(index,obj){
		var retStr="";
		switch(obj.ret-0){
		case 0:retStr="";break;
		case 1:retStr="同意";break;
		case 2:retStr="不同意";break;
		case 3:retStr="已阅";break;
		default:
			retStr="未知";
		}
		opinionGroup.append(" <li class='list-group-item'><div style='line-height:28px;'>"+obj.content+"</div>"+
				"<div style='font-size:12px;color:#999;height:24px;line-height:24px;'>"+obj.user_name+" "+retStr+" "+formatDate(obj.create_time)+"</div>"
				+"</li>");
	});
	opinionTitle.text(opinionTitle.text().replace("0",opinion.length));
	if(opinion.length==0)opinionGroup.append(" <li class='list-group-item'>暂无意见</li>");
	
});
var fallback=new Array();
var handlerJsp="lingx/workflow/page/handler.jsp";
function lingxFallback(){
	var modal=$(".bs-sign-modal-sm");
	var group=modal.find(".list-group");
	group.empty();
	fallback=new Array();
	$.post(handlerJsp,{c:"getPopleTask",instance_id:instanceId},function(json){
		$.each(json,function(ind,obj){
			if(defineTaskId==obj.task_id)return ;
			for(var i=0;i<fallback.length;i++){
				if(fallback[i]==obj.task_id){
					return ;
				}
			}
			fallback.push(obj.task_id);
			group.append($("<li class='list-group-item' ref-id='"+obj.id+"' style='cursor:pointer;' >"+obj.name+" ["+obj.user_name+"]"+"</li>").bind("click",function(){
				var el=$(this);
				if(!confirm("确认回退该任务吗？"))return ;
				//window.location.href="w?m=fallback&_TASK_ID="+taskId+"&target_task_id="+(el.attr("ref-id"));
				$.post("w",{m:"fallback",_TASK_ID:taskId,target_task_id:el.attr("ref-id")},function(json){
					jsonRet(json);
				},"json");
			}));
		});
	},"JSON");
	
		
	modal.modal("show");
}

function lingxUpload(){
	var modal=$(".bs-sign-modal-sm");
	var group=modal.find(".list-group");
	
	group.empty();
	group.append($("<li class='list-group-item'><b>附件上传</b></li>"));
	group.append($("<li class='list-group-item' ref-id='' style='cursor:pointer;' ><div id=\"btnContainer\"><input id='file' name='file' type='file' /></div></li>"));
	group.append($("<li class='list-group-item'>附件名称：<input id='attId' name='attName' /><span style='color:red;'>*</span></li>"));
	
	var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>确定</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
	
	group.find('input[type="file"]').on('change', function() {
	      var reg = /[^\\\/]*[\\\/]+/g; //匹配文件的名称和后缀的正则表达式
	      var name = $(this).val().replace(reg, '');
	      var postfix = /\.[^\.]+/.exec(name);//获取文件的后缀
	      //var text =name.substr(0,postfix['index']);//获取没有后缀的名称
	      $("#attId").val(name);
	    });
	btns.find(".ok").bind("click",function(){

		 $.ajaxFileUpload({
			url: 'lingx/template/upload/handler.jsp', 
		    type: 'post',
		    secureuri: false, //一般设置为false
		    fileElementId: 'file', // 上传文件的id、name属性名
		    dataType: 'json', //返回值类型，一般设置为json、application/json
		    success: function(json, status){  
		    	alert(json.message);
			       if(json.code==1){
			    	   $.post(handlerJsp,{c:"upload",id:instanceId,name:$("#attId").val(),path:json.path,length:json.length},function(json){
			    		   lingxReload();
			    	   },"json");
			   			modal.modal("hide");
			       }else{
			       }
		    },
		    error: function(json, status, e){ 
		        alert(e);
		    }
		 });
	});
	btns.find(".cancel").bind("click",function(){
		modal.modal("hide");
	});
	group.append(btns);	
	
	modal.modal("show");
}
function lingxDelegate(){
	var modal=$(".bs-sign-modal-sm");
	var group=modal.find(".list-group");
	group.empty();
	group.append($("<li class='list-group-item'><iframe class='selectUsers' src='lingx/common/select_users.jsp' style='border:0px none;width:550px;height:332px;padding:0px;margin:0px;'></iframe></li>"));
	var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>委托</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
	btns.find(".ok").bind("click",function(){
		var ifr=$(".selectUsers");
		var val=ifr[0].contentWindow.getValue();
		if(val){
			//window.location.href="w?m=delegate&_TASK_ID="+taskId+"&user_id="+val;
			$.post("w",{m:"delegate",_TASK_ID:taskId,user_id:val},function(json){
				jsonRet(json);
			},"json");
		}else{
			alert("请选择用户");
		}
	});
	btns.find(".cancel").bind("click",function(){
		modal.modal("hide");
	});
	group.append(btns);
	modal.modal("show");
	
}
function lingxCC(){
	var modal=$(".bs-sign-modal-sm");
	var group=modal.find(".list-group");
	group.empty();
	group.append($("<li class='list-group-item'><iframe class='selectUsers' src='lingx/common/select_users.jsp?multi=true' style='border:0px none;width:550px;height:332px;padding:0px;margin:0px;'></iframe></li>"));
	var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>抄送</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
	btns.find(".ok").bind("click",function(){
		var ifr=$(".selectUsers");
		var val=ifr[0].contentWindow.getValue();
		if(val){
			$.post("w",{m:"cc",_TASK_ID:taskId,user_ids:val},function(json){alert(json.message);modal.modal("hide");},"json");
			//window.location.href="w?m=cc&_TASK_ID="+taskId+"&user_id="+val;
		}else{
			alert("请选择用户");
		}
	});
	btns.find(".cancel").bind("click",function(){
		modal.modal("hide");
	});
	group.append(btns);
	modal.modal("show");
}
function lingxRollback(){
	if(!confirm("确定将流程退回至上一节点吗？"))return;
	$.post("w",{m:"approve",_TASK_ID:taskId,ret_approve:2,content_approve:"退回"},function(json){
		jsonRet(json);
	},"json");
}
function lingxApprove(){
	var modal=$(".bs-sign-modal-sm");
	var group=modal.find(".list-group");
	group.empty();
	group.append($("<li class='list-group-item'><b>任务审批</b></li>"));
	group.append($("<li class='list-group-item'><!-- <input id='ret3' name='ret' type='radio' value='3' /> <label for='ret3'> 已阅</label> &nbsp;&nbsp;--> <input id='ret1' name='ret' type='radio' value='1' /> <label for='ret1'> 同意</label> &nbsp;&nbsp;   <input id='ret2' name='ret' type='radio' value='2' /> <label for='ret2'> 不同意</label> <span style='color:red;'>*</span> </li>"));
	var conts=$("<li class='list-group-item'><textarea class='opintion_content' style='width:500px;height:200px;'/><span style='color:red;'>*</span><br/><span style='font-size:12px;'>常用短语：<a style='font-size:12px;' href='javascript:;'>1. 同意，按规定办理 </a>;<a style='font-size:12px;' href='javascript:;'>2. 按流程办理</a>; <a style='font-size:12px;' href='javascript:;'>3. 交相关部门处理</a></span></li>");
	conts.find("a").bind("click",function(){
		var el=$(this);
		$(".opintion_content").val(el.text().substring(2));
	});
	group.append(conts);
	group.find("input[name='ret']").bind("change",function(){
		var _this=this;
		if(_this.checked){
			$(".opintion_content").val("1"==_this.value?"同意":"不同意");
		}
	});
	var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>确定</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
	btns.find(".ok").bind("click",function(){
		var ret=$("input[name='ret']:checked").val();
		var cont=$(".opintion_content").val();
		if(!ret){
			alert("请选择同意或不同意");
			return;
		}
		//window.location.href="w?m=approve&_TASK_ID="+taskId+"&ret_approve="+ret+"&content_approve="+encodeURI(encodeURI(cont));
		handlerListController();
		$("#m").val("save");
		var array=$("form").serializeArray();
		var fparams={};
		for(var i=0;i<array.length;i++){
			fparams[array[i].name]=array[i].value;
		}
		$.post("w",fparams,function(json1){
			//jsonRet(json);
			$.post("w",{m:"approve",_TASK_ID:taskId,ret_approve:ret,content_approve:cont},function(json){
				jsonRet(json);
			},"json");
		},"json");
		
	});
	btns.find(".cancel").bind("click",function(){
		modal.modal("hide");
	});
	group.append(btns);
	modal.modal("show");
}
function lingxView(){

	var modal=$(".bs-sign-modal-sm");
	var group=modal.find(".list-group");
	group.empty();
	group.append($("<li class='list-group-item'><b>任务查阅</b></li>"));
	group.append($("<li class='list-group-item'><input id='ret3' name='ret' type='radio' value='3' checked='checked' /> <label for='ret3'> 已阅</label> <span style='color:red;'>*</span> </li>"));
	var conts=$("<li class='list-group-item'><textarea class='opintion_content' style='width:500px;height:200px;'/><span style='color:red;'>*</span><br/><span style='font-size:12px;'>常用短语：<a style='font-size:12px;' href='javascript:;'>1. 同意，按规定办理 </a>;<a style='font-size:12px;' href='javascript:;'>2. 按流程办理</a>; <a style='font-size:12px;' href='javascript:;'>3. 交相关部门处理</a></span></li>");
	conts.find("a").bind("click",function(){
		var el=$(this);
		$(".opintion_content").val(el.text().substring(2));
	})
	group.append(conts);
	var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>确定</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
	btns.find(".ok").bind("click",function(){
		var ret=$("input[name='ret']:checked").val();
		var cont=$(".opintion_content").val();
		if(!ret){
			alert("请选择同意或不同意");
			return;
		}
		//window.location.href="w?m=approve&_TASK_ID="+taskId+"&ret_approve="+ret+"&content_approve="+encodeURI(encodeURI(cont));
		$.post("w",{m:"approve",_TASK_ID:taskId,ret_approve:ret,content_approve:cont},function(json){
			jsonRet(json);
		},"json");
	});
	btns.find(".cancel").bind("click",function(){
		modal.modal("hide");
	});
	group.append(btns);
	modal.modal("show");
}
function lingxClaim(){
	window.location.href="w?m=claim&_TASK_ID="+taskId;
}
/**
 * 列表控件特殊处理
 */
function handlerListController(){
	var list=$("table.lingx-wf-list");
	for(var k=0;k<list.length;k++){
		var valueList=[];
		var obj=list[k];
		var tb=$(obj);
		var trs=tb.find("tr.data-tr");
		for(var i=0;i<trs.length;i++){
			var tr=$(trs[i]);
			var ipts=tr.find("input");
			var valueMap={};var isEmpty="";
			for(var j=0;j<ipts.length;j++){
				var ipt=$(ipts[j]);
				valueMap[ipt.attr("data-name")]=ipt.val();
				isEmpty+=ipt.val();
			}
			if(!!isEmpty){
				valueList.push(valueMap);
			}else{
				tr.remove();
			}
			
		}
		var newInput=$("input[name='list_"+tb.attr("field-name")+"']");
		if(newInput.length==0)newInput=$("<input type='hidden' name='list_"+tb.attr("field-name")+"'>");
		newInput.val(JSON.stringify(valueList));
		$("#m").after(newInput);
	}
}
function lingxSave(){
	handlerListController();
	$("#m").val("save");
	var array=$("form").serializeArray();
	var fparams={};
	for(var i=0;i<array.length;i++){
		fparams[array[i].name]=array[i].value;
	}
	$.post("w",fparams,function(json){
		jsonRet(json);
	},"json");
	//$("#form").submit();
	return true;
}
function lingxSubmit(){
	handlerListController();
	if(!confirm("确认要提交任务吗？")){return;}
	var isValid=true;
	var array=new Array();
	var currEl=null,nullEl=null;
	var ipts1=$("input[notNull='true']");
	var ipts2=$("select[notNull='true']");
	var ipts3=$("textarea[notNull='true']");
	
	array=array.concat(ipts1,ipts2,ipts3);
	
	for(var i=0;i<array.length;i++){
		currEl=$(array[i]);
		if(!currEl.prop("name"))continue;
		if(isValid){
			isValid=currEl.val();
			nullEl=currEl;
		}else{
			break;
		}
	}
	if(isValid){
		$("#m").val("submit");
		var array=$("form").serializeArray();
		var fparams={};
		for(var i=0;i<array.length;i++){
			fparams[array[i].name]=array[i].value;
		}
		$.post("w",fparams,function(json){
			jsonRet(json);
		},"json");
		//console.log(fparams);
		//$("#form").submit();
	}else{
		lgxInfo("有数据不可为空，请检查");
		nullEl.focus();
	}
	
	/* var modal=$(".bs-sign-modal-sm");
	var group=modal.find(".list-group");
	group.empty();
	group.append($("<li class='list-group-item'><b>1、当前任务</b><br>"
			+"当前任务：<b>填写表单</b>，张三</li>"));
	group.append($("<li class='list-group-item'><b>2、后续步骤</b><br>"
			+"下一任务：<b>部门审批</b>，处理人：李四</li>"));
	group.append($("<li class='list-group-item'><b>3、事务提醒</b><br>"
			+"提醒对象：下一步聚，发起人，所有经办人</li>"));
	group.append($("<li class='list-group-item'><div class='input-group'>  <span class='input-group-addon'>提醒内容</span>  <input type='text' class='form-control'> </div></li>"));

	var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>确定</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
	btns.find(".ok").bind("click",function(){
		
	});
	btns.find(".cancel").bind("click",function(){
		modal.modal("hide");
	});
	group.append(btns);modal.modal("show"); */
	return true;
}

function checkForm(){
	
	return true;
}

function init(){
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
	
	var editors=$(".lingx-wf-ipt");
	if(",0,"!=authTag){
	for(var i=0;i<editors.length;i++){
		var obj=editors[i];
		var el=$(obj);
		var authCfg=","+el.attr("authCfg")+",";
		var authType=el.attr("authType")||true;
		if(authCfg){
			if(authType){
				//true
				if(authCfg.indexOf(authTag)>-1){
					el.removeProp("disabled");
					el.removeAttr("disabled");
				}else{
					el.prop("disabled","disabled");
					el.attr("disabled","disabled");
				}
			}else{
				//false
				if(authCfg.indexOf(authTag)>-1){
					el.prop("disabled","disabled");
					el.attr("disabled","disabled");
				}else{
					el.removeProp("disabled");
					el.removeAttr("disabled");
				}
			}
		}
	}
	}
	
	var signs=$(".lingx-wf-sign");
	$.each(signs,function(index,obj){
		var el=$(obj);
		el.prop("src",$("input[name='"+el.prop("id")+"']").val());
		if(!el.prop("disabled"))
		el.bind("click",function(){
			var el1=$(this);
			$.post("e?e=tlingx_wf_common_sign&m=combo&t=3",{user_id:currentUserId},function(json){
				var modal=$(".bs-sign-modal-sm");
				var group=modal.find(".list-group");
				group.empty();
				json.push({name:"删除",path:"lingx/images/sign.png"});
				
				for(var i=0;i<json.length;i++){
					group.append($("<li class='list-group-item' style='cursor:pointer;' ref='"+el.prop("id")+"' path='"+json[i].path+"'>"+json[i].name+"</li>").bind("click",function(){
						var el2=$(this);
						var path=el2.attr("path");
						var ref=el2.attr("ref");
						$("input[name='"+ref+"']").val(path);
						$("#"+ref).prop("src",path);
					}));
				}
				modal.modal('show');
			},"JSON");
		});
	});
	var selectCacheInputName="";
	//用户单选
	var users=$(".lingx-wf-user");
	$.each(users,function(ind,obj){
		var el=$(obj);
		var val=$("<input type='hidden' >");
		var name=el.prop("name");
		el.prop("name",name+"_txt");
		el.attr("ref-name",name);
		val.prop("name",name);
		el.after(val);
		if(!el.prop("disabled"))
			el.bind("click",function(){
				selectCacheInputName=$(this).prop("name").replace("_txt","");
				var modal=$(".bs-sign-modal-sm");
				var group=modal.find(".list-group");
				group.empty();
				group.append($("<li class='list-group-item'><iframe class='selectUsers' src='lingx/common/select_users.jsp' style='border:0px none;width:550px;height:332px;padding:0px;margin:0px;'></iframe></li>"));
				var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>选择</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
				btns.find(".ok").bind("click",function(){
					var ifr=$(".selectUsers");
					var val=ifr[0].contentWindow.getValue();
					var txt=ifr[0].contentWindow.getText();
					$("input[name='"+selectCacheInputName+"']").val(val);
					$("input[name='"+selectCacheInputName+"_txt']").val(txt);
					modal.modal("hide");
				});
				btns.find(".cancel").bind("click",function(){
					modal.modal("hide");
				});
				group.append(btns);
				modal.modal("show");
			});
		
	});
	//用户多选
	users=$(".lingx-wf-users");
	$.each(users,function(ind,obj){
		var el=$(obj);
		var val=$("<input type='hidden' >");
		var name=el.prop("name");
		el.prop("name",name+"_txt");
		el.attr("ref-name",name);
		val.prop("name",name);
		el.after(val);
		if(!el.prop("disabled"))
			el.bind("click",function(){
				selectCacheInputName=$(this).prop("name").replace("_txt","");
				var modal=$(".bs-sign-modal-sm");
				var group=modal.find(".list-group");
				group.empty();
				group.append($("<li class='list-group-item'><iframe class='selectUsers' src='lingx/common/select_users.jsp?multi=true' style='border:0px none;width:550px;height:332px;padding:0px;margin:0px;'></iframe></li>"));
				var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>选择</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
				btns.find(".ok").bind("click",function(){
					var ifr=$(".selectUsers");
					var val=ifr[0].contentWindow.getValue();
					var txt=ifr[0].contentWindow.getText();
					$("input[name='"+selectCacheInputName+"']").val(val);
					$("textarea[name='"+selectCacheInputName+"_txt']").val(txt);
					modal.modal("hide");
				});
				btns.find(".cancel").bind("click",function(){
					modal.modal("hide");
				});
				group.append(btns);
				modal.modal("show");
			});
		
	});
	//组织单选
	var orgs=$(".lingx-wf-org");
	$.each(orgs,function(ind,obj){
		var el=$(obj);
		var val=$("<input type='hidden' >");
		var name=el.prop("name");
		el.prop("name",name+"_txt");
		el.attr("ref-name",name);
		val.prop("name",name);
		el.after(val);
		if(!el.prop("disabled"))
			el.bind("click",function(){
				selectCacheInputName=$(this).prop("name").replace("_txt","");
				var modal=$(".bs-sign-modal-sm");
				var group=modal.find(".list-group");
				group.empty();
				group.append($("<li class='list-group-item'><iframe class='selectUsers' src='lingx/common/select_orgs.jsp' style='border:0px none;width:550px;height:332px;padding:0px;margin:0px;'></iframe></li>"));
				var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>选择</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
				btns.find(".ok").bind("click",function(){
					var ifr=$(".selectUsers");
					var val=ifr[0].contentWindow.getValue();
					var txt=ifr[0].contentWindow.getText();
					$("input[name='"+selectCacheInputName+"']").val(val);
					$("input[name='"+selectCacheInputName+"_txt']").val(txt);
					modal.modal("hide");
				});
				btns.find(".cancel").bind("click",function(){
					modal.modal("hide");
				});
				group.append(btns);
				modal.modal("show");
			});
		
	});
	//组织多选
	orgs=$(".lingx-wf-orgs");
	$.each(orgs,function(ind,obj){
		var el=$(obj);
		var val=$("<input type='hidden' >");
		var name=el.prop("name");
		el.prop("name",name+"_txt");
		el.attr("ref-name",name);
		val.prop("name",name);
		el.after(val);
		if(!el.prop("disabled"))
			el.bind("click",function(){
				selectCacheInputName=$(this).prop("name").replace("_txt","");
				var modal=$(".bs-sign-modal-sm");
				var group=modal.find(".list-group");
				group.empty();
				group.append($("<li class='list-group-item'><iframe class='selectUsers' src='lingx/common/select_orgs.jsp?multi=true' style='border:0px none;width:550px;height:332px;padding:0px;margin:0px;'></iframe></li>"));
				var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>选择</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
				btns.find(".ok").bind("click",function(){
					var ifr=$(".selectUsers");
					var val=ifr[0].contentWindow.getValue();
					var txt=ifr[0].contentWindow.getText();
					$("input[name='"+selectCacheInputName+"']").val(val);
					$("textarea[name='"+selectCacheInputName+"_txt']").val(txt);
					modal.modal("hide");
				});
				btns.find(".cancel").bind("click",function(){
					modal.modal("hide");
				});
				group.append(btns);
				modal.modal("show");
			});
		
	});
	
	//外部连接
	var links=$(".lingx-wf-link");
	$.each(links,function(ind,obj){
		var el=$(obj);
		var url=el.attr("url");
		url=Lingx.urlAddParams(url,{instance_id:instanceId});
		el.empty();
		el.append('<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+url+'"> </iframe>');
	});
	//
	
	var selects=$("select");
	$.each(selects,function(index,obj){
		var el=$(obj);
		var etype=el.attr("refEntity");
		var url="";
		if("tlingx_optionitem"==etype){
			var code1=el.attr("json");
			url="e?e=tlingx_option&m=items&lgxsn=1&code="+code1;
			$.post(url,{},function(json){
				for(var i=0;i<json.length;i++){
					el.append("<option value='"+json[i].value+"'>"+json[i].text+"</option>");
				}
				setFormValue();
			},"json");
		}else{
			url="e?e="+etype+"&m=combo&lgxsn=1";
			var param=el.attr("json");
			$.post(url,param?$.parseJSON(param):{},function(json){
				for(var i=0;i<json.length;i++){
					el.append("<option value='"+json[i].value+"'>"+json[i].text+"</option>");
				}
				setFormValue();
			},"json");
		}
		
	});
	//列表控件
	links=$(".lingx-wf-list");
	$.each(links,function(ind,obj){
		var el=$(obj);var count=0;
		var json=el.attr("json");
		var disabled=!!el.attr("disabled");
		el.empty();
		json=JSON.parse(json);
		var name=json.name;
		var table=$("<table class='table table-bordered lingx-wf-list' field-name='"+json.name+"'>");
		var tr=$("<tr>");
		for(var i=0;i<json.field.length;i++){
			var obj=json.field[i];
			tr.append("<th>"+obj+"</th>");
			count++;
		}
		table.append(tr);
		
		
		//值初始化
		var valueList=[];
		for(var i=0;i<values.length;i++){
			var obj=values[i];
			if(("list_"+name)==obj.name){
				valueList=JSON.parse(obj.value);
				break;
			}
		}
		for(var i=0;i<valueList.length;i++){
			var val=valueList[i];
			var _tr=$("<tr class='data-tr'>");
			for(var j=0;j<json.field.length;j++){
				var obj=json.field[j];
				_tr.append("<td><input style='width:120px;' "+(disabled?"disabled='disabled'":"")+" data-name='"+obj+"' value='"+val[obj]+"'/></td>");
			}
			table.append(_tr);
		}
		//end 值初始化
		var tr2=$("<tr><td colspan='"+count+"' style='text-align:center;color:#999;cursor: pointer;'>新增一行</td></tr>");
		if(!disabled)
		tr2.find("td").bind("click",function(){
			var _this=$(this);
			var _tr=$("<tr class='data-tr'>");
			for(var i=0;i<json.field.length;i++){
				var obj=json.field[i];
				_tr.append("<td><input style='width:120px;' data-name='"+obj+"'/></td>");
			}
			_this.parent().before(_tr);
		});
		if(!disabled)
		table.append(tr2);
		table.appendTo(el);
		el.css("border","0px none");
		//el.html(json);
	});
	//附件上传
	links=$(".lingx-wf-upload");
	$.each(links,function(ind,obj){
		var el=$(obj);
		//var url=el.attr("url");
		var json=el.attr("json");
		var disabled=!!el.attr("disabled");
		el.empty();
		el.css("border","0px none");
		el.css("height","auto");
		el.css("width","auto");
		json=JSON.parse(json);
		el.append("<input id='uploadEl_"+json.name+"' name='uploadEl_"+json.name+"' type='hidden'>");
		el.append("<input id='uploadEl_"+json.name+"_val' class='lingx-wf-upload-filepath' name='uploadEl_"+json.name+"_val' type='hidden'>");
		if(!disabled)
		el.append('<button type="button" '+(disabled?'disabled=""disabled"':'')+' onclick="openWindow(\'文件上传框\',\'lingx/template/upload/upload.jsp?cmpId=uploadEl_'+json.name+'\');">附件上传</button>');
	});
	//对话框列表单选
	var dialogoptions=$(".lingx-wf-dialogoption");
	$.each(dialogoptions,function(ind,obj){
		var el=$(obj);
		var valEl=$("<input type='hidden' />");
		var btn=el.parent().find("button");
		var etype=el.attr("refEntity");
		var id=el.prop("id");
		el.after(valEl);
		valEl.prop("id",el.prop("id")+"_val");
		valEl.prop("name",el.prop("name"));
		el.prop("name",el.prop("name")+"_txt");
		
		btn.bind("click",function(){
			openWindow("选择框",Lingx.urlAddParams("e?e="+etype+"&m=grid&cmpId="+id,{}));
		});
		if(el.prop("disabled")){
			btn.prop("disabled",true);
			el.prop("disabled",false);
		}
	});
	//对话框列表多选
	var dialogoption2s=$(".lingx-wf-dialogoption2");
	$.each(dialogoption2s,function(ind,obj){
		var el=$(obj);
		var valEl=$("<input type='hidden' />");
		var btn=el.parent().find("button");
		var etype=el.attr("refEntity");
		var id=el.prop("id");
		el.after(valEl);
		valEl.prop("id",el.prop("id")+"_val");
		valEl.prop("name",el.prop("name"));
		el.prop("name",el.prop("name")+"_txt");
		
		btn.bind("click",function(){
			openWindow("选择框",Lingx.urlAddParams("e?e="+etype+"&m=combogrid2&cmpId="+id,{text:encodeURI(encodeURI(el.val())),value:valEl.val()}));
		});
		if(el.prop("disabled")){
			btn.prop("disabled",true);
			el.prop("disabled",false);
		}
	});
	//对话框树型单选
	var dialogtrees=$(".lingx-wf-dialogtree");
	$.each(dialogtrees,function(ind,obj){
		var el=$(obj);
		var valEl=$("<input type='hidden' />");
		var btn=el.parent().find("button");
		var etype=el.attr("refEntity");
		var id=el.prop("id");
		el.after(valEl);
		valEl.prop("id",el.prop("id")+"_val");
		valEl.prop("name",el.prop("name"));
		el.prop("name",el.prop("name")+"_txt");
		
		btn.bind("click",function(){
			openWindow("选择框",Lingx.urlAddParams("e?e="+etype+"&m=tree&cmpId="+id,{}));
		});
		if(el.prop("disabled")){
			btn.prop("disabled",true);
			el.prop("disabled",false);
		}
	});
	//对话框树型多选
	var dialogtree2s=$(".lingx-wf-dialogtree2");
	$.each(dialogtree2s,function(ind,obj){
		var el=$(obj);
		var valEl=$("<input type='hidden' />");
		var btn=el.parent().find("button");
		var etype=el.attr("refEntity");
		var id=el.prop("id");
		el.after(valEl);
		valEl.prop("id",el.prop("id")+"_val");
		valEl.prop("name",el.prop("name"));
		el.prop("name",el.prop("name")+"_txt");
		
		btn.bind("click",function(){
			openWindow("选择框",Lingx.urlAddParams("e?e="+etype+"&m=combotree2&cmpId="+id,{text:encodeURI(encodeURI(el.val())),value:valEl.val()}));
		});
		if(el.prop("disabled")){
			btn.prop("disabled",true);
			el.prop("disabled",false);
		}
	});
}
//lingxSet({cmpId:cmpId,text:txt,value:val});
function lingxSet(options){
	$("#"+options.cmpId).val(options.text);
	$("#"+options.cmpId+"_val").val(options.value);
	if(options.cmpId.indexOf("upload")==0){
		 //处理下载标签
		 handlerDownloadTag();
	}
}
function formatDate(temp){
	
	return temp.substring(0,4)+"-"+temp.substring(4,6)+"-"+temp.substring(6,8)+" "+temp.substring(8,10)+":"+temp.substring(8,10);
}
function jsonRet(json){
	if(json.code==1){
		lgxInfo(json.message);
		if(json.needUser){
			openUserDialog(true,function(value){
				$.post(handlerJsp,{c:"setApprover",taskId:json.taskId,userId:value},function(json){},"json");
			});
		}
	}else if(json.code==2){
		window.location.href="w?m=form&is_mobile=false&_TASK_ID="+json._TASK_ID;
	}else if(json.code==3){
		alert(json.message);
		if(json.needUser){
			openUserDialog(true,function(value){
				$.post(handlerJsp,{c:"setApprover",taskId:json.taskId,userId:value},function(json){
					alert("操作成功");
					reloadParentWindow();
					window.close();
				},"json");
			});
		}else{
			reloadParentWindow();
			window.close();
		}
		
	}else{
		alert(json.message);
	}
}
$(function () {
	  $('[data-toggle="tooltip"]').tooltip();
	});
function lingxClose(){
	if(confirm("请确认数据已保存后再关闭，确定关闭吗？")){
		reloadParentWindow();
		window.close();
	}
}

function lingxReload(){
	window.location.reload();
}
function lingxComment(){
	var modal=$(".bs-sign-modal-sm");
	var group=modal.find(".list-group");
	group.empty();
	group.append($("<li class='list-group-item'><b>任务评论</b></li>"));
	var conts=$("<li class='list-group-item'><textarea class='opintion_content' style='width:500px;height:200px;'/><span style='color:red;'>*</span><br/><span style='font-size:12px;'>常用短语：<a style='font-size:12px;' href='javascript:;'>1. 同意，按规定办理 </a>;<a style='font-size:12px;' href='javascript:;'>2. 按流程办理</a>; <a style='font-size:12px;' href='javascript:;'>3. 交相关部门处理</a></span></li>");
	conts.find("a").bind("click",function(){
		var el=$(this);
		$(".opintion_content").val(el.text().substring(2));
	});
	group.append(conts);
	var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>确定</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
	btns.find(".ok").bind("click",function(){
		var cont=$(".opintion_content").val();
		$.post("w",{m:"comment",_TASK_ID:taskId,content:cont},function(json){
			jsonRet(json);
		},"json");
	});
	btns.find(".cancel").bind("click",function(){
		modal.modal("hide");
	});
	group.append(btns);
	modal.modal("show");
}

function reloadParentWindow(){
	if(window.opener && !window.opener.closed&&window.opener.reloadGrid) {
		window.opener.reloadGrid();
	}
}
//处理上传控件的下载标签
function handlerDownloadTag(){
	var els=$(".lingx-wf-upload-filepath");
	$.each(els,function(ind,obj){
		var el=$(obj);
		if(el.val()){
			var as=el.parent().find(".lingx-wf-upload-download");
			as.remove();
			//el.after("<a target='_blank' href='"+el.val()+"' class='lingx-wf-upload-download' style='color:red;'>附件下载</a><br/>");
			var array=$.parseJSON(el.val());
			for(var i=0;i<array.length;i++){
				el.after("<li  class='lingx-wf-upload-download'><a target='_blank' href='"+array[i].value+"'>点击下载《"+array[i].text+"》</a></li>");
			}
			
		}
	});
}

function openUserDialog(isMulti,callback){
	var modal=$(".bs-sign-modal-sm");
	var group=modal.find(".list-group");
	group.empty();
	group.append($("<li class='list-group-item'><iframe class='selectUsers' src='lingx/common/select_users.jsp?multi="+isMulti+"' style='border:0px none;width:550px;height:332px;padding:0px;margin:0px;'></iframe></li>"));
	var btns=$("<li class='list-group-item' style='text-align:right;'>  <button type='button' class='btn btn-success ok'>确定</button> <button type='button' class='btn btn-default cancel'>取消</button> </li>");
	btns.find(".ok").bind("click",function(){
		var ifr=$(".selectUsers");
		var val=ifr[0].contentWindow.getValue();
		callback(val);
	});
	btns.find(".cancel").bind("click",function(){
		modal.modal("hide");
	});
	group.append(btns);
	modal.modal("show");
}