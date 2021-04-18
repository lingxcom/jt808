var WorkFlow=function(options){
	options=options||{};

	var moveLeft=250,moveTop=100;
	var _this=this;
	_this.mousePressed=false;//鼠标左键按下
	_this.pointer=0;//0为指针，1为移动，2为任务框，3为连接线
	_this.action=options.action||"";//服务器的处理URL
	_this.mainDiv=options.el||"body";//画图的DIV
	_this.defineId=0;//流程定义ID
	_this.displayLine=true;
	_this.buffer=[];//共用缓存
	_this.setDisplayLine=function(temp){
		_this.displayLine=temp;
	};
	_this.setDefineId=function(id){
		_this.defineId=id;
	};
	_this.getDefineId=function(){
		return _this.defineId;
	};
	/**
	 * 熏置画布
	 */
	_this.reload=function(){//重置流程图
		//_this.defineId=id;
		_this.clearModel();
		$(".task").remove();
		$(".line").remove();
		$(".line_set").remove();
		$(".tag").remove();
		_this.mousePressed=false;
		{
			$.post(_this.action,{c:"getDefineTaskAndLine",id:_this.defineId},function(json){
				//_this.buffer=json;
				//console.log(json);
				$.each(json.tasks,function(index,obj){
					_this.task(obj.id,obj.name,obj.type,obj.top,obj.left,obj.width,obj.height,obj.auth_tag);
				});
				$.each(json.lines,function(index,obj){
					_this.line(obj.id,obj.name,obj.content,obj.top1,obj.left1,obj.top2,obj.left2,obj.source_point,obj.target_point,obj.type);
				});
	
				_this.setPointer(_this.pointer);//启动相应的操作模式
			},"json");
		}
	};
	/**
	 * 根据JSON来重置画布
	 */
	_this.reloadJSON=function(json){
		$.each(json.tasks,function(index,obj){
			_this.task(obj.id,obj.name,obj.type,obj.top-moveTop,obj.left-moveLeft,obj.width,obj.height,obj.auth_tag);
		});
		$.each(json.lines,function(index,obj){
			_this.line(obj.id,obj.name,obj.content,obj.top1-moveTop,obj.left1-moveLeft,obj.top2-moveTop,obj.left2-moveLeft,obj.source_point,obj.target_point,obj.type);
		});

		//_this.setPointer(_this.pointer);//启动相应的操作模式
	};
	/**
	 * 设置指针类型
	 */
	_this.setPointer=function(temp){
		_this.pointer=temp;
		switch(_this.pointer){
		case 0://指针
			_this.startCommonModel();
			break;
		case 1://移动
			_this.startMoveModel();
			break;
		case 2://手动任务
			_this.startTaskModel(3);
			break;
		case 3://前进线
			_this.startLineModel(1);
			break;
		case 4://删除
			_this.startDeleteModel();
			break;
		case 5://开始任务
			_this.startTaskModel(1);
			break;
		case 6://结束任务
			_this.startTaskModel(2);
			break;
		case 7://自动任务
			_this.startTaskModel(4);
			break;
		case 8://动态会签
			_this.startTaskModel(5);
			break;
		case 9://后退连接线
			_this.startLineModel(2);
			break;
		case 10://子流程任务
			_this.startTaskModel(6);
			break;
		}
	};
	/**
	 * 启动任务框编辑模式
	 */
	_this.startTaskModel=function(type){
		_this.clearModel();
		var div=$(_this.mainDiv);
		div.addClass("hand");
		div.bind("click",function(e){
			/*var d=$("<div>");
			d.addClass("task");
			d.addClass("task3");
			d.addClass("ie");
			d.appendTo(div);
			d.offset({top:e.pageY-d.height()/2,left:e.pageX-d.width()/2});
			*/
			$.post(_this.action,{c:"addTask",defineId:_this.defineId,top:e.pageY,left:e.pageX,width:98,height:48,type:type},function(json){
				if(json.code==1)
				_this.reload();
				else
					lgxInfo(json.message);
			},"json");
			
		});
	};
	/**
	 * 清除界面元素
	 */
	_this.clearModel=function(){
		var div=$(_this.mainDiv);
		div.prop("class","");
		div.unbind();
		$(".task").unbind();
		$(".line_set").unbind();
		$(".line").unbind();
		$(_this.mainDiv).unbind();
	};
	/**
	 * 启动一般模式
	 */
	_this.startCommonModel=function(){
		_this.clearModel();
		var div=$(_this.mainDiv);
		$(".task").bind("dblclick",function(){
			var t=$(this);
			
			openWindow("编辑任务","e?e=tlingx_wf_define_task&m=task"+t.attr("ref-type")+"&id="+t.prop("id"));
		});/*.bind("mouseenter",function(){
			var el=$(this);
			var d=$("<div>");
			d.addClass("task-op");
			el.append(d);
			//d.offset({top:el.top,left:el.left});
			d.append("有志者事竟成1");
		}).bind("mouseleave",function(){
			$(".task-op").remove();
		});*/
		$(".line_set").bind("dblclick",function(){
			var t=$(this);
			openWindow("编辑连接线","e?e=tlingx_wf_define_line&m=edit&id="+t.prop("id"));
		});
		$(".line").addClass("hand");
		$(".line").bind("dblclick",function(){
			var t=$(this);
			openWindow("编辑连接线","e?e=tlingx_wf_define_line&m=edit&id="+t.attr("ref-id"));
		});
	};
	/**
	 * 启动移动模式
	 */
	_this.startMoveModel=function(){
		_this.clearModel();
		var div=$(_this.mainDiv);
		$(".task").bind("mouseenter",function(){
			var t=$(this);
			t.addClass("hand");
		});
		$(".task").bind("mousedown",function(){
			_this.mousePressed=true;
			var t=$(this);
			t.addClass("move");
		});
		$(".task").bind("mouseup",function(){
			_this.mousePressed=false;
			var t=$(this);
			t.removeClass("move");
			$.post(_this.action,{c:"saveTask",id:t.prop("id"),top:t.offset().top+t.height()/2,left:t.offset().left+t.width()/2,width:t.width(),height:t.height()},function(json){
				_this.reload();
			},"json");
		});
		$(".task").bind("mousemove",function(e){
			var t=$(this);
			if(_this.mousePressed){
				var o=t.offset();
				t.offset({top:e.pageY-t.height()/2,left:e.pageX-t.width()/2});
			}
		});
		$(".line").addClass("hand");
		$(".line").bind("click",function(){
			openWindow("设置连接点","e?e=tlingx_wf_define_line&m=setpoint&id="+$(this).attr("ref-id"));
		});
	};
	
	/**
	 * 启动连接线模式
	 */
	_this.startLineModel=function(type){
		_this.clearModel();
		$(_this.mainDiv).bind("mouseenter",function(){
			//$(".point").remove();
		});
		//$(".task").addClass("hand");
		$(".task").bind("mouseenter",function(){
			var task=$(this);
			var w=task.width(),h=task.height(),t=task.offset().top,l=task.offset().left;
			var pt=t,pl=l+(w/2);//上
			_this.addPoint(pt-4,pl-2,0,task);
			pt=t+(h/2),pl=l+w;//右
			_this.addPoint(pt-2,pl-2,1,task);
			pt=t+h,pl=l+(w/2);//下
			_this.addPoint(pt-2,pl-2,2,task);
			pt=t+(h/2),pl=l;//左
			_this.addPoint(pt-2,pl-4,3,task);
		});
		$(".task").bind("mouseleave",function(){
			$(".point").remove();
		});
		$(".task").bind("click",function(ev){
			if(_this.mousePressed){
				var t=$(this);
				 var line=$(".lineDraw");
				 //var p1=line.attr("p1");lgxInfo("p1:"+p1);
				 //var p2=line.attr("p2");lgxInfo("p2:"+p2);
				 var startX=line.offset().left,startY=line.offset().top;
				// var l = ev.pageX,  t = ev.pageY;
				 var l=t.offset().left+t.width()/2,t=t.offset().top+t.height()/2;
				 //_this.line(startX,startY,l,t);
				// _this.line(startY,startX,t,l);
				 $.post(_this.action,{defineId:_this.defineId,c:"addLine",top1:startY,left1:startX,top2:t,left2:l,sourcePoint:_this.buffer[0]||0,targetPoint:_this.buffer[1]||0,type:type},function(json){
					 if(json.code==1)
					 _this.reload();
					 else
						 lgxInfo(json.message);
				 },"json");
				 
				 _this.buffer=[];
					_this.mousePressed=false;
					$(".lineDraw").removeClass("lineDraw");
			}else{
				var t=$(this);
				var line=$('<div class="line lineDraw">');
				line.offset({top:t.offset().top+t.height()/2,left:t.offset().left+t.width()/2});
				line.appendTo(_this.mainDiv);
				_this.mousePressed=true;
				
			}
		});
		$(".task").bind("mouseup",function(ev){
			
		});
		$(_this.mainDiv+",.task").bind("mousemove",function(ev){
			
		});
		$(".line").addClass("hand");
		$(".line").bind("click",function(){
			openWindow("设置连接点","e?e=tlingx_wf_define_line&m=setpoint&id="+$(this).attr("ref-id"));
		});
	};
	/**
	 * 启动删除模式
	 */
	_this.startDeleteModel=function(){
		_this.clearModel();
		$(".task").addClass("hand");
		$(".task").bind("click",function(){
			var t=$(this);
			if(confirm("是否删除任务节点["+t.text()+"]？")){
				$.post(_this.action,{c:"deleteTask",id:t.prop("id")},function(json){},"json");
				t.remove();
			}
		});
		$(".line_set").addClass("hand");
		$(".line_set").bind("click",function(){
			var t=$(this);
			if(confirm("是否删除连接线["+t.text()+"]？")){
				$.post(_this.action,{c:"deleteLine",id:t.prop("id")},function(json){},"json");
				t.remove();
				$("."+t.prop("id")).remove();
			}
		});

		$(".line").addClass("hand");
		$(".line").bind("click",function(){
			var t=$(this);
			if(confirm("是否删除连接线["+t.text()+"]？")){
				$.post(_this.action,{c:"deleteLine",id:t.attr("ref-id")},function(json){},"json");
				t.remove();
				$("."+t.attr("ref-id")).remove();
			}
		});
	};
	/**
	 * 画任务框
	 */
	_this.task=function(id,name,type,top,left,width,height,auth_tag){
		var d=$("<div>");
		d.addClass("task");
		d.addClass("task"+type);
		d.addClass("ie");
		d.appendTo(_this.mainDiv);
		d.offset({top:top-height/2,left:left-width/2});
		d.width(width);
		d.height(height);
		d.prop("id",id);
		d.attr("ref-type",type);
		d.text(name);
		if(auth_tag>0){
			var tag=$("<div class='tag'>");
			tag.addClass("ie");
			tag.html(auth_tag);
			tag.appendTo(_this.mainDiv);
			tag.offset({top:top-height/2+2,left:left-width/2+6});
		}
	};
	/**
	 * 画任务框
	 */
	_this.currentTask=function(top,left,width,height){
		top=top-moveTop;
		left=left-moveLeft;
		var d=$("<div>");
		d.addClass("current-task");
		d.addClass("ie");
		//d.addClass("task"+type);
		d.appendTo(_this.mainDiv);
		d.offset({top:top-height/2,left:left-width/2});
		d.width(width);
		d.height(height);
		d.text(name);
	};
	/**
	 * 取出连接线的四个方位
	 */
	_this.getPoint4=function(t,l,w,h,type){
		t=t-h/2;
		l=l-w/2;
		var point4={};
		var pt=t,pl=l+(w/2);//上
		if(type==2)pl=pl+5;
		point4["top"]={top:pt,left:pl};
		
		pt=t+(h/2),pl=l+w;//右
		if(type==2)pt=pt+5;
		point4["right"]={top:pt,left:pl};
		
		pt=t+h,pl=l+(w/2);//下
		if(type==2)pl=pl+5;
		point4["bottom"]={top:pt,left:pl};
		
		pt=t+(h/2),pl=l;//左
		if(type==2)pt=pt+5;
		point4["left"]={top:pt,left:pl};
		
		return point4;
	};
	/**
	 * 根据方位取要连线的点
	 */
	_this.offset=function(top,left,point,type){
		var w=98,h=48;
		var point4=_this.getPoint4(top, left, w, h,type);
		var offset=null;
		switch(point){
		case 0:
			offset=point4["top"];
			break;
		case 1:
			offset=point4["right"];
			break;
		case 2:
			offset=point4["bottom"];
			break;
		case 3:
			offset=point4["left"];
			break;
		default:
			alert("方位不对，不大于3:"+point);
		}
		return offset;
	};
	/**
	 * 画方位点的延伸半线
	 */
	_this.line2=function(offset,point,width,height,id,type){
		var offsetRet=null;
		var line=$('<div class="line">');
		line.appendTo(_this.mainDiv);
		line.addClass(id);
		line.attr("ref-id",id);
		//line.height(Math.abs(offset1.top-offset2.top));
		//line.css("border-left","1px solid #999");
		switch(point){
		case 0://上
			offsetRet={top:offset.top-height,left:offset.left};
			line.offset(offsetRet);
			line.height(height);
			if(type==1)
				line.css("border-left","1px solid #999");
			else
				line.css("border-left","1px solid #00cbfe");
			break;
		case 1://右
			line.offset(offset);
			line.width(width);
			if(type==1)
				line.css("border-top","1px solid #999");
			else
				line.css("border-top","1px solid #00cbfe");
			offsetRet={top:offset.top,left:offset.left+width};
			break;
		case 2://下
			offsetRet={top:offset.top+height,left:offset.left};
			line.offset(offset);
			line.height(height);
			if(type==1)
				line.css("border-left","1px solid #999");
			else
				line.css("border-left","1px solid #00cbfe");
			break;
		case 3://左
			line.width(width);
			if(type==1)
				line.css("border-top","1px solid #999");
			else
				line.css("border-top","1px solid #00cbfe");
			offsetRet={top:offset.top,left:offset.left-width};
			line.offset(offsetRet);
			break;
		default:
			alert("方位不对，不大于3:"+point);
		}

		return offsetRet;
	};
	/**
	 * 画中间连线
	 */
	_this.line3=function(offset1,offset2,id,type){
		var line=$('<div class="line">');
		line.appendTo(_this.mainDiv);
		line.addClass(id);
		line.attr("ref-id",id);
		var h=Math.abs(offset1.top-offset2.top),w=Math.abs(offset1.left-offset2.left);
		if(h>w){
			var so=offset1.top<offset2.top?offset1:offset2;
			line.offset(so);
			line.height(h+1);
			if(type==1)
				line.css("border-left","1px solid #999");
			else
				line.css("border-left","1px solid #00cbfe");
		}else{
			var so=offset1.left<offset2.left?offset1:offset2;
			line.offset(so);
			line.width(w+1);
			if(type==1)
				line.css("border-top","1px solid #999");
			else
				line.css("border-top","1px solid #00cbfe");
		}
	};
	/**
	 * 画箭头
	 */
	_this.line4=function(offset,point,id,type){
		var px=6;
		var line=$('<div class="line">');
		line.addClass(id);
		line.attr("ref-id",id);
		switch(point){
		case 0://上
			offset.top=offset.top-1;
			for(var i=0;i<px;i++){
				var l=line.clone();
				l.appendTo(_this.mainDiv);
				l.offset({top:offset.top-i,left:offset.left-i});
				l.width(i*2+1);
				if(type==1)
					l.css("border-top","1px solid #999");
				else
					l.css("border-top","1px solid #00cbfe");
			}
			break;
		case 1://右
			offset.left=offset.left+2;
			for(var i=0;i<px;i++){
				var l=line.clone();
				l.appendTo(_this.mainDiv);
				l.offset({top:offset.top-i,left:offset.left+i});
				l.height(i*2+1);
				if(type==1)
					l.css("border-left","1px solid #999");
				else
					l.css("border-left","1px solid #00cbfe");
			}
			break;
		case 2://下
			offset.top=offset.top+2;
			for(var i=0;i<px;i++){
				var l=line.clone();
				l.appendTo(_this.mainDiv);
				l.offset({top:offset.top+i,left:offset.left-i});
				l.width(i*2+1);
				if(type==1)
					l.css("border-top","1px solid #999");
				else
					l.css("border-top","1px solid #00cbfe");
			}
			break;
		case 3://左
			offset.left=offset.left-1;
			for(var i=0;i<px;i++){
				var l=line.clone();
				l.appendTo(_this.mainDiv);
				l.offset({top:offset.top-i,left:offset.left-i});
				l.height(i*2+1);
				if(type==1)
					l.css("border-left","1px solid #999");
				else
					l.css("border-left","1px solid #00cbfe");
			}
			break;
		default:
			alert("方位不对，不大于3:"+point);
		}
	};
	/**
	 * 画折线，效率高
	 */
	_this.line=function(id,name,isTip,top1,left1,top2,left2,sourcePoint,targetPoint,type){
		if(!_this.displayLine)return;
		if(top1==top2&&left1==left2)return;
		var offset1=_this.offset(top1,left1,sourcePoint,type),offset2=_this.offset(top2,left2,targetPoint,type);
		var h=Math.abs(offset1.top-offset2.top),w=Math.abs(offset1.left-offset2.left);
		if((sourcePoint+targetPoint)%2==0){
			w=w/2;
			h=h/2;
		}
		var ret1=_this.line2(offset1,sourcePoint,w,h,id,type);
		var ret2=_this.line2(offset2,targetPoint,w,h,id,type);
		_this.line3(ret1,ret2,id,type);
		//_this.line4(offset1,sourcePoint,id);//test
		_this.line4(offset2,targetPoint,id,type);
		
		var tdiv=ret1.top<ret2.top?ret1.top:ret2.top;
		var ldiv=ret1.left<ret2.left?ret1.left:ret2.left;
		tdiv+=Math.abs(ret1.top-ret2.top)/2;
		ldiv+=Math.abs(ret1.left-ret2.left)/2;
		if(isTip){
			//画框line_set
			var d=$("<div>");
			d.appendTo(_this.mainDiv);
			d.addClass("line_set");
			d.addClass("ie");
			d.offset({top:tdiv-10,left:ldiv-20});
			d.text(name);
			d.prop("id",id);
			if(type==2){
				d.css("border","1px solid #00cbfe");
			}
		}
		//if(a>b){//竖线
			
			
		//}else{//横线
			
		//}
	};
	
	/**
	 * 画连接线，用div点描出来的，由于效率太低，停用
	 */
	_this.line_bak=function(id,name,isTip,top1,left1,top2,left2,sourcePoint,targetPoint){
		if(!_this.displayLine)return;
		if(top1==top2&&left1==left2)return;
		var a=Math.abs(top1-top2),b=Math.abs(left1-left2),min,max;
		var x=left1,y=top1,x1=left2,y1=top2,x2,y2,tdiv=0,ldiv=0;
		if(a>b){//以top循环
			min=top1>top2?top2:top1;
			max=top1<top2?top2:top1;
			var half=min+((max-min)/2);
			for(;min<max;min++){
				y2=min;
				x2=(x-x1)/((y-y1)/(y2-y1))+x1;
				var line=$('<div class="line">');
				line.offset({top:y2,left:x2});
				line.addClass(id);
				line.attr("ref-id",id);
				line.appendTo(_this.mainDiv);
				if(min==half){
					tdiv=y2;
					ldiv=x2;
				}
			}
		}else{//以left循环
			min=left2<left1?left2:left1;
			max=left2>left1?left2:left1;
			var half=min+((max-min)/2);
			for(;min<max;min++){
				x2=min;
				y2=(y-y1)/((x-x1)/(x2-x1))+y1;
				var line=$('<div class="line">');
				line.offset({top:y2,left:x2});
				line.addClass(id);
				line.attr("ref-id",id);
				line.appendTo(_this.mainDiv);
				if(min==half){
					tdiv=y2;
					ldiv=x2;
				}
			}
		}
		
		if(isTip){
			//画框line_set
			var d=$("<div>");
			d.appendTo(_this.mainDiv);
			d.addClass("line_set");
			d.addClass("ie");
			d.offset({top:tdiv-10,left:ldiv-20});
			d.text(name);
			d.prop("id",id);
		}
	};
	/**
	 * 描点
	 */
	_this.addPoint=function(top,left,p,task){
		var d=$("<div>");
		d.addClass("point");
		d.addClass("ie");
		d.attr("p",p);
		d.offset({top:top,left:left});
		d.appendTo(task);
		d.hover(
				  function () {
					$(this).removeClass("point");
				    $(this).addClass("point_hover");
				    var o=$(this).offset();
				    $(this).offset({top:o.top-2,left:o.left-2});
				  },
				  function () {
				    $(this).removeClass("point_hover");
				    $(this).addClass("point");
				    var o=$(this).offset();
				    $(this).offset({top:o.top+2,left:o.left+2});
				  }
				);
		d.bind("click",function(){
			var p=$(this).attr("p");
			_this.buffer.push(p);
		});
	};
};