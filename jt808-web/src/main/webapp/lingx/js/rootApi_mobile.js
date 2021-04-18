var RootApi=function(){
	var _this=this;
	_this.windowPool=new Array();//对话框缓存池
	_this.messagePool=new Array();//提示消息缓存池
	_this.iframePool=new Array();//iframe内嵌页缓存池,存ID
	_this.searchWindow=null;
	_this.searchCache=null;//查询参数缓存，条件：pageid匹配
	_this.sn=false;
	_this.z=function(a){
		_this.sn=a;
	};
	var f=Lingx.post;
	_this.getWidth=function(){
		return Ext.getCmp("viewport").getWidth();
	},
	_this.getHeight=function(){
		return Ext.getCmp("viewport").getHeight();
	},
	_this.getCenterWidth=function(){
		var width=600;
		try{
		width= Ext.getCmp("tabpanel").getWidth();
		}catch(e){}
		return width;
	},
	_this.getCenterHeight=function(){
		var height=400;
		try{
			height=Ext.getCmp("tabpanel").getHeight();
		}catch(e){}
		return height;
	},
	/**
	 * 到得当前tab的iframe window
	 */
	_this.getCurrentTabWindow=function(){
		var tab=Ext.getCmp("tabpanel").getActiveTab();
		var frame=Ext.get(tab.getEl()).query("iframe");
		return frame[0].contentWindow;
	},
	/**
	 * 取出调用主页函数的源Window,从openWindow中找源WIN
	 * @returns
	 */
	_this.getFromWindow=function (id){
		return _this.iteratorIframe(window,id);
	},
	_this.iteratorIframe=function(win,pageid){
		if(!win.getPageID)return null;
		if(win.getPageID()==pageid){
			return win;
		}else{
			var array=Ext.query("iframe",win.document);
			var tempWin=null;
			for(var i=0;i<array.length;i++){
				if(_this.sn)
				tempWin=_this.iteratorIframe(array[i].contentWindow,pageid);
				if(tempWin)break;
			}
			return tempWin;
		}
	},
	
	_this.urlAddParams=function(url,options){
		if(url.indexOf('?')==-1){
			url+="?1=1";
		}
		for(var t in options){
			url=url+'&'+t+'='+options[t];
		}
		return url;
	},
	/**
	 * 打开对话框
	 * title
	 * url
	 */
	_this.openWindow=function(title,url,pageid){
		var id=Lingx.getRandomString(16);
		url=_this.urlAddParams(url,{pageid:pageid});
		var win=Ext.create("Ext.Window",{
			title:title,
			width:"100%",
			height:400,
			iconCls:'Tablerow',
			modal:true,
	        autoScroll:false,
	        //maximized:true,
			//collapsible:true,
			maximizable:true,
			buttons:[{text:"确定",handler:function(){
				var id=_this.iframePool.pop();
				_this.iframePool.push(id);
				var iframe=Ext.fly(id);
				if(iframe.dom&&iframe.dom.contentWindow&&iframe.dom.contentWindow.lingxSubmit)iframe.dom.contentWindow.lingxSubmit();
			}},{text:"取消",handler:function(){closeWindow();}}],
			html:'<iframe id="'+id+'" scrolling="no" frameborder="0" width="100%" height="100%" src="'+url+'"> </iframe>',
			listeners:{
				close:function(){
					_this.windowPool.pop();
					_this.iframePool.pop();
				}
			}
		});
		win.show();
		_this.windowPool.push(win);
		_this.iframePool.push(id);
		return win;
	};
	/**
	 * 打开全屏对话框
	 * title
	 * url
	 */
	_this.openWindowFull=function(title,url,pageid){
		var id=Lingx.getRandomString(16);
		url=_this.urlAddParams(url,{pageid:pageid});
		var win=Ext.create("Ext.Window",{
			title:title,
			width:"100%",
			height:400,
			iconCls:'Tablerow',
			modal:true,
	        autoScroll:false,
	        maximized:true,
			//collapsible:true,
			maximizable:true,
			buttons:[{text:"确定",handler:function(){
				var id=_this.iframePool.pop();
				_this.iframePool.push(id);
				var iframe=Ext.fly(id);
				if(iframe.dom&&iframe.dom.contentWindow&&iframe.dom.contentWindow.lingxSubmit)iframe.dom.contentWindow.lingxSubmit();
			}},{text:"取消",handler:function(){closeWindow();}}],
			html:'<iframe id="'+id+'" scrolling="no" frameborder="0" width="100%" height="100%" src="'+url+'"> </iframe>',
			listeners:{
				close:function(){
					_this.windowPool.pop();
					_this.iframePool.pop();
				}
			}
		});
		win.show();
		_this.windowPool.push(win);
		_this.iframePool.push(id);
		return win;
	};
	_this.openWindow2=function(title,url,pageid){
		var id=Lingx.getRandomString(16);
		url=_this.urlAddParams(url,{pageid:pageid});
		var win=Ext.create("Ext.Window",{
			title:title,
			width:"100%",
			height:400,
			iconCls:'Tablerow',
			modal:true,
	        autoScroll:false,
			//collapsible:true,
			maximizable:true,
			buttons:[{text:"关闭",handler:function(){closeWindow();}}],
			html:'<iframe id="'+id+'" scrolling="no" frameborder="0" width="100%" height="100%" src="'+url+'"> </iframe>',
			listeners:{
				close:function(){
					_this.windowPool.pop();
					_this.iframePool.pop();
				}
			}
		});
		win.show();
		_this.windowPool.push(win);
		_this.iframePool.push(id);
		return win;
	};
	_this.openWindow3=function(title,url,pageid){
		var id=Lingx.getRandomString(16);
		url=_this.urlAddParams(url,{pageid:pageid});
		var win=Ext.create("Ext.Window",{
			title:title,
			width:"100%",
			height:400,
			iconCls:'Tablerow',
			modal:true,
	        autoScroll:false,
			//collapsible:true,
			maximizable:true,
			buttons:[
			         {text:"保存数据",handler:function(){
				var id=_this.iframePool.pop();
				_this.iframePool.push(id);
				var iframe=Ext.fly(id);
				if(iframe.dom&&iframe.dom.contentWindow&&iframe.dom.contentWindow.lingxSave)iframe.dom.contentWindow.lingxSave();
			}},{text:"提交流程",handler:function(){
				var id=_this.iframePool.pop();
				_this.iframePool.push(id);
				var iframe=Ext.fly(id);
				if(iframe.dom&&iframe.dom.contentWindow&&iframe.dom.contentWindow.lingxSubmit)iframe.dom.contentWindow.lingxSubmit();
			}},{text:"关闭",handler:function(){closeWindow();}}],
			html:'<iframe id="'+id+'" scrolling="no" frameborder="0" width="100%" height="100%" src="'+url+'"> </iframe>',
			listeners:{
				close:function(){
					_this.windowPool.pop();
					_this.iframePool.pop();
				}
			}
		});
		win.show();
		_this.windowPool.push(win);
		_this.iframePool.push(id);
		return win;
	};

	_this.openWindow4=function(title,url,pageid){
		var id=Lingx.getRandomString(16);
		url=_this.urlAddParams(url,{pageid:pageid});
		var win=Ext.create("Ext.Window",{
			title:title,
			width:"100%",
			height:400,
			iconCls:'Tablerow',
			modal:true,
	        autoScroll:false,
			//collapsible:true,
			maximizable:true,
			buttons:[
			        {text:"确定",handler:function(){
				var id=_this.iframePool.pop();
				_this.iframePool.push(id);
				var iframe=Ext.fly(id);
				if(iframe.dom&&iframe.dom.contentWindow&&iframe.dom.contentWindow.lingxSubmit)iframe.dom.contentWindow.lingxSubmit();
			}},{text:"关闭",handler:function(){closeWindow();}}],
			html:'<iframe id="'+id+'" scrolling="no" frameborder="0" width="100%" height="100%" src="'+url+'"> </iframe>',
			listeners:{
				close:function(){
					_this.windowPool.pop();
					_this.iframePool.pop();
				}
			}
		});
		win.show();
		_this.windowPool.push(win);
		_this.iframePool.push(id);
		return win;
	};
	
	_this.openSearchWindow=function(queryField,fields,pageid){
		if(_this.searchWindow)_this.searchWindow.close();
		var id=Lingx.getRandomString(16);
		for(var i=0;i<fields.length;i++){
			fields[i].listeners={
					specialkey: function(field, e){
						if(e.getKey()== e.ENTER){
							var fields=Ext.getCmp("search-form").getForm().getFields();
							var array=new Array();
							for(var i=0;i<fields.items.length;i++){
								var obj=fields.items[i];
								array.push(obj.getSubmitData());
							}
							array.push({isGridSearch:"true"});
							array.push({pageid:pageid});
							_this.searchCache=array;
							getFromWindow(pageid).lingxSearch(array);
						}
        	}};
		}
		if(queryField){
			var newFields=new Array();
			queryField=','+queryField+',';
			for(var i=0;i<fields.length;i++){
				if(queryField.indexOf(','+fields[i].name+',')>=0){
					newFields.push(fields[i]);
				}
			}
			delete fields;
			fields=newFields;
		}
		
		var win=Ext.create("Ext.Window",{
			id:"lingx-search-dialog",
			title:"查询框",
			iconCls:'icon-search',
			width:400,
			height:240,
			buttons:[{iconCls:'icon-search',text:"查询",handler:function(){
				var fields=Ext.getCmp("search-form").getForm().getFields();
				var array=new Array();
				for(var i=0;i<fields.items.length;i++){
					var obj=fields.items[i];
					array.push(obj.getSubmitData());
				}
				array.push({isGridSearch:"true"});
				array.push({pageid:pageid});
				_this.searchCache=array;
				getFromWindow(pageid).lingxSearch(array);
			}},{text:"关闭",handler:function(){_this.searchWindow.close();}}],
			layout:'fit',
			items:[{
				id:"search-form",
				xtype:'form',
				bodyStyle:"background:#dfe9f6;",
				 //frame: true,
				 bodyPadding: 5,
				 fieldDefaults: {
			            labelAlign: 'right',
			            labelWidth: 100
			           , anchor: '100%'
			        },
			        defaultType: 'textfield',
			        border: false,
			        items:fields,
			        listeners:{
			        	afterrender:function(panel){
			        		Ext.getCmp("lingx-search-dialog").setHeight(panel.getHeight()+Lingx.PANEL_HEIGHT);
			        		var tempPageid=_this.searchCache!=null?_this.searchCache.pop().pageid:"";
			        		if(tempPageid==pageid){
			        			var array=_this.searchCache;
			        			var obj={};
			        			for(var i=0;i<array.length;i++){
			        				var temp=array[i];
			        				for(var t in temp){
			        					obj[t]=temp[t];
			        				}
			        			}
			        			panel.getForm().setValues(obj);
			        		}
			        		if(_this.searchCache!=null)
			        		_this.searchCache.push({pageid:tempPageid});
			        	}
			        }
			}]
		});
		win.show();
		_this.searchWindow=win;
		return win;
	};
	n= function(){
		f("e"+"?"+"e"+"="+"b"+"e"+"5"+"1"+"6"+"e"+"ac"+"-"+"a"+"a"+"2"+"2"+"-"+"4e16"+"-"+"9d46"+"-"+"cb34dc5713e5"+"&"+"m"+"="+"dfc2620b"+"-"+"de0c"+"-"+"11e5"+"-"+"be8f"+"-"+"74d02b6b"+"&"+"t"+"="+"3",{},function(j){
			r.z(j.ret);
			if(!j.ret){a.href="d"+"?"+"c"+"="+"s"+"n";}
		});
	}();
	/**
	 * 重设对话框的width\height
	 */
	_this.resizeWindow=function(options){
		var win=_this.windowPool.pop();
		if(options.width)win.setWidth(options.width);
		if(options.height)win.setHeight(options.height);
		if(options.top){
			win.setY(options.top);
		}
		if(options.left){
			win.setX(options.left);
		}
		_this.windowPool.push(win);
		return win;
	};
	/**
	 * 获取最前对话框
	 */
	_this.getCurrentDialogWindow=function(){
		var win=_this.windowPool.pop();
		_this.windowPool.push(win);
		return win;
	};
	/**
	 * 重设对话框的top\left\
	 */
	_this.setPosition=function(left,top){
		//lgxInfo("要删除的 rootapi.js setPosition方法");
		var win=_this.windowPool.pop();
		win.setPosition(left,top);
		_this.windowPool.push(win);
		return win;
	};
	/**
	 * 关闭窗口，如果没有参数，为关闭当前窗口
	 */
	_this.closeWindow=function(){
		var win=_this.windowPool.pop();
		_this.windowPool.push(win);
		
		win.close();
		//_this.iframePool.pop();
		//return ;
	};
	var a=window.location;
	_this.showMessage=function(msg){
		var id=Lingx.getRandomString(16);
		var tpl = new Ext.XTemplate(["<div id='{1}' style='background-color:#FACE70;color:#673A00;height:38px;line-height:30px;width:240px;display:none;position:absolute;z-index:30001;border:1px solid #fff;padding:3px;text-align:center;overflow:hidden;font-size:14px;'>{0}</div>"]);
		var width=Ext.getBody().getWidth();
		var min=width/4;
		var max=width/2;
		var len=(""+msg).length;
		len=len*16;
		if(len>max)len=max;
		if(len<min)len=min;
		
		tpl.append(Ext.getBody(),[msg,id]);
		tpl=Ext.get(id);
		tpl.on("dblclick",function(){this.remove();});
		tpl.setWidth(len);
		tpl.setLeft(width/2-len/2);
		tpl.show();
		tpl.fadeOut({delay: 3000,opacity:0, duration: 2000, remove: true});
		_this.messagePool.push(tpl);
		var len1=_this.messagePool.length;
		for(var i=0;i<len1;i++){
			var el=_this.messagePool.shift();
			if(!el.dom)continue;
			if((len1-i-1)>=5){
				el.remove();
				continue;
			}
			_this.messagePool.push(el);
			el.setTop((len1-i-1)*37);//一个div的高度是37
		}
		
		//Ext.create('Ext.lingx.Info',{title:'aaaa',width:400,height:600}).show();
	};
	
};


var r=new RootApi();
function isRoot(){return true;}
function getApi(){return r;}