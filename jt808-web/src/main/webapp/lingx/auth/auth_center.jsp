<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实体查看</title>

<%@ include file="/lingx/include/include_JavaScriptAndCss4.jsp"%> 
<style type="text/css">
.edit_word{font-size:12px;line-height:24px;text-align:right;width:100px;}
</style>
<script type="text/javascript">

var fromPageId='';
var entityCode='tlingx_role';
var methodCode='tree';
var entityId='';
var params={};
var entityFields={};
var grid_group={};

var handler="<%=basePath %>lingx/auth/auth_center_handler.jsp";



var extParams={};
var cmpId='';
var textField='id';
var valueField='id';

/**
 * 刷新列表数据
 */
 function reloadGrid(){
	 /* var store=Ext.getCmp("datas").getStore();
		store.getRootNode().remove();
		store.setRootNode({
			 text: "ROOT",
	         id:entityId,
	         expanded: true
		}); */
}
 function reloadMethod(){
	 var store=Ext.getCmp("datas").getStore();
		store.getRootNode().remove();
		store.setRootNode({
			 text: "ROOT",
	         id:entityId,
	         expanded: true
		});
 }
function reloadTree(tree1){
	var store=Ext.getCmp(tree1).getStore();
	store.getRootNode().remove();
	store.setRootNode({
		 text: "ROOT",
         id:0,
         expanded: true
	});
}
Ext.onReady(function(){

	
	entityId=entityId?entityId:0;
	Lingx.post("g",{e:entityCode},function(json){
		Ext.each(json.fields,function(obj,index,self){
			if(obj.comboType=='ref-display')textField=obj.code;
			if(obj.comboType=='ref-value')valueField=obj.code;
		});
		json.toolbar.push({iconCls:"icon-reload",text:"刷新",handler:reloadMethod});
	Ext.create("Ext.Viewport",{
		id:'viewport',
		layout:'border',
		items:[{
			id:"datas",
			region:"west",
			 width:"25%",
            animate:false,
            border:false,split: true,
            autoScroll: true,
            rootVisible: false,
            store: Ext.create('Ext.data.TreeStore', {
        		proxy: {
        	         type: 'ajax',
        	         url: "e?e="+entityCode+"&m=tree&lgxsn=1",//"+methodCode+"
        	         reader: {
        	             type: 'json'
        	         }
        	     },
        	     root: {
        	         text: "ROOT",
        	         id:entityId,
        	         expanded: true
        	    },
        	     autoLoad: true,
        	     listeners:{
        	    	 datachanged:function(){
         	    		if(Ext.getCmp("datas")&&Ext.getCmp("datas").getSelectionModel().getCount()==0)
      		    		Ext.getCmp("datas").getSelectionModel().select(0);
      		    	}
         	     }
        	}),
			xtype:"treepanel",
			dockedItems: [{
	    	        xtype: 'toolbar',
	    	        items:json.toolbar,
	    	        dock: 'top',
	    	        displayInfo: true
	    	}],
	        listeners:{
	        	itemdblclick:function(view,record,item,index,event,obj){
	        		openViewWindow(entityCode,json.name,record.data.id);
	        	},select:function(view,record,item,index,event,obj){
	        		extParams["roleid"]=record.data.id;
	        		reloadTree("funcTree");
	        		reloadTree("roleTree");
	        		reloadTree("orgTree");
	        		reloadTree("menuTree");
	        		reloadUser(record.data.id);
	        		//lgxInfo(record.data.id);
	        	}
	        }
		},{
			id:'tabpanel',
			region:'center',
			xtype:'tabpanel',
			activeTab: 0,
            split: true,
			items:[ //{title:"分配用户",html: '<iframe id="tlingx_userrole" scrolling="auto" frameborder="0" width="100%" height="100%" src=""> </iframe>'},
				{title:"菜单授权",
					id:"menuTree",
					autoScroll: true,
		            rootVisible: false,
		            store: Ext.create('Ext.data.TreeStore', {
		            	id:'menuStore',
		        		proxy: {
		        	         type: 'ajax',
		        	         url:  handler+"?c=menuTree",//"+methodCode+"
		        	         reader: {
		        	             type: 'json'
		        	         }
		        	     },
		        	     root: {
		        	         text: "ROOT",
		        	         id:entityId,
		        	         expanded: true
		        	    },
		        	     autoLoad: true,
		        	     listeners:{
		        	    	 beforeload:function(){
		     		    		Ext.apply(this.proxy.extraParams,extParams);  
		     		    	},
		      		    	load:function(){
		      		    		///Ext.getCmp("tree").getSelectionModel().select(0);
		      		    	}
		         	     }
		        	}),
					xtype:"treepanel",
					listeners:{
						checkchange:function(record,checked){
							 if (checked) {
								 Lingx.post(handler,{c:"trolemenu_add",roleid:extParams["roleid"],menuid:record.data.id},function(json){});
				                 record.bubble(function(parentNode) {
				                    // parentNode.set('checked', true);
				                     //var node=parentNode;
				                    // Lingx.post(handler,{c:"trolemenu_add",roleid:extParams["roleid"],menuid:node.data.id},function(json){});
						                
				                 });
				                 record.cascadeBy(function(node) {
				                    // node.set('checked', true);
				                     //Lingx.post(handler,{c:"trolemenu_add",roleid:extParams["roleid"],menuid:node.data.id},function(json){});
					                 
				                    // var temp=node.data.id;
				                     //temp=temp+"";
				                     //if(temp.indexOf("_")!=-1)
									//	addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
				                     //tree.fireEvent("checkchange",node,true);
				                 });
				                 record.expand();
				                 record.expandChildren();
				                 //reloadSsjk();
				             } else {
				            	 Lingx.post(handler,{c:"trolemenu_del",roleid:extParams["roleid"],menuid:record.data.id},function(json){});
				                 //record.collapse();
				                 //record.collapseChildren();
				                 record.cascadeBy(function(node) {
				                    //node.set('checked', false);
				                    //Lingx.post(handler,{c:"trolemenu_del",roleid:extParams["roleid"],menuid:node.data.id},function(json){});
					                 
				                    // var temp=node.data.id;
				                    // temp=temp+"";
				                    // if(temp.indexOf("_")!=-1)
									//	remove(temp.substring(temp.indexOf("_")+1));
				                 });
				                 record.bubble(function(parentNode) {
				                    var childHasChecked=false;
				                    var childNodes = parentNode.childNodes;
				                    if(childNodes || childNodes.length>0){
				                        for(var i=0;i<childNodes.length;i++){
				                            if(childNodes[i].data.checked){
				                                childHasChecked= true;
				                                break;
				                            }
				                        }
				                    }
				                    if(!childHasChecked){
				                        // parentNode.set('checked', false);
					                    // var node=parentNode;
					                    // Lingx.post(handler,{c:"trolemenu_del",roleid:extParams["roleid"],menuid:node.data.id},function(json){});
						                 
				                     }
				                 });
				                
				             }
						}
					}},{title:"功能授权",
				id:"funcTree",
				autoScroll: true,
	            rootVisible: false,
	            store: Ext.create('Ext.data.TreeStore', {
	            	id:'funcStore',
	        		proxy: {
	        	         type: 'ajax',
	        	         url: handler+"?c=funcTree",//"+methodCode+"
	        	         reader: {
	        	             type: 'json'
	        	         }
	        	     },
	        	     root: {
	        	         text: "ROOT",
	        	         id:entityId,
	        	         expanded: true
	        	    },
	        	     autoLoad: true,
	        	     listeners:{
	        	    	 beforeload:function(){
		     		    		Ext.apply(this.proxy.extraParams,extParams);  
		     		    	},
	      		    	load:function(){
	      		    		//Ext.getCmp("tree").getSelectionModel().select(0);
	      		    	}
	         	     }
	        	}),
				xtype:"treepanel",
				listeners:{
					checkchange:function(record,checked){
						 if (checked) {
			                 record.bubble(function(parentNode) {
			                     parentNode.set('checked', true);
			                     var node=parentNode;
			                     Lingx.post(handler,{c:"trolefunc_add",roleid:extParams["roleid"],funcid:node.data.id},function(json){});
				                    
			                 });
			                 record.cascadeBy(function(node) {
			                     node.set('checked', true);
			                     Lingx.post(handler,{c:"trolefunc_add",roleid:extParams["roleid"],funcid:node.data.id},function(json){});
			                    // var temp=node.data.id;
			                     //temp=temp+"";
			                     //if(temp.indexOf("_")!=-1)
								//	addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
			                     //tree.fireEvent("checkchange",node,true);
			                 });
			                 record.expand();
			                 record.expandChildren();
			                 //reloadSsjk();
			             } else {
			                 record.collapse();
			                 record.collapseChildren();
			                 record.cascadeBy(function(node) {
			                     node.set('checked', false);
			                     Lingx.post(handler,{c:"trolefunc_del",roleid:extParams["roleid"],funcid:node.data.id},function(json){});
				                    
			                     /* var temp=node.data.id;
			                     temp=temp+"";
			                     if(temp.indexOf("_")!=-1)
									remove(temp.substring(temp.indexOf("_")+1)); */
			                 });
			                 record.bubble(function(parentNode) {
			                    var childHasChecked=false;
			                    var childNodes = parentNode.childNodes;
			                    if(childNodes || childNodes.length>0){
			                        for(var i=0;i<childNodes.length;i++){
			                            if(childNodes[i].data.checked){
			                                childHasChecked= true;
			                                break;
			                            }
			                        }
			                    }
			                    if(!childHasChecked){
			                         parentNode.set('checked', false);
				                     var node=parentNode;
				                     Lingx.post(handler,{c:"trolefunc_del",roleid:extParams["roleid"],funcid:node.data.id},function(json){});
					                    
			                     }
			                 });
			                
			             }
					}
				}
				},{title:"组织授权",
					id:"orgTree",
					autoScroll: true,
		            rootVisible: false,
		            store: Ext.create('Ext.data.TreeStore', {
		            	id:'orgStore',
		        		proxy: {
		        	         type: 'ajax',
		        	         url:  handler+"?c=orgTree",//"+methodCode+"
		        	         reader: {
		        	             type: 'json'
		        	         }
		        	     },
		        	     root: {
		        	         text: "ROOT",
		        	         id:entityId,
		        	         expanded: true
		        	    },
		        	     autoLoad: true,
		        	     listeners:{
		        	    	 beforeload:function(){
			     		    		Ext.apply(this.proxy.extraParams,extParams);  
			     		    	},
		      		    	load:function(){
		      		    		//Ext.getCmp("tree").getSelectionModel().select(0);
		      		    	}
		         	     }
		        	}),
					xtype:"treepanel",
					listeners:{
						checkchange:function(record,checked){
							 if (checked) {
				                 record.bubble(function(parentNode) {
				                     //parentNode.set('checked', true);
				                 });
				                 record.cascadeBy(function(node) {
				                     node.set('checked', true);
				                     Lingx.post(handler,{c:"troleorg_add",roleid:extParams["roleid"],orgid:node.data.id},function(json){});
					                 
				                    // var temp=node.data.id;
				                     //temp=temp+"";
				                     //if(temp.indexOf("_")!=-1)
									//	addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
				                     //tree.fireEvent("checkchange",node,true);
				                 });
				                 record.expand();
				                 record.expandChildren();
				                 //reloadSsjk();
				             } else {
				                 record.collapse();
				                 record.collapseChildren();
				                 record.cascadeBy(function(node) {
				                     node.set('checked', false);
				                     Lingx.post(handler,{c:"troleorg_del",roleid:extParams["roleid"],orgid:node.data.id},function(json){});
					                 
				                     /* var temp=node.data.id;
				                     temp=temp+"";
				                     if(temp.indexOf("_")!=-1)
										remove(temp.substring(temp.indexOf("_")+1)); */
				                 });
				                 record.bubble(function(parentNode) {
				                    var childHasChecked=false;
				                    var childNodes = parentNode.childNodes;
				                    if(childNodes || childNodes.length>0){
				                        for(var i=0;i<childNodes.length;i++){
				                            if(childNodes[i].data.checked){
				                                childHasChecked= true;
				                                break;
				                            }
				                        }
				                    }
				                    if(!childHasChecked){
				                        // parentNode.set('checked', false);
				                     }
				                 });
				                
				             }
						}
					}
					},{title:"角色授权",
						id:"roleTree",
						autoScroll: true,
			            rootVisible: false,
			            store: Ext.create('Ext.data.TreeStore', {
			            	id:'roleStore',
			        		proxy: {
			        	         type: 'ajax',
			        	         url:  handler+"?c=roleTree",//"+methodCode+"
			        	         reader: {
			        	             type: 'json'
			        	         }
			        	     },
			        	     root: {
			        	         text: "ROOT",
			        	         id:entityId,
			        	         expanded: true
			        	    },
			        	     autoLoad: true,
			        	     listeners:{
			        	    	 beforeload:function(){
				     		    		Ext.apply(this.proxy.extraParams,extParams);  
				     		    	},
				     		    load:function(){
			      		    		//Ext.getCmp("tree").getSelectionModel().select(0);
			      		    	}
			         	     }
			        	}),
						xtype:"treepanel",
						listeners:{
							checkchange:function(record,checked){
								 if (checked) {
					                 record.bubble(function(parentNode) {
					                    // parentNode.set('checked', true);
					                 });
					                 record.cascadeBy(function(node) {
					                     node.set('checked', true);
					                     Lingx.post(handler,{c:"trolerole_add",roleid:extParams["roleid"],refroleid:node.data.id},function(json){});
						                 
					                    // var temp=node.data.id;
					                     //temp=temp+"";
					                     //if(temp.indexOf("_")!=-1)
										//	addCar(temp.substring(temp.indexOf("_")+1),node.data.text);
					                     //tree.fireEvent("checkchange",node,true);
					                 });
					                 record.expand();
					                 record.expandChildren();
					                 //reloadSsjk();
					             } else {
					                 record.collapse();
					                 record.collapseChildren();
					                 record.cascadeBy(function(node) {
					                     node.set('checked', false);
					                     Lingx.post(handler,{c:"trolerole_del",roleid:extParams["roleid"],refroleid:node.data.id},function(json){});
						                 
					                    /*  var temp=node.data.id;
					                     temp=temp+"";
					                     if(temp.indexOf("_")!=-1)
											remove(temp.substring(temp.indexOf("_")+1)); */
					                 });
					                 record.bubble(function(parentNode) {
					                    var childHasChecked=false;
					                    var childNodes = parentNode.childNodes;
					                    if(childNodes || childNodes.length>0){
					                        for(var i=0;i<childNodes.length;i++){
					                            if(childNodes[i].data.checked){
					                                childHasChecked= true;
					                                break;
					                            }
					                        }
					                    }
					                    if(!childHasChecked){
					                      //  parentNode.set('checked', false);
					                     }
					                 });
					                
					             }
							}
						}}
					]
		}]
	});

	});
});

function reloadUser(roleid){
	//var frame=document.getElementById("tlingx_userrole");

	//frame.src='e?e=tlingx_userrole&m=grid&role_id='+roleid;
	
}
</script>
</head>
<body>
</body>

</html>