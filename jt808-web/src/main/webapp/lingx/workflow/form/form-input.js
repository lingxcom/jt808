$(function(){
	var inputs=$(".lingx-wf");
	$.each(inputs,function(ind,obj){
		var ipt=$(obj);
		var editor=(ipt.attr("leditor"));
		if(editor&&editor!="text"){
			LF.get(editor).create(obj);
		}
	});
});

var LingxForm=function(){
	var _this=this;
	_this.inputs=[];//输入控件
	_this.values={};
	_this.reg=function(obj){//注册控件
		_this.inputs.push(obj);
	};
	_this.get=function(key){
		var tmp=new Empty();
		for(var i=0;i<_this.inputs.length;i++){
			if(key==_this.inputs[i].xtype){
				tmp=_this.inputs[i];
			}
		}
		return tmp;
	};
	_this.setValues=function(values){
		_this.values=values;
	};
	_this.getValue=function(key){
		return _this.values[key];
	};
};
var LF=new LingxForm();
var Empty=function(){
	this.create=function(el){};
};
var TextField=function(){
	var _this=this;
	_this.xtype="text";
	_this.create=function(el){
		var newInput=$("<input type='text'>");
		var oldInput=$(el);
		newInput.prop("name",oldInput.attr("lname"));
		newInput.width(oldInput.attr("lwidth")||200);

		oldInput.after(newInput);
		oldInput.remove();
	};
};
var TextareaField=function(){
	var _this=this;
	_this.xtype="textarea";
	_this.create=function(el){
		var newInput=$("<textarea>");
		var oldInput=$(el);
		newInput.prop("name",oldInput.attr("lname"));
		newInput.width(oldInput.attr("lwidth")||400);
		newInput.height(oldInput.attr("lheight")||100);
		
		oldInput.after(newInput);
		oldInput.remove();
	};
};
var DateField=function(){
	var _this=this;
	_this.xtype="datefield";
	_this.create=function(el){
		var newInput=$(el);
		newInput.datetimepicker({format: newInput.attr("lparam")});
	};
};
var RadioField=function(){
	var _this=this;
	_this.xtype="radio";
	_this.radioText="<input id='{id}' type='radio' name='{code}' {checked} value='{value}'  />";
	_this.labelText="<label for='{id}'>{text}</label> ";
	_this.create=function(el){
		var oldInput=$(el);
		var newInput=$("<div>");
		var name=oldInput.attr("lname");
		$.post("e?e=tlingx_option&m=items&lgxsn=1&code="+oldInput.attr("lparam"),{},function(json){
			var temp="";
			for(var i=0;i<json.length;i++){
 		    	   var record=json[i];
	 		    	   var id=record.value+"_radio_"+Lingx.getRandomString(8);
	 		    	  temp=temp+(_this.radioText.format({id:id,code:name,checked:record.value==LF.getValue(name)?"checked":"",value:record.value})+_this.labelText.format({id:id,text:record.text}));

			}
		},"json");
	};
};
LF.reg(new TextField());
LF.reg(new TextareaField());
LF.reg(new DateField());
LF.reg(new RadioField());