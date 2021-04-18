var selectedtree=function(){
	var selected = {
			$this : null,
			item_item : {},
			item_chose : {},
			/*
			 * 初始化方法:init(id,items,right_fn,title,now_selected) id:返回对象的id item：左侧树
			 * eg:{text:"tree",value:"0",child:[{value:'1',text:'节点1'},{value:'1',text:'节点2'}]}
			 * right_fn：获取右侧列表方法 eg:function(id){ var ret={value:"id",text:"这是结果"}
			 * alert("这是左侧选中节点的id"); return ret; } title:标题 now_selected：当前选中项
			 * 
			 */
			init : function(title, items, right_fn,  now_selected){
				this.item_item.children=items || [];
				if (this.$this == null) {
					$("body").append(this.html(title, now_selected));
					this.$this = $("body").find(".select-card:last");
					this.mask = mui.createMask(function() {
						$("body").find(".select-card:last").addClass("mui-hidden");
					});
					$t = this;
					this.$this.find(".left").find("ul").on("click", "li", function() {
						$t.leftClick(this);
					});
					this.$this.find(".right").find("ul").on("touchend", "li", function() {
						$t.rightClick(this);
					});
					$(document).on("touchend", "#sub", function() {
						$t.submit();
					});
					$(document).on("touchend", "#req", function() {
						$t.mask.close();
					});
					this.$this.find("#selected-box").on("tap","div",function(){
						$("#"+$(this).attr("val")).find(".chose-active").removeClass("chose-active");
						$(this).remove();
					})
					$(document).on("touchend", "#d_now_select", function() {
						var i = $t.way.length;
						if (i > 0) {
							$t.way.splice(i - 1, 1)
						}
						$t.leftList();
						var val=$t.item_item;
						for (x in $t.way) {
							val = val.children[$t.way[x]];
						}
						$t.putRight(val.value||0);
					});
				} else {
					this.$this.find(".title").find("span").html(title);
				}
				this.leftList(this.item_item);
				if (right_fn != null) this.rightList = right_fn;
				this.$this.find(".left").find("ul").find("li:first").click();
//				alert(this.rightList);
//				this.show();
			},
			mask : null,
			show : function(fn,now_selected) {
				var n =now_selected || [];
				var selected="";
				for (x in n) {
					selected += this.item(n[x].value, n[x].text);
				}
				if(n.length>0)this.$this.find("#selected-box").html(selected);
				this.mask.show();// 显示遮罩
				this.submitfn=fn;
				$(this.$this).removeClass("mui-hidden");
			},
			html : function(title, now_selected) {
				var t = title || "多选选择器";
				var n = now_selected || [];
				var selected = "";
				for (x in n) {
					selected += this.item(n[x].value, n[x].text);
				}
				return "<div class=\"select-card mui-hidden\">"
						+ "<div class=\"top\">"
						+ "<div class='title'>"
						+"<a id='req' type=\"button\" class=\"mui-btn mui-btn-danger\">取消</a><span>"
						+ title
						+"</span><a id='sub' type=\"button\" class=\"mui-btn mui-btn-primary\">确定</a>"
						+ "</div>"
						+ "<div id='selected-box' class=\"selected-box\">"
						+ selected
						+ "</div>"
						+ "</div>"
						+ "<div class=\"body\">"
						+ "<div class=\"now_select\" id='d_now_select'>root</div>"
						+ "<div class=\"left\"><ul class=\"mui-table-view\"></ul></div>"
						+ "<div class=\"right\"><ul class=\"mui-table-view\"></ul></div>"
						+ "</div></div>";
			},
			item : function(value, text) {
				return "<div class='selected' val='" + value + "'>" + text + "</div>"
			},
			liItem : function(value, text) {
				return "<li class=\"mui-table-view-cell\" id='" + n[x].value
						+ "'><a class=\"mui-navigate-right\">" + n[x].text
						+ "</a></li>";
			},
			leftList : function() {
				var val = this.item_item;
				if(this.way.length>0){
					for (x in this.way) {
						val = val.children[this.way[x]];
					}
					this.$this.find(".now_select").html(val.text || "root")
				}else{
					this.$this.find(".now_select").html("<img style='height:24px;margin-top:10px;' src='data:image/png;base64,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'>");
				}
				var n = val.children || [];
				var ret = "";
				for (x in n) {
					ret += "<li class=\"mui-table-view-cell\" id='" + n[x].value
							+ "'><a class=\"mui-navigate-right\">" + n[x].text
							+ "</a></li>";
				}
				this.$this.find(".left").find("ul").html(ret);
			},
			rightList : function(id) {
				var ret = [ {
					value : "",
					text : "无"
				} ];
				return ret;
			},
			putRight : function(id) {
				var h = "";
				this.$this.find(".right").find("ul").html("<li class=\"mui-table-view-cell\"><a class=\"mui-navigate-right\">无</a></li>");
				var l = this.rightList(id);
				if(l!=null&&l.length>0){
					for (x in l) {
						
						h += "<li class=\"mui-table-view-cell\" id='" + l[x].value
								+ "'><a class=\"mui-navigate-right "+is_chose+"\">" + l[x].text
								+ "</a></li>";
					}
					this.$this.find(".right").find("ul").html(h);
				}else{
//					h += "<li class=\"mui-table-view-cell\"><a class=\"mui-navigate-right\">无</a></li>";
				} 
			},
			pushRight : function(l) {
				var h = "";
				this.$this.find(".right").find("ul").html("<li class=\"mui-table-view-cell\"><a class=\"mui-navigate-right\">加载中...</a></li>");
				if(l!=null&&l.length>0){
					for (x in l) {
						var is_chose="";
						this.$this.find("#selected-box").find("div[val='"+l[x].value+"']").each(function(){
							is_chose="chose-active";
						});
						h += "<li class=\"mui-table-view-cell\" id='" + l[x].value
								+ "'><a class=\"mui-navigate-right "+is_chose+"\">" + l[x].text
								+ "</a></li>";
					}
				}else{
					h += "<li class=\"mui-table-view-cell\"><a class=\"mui-navigate-right\">无</a></li>";
				} 
				this.$this.find(".right").find("ul").html(h);
			},
			leftClick : function(obj) {
				var id = $(obj).parent().find("li").index(obj);
				this.putRight($(obj).attr("id"));
				this.way.push(id);
				this.leftList();
			},
			rightClick : function(obj) {
				if($(obj).attr("id")!=null&&$(obj).attr("id")!=""&&this.$this.find("#selected-box").find("div[val='"+$(obj).attr("id")+"']").length==0){
					$(obj).find("a").addClass("chose-active");
					this.$this.find("#selected-box").append(this.item($(obj).attr("id"),$(obj).text()));
				}else{
					$(obj).find("a").removeClass("chose-active");
					this.$this.find("#selected-box").find("div[val='"+$(obj).attr("id")+"']").remove();
				}
			},
			way : [],
			submitfn:null,
			submit:function(fn){
				var ret=[];
				this.$this.find("#selected-box").find("div").each(function(){
					ret.push({value:$(this).attr("val"),text:$(this).text()});
				})
				
				this.mask.close();
				this.submitfn(ret);
			}
		};
	return selected;
}
﻿