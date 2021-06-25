function createCarMenu(record){
	var carno=record.data.text;
	var ind=carno.lastIndexOf("-");
	carno=carno.substring(0,ind);
var car_menu=Ext.create("Ext.menu.Menu",{
    						 floating : true,  
    						 items:[{
    							 text:"基础信息",
    							 iconCls:"Pencil",
    							 handler:function(){
    								 openWindow(carno+"-修改","e?e=tgps_car&m=edit&id="+record.data.id.substring(record.data.id.indexOf("_")+1));
    							 }
    						 },{
    							 text:"变更分组",
    							 iconCls:"Folderedit",
    							 handler:function(){
    								 openWindow(carno+"-修改分组","e?e=tgps_group_car&m=change&id="+record.data.id.substring(record.data.id.indexOf("_")+1));
    							 }
    						 }
    						 ,
    						{
    							 text:"轨迹回放",
    							 iconCls:"Map",
    							 handler:function(){
    								openReadonlyWindow(carno+"-轨迹回放","gps/history/history_gaode.jsp?id="+record.data.id.substring(record.data.id.indexOf("_")+1),"full1");
       							 
    							 }
    						 }
    						
							]
 		                });
return car_menu;
}