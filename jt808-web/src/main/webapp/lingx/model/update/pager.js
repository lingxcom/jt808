
/**
 * options.id avalon 所需要的$id
 * options.total 总记录数
 * options.rows 行数
 * options.callback
 */
var Pager=function(options){
	var _this=this;
	_this.callback=options.callback||function(){};
	_this.model=avalon.define({
		$id:options.id,
		currentPage:3,
		rows:10,
		totalRecord:100,
		totalPage:10,
		list:[],
		liPageNums:3,
		init:function(options){
			_this.model.reset(options);
			_this.model.currentPage=1;
			
		},
		jump:function(page){
			_this.callback.call(_this,page,_this.model.rows);
			_this.model.currentPage=page;
			_this.model.refresh();
			//alert(page);
		},
		prev:function(){
			if(_this.model.currentPage-1<1)return;
			_this.model.jump(_this.model.currentPage-1);
		},
		next:function(){
			if(_this.model.currentPage+1>_this.model.totalPage)return;
			_this.model.jump(_this.model.currentPage+1);
		},
		refresh:function(){
			_this.model.list=[];
			var ll=new Array();
			var cp=_this.model.currentPage;
			for(var i=_this.model.liPageNums;i>0;i--){
				ll.push(cp-i);
			}
			ll.push(cp);
			for(var i=1;i<=_this.model.liPageNums;i++){
				ll.push(cp+i);
			}
			while(ll[0]<1){
				for(var i=0;i<ll.length;i++){
					ll[i]=ll[i]+1;
				}
			}
			while(ll[ll.length-1]>_this.model.totalPage){
				for(var i=0;i<ll.length;i++){
					ll[i]=ll[i]-1;
				}
			}
			for(var i=0;i<ll.length;i++){
				if(ll[i]>=1&&ll[i]<=_this.model.totalPage){
					_this.model.list.push(ll[i]);
				}
			}
		},
		/**
		 * options.total 总记录数
		 * options.rows 每页记录数
		 */
		reset:function(options){
			_this.model.rows=options.rows||_this.model.rows;
			_this.model.totalRecord=options.total||0;
			_this.model.totalPage=_this.model.totalRecord%_this.model.rows==0?_this.model.totalRecord/_this.model.rows:parseInt(_this.model.totalRecord/_this.model.rows+1);
			_this.model.refresh();
		}
	});
	_this.getModel=function(){return _this.model;};
	_this.model.init(options);
};