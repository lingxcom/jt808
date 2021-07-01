
var pageId=Lingx.getRandomString(64);
/**
 * 格式化时间
 */
function formatTime(value){
	return (value.substring(0,4)+"-"+value.substring(4,6)+"-"+value.substring(6,8)+" "+value.substring(8,10)+":"+value.substring(10,12)+":"+value.substring(12,14));
}
/**
 * 获取页面ID
 * @returns {___anonymous_pageId}
 */
function getPageID(){
	if(!pageId){
		pageId=Lingx.getRandomString(64);
	}
	return pageId;
}
/**
 * 得到页面宽度
 * @returns
 */
function getRootWidth(){
	return Lingx.getRootWindow().getApi().getWidth();
}
/**
 * 得到页面高度
 * @returns
 */
function getRootHeight(){
	return Lingx.getRootWindow().getApi().getHeight();
}
/**
 * 得到工作区域的宽度
 */
function getCenterWidth(){
	return Lingx.getRootWindow().getApi().getCenterWidth();
}
/**
 * 得到工作区域的高度
 */
function getCenterHeight(){
	return Lingx.getRootWindow().getApi().getCenterHeight();
}
/**
 * 打开单个数据的展示窗口
 * @param ecode 对象代码
 * @param ename 对象名称
 * @param eid 对象ID
 */
function openViewWindow(ecode,ename,eid,type){
	openWindow2(ename+"-查看","e?e="+ecode+"&m=view&id="+eid,type);//原本是openWindow4
}
/**
 * 得到当前工作区域的TAB窗口对象
 */
function getCurrentTabWindow(){
	return Lingx.getRootWindow().getApi().getCurrentTabWindow();
}
/**
 * 获得源页面的对象，通常在对话框中会调用
 * @param fromPageId
 */
function getFromWindow(fromPageId){
	return Lingx.getRootWindow().getApi().getFromWindow(fromPageId);
}
/**
 * 打开对话框，有“确定”和“取消”按钮
 * @param title
 * @param url
 */
function openWindow(title,url,type){
	type=type||"default";
	switch(type){
	case "default":
		Lingx.getRootWindow().getApi().openWindow(title,url,getPageID());
		break;
	case "full1":
		openWindowFull(title,url);//这个是EXT的
		break;
	case "full2":
		openFullWindow(url);//这个是原生的
		break;
	case "readonly":
		Lingx.getRootWindow().getApi().openReadonlyWindow(title,url,getPageID());
		break;
	}
}
/**
 * 打开只读的对话框
 * @param title
 * @param url
 */
function openReadonlyWindow(title,url,type){
	type=type||"default";
	switch(type){
	case "default":
		Lingx.getRootWindow().getApi().openReadonlyWindow(title,url,getPageID());
		break;
	case "full1":
		Lingx.getRootWindow().getApi().openReadonlyWindowFull(title,url,getPageID());
		break;
	}
}
function openWindowFull(title,url){
	Lingx.getRootWindow().getApi().openWindowFull(title,url,getPageID());
}
/**
 * 打开对话框，只有“关闭”按钮
 * @param title
 * @param url
 */
function openWindow2(title,url){
	Lingx.getRootWindow().getApi().openWindow2(title,url,getPageID());
}
/**
 * 打开对话框，有“提交”、“确定”、“关闭”按钮
 * @param title
 * @param url
 */
function openWindow3(title,url){
	Lingx.getRootWindow().getApi().openWindow3(title,url,getPageID());
}
/**
 * 打开对话框，有“确定”、“关闭”按钮
 * @param title
 * @param url
 */
function openWindow4(title,url){
	Lingx.getRootWindow().getApi().openWindow4(title,url,getPageID());
}
/**
 * 打开查询对话框
 * @param queryField
 * @param fields
 */
function openSearchWindow(queryField,fields){
	Lingx.getRootWindow().getApi().openSearchWindow(queryField,fields,getPageID());
}
/**
 * 重置当前对话框的宽高
 * @param options
 */
function resizeWindow(options){
	return Lingx.getRootWindow().getApi().resizeWindow(options);
}
/**
 * 获取当前对话框Ext.Window
 */
function getCurrentDialogWindow(){
	return Lingx.getRootWindow().getApi().getCurrentDialogWindow();
}
/**
 * 关闭当前对话框
 */
function closeWindow(){
	Lingx.getRootWindow().getApi().closeWindow();
}
/**
 * 在顶部中间显示提示信息
 * @param msg
 */
function showMessage(msg){
	Lingx.getRootWindow().getApi().showMessage(msg);
}
/**
 * 在顶部中间显示提示信息
 * @param msg
 */
function lgxInfo(msg){
	Lingx.getRootWindow().getApi().showMessage(msg);
}
/**
 * 取得主操作页面
 */
function getRootWindow(){
	return Lingx.getRootWindow();
}

function getFieldCodeFormMethodFieldByStr(etype,fields){
	var temp="";
	for(var i=0;i<fields.length;i++){
		var f=fields[i];
		if(f.refEntity==etype){
			temp=f.code;
			break;
		}
	}
	return temp;
}
function getFieldCodeFormMethodFieldByObj(obj,fields){
	var temp="";
	for(var i=0;i<fields.length;i++){
		var f=fields[i];
		if(obj[f.code]){
			temp=f.code;
			break;
		}
	}
	return temp;
}

function removeDefaultAttribute(params){
	delete params.e;
	delete params.m;
	delete params.pageid;
	
}

function openFullWindow(url){
	  var redirectUrl = url ;
	  var width = screen.width ;
	  var height = screen.height ;
	  if (height == 768 ) height -= 75 ;
	  if (height == 600 ) height -= 60 ;
	  var szFeatures = "top=0," ; 
	  szFeatures +="left=0," ;
	  szFeatures +="width="+width+"," ;
	  szFeatures +="height="+height+"," ; 
	  szFeatures +="directories=no," ;
	  szFeatures +="status=yes," ;
	  szFeatures +="menubar=no," ;
	  if (height <= 600 ) szFeatures +="scrollbars=yes," ;
	  else szFeatures +="scrollbars=yes," ;
	  szFeatures +="resizable=yes" ; 
	  window.open(redirectUrl,"new",szFeatures) ;
}
function lingxSet(options){
	try{
	var input=Ext.getCmp(options.cmpId);
	input.setValue(options.value);
	input.setText(options.text);
	}catch(e){
		$("#"+options.cmpId+"_value").val(options.value);
		$("#"+options.cmpId+"_text").val(options.text);
	}
}
function cascadeRefersh(options){
	request_params=options;
	params=options;
	lingxSearch([]);
	//resetExtParams();
	//reloadGrid();
}
//处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
function banBackSpace(e){   
    var ev = e || window.event;//获取event对象   
    var obj = ev.target || ev.srcElement;//获取事件源   
    var t = obj.type || obj.getAttribute('type');//获取事件源类型  
    //获取作为判断条件的事件类型
    var vReadOnly = obj.getAttribute('readonly');
    //处理null值情况
    vReadOnly = (vReadOnly == "") ? false : vReadOnly;
    
    //当是对话框取到回车事件事，自动提交
    //alert(ev.keyCode +" vs "+t);
    if(ev.keyCode==13&&(t==null||t=="radio")){
    	if(window.lingxSubmit){
    		lingxSubmit();
    	}else if(window.getCurrentDialogWindow){
    		var win1=window.getCurrentDialogWindow();
    		var ifr=Ext.get(win1.getEl()).query("iframe");
    		if(ifr.length>0&&ifr[0].contentWindow.lingxSubmit)ifr[0].contentWindow.lingxSubmit();
    	}
    	return false;
    }
    //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
    //并且readonly属性为true或enabled属性为false的，则退格键失效
    var flag1=(ev.keyCode == 8 && (t=="password" || t=="text" || t=="textarea") 
                && vReadOnly=="readonly")?true:false;
    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
    var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
                ?true:false;        
    
    //判断
    if(flag2){
        return false;
    }
    if(flag1){   
        return false;   
    }   
}

window.onload=function(){
//禁止后退键 作用于Firefox、Opera
document.onkeypress=banBackSpace;
//禁止后退键  作用于IE、Chrome
//document.onkeydown=banBackSpace;
}