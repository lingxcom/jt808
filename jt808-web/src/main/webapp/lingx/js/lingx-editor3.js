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
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>创建版式<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>普通版式</a></li>").bind("click",function(){
			var text="<ul class='process-info'>"
					  +"<li>"
					  +"<label>请假总天数：</label>"
					  +"<p>1天</p>"
					  +"</li>"
					  +"<li>"
					  +"<label>请假开始时间：</label>"
					  +"<p>2017-03-23 08:30</p>"
					  +"</li>"
					  +"<li>"
					  +"<label>请假结束时间：</label>"
					  +"<p>2017-03-23 17:30</p>"
					  +"</li>"
					  +"<li>"
					  +"<label>理由：</label>"
					  +"<p>测试用的</p>"
					  +"</li>"
					  +"</ul>";
			var ipt=$(text);
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);

		_this.toolbar.append(div);/**/
		//表单结束
		
		//子项开始
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>元素子项<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>内容项li</a></li>").bind("click",function(){
			//var tmp=prompt("输入选择项：");
			var ipt=$("<li>"
					  +"<label>标题：</label>"
					  +"<p>内容</p>"
					  +"</li>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		

		//外部连接
		li=$(" <li><a href='javascript:;'>外部链接 link</a></li>").bind("click",function(){
			var ipt=$("<li><div style='width:100%;height:200px;border:0px none;margin:0px;' class=' mui-input-row lingx-wf-link lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' >外部链接控件（只有启动流程后才能真实展现）</div></li>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		//附件上传
		li=$(" <li><a href='javascript:;'>附件上传 upload</a></li>").bind("click",function(){
			var ipt=$("<li><div style='width:200px;height:40px;border:1px solid #999;margin:5px;' class='lingx-wf-upload lingx-wf-ipt' authCfg='1,2,3,4,5,6,7,8,9' >附件上传控件（只有启动流程后才能真实展现）</div></li>");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		
		_this.toolbar.append(div);
		//子项结束
		
		
		//环境变量
		div=$("<div class='btn-group' role='group'>  <button class='btn btn-success dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>环境变量<span class='caret'></span>  </button>  <ul class='dropdown-menu' aria-labelledby='dropdownMenu1'>     </ul></div>");
		
		li=$(" <li><a href='javascript:;'>流程发起人</a></li>").bind("click",function(){
			var ipt="${_USER_NAME}";
			//_this.addId(ipt);
			//_this.bindClick(ipt);
			_this.currentEl.append(ipt);
			//_this.currentEl.html(_this.currentEl.html()+ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		li=$(" <li><a href='javascript:;'>流程发起部门</a></li>").bind("click",function(){
			var ipt=$("${_ORG_NAME}");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//<div class='well'>...</div>
		li=$(" <li><a href='javascript:;'>流程发起时间</a></li>").bind("click",function(){
			var ipt=$("${_START_TIME_F}");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		//
		li=$(" <li><a href='javascript:;'>流程标题</a></li>").bind("click",function(){
			var ipt=$("${_TITLE}");
			_this.addId(ipt);
			_this.bindClick(ipt);
			_this.currentEl.append(ipt);
		});
		div.find(".dropdown-menu").append(li);
		
		_this.toolbar.append(div);
		//
		li=$(" <li><a href='javascript:;'>当前年份</a></li>").bind("click",function(){
			var ipt=$("${_YEAR}");
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
		el.find("div,td,span,input,textarea,select,option,li,p").bind("click",function(ev){
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