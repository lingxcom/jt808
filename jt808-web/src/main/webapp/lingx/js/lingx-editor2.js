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
		
		//表单开始
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>创建表单<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>普通表单</a></li>").bind("click",function(){
			var text="<div class='mui-content'>"
					  +"<div class='mui-content-padded' style='margin: 5px;'>"
					  +"	<div class='mui-input-group'>"
					  +"		<div class='mui-input-row'>"
					  +"			<label>Input</label>"
					  +"			<input type='text' class='mui-input-clear lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' placeholder='Input'>"
					  +"		</div>"
					  +"		<div class='mui-input-row'>"
					  +"			<label>Input</label>"
					  +"			<input type='text' class='mui-input-clear lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' placeholder='Input'>"
					  +"		</div>"
					  +"	</div>"
					  +"</div>"
					  +"</div>";
			var ipt=$(text);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);

		_this.toolbar.append(div);/**/
		//表单结束
		//控件开始
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>常用控件<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>只读</a></li>").bind("click",function(){
			var html="<div class='mui-input-row' style='line-height:40px;'>"
			  +"			<label>Label</label>"
			  +"			Label"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>文本</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
			  +"			<label>Input</label>"
			  +"			<input type='text' class='mui-input-clear lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' placeholder='Input'>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>文本域</a></li>").bind("click",function(){
			var html="<div class='mui-input-row' style='margin: 10px 5px;'>"
			  +"			<textarea class='lingx-wf-ipt' rows='5' authCfg='1,2,3,4,5,6,7,8,9' placeholder='textarea'></textarea>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);
		

		li=$(" <li><a href='javascript:;'>选择框</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
			  +"			<label>Select</label>"
			  +"			<select class='lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9'><option value=''>请选择</option></select>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);
		
		
		li=$(" <li><a href='javascript:;'>用户单选</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
			  +"			<label>User</label>"
			  +"			<input  type='text' class='mui-input-clear lingx-wf-ipt lingx-wf-user' readonly='readonly' authCfg='1,2,3,4,5,6,7,8,9' placeholder='User'/>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>用户多选</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
			  +"			<label>Users</label>"
			  +"			<input  type='text' class='mui-input-clear lingx-wf-ipt lingx-wf-users' readonly='readonly' authCfg='1,2,3,4,5,6,7,8,9' placeholder='Users'/>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>组织单选</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
			  +"			<label>Org</label>"
			  +"			<input  type='text' class='mui-input-clear lingx-wf-ipt lingx-wf-org' readonly='readonly' authCfg='1,2,3,4,5,6,7,8,9' placeholder='Org'/>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);
		
		//外部连接
		li=$(" <li><a href='javascript:;'>外部链接 link</a></li>").bind("click",function(){
			var ipt=$("<span><div style='width:100%;height:200px;border:0px none;margin:0px;' class=' mui-input-row lingx-wf-link lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' >外部链接控件（只有启动流程后才能真实展现）</div></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		

		//附件上传
		li=$(" <li><a href='javascript:;'>附件上传 upload</a></li>").bind("click",function(){
			var ipt=$("<span><div style='width:200px;height:40px;border:1px solid #999;margin:5px;' class='lingx-wf-upload lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' >附件上传控件（只有启动流程后才能真实展现）</div></span>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		_this.toolbar.append(div);
		//控件结束
		//对话框开始
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>对话框<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		//对话框列表选择[单选]
		li=$(" <li><a href='javascript:;'>对话框列表选择[单选]</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
				  +"			<label>Dialog</label>"
				  +"<input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='mui-input-clear  lingx-wf-dialogoption lingx-wf-ipt' readonly placeholder='Dialog' />"
				  +"</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//对话框列表选择[多选]
		li=$(" <li><a href='javascript:;'>对话框列表选择[多选]</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
				  +"			<label>Dialog</label>"
				  +"<input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='mui-input-clear  lingx-wf-dialogoption2 lingx-wf-ipt' readonly placeholder='Dialog' />"
				  +"</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		//对话框树型选择[单选]
		li=$(" <li><a href='javascript:;'>对话框树型选择[单选]</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
				  +"			<label>Dialog</label>"
				  +"<input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='mui-input-clear  lingx-wf-dialogtree lingx-wf-ipt' readonly placeholder='Dialog' />"
				  +"</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//对话框树型选择[多选]
		li=$(" <li><a href='javascript:;'>对话框树型选择[多选]</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
				  +"			<label>Dialog</label>"
				  +"<input type='text' authCfg='1,2,3,4,5,6,7,8,9' class='mui-input-clear lingx-wf-dialogtree2 lingx-wf-ipt' readonly placeholder='Dialog'/>"
				  +"</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		_this.toolbar.append(div);
		//时间控件
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>时间控件<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>日期</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
			  +"			<label>Date</label>"
			  +"			<input  type='text' class='mui-input-clear lingx-wf-ipt lingx-wf-date' readonly='readonly' data-options='{\"type\":\"date\"}' authCfg='1,2,3,4,5,6,7,8,9' placeholder='Date'/>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);
		
		
		li=$(" <li><a href='javascript:;'>日期时间</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
			  +"			<label>DateTime</label>"
			  +"			<input  type='text' class='mui-input-clear lingx-wf-ipt lingx-wf-datetime' readonly='readonly' data-options='{\"type\":\"datetime\"}' authCfg='1,2,3,4,5,6,7,8,9' placeholder='DateTime'/>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>时间</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
			  +"			<label>Time</label>"
			  +"			<input  type='text' class='mui-input-clear lingx-wf-ipt lingx-wf-time' readonly='readonly' data-options='{\"type\":\"time\"}' authCfg='1,2,3,4,5,6,7,8,9' placeholder='Time'/>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);

		li=$(" <li><a href='javascript:;'>月份</a></li>").bind("click",function(){
			var html="<div class='mui-input-row'>"
			  +"			<label>Month</label>"
			  +"			<input  type='text' class='mui-input-clear lingx-wf-ipt lingx-wf-month' readonly='readonly' data-options='{\"type\":\"month\"}' authCfg='1,2,3,4,5,6,7,8,9' placeholder='Month'/>"
			  +"		</div>";
			var ipt=$(html);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);

		});
		div.find(".dropdown-menu").append(li);

		_this.toolbar.append(div);

		//时间控件结束
		//子项开始
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>元素子项<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>选项 option</a></li>").bind("click",function(){
			var tmp=prompt("输入选择项：");
			var ipt=$("<option>"+tmp+"</option> ");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		_this.toolbar.append(div);
		//子项结束
		

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
		el.find("div,td,span,input,textarea,select,option,li,label").bind("click",function(ev){
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
				json:el.attr("json")||"",
				placeholder:el.attr("placeholder")||""
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