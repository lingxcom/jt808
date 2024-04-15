webpackJsonp([57],{Szgi:function(t,e){},s7Yf:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i("RfNU"),r=i("29oq"),l={name:"GB28181VideoHistory",data:function(){return{callId:"",carInfo:{},stimeFormat:"",etimeFormat:"",timeStr:"",car_no:"",value1:0,isOperat:!1,data:[],currentRow:{},form:{car_id:"",tdh:"1",stime:"",etime:""},options:[{label:"0.5倍速",value:"0.5"},{label:"1倍速",value:"1"},{label:"2倍速",value:"2"},{label:"4倍速",value:"4"}],speed:"1"}},methods:{resumePlay:function(){this.callApi({apicode:1146,deviceId:this.form.car_id,channel:this.form.tdh,callId:this.callId,type:"1"},function(t){})},pausePlay:function(){this.callApi({apicode:1146,deviceId:this.form.car_id,channel:this.form.tdh,callId:this.callId,type:"2"},function(t){})},changeSpeed:function(){this.callApi({apicode:1146,deviceId:this.form.car_id,channel:this.form.tdh,callId:this.callId,type:"3",param1:this.speed},function(t){})},formatTdh:function(){return this.form.tdh},resetCarInfo:function(){var t=new Date(this.formatDatetime(null,null,this.STIME,null).replace(/-/g,"/")).getTime()+1e3*this.progress1,e=new Date;e.setTime(t);for(var i=e.format("yyyyMMddhhmmss"),a=0;a<this.latlngs.length-1;a++){var r=this.latlngs[a].gpstime,l=this.latlngs[a+1].gpstime;if(i>=r&&i<l){this.latlngs[a].carno=this.car_no,this.carInfo=this.latlngs[a];break}}},stopPlay:function(){this.value1=0,this.progress1=0,this.$refs.player.stop()},startPlay:function(){this.requestPlay(this.currentRow)},uploadFtp:function(){this.lgxInfo("正在开发中...")},setRowFormatTime:function(){this.stimeFormat=this.formatDatetime(null,null,this.currentRow.stime,null),this.etimeFormat=this.formatDatetime(null,null,this.currentRow.etime,null),this.timeStr=this.formatSC({stime:this.formatDatetime2(this.stimeFormat),etime:this.formatDatetime2(this.etimeFormat)})},videoEvent:function(t){if(!this.isDrag){this.ctimeP=t;var e=this.ctimeP-this.stimeP;(e>100||e<0||!e)&&(e=0),e>0&&(this.progress1=this.progress1+e,this.value1=parseInt(this.progress1/this.TIME*1e3),this.resetCarInfo()),this.stimeP=t-0}},changecomplete:function(t){this.isDrag=!0,this.progress1=t/1e3*this.TIME,this.isDrag=!1,this.callApi({apicode:1113,deviceId:this.form.car_id,channel:this.form.tdh,stime:this.STIME,time:parseInt(this.progress1)},function(t){})},selectRow:function(t,e,i){this.currentRow=t},requestPlay:function(t,e,i){var a=this,r=new Date(this.formatDatetime(null,null,t.etime,null).replace(/-/g,"/")).getTime()-new Date(this.formatDatetime(null,null,t.stime,null).replace(/-/g,"/")).getTime();this.progress1=0,this.value1=0,this.TIME=r/1e3,this.STIME=t.stime,this.stopPlay(),this.$refs.player.onLoading(),this.callApi({apicode:1145,deviceId:this.form.car_id,channel:this.form.tdh,stime:t.startTime,etime:t.endTime,type:t.type},function(t){1==t.code?(a.value1=0,a.progress1=0,a.callId=t.callId,a.$refs.player.pullVideo(t.pull_address,{tid:a.form.car_id,tdh:a.form.tdh})):a.lgxInfo2(t.message)})},queryVideo:function(){var t=this,e=this.formatDatetime2(this.form.stime),i=this.formatDatetime2(this.form.etime),a=this.$loading({lock:!0,text:"正在加载数据，请稍候...",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"});this.callApi({apicode:1144,deviceId:this.form.car_id,channel:this.form.tdh,stime:e,etime:i},function(e){a.close(),1==e.code?(t.data=e.data,t.currentRow=t.data[0],t.lgxInfo("成功加载历史视频数据，请先选择要操作的记录。")):t.lgxInfo2(e.message)})},lingxnodeclick:function(t,e,i){if(t.tdh){this.isOperat=!0;var a=t.id.split("_");this.form.car_id=a[0],this.car_no=i.node.parent.data.text,this.form.tdh=a[1],this.clearAll()}else this.isOperat=!1},formatDatetime:function(t,e,i,a){return i?i.substring(0,4)+"-"+i.substring(4,6)+"-"+i.substring(6,8)+" "+i.substring(8,10)+":"+i.substring(10,12)+":"+i.substring(12,14):""},formatDatetime2:function(t){return t.replace(/-/g,"").replace(" ","").replace(/:/g,"")},formatCarno:function(){return this.car_no},formatLength:function(t,e,i,a){return parseFloat(i/1024/1024).toFixed(2)+"MB"},initVideo:function(){},clearAll:function(){this.value1=0,this.progress1=0,this.data=[],this.currentRow={}}},watch:{stimeFormat:function(){this.timeStr=this.formatSC({stime:this.formatDatetime2(this.stimeFormat),etime:this.formatDatetime2(this.etimeFormat)})},etimeFormat:function(){this.timeStr=this.formatSC({stime:this.formatDatetime2(this.stimeFormat),etime:this.formatDatetime2(this.etimeFormat)})}},created:function(){var t=new Date,e=t.getFullYear(),i=t.getMonth()+1,a=t.getDate();i<10&&(i="0"+i),a<10&&(a="0"+a),this.form.stime=e+"-"+i+"-"+a+" 00:00:00",this.form.etime=e+"-"+i+"-"+a+" 23:59:59"},mounted:function(){this.initVideo()},components:{GB28181Tree:a.default,GB28181Video:r.default}},s={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("el-container",{staticStyle:{height:"100%"}},[i("el-aside",{staticStyle:{"background-color":"#fff","border-right":"#c1c5cd 1px solid",overflow:"hidden"},attrs:{width:"300px"}},[i("div",{staticStyle:{height:"70%",overflow:"auto"}},[i("GB28181Tree",{ref:"lingxCarTree",staticStyle:{height:"70%"},attrs:{isChecked:!1,isVideo:!0},on:{lingxnodeclick:t.lingxnodeclick}})],1),t._v(" "),i("div",{staticStyle:{height:"30%","border-top":"#c1c5cd 1px solid",padding:"10px","background-color":"#ced9e7"}},[i("el-form",{attrs:{"label-width":"80px",size:"mini"}},[i("el-form-item",{attrs:{label:"开始时间"}},[i("el-date-picker",{staticStyle:{width:"199px"},attrs:{type:"datetime","value-format":"yyyy-MM-dd HH:mm:ss",placeholder:"选择开始时间"},model:{value:t.form.stime,callback:function(e){t.$set(t.form,"stime",e)},expression:"form.stime"}})],1),t._v(" "),i("el-form-item",{attrs:{label:"结束时间"}},[i("el-date-picker",{staticStyle:{width:"199px"},attrs:{type:"datetime","value-format":"yyyy-MM-dd HH:mm:ss",placeholder:"选择结束时间"},model:{value:t.form.etime,callback:function(e){t.$set(t.form,"etime",e)},expression:"form.etime"}})],1),t._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"primary",disabled:!t.isOperat},on:{click:t.queryVideo}},[t._v("查询视频")]),t._v(" "),i("el-popover",{attrs:{placement:"top-start",width:"300",trigger:"click"}},[i("el-form",{ref:"form",attrs:{model:t.form,"label-width":"80px",size:"mini"}},[i("el-form-item",{attrs:{label:"摄像头"}},[i("div",[t._v(t._s(t.car_no)+" CH-"+t._s(t.currentRow.tdh))])]),t._v(" "),i("el-form-item",{attrs:{label:"视频时长"}},[i("div",[t._v(t._s(t.timeStr))])]),t._v(" "),i("el-form-item",{attrs:{label:"起始时间"}},[i("el-date-picker",{attrs:{type:"datetime","value-format":"yyyy-MM-dd HH:mm:ss",placeholder:""},model:{value:t.stimeFormat,callback:function(e){t.stimeFormat=e},expression:"stimeFormat"}})],1),t._v(" "),i("el-form-item",{attrs:{label:"截止时间"}},[i("el-date-picker",{attrs:{type:"datetime","value-format":"yyyy-MM-dd HH:mm:ss",placeholder:""},model:{value:t.etimeFormat,callback:function(e){t.etimeFormat=e},expression:"etimeFormat"}})],1),t._v(" "),i("el-form-item",{staticStyle:{"text-align":"right"}},[i("el-button",{attrs:{type:"primary"},on:{click:t.uploadFtp}},[t._v("立即上传")])],1)],1),t._v(" "),i("el-button",{staticStyle:{"margin-left":"10px"},attrs:{slot:"reference",type:"warning",disabled:!t.isOperat||!t.currentRow.tdh},on:{click:t.setRowFormatTime},slot:"reference"},[t._v("上传FTP")])],1)],1),t._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"success",disabled:!t.isOperat||!t.currentRow.tdh},on:{click:t.startPlay}},[t._v("开始播放")]),t._v(" "),i("el-button",{attrs:{type:"danger",disabled:!t.isOperat},on:{click:t.stopPlay}},[t._v("停止播放")])],1),t._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"success",disabled:!t.isOperat},on:{click:t.resumePlay}},[t._v("恢复播放")]),t._v(" "),i("el-button",{attrs:{type:"danger",disabled:!t.isOperat},on:{click:t.pausePlay}},[t._v("暂停播放")])],1),t._v(" "),i("el-form-item",[i("el-select",{attrs:{placeholder:"播放倍速"},on:{change:function(e){return t.changeSpeed()}},model:{value:t.speed,callback:function(e){t.speed=e},expression:"speed"}},t._l(t.options,function(t){return i("el-option",{key:t.value,attrs:{label:t.label,value:t.value}})}),1)],1)],1)],1)]),t._v(" "),i("el-container",{staticStyle:{width:"100%",height:"100%",margin:"0px",padding:"0px"}},[i("el-main",{staticStyle:{margin:"0px",padding:"0px"}},[i("div",{staticStyle:{width:"100%",height:"100%",margin:"0px",padding:"0px","text-align":"center",position:"relative",overflow:"hidden"},attrs:{id:"container"}},[i("div",{staticStyle:{top:"0px",left:"0px",right:"0px",bottom:"38px",position:"absolute"}},[i("GB28181Video",{ref:"player",attrs:{callId0:t.callId,isToolbar:!1,isDblclickFullScreen:!0},on:{timeupdate:t.videoEvent}})],1),t._v(" "),i("div",{staticStyle:{left:"0px",right:"0px",bottom:"0px",position:"absolute",height:"38px","line-height":"38px","background-color":"#000000",overflow:"hidden"}},[i("el-slider",{attrs:{"show-tooltip":!1,max:1e3},on:{change:t.changecomplete},nativeOn:{mousedown:function(e){t.isDrag=!0},mouseup:function(e){t.isDrag=!1}},model:{value:t.value1,callback:function(e){t.value1=e},expression:"value1"}})],1)])]),t._v(" "),i("el-footer",{staticClass:"noselect",staticStyle:{"background-color":"#fff",height:"300px",padding:"0px"}},[i("el-table",{ref:"table",staticStyle:{width:"100%"},attrs:{size:"mini",height:"300",stripe:"","highlight-current-row":"",data:t.data},on:{"row-click":t.selectRow,"row-dblclick":t.requestPlay}},[i("el-table-column",{attrs:{label:"",width:"36px"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v("\n                        "+t._s(e.$index+1)+"\n                    ")]}}])}),t._v(" "),i("el-table-column",{attrs:{prop:"deviceID",label:"设备编号",width:"180","show-overflow-tooltip":""}}),t._v(" "),i("el-table-column",{attrs:{prop:"stime",sortable:"",label:"开始时间",width:"160",formatter:t.formatDatetime}}),t._v(" "),i("el-table-column",{attrs:{prop:"etime",sortable:"",label:"结束时间",width:"160",formatter:t.formatDatetime}}),t._v(" "),i("el-table-column",{attrs:{prop:"filePath","show-overflow-tooltip":"",label:"存储文件名称",width:"300"}})],1)],1)],1)],1)},staticRenderFns:[]};var o=i("VU/8")(l,s,!1,function(t){i("Szgi")},null,null);e.default=o.exports}});