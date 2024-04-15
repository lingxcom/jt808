webpackJsonp([34],{"+iSn":function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={name:"MileageMonth",data:function(){return{value2:"",value3:"",value4:"",excelAble:!1,tids:[],gids:[],fields:[{name:"终端ID",code:"tid"},{name:"车牌号码",code:"carno"},{name:"隶属车队",code:"group"}],pickerOptions:{disabledDate:function(e){return(new Date).format("yyyyMMdd")<e.format("yyyyMMdd")},shortcuts:[{text:"最近一个月",onClick:function(e){var t=new Date,i=new Date;i.setMonth(i.getMonth()-1),e.$emit("pick",[i,t])}},{text:"最近三个月",onClick:function(e){var t=new Date,i=new Date;i.setMonth(i.getMonth()-3),e.$emit("pick",[i,t])}},{text:"最近六个月",onClick:function(e){var t=new Date,i=new Date;i.setMonth(i.getMonth()-6),e.$emit("pick",[i,t])}},{text:"最近一年",onClick:function(e){var t=new Date,i=new Date;i.setMonth(i.getMonth()-12),e.$emit("pick",[i,t])}}]},tableData:[]}},methods:{month3:function(){var e=new Date,t=new Date;t.setMonth(t.getMonth()-3),this.value3=t.getTime(),this.value4=e.getTime()},month6:function(){var e=new Date,t=new Date;t.setMonth(t.getMonth()-6),this.value3=t.getTime(),this.value4=e.getTime()},month12:function(){var e=new Date,t=new Date;t.setMonth(t.getMonth()-12),this.value3=t.getTime(),this.value4=e.getTime()},excel:function(){for(var e=[{title:"车牌号码",field:"carno"},{title:"隶属车队",field:"group"},{title:"终端ID",field:"tid"}],t=new Date(this.value3),i=new Date(this.value4);t.getTime()<=i.getTime();){var a=t.format("yyyy-MM");e.push({title:a,field:a}),t.setMonth(t.getMonth()+1)}!function(e,t,i){for(var a="",l="",s=0;s<e.length;s++)e[s].title&&(l+=e[s].title+",");l=l.slice(0,-1),a+=l+"\r\n";for(var s=0;s<t.length;s++){for(var l="",o=0;o<e.length;o++)e[o].title&&(e[o].formater||(e[o].formater=n),l+='"'+e[o].formater("","",t[s][e[o].field]?t[s][e[o].field]:"0")+'"\t,');l.slice(0,l.length-1),a+=l+"\r\n"}if(""==a)return void alert("Invalid data");var i=i,c=new Blob(["\ufeff"+a],{type:"text/csv"});if(window.navigator&&window.navigator.msSaveOrOpenBlob)window.navigator.msSaveOrOpenBlob(a,i+".csv");else{var r=document.createElement("a");r.href=URL.createObjectURL(c),r.style="visibility:hidden",r.download=i+".csv",document.body.appendChild(r),r.click(),document.body.removeChild(r)}}(e,this.tableData,"里程统计(月)")},searchGrid:function(){if(0!=this.tids.length||0!=this.gids.length)if(this.value3&&this.value4){var e=this;this.fields=[{name:"终端ID",code:"tid"},{name:"车牌号码",code:"carno"},{name:"隶属车队",code:"group"}];for(var t=new Date(this.value3),i=new Date(this.value4);t.getTime()<=i.getTime();){var a=t.format("yyyy-MM");this.fields.push({name:a,code:a}),t.setMonth(t.getMonth()+1)}this.callApi({apicode:1122,tids:this.tids,gids:this.gids,stime:this.value3,etime:this.value4},function(t){e.excelAble=!0,e.tableData=t.data})}else this.lgxInfo2("操作失败，日期不可为空!");else this.lgxInfo2("操作失败，请选择要统计的车辆!")},lingxcheckchange:function(e,t){if(t)(e.id+"").indexOf("_")>0?this.tids.push(e.value+""):this.gids.push(e.value);else{for(var i=-1,a=0;a<this.tids.length;a++)if(e.value==this.tids[a]){i=a;break}i>=0&&this.tids.splice(i,1),i=-1;for(a=0;a<this.gids.length;a++)if(e.value==this.gids[a]){i=a;break}i>=0&&this.gids.splice(i,1)}},lingxnodeclick:function(e){}},components:{LingxCarTree:i("Zwc7").a}};function n(e,t,i){return i}var l={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticStyle:{height:"100%"}},[i("el-container",{staticStyle:{height:"100%"}},[i("el-aside",{staticStyle:{"background-color":"#fff","border-right":"#c1c5cd 1px solid"},attrs:{width:"260px"}},[i("LingxCarTree",{ref:"lingxCarTree",on:{lingxcheckchange:e.lingxcheckchange,lingxnodeclick:e.lingxnodeclick}})],1),e._v(" "),i("el-container",{staticStyle:{width:"100%",height:"100%",margin:"0px",padding:"0px"}},[i("el-header",{staticStyle:{padding:"0px"},attrs:{height:"42"}},[i("div",{staticStyle:{margin:"5px"}},[i("span",[i("el-date-picker",{staticStyle:{width:"150px"},attrs:{"value-format":"timestamp",type:"month",size:"small",placeholder:"起始日期"},model:{value:e.value3,callback:function(t){e.value3=t},expression:"value3"}}),e._v(" "),i("el-date-picker",{staticStyle:{width:"150px"},attrs:{"value-format":"timestamp",type:"month",size:"small",placeholder:"截止日期"},model:{value:e.value4,callback:function(t){e.value4=t},expression:"value4"}})],1),e._v(" "),i("el-button",{staticStyle:{padding:"10px 15px 8px 15px"},attrs:{size:"small",type:"primary",plain:"",icon:"el-icon-search"},on:{click:e.searchGrid}},[e._v("查询")]),e._v(" "),i("el-button",{attrs:{disabled:!e.excelAble,icon:"el-icon-notebook-2",size:"small",type:"success",plain:""},on:{click:e.excel}},[e._v("导出")]),e._v(" "),i("el-button",{staticStyle:{padding:"10px 15px 8px 15px"},attrs:{size:"small",plain:""},on:{click:e.month3}},[e._v("最近3个月")]),e._v(" "),i("el-button",{staticStyle:{padding:"10px 15px 8px 15px"},attrs:{size:"small",plain:""},on:{click:e.month6}},[e._v("最近6个月")]),e._v(" "),i("el-button",{staticStyle:{padding:"10px 15px 8px 15px"},attrs:{size:"small",plain:""},on:{click:e.month12}},[e._v("最近1年")])],1)]),e._v(" "),i("el-main",{staticStyle:{margin:"0px",padding:"0px"}},[i("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"","highlight-current-row":"",size:"mini",height:"100%"}},[i("el-table-column",{attrs:{label:"",width:"42px"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n               "+e._s(t.$index+1)+"\n           ")]}}])}),e._v(" "),e._l(e.fields,function(e,t){return i("el-table-column",{key:t,attrs:{prop:e.code,label:e.name}})})],2)],1)],1)],1)],1)},staticRenderFns:[]};var s=i("VU/8")(a,l,!1,function(e){i("Rr5s")},null,null);t.default=s.exports},Rr5s:function(e,t){}});