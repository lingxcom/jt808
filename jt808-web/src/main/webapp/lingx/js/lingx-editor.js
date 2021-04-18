var LingxEditor=function(){
	var _this=this;
	_this.mainPanel=$("#editor");
	_this.toolbar=$("<div class='btn-group'>");
	_this.c=$("<div id='c' contenteditable='true'>");
	_this.footer=$("<div>");
	_this.textarea=$("<textarea class='textarea'>");
	_this.currentEl=null;//当前选中的标签
	_this.cacheEl=null;//缓存，在复制、剪切、粘贴中的缓存
	_this.init=function(){
		var div,li;
		_this.mainPanel.append(_this.toolbar);

		//表格开始

		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>表格<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		li=$(" <li><a href='javascript:;'>插入表格</a></li>").bind("click",function(){
			var ipt=$("<table border=1 width='100%' class='table table-bordered'></table>");
			var rc=prompt("请输入行数与列数，以,隔开。例如：3,4");
			var array=(rc||"3,4").split(",");
			var newTr=null;
			if(array.length!=2)return;
			for(var i=0;i<array[0];i++){
				newTr=$("<tr>");
				for(var j=0;j<array[1];j++){
					newTr.append("<td>&nbsp;</td>");
				}
				ipt.append(newTr);
			}
			
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
			
		});
		div.find(".dropdown-menu").append(li);
		li=$(" <li><a href='javascript:;'>向右合并</a></li>").bind("click",function(){
			if(_this.currentEl[0].nodeName=="TD"){
				var ntd=_this.currentEl.next();
				if(ntd[0].nodeName=="TD"){
					var html=_this.currentEl.html()+ntd.html();
					ntd.remove();
					var colspan=_this.currentEl.prop("colspan")||1;
					_this.currentEl.prop("colspan",colspan+1);
					_this.currentEl.html(html);
				}
			}
		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>向下合并</a></li>").bind("click",function(){
			if(_this.currentEl[0].nodeName=="TD"){
				var ind=(_this.currentEl.index());
				var ntr=_this.currentEl.parent().next();
				if(ntr[0].nodeName=="TR"){
					var ntd=ntr.find(":eq("+ind+")");
					if(ntd[0].nodeName=="TD"){
						var html=_this.currentEl.html()+ntd.html();
						ntd.remove();
						var rowspan=_this.currentEl.prop("rowspan")||1;
						_this.currentEl.prop("rowspan",rowspan+1);
						_this.currentEl.html(html);
					}
				}
				
			}
		});
		div.find(".dropdown-menu").append(li);
		li=$(" <li><a href='javascript:;'>插入行</a></li>").bind("click",function(){
			var tr=_this.currentEl.parent();
			var tds=tr.children("td");
			var tdNum=0;
			for(var i=0;i<tds.length;i++){
				tdNum+=($(tds[i]).prop("colspan")||1);
			}
			var newTr=$("<tr>");
			for(var i=0;i<tdNum;i++){
				newTr.append("<td>&nbsp;</td>");
			}
			_this.addId(newTr);
			_this.bindClick(newTr);
			tr.parent().append(newTr);
		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>插入列</a></li>").bind("click",function(){
			var trs=_this.currentEl.parent().parent().children("tr");
			for(var i=0;i<trs.length;i++){
				var newTd=$("<td>&nbsp;</td>");
				_this.addId(newTd);
				_this.bindClick(newTd);
				$(trs[i]).append(newTd);
			}
		});
		div.find(".dropdown-menu").append(li);
		
		_this.toolbar.append(div);
		//表格结束
		//输入框开始
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>控件<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		/*li=$(" <li><a href='javascript:;'>文本框 bootstarp</a></li>").bind("click",function(){
			var ipt=$("<div class='input-group'>  <span class='input-group-addon'>Name</span>  <input type='text' class='form-control'>  <span class='input-group-addon'>.00</span></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);*/
		
		li=$(" <li><a href='javascript:;'>文本框 text</a></li>").bind("click",function(){
			var ipt=$("<span><input type='text'  class='lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' /></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>文本域 textarea</a></li>").bind("click",function(){
			var ipt=$("<span><textarea  class='lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' /> </span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);


		li=$(" <li><a href='javascript:;'>选择器 select</a></li>").bind("click",function(){
			var ipt=$("<span> <select class='lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9'><option value=''>请选择</option> </select> </span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		

		li=$(" <li><a href='javascript:;'>选项 option</a></li>").bind("click",function(){
			var tmp=prompt("输入选择项：");
			var ipt=$("<option>"+tmp+"</option> ");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		

		/*li=$(" <li><a href='javascript:;'>签章</a></li>").bind("click",function(){
			var id="S"+_this.getRandomString(9);
			var ipt=$("<span><input name='"+id+"' type='hidden' value='lingx/images/sign.png' /><img id='"+id+"' class='lingx-wf-sign lingx-wf-ipt' src='lingx/images/sign.png'></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.bindField(ipt);//域事件绑定
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);*/

		//用户单选
		li=$(" <li><a href='javascript:;'>用户单选 user</a></li>").bind("click",function(){
			var ipt=$("<span><input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='lingx-wf-user lingx-wf-ipt' readonly /></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//用户多选
		li=$(" <li><a href='javascript:;'>用户多选 users</a></li>").bind("click",function(){
			var ipt=$("<span><textarea style='height:30px;' type='text' authCfg='1,2,3,4,5,6,7,8,9' class='lingx-wf-users lingx-wf-ipt' readonly /></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//组织单选
		li=$(" <li><a href='javascript:;'>组织单选 org</a></li>").bind("click",function(){
			var ipt=$("<span><input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='lingx-wf-org lingx-wf-ipt' readonly /></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//组织多选
		li=$(" <li><a href='javascript:;'>组织多选 orgs</a></li>").bind("click",function(){
			var ipt=$("<span><textarea style='height:30px;' type='text' authCfg='1,2,3,4,5,6,7,8,9' class='lingx-wf-orgs lingx-wf-ipt' readonly /></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//外部连接
		li=$(" <li><a href='javascript:;'>外部链接 link</a></li>").bind("click",function(){
			var ipt=$("<span><div style='width:600px;height:400px;border:1px solid #999;margin:5px;' class='lingx-wf-link lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' >外部链接控件（只有启动流程后才能真实展现）</div></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//列表编辑
/*		li=$(" <li><a href='javascript:;'>列表编辑 list</a></li>").bind("click",function(){
			var ipt=$("<span><div style='width:600px;height:400px;border:1px solid #999;margin:5px;' class='lingx-wf-list lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' >列表编辑控件（只有启动流程后才能真实展现）</div></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		*/
		//附件上传
		li=$(" <li><a href='javascript:;'>附件上传 upload</a></li>").bind("click",function(){
			var ipt=$("<span><div style='width:200px;height:40px;border:1px solid #999;margin:5px;' class='lingx-wf-upload lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' >附件上传控件（只有启动流程后才能真实展现）</div></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		/*
		li=$(" <li><a href='javascript:;'>输入域</a></li>").bind("click",function(){
			var ipt=$("<span contenteditable='false' class='ipt'><span class='s'>[</span><span class='v' contenteditable='true'>XXXX</span><span contenteditable='true' class='e'>]</span></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.bindField(ipt);//域事件绑定
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);*/

/*		li=$(" <li><a href='javascript:;'>选择域</a></li>").bind("click",function(){
			var ipt=$("<span contenteditable='false' class='ipt'><span class='s'>[</span><span class='v' contenteditable='true'>XXXX</span><span contenteditable='true' class='e'>]</span></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);*/
		
		_this.toolbar.append(div);
		//输入框结束
		//对话框开始
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>对话框<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		//对话框列表选择[单选]
		li=$(" <li><a href='javascript:;'>对话框列表选择[单选]</a></li>").bind("click",function(){
			var ipt=$("<span><input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='lingx-wf-dialogoption lingx-wf-ipt' readonly /><button type='button' >选择</button></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//对话框列表选择[多选]
		li=$(" <li><a href='javascript:;'>对话框列表选择[多选]</a></li>").bind("click",function(){
			var ipt=$("<span><input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='lingx-wf-dialogoption2 lingx-wf-ipt' readonly /><button type='button' >选择</button></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		//对话框树型选择[单选]
		li=$(" <li><a href='javascript:;'>对话框树型选择[单选]</a></li>").bind("click",function(){
			var ipt=$("<span><input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='lingx-wf-dialogtree lingx-wf-ipt' readonly /><button type='button' >选择</button></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//对话框树型选择[多选]
		li=$(" <li><a href='javascript:;'>对话框树型选择[多选]</a></li>").bind("click",function(){
			var ipt=$("<span><input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='lingx-wf-dialogtree2 lingx-wf-ipt' readonly /><button type='button' >选择</button></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		_this.toolbar.append(div);
		
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>时间<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
/*		li=$(" <li><a href='javascript:;'>bootstarp yyyy-MM-dd</a></li>").bind("click",function(){
			var ipt=$("<div class='input-group date form_date' data-date='' data-date-format='yyyy-mm-dd' data-link-field='dtp_input2' data-link-format='yyyy-mm-dd'>                    <input class='form-control' size='16' type='text' value='' readonly>                    <span class='input-group-addon'><span class='glyphicon glyphicon-remove'></span></span>					<span class='input-group-addon'><span class='glyphicon glyphicon-calendar'></span></span>                </div>         <input type='hidden' id='dtp_input2' value='' />");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
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
		});
		div.find(".dropdown-menu").append(li);*/
		
		li=$(" <li><a href='javascript:;'>日期 yyyy-MM-dd</a></li>").bind("click",function(){
			var ipt=$("<span> <input type='text' class='form_date lingx-wf-ipt' readonly='readonly' authCfg='1,2,3,4,5,6,7,8,9' data-date-format='yyyy-mm-dd'> </span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
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
		});
		div.find(".dropdown-menu").append(li);
		
/*		li=$(" <li><a href='javascript:;'>bootstarp yyyy-MM-dd HH:mm</a></li>").bind("click",function(){
			var ipt=$("<div class='input-group date form_datetime' data-date='' data-date-format='yyyy-mm-dd' data-link-field='dtp_input2' data-link-format='yyyy-mm-dd'>                    <input class='form-control' size='16' type='text' value='' readonly>                    <span class='input-group-addon'><span class='glyphicon glyphicon-remove'></span></span>					<span class='input-group-addon'><span class='glyphicon glyphicon-calendar'></span></span>                </div>         <input type='hidden' id='dtp_input2' value='' />");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
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
		});
		div.find(".dropdown-menu").append(li);*/
		
		li=$(" <li><a href='javascript:;'>日期时间 yyyy-MM-dd HH:mm</a></li>").bind("click",function(){
			var ipt=$("<span> <input type='text' class='form_datetime lingx-wf-ipt' readonly='readonly' authCfg='1,2,3,4,5,6,7,8,9' data-date-format='yyyy-mm-dd hh:ii'> </span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
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
		});
		div.find(".dropdown-menu").append(li);
		
		_this.toolbar.append(div);
		//时间结束
		//面板开始
		/*div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>面板<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>内容</a></li>").bind("click",function(){
			var ipt=$("<div class='panel panel-info'> <div class='panel-body'>    content  </div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);

		li=$(" <li><a href='javascript:;'>标题/内容</a></li>").bind("click",function(){
			var ipt=$("<div class='panel panel-info'>  <div class='panel-heading'>title</div>  <div class='panel-body'>    content  </div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>标题/内容/脚注</a></li>").bind("click",function(){
			var ipt=$("<div class='panel panel-info'>  <div class='panel-heading'>title</div>  <div class='panel-body'>    content  </div><div class='panel-footer'>footer</div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		_this.toolbar.append(div);*/
		//面板结束
		//分栏开始
		/*div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>手机专用<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>两栏</a></li>").bind("click",function(){
			var ipt=$("<div class='row'>  <div class='col-lg-6'>        content    </div>    <div class='col-lg-6'>       content    </div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		li=$(" <li><a href='javascript:;'>三栏</a></li>").bind("click",function(){
			var ipt=$("<div class='row'>    <div class='col-lg-4'>       content    </div>    <div class='col-lg-4'>        content    </div>    <div class='col-lg-4'>       content    </div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		li=$(" <li><a href='javascript:;'>四栏</a></li>").bind("click",function(){
			var ipt=$("<div class='row'>    <div class='col-lg-3'>       content    </div>    <div class='col-lg-3'>       content    </div>    <div class='col-lg-3'>        content    </div>    <div class='col-lg-3'>       content    </div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		li=$(" <li><a href='javascript:;'>六栏</a></li>").bind("click",function(){
			var ipt=$("<div class='row'>    <div class='col-lg-2'>       content    </div>    <div class='col-lg-2'>       content    </div>        <div class='col-lg-2'>       content    </div>    <div class='col-lg-2'>       content    </div>    <div class='col-lg-2'>        content    </div>    <div class='col-lg-2'>       content    </div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>文本框 bootstarp</a></li>").bind("click",function(){
			var ipt=$("<div class='input-group'>  <span class='input-group-addon'>Name</span>  <input type='text' class='form-control'>  <span class='input-group-addon'>.00</span></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		

		li=$(" <li><a href='javascript:;'>内容</a></li>").bind("click",function(){
			var ipt=$("<div class='panel panel-info'> <div class='panel-body'>    content  </div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);

		li=$(" <li><a href='javascript:;'>标题/内容</a></li>").bind("click",function(){
			var ipt=$("<div class='panel panel-info'>  <div class='panel-heading'>title</div>  <div class='panel-body'>    content  </div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>标题/内容/脚注</a></li>").bind("click",function(){
			var ipt=$("<div class='panel panel-info'>  <div class='panel-heading'>title</div>  <div class='panel-body'>    content  </div><div class='panel-footer'>footer</div></div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		_this.toolbar.append(div);*/
		//分栏结束
		//<div class='dropdown'>  <button class='btn btn-default dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>    Dropdown    <span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>    <li><a href='#'>Action</a></li>  </ul></div>
		//表单开始
		/*div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>表单<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>普通表单</a></li>").bind("click",function(){
			var ipt=$("<form class='navbar-form' >  <div class='form-group'>    content  </div></form>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);

		_this.toolbar.append(div);*/
		//表单结束
		//提示框
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>提示框<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>success</a></li>").bind("click",function(){
			var ipt=$("<div class='alert alert-success' role='alert'>...</div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		li=$(" <li><a href='javascript:;'>info</a></li>").bind("click",function(){
			var ipt=$("<div class='alert alert-info' role='alert'>...</div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		li=$(" <li><a href='javascript:;'>warning</a></li>").bind("click",function(){
			var ipt=$("<div class='alert alert-warning' role='alert'>...</div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		li=$(" <li><a href='javascript:;'>danger</a></li>").bind("click",function(){
			var ipt=$("<div class='alert alert-danger' role='alert'>...</div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		_this.toolbar.append(div);
		//提示框结束
		
		//列表开始
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>列表<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>列表</a></li>").bind("click",function(){
			var ipt=$("<ul class='list-group'>  <li class='list-group-item'>Item</li></ul>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);

		li=$(" <li><a href='javascript:;'>子项</a></li>").bind("click",function(){
			var ipt=$("<li class='list-group-item'>Item</li>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);

		_this.toolbar.append(div);
		//列表结束
		
		//其它开始
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>其他<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>数字提示</a></li>").bind("click",function(){
			var ipt=$("<span class='badge'>0</span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//<div class='well'>...</div>
		li=$(" <li><a href='javascript:;'>内容Well</a></li>").bind("click",function(){
			var ipt=$("<div class='well'>...</div>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//图标
		li=$(" <li><a href='javascript:;'>图标</a></li>").bind("click",function(){
			var ipt=$(" <span class='glyphicon glyphicon-search' aria-hidden='true'></span> ");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//红*
		li=$(" <li><a href='javascript:;'>红色标记*</a></li>").bind("click",function(){
			var ipt=$("<span style='color:red;'>*</span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		_this.toolbar.append(div);
		//标题样式
		li=$(" <li><a href='javascript:;'>标题样式</a></li>").bind("click",function(){
			_this.currentEl.css("font-size","20px");
			_this.currentEl.css("line-height","40px");
			_this.currentEl.css("text-align","center");
			_this.currentEl.css("font-weight","bold");//font-weight:bold;
		});
		div.find(".dropdown-menu").append(li);
		_this.toolbar.append(div);
		//其它结束
		//环境变量
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>环境变量<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>流程发起人</a></li>").bind("click",function(){
			var ipt=$("<span>${_USER_NAME}</span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>流程发起部门</a></li>").bind("click",function(){
			var ipt=$("<span>${_ORG_NAME}</span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//<div class='well'>...</div>
		li=$(" <li><a href='javascript:;'>流程发起时间</a></li>").bind("click",function(){
			var ipt=$("<span>${_START_TIME_F}</span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//
		li=$(" <li><a href='javascript:;'>流程标题</a></li>").bind("click",function(){
			var ipt=$("<span>${_TITLE}</span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		_this.toolbar.append(div);
		//
		li=$(" <li><a href='javascript:;'>当前年份</a></li>").bind("click",function(){
			var ipt=$("<span>${_YEAR}</span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		_this.toolbar.append(div);
		//环境变量
		//_this.mainPanel.append("<hr>");

		_this.mainPanel.append(_this.footer);
		_this.mainPanel.append(_this.c);
		
		_this.footer.addClass("footer");
		
		_this.currentEl=_this.c;
		_this.bindClick(_this.c);
	};
	_this.bindClick=function(el){
		el.bind("click",function(ev){
			ev.stopPropagation(); 
			var _el=$(this);
			_this.displayLayout(_el);
			_this.currentEl=$(this);
		});
		el.find("div,td,span,input,textarea,select,option,li").bind("click",function(ev){
			ev.stopPropagation(); 
			var _el=$(this);
			_this.displayLayout(_el);
			_this.currentEl=$(this);
		});
		el.find("input,.lingx-wf-ipt").bind("click",function(ev){
			var el=$(this);
			inputId=el.prop("id");
			Ext.getCmp('propertyGrid').setSource({
				name:el.prop("name"),
				authType:el.attr("authType")||true,
				authCfg:el.attr("authCfg")||"",
				notView:el.attr("notView")||"",
				notNull:el.attr("notNull")||false,
				refEntity:el.attr("refEntity")||"",
				url:el.attr("url")||"",
				json:el.attr("json")||""
				});			
		});
	};
	/**
	 * 为新增元素加上ID，如果已经有ID则不变
	 */
	_this.addId=function(el){
		var els=$(el).find("*");
		
		$.each(els,function(ind,obj){
			if(!obj.id){
				obj.id=_this.getRandomString(10);
			}
		});
		if(el[0]&&!el[0].id){
			el[0].id=_this.getRandomString(10);
		}
	};
	_this.getRandomString=function (num){
		var temp="1234567890qwertyuiopasdfghjklzxcvbnmASDFGHJKLQWERTYUIOPZXCVBNM";
		var ret="";
		for(var i=0;i<num;i++){
			ret+=temp.charAt(temp.length*Math.random());
		}
		return ret;
	};
	/**
	 * 展示结构层次
	 */
	_this.displayLayout=function(el){
		var c=0;
		var temp="";
		if(!el[0]||el[0].id=='c'){
			//
		}else{
			do{
				temp="<a href='javascript:;' ref-id='"+el[0].id+"' >"+ el[0].nodeName+"</a>"+">"+temp;
				el=el.parent();
				c++;
			}while(el.prop("id")!='c'&&c<100);
		}
		temp="<a href='javascript:;' ref-id='c' >MAIN</a>>"+temp;
		temp="<div>"+temp.substring(0, temp.length-1)+"</div>";
		var link=$(temp);
		_this.footer.empty();
		_this.footer.append(link);
		link.find("a").bind("click",function(){
			//下边导航，单击开始
			var _a=$(this);
			//编辑栏
			var tip=$("<div class='footer-tip'>");
			tip.appendTo("body");
			//tip.offset(_a.offset());
			tip.offset({top:_a.offset().top-12,left:_a.offset().left});
			tip.append($("<a href='javascript:;' ref-id='"+_a.attr("ref-id")+"'>定位</a>").bind("click",function(){
				var tempEl=$(this);
				_this.currentEl=$("#"+tempEl.attr("ref-id"));
				_this.displayLayout(_this.currentEl);
				$(".footer-tip").remove();
			}));
			tip.append(" | ");
			tip.append($("<a href='javascript:;' ref-id='"+_a.attr("ref-id")+"'>样式</a>").bind("click",function(){
				var tempEl=$(this);
				var targetEl=$("#"+tempEl.attr("ref-id"));
				var css=prompt("请输入CSS，以:与;隔开");
				if(!css)return;
				var cssArray=css.split(";");
				$.each(cssArray,function(ind,obj){
					if(obj){
					var arr=obj.split(":");
					if(arr.length==2){
						lgxInfo("样式:"+arr[0]+"->"+arr[1]);
						targetEl.css(arr[0],arr[1]);
					}
					}
				});
				//lgxInfo(css);
			}));
			tip.append(" | ");
			tip.append($("<a href='javascript:;' ref-id='"+_a.attr("ref-id")+"'>属性</a>").bind("click",function(){
				var tempEl=$(this);
				var targetEl=$("#"+tempEl.attr("ref-id"));
				var css=prompt("请输入属性，以:与;隔开");
				if(!css)return;
				var cssArray=css.split(";");
				$.each(cssArray,function(ind,obj){
					if(obj){
					var arr=obj.split(":");
					if(arr.length==2){
						lgxInfo("属性:"+arr[0]+"->"+arr[1]);
						targetEl.prop(arr[0],arr[1]);
					}
					}
				});
			}));
			tip.append(" | ");
			tip.append($("<a href='javascript:;' ref-id='"+_a.attr("ref-id")+"'>源码</a>").bind("click",function(){
				var tempEl=$(this);
				//alert($("#"+tempEl.attr("ref-id")).html());
				_this.currentEl=$("#"+tempEl.attr("ref-id"));
				_this.displayLayout(_this.currentEl);
				$(".footer-tip").remove();
				openWindow("HTML编辑","lingx/workflow/form/textarea.jsp");
			}));
			tip.bind("mouseleave",function(){
				$(this).remove();
			});
			tip.append(" <br> ");
			tip.append($("<a href='javascript:;' ref-id='"+_a.attr("ref-id")+"'>复制</a>").bind("click",function(){
				var tempEl=$(this);
				_this.cacheEl=$("#"+tempEl.attr("ref-id")).clone(true);
				
			}));
			tip.append(" | ");
			tip.append($("<a href='javascript:;' ref-id='"+_a.attr("ref-id")+"'>剪切</a>").bind("click",function(){
				var tempEl=$(this);
				_this.cacheEl=$("#"+tempEl.attr("ref-id"));
				_this.cacheEl.remove();
			}));
			tip.append(" | ");
			tip.append($("<a href='javascript:;' ref-id='"+_a.attr("ref-id")+"'>粘贴</a>").bind("click",function(){
				if(_this.cacheEl==null)return;
				var tempEl=$(this);
				var targetEl=$("#"+tempEl.attr("ref-id"));
				targetEl.append(_this.cacheEl);
				_this.cacheEl=null;
			}));
			tip.append(" | ");
			tip.append($("<a href='javascript:;' ref-id='"+_a.attr("ref-id")+"'>删除</a>").bind("click",function(){
				var tempEl=$(this);
				$("#"+tempEl.attr("ref-id")).remove();
				_this.currentEl=_this.c;
				_this.displayLayout(_this.currentEl);
				$(".footer-tip").remove();
			}));
			tip.bind("mouseleave",function(){
				$(this).remove();
			});
			var targetEl=$("#"+_a.attr("ref-id"));
			//下边导航，单击结束
		}).bind("mouseenter",function(){
			var _a=$(this);
			var shadow=$("<div class='shadow'>");
			var target=$("#"+_a.attr("ref-id"));
			shadow.appendTo("body");
			shadow.offset(target.offset());
			shadow.width(target.outerWidth(true));
			shadow.height(target.outerHeight(true));
		}).bind("mouseleave",function(){
			$(".shadow").remove();
		});
	};

	_this.bindFieldAll=function(ipt){
		var els=ipt.find(".ipt");
		$.each(els,function(ind,obj){
			_this.bindField($(obj));
		});
	};
	_this.bindField=function(ipt){
		ipt.find(".e").bind("keyup",function(){
			var el=$(this);
			var tmp=el.text();
			if(tmp!=']'){
				el.text(']');
				el.parent().after(tmp.substring(1));
				//.focus();
				_this.getC(el.parent());
				//alert(el.parent().html().length);
				//console.log(el.parent());
				//window.getSelection().setSelectionRange(el.parent().html().length);
			}
		});
		ipt.bind("focus",function(){
		});
		ipt.find(".v").bind("click",function(ev){
			//console.log(ev);
		});
		ipt.find(".v").bind("keyup",function(ev){
			var el=$(this);
			var ipt=$("<span contenteditable='false' class='ipt'><span class='s'>[</span><span class='v' contenteditable='true'>"+el.text()+"</span><span contenteditable='true' class='e'>]</span></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.bindField(ipt);//域事件绑定
			el.parent().after(ipt);
			ipt.find(".v").focus();
			el.parent().remove();
		});
		ipt.bind("mouseover",function(){
			
		});
	};
	_this.getContent=function(){
		return _this.c.html();
	};
	
	_this.setContent=function(content){
		var ipt=$(content);
		_this.addId(ipt);
		_this.bindClick(ipt);
		_this.bindFieldAll(ipt);
		_this.c.empty();
		_this.c.append(ipt);
	};
	_this.getCurrentContent=function(){
		return _this.currentEl.html();
	};
	_this.setCurrentContent=function(content){
		var ipt=$(content);
		_this.addId(ipt);
		_this.bindClick(ipt);
		_this.bindFieldAll(ipt);
		_this.currentEl.empty();
		_this.currentEl.append(ipt);
	};
	_this.getC=function (that){  
        if(document.all){  
            that.range=document.selection.createRange();  
            that.range.select();  
            that.range.moveStart("character",-1);   
        }else{  
            that.range=window.getSelection().getRangeAt(0);  
            that.range.setStart(that.range.startContainer,1);  
        }  
	}; 
};