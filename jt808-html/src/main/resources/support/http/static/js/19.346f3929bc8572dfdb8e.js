webpackJsonp([19],{"0Cn2":function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={name:"MileageDay",data:function(){return{value2:"",value3:"",value4:"",excelAble:!1,tids:[],gids:[],fields:[{name:"终端ID",code:"tid"},{name:"车牌号码",code:"carno"},{name:"隶属车队",code:"group"}],pickerOptions:{disabledDate:function(e){return(new Date).format("yyyyMMdd")<e.format("yyyyMMdd")},shortcuts:[{text:"最近一天",onClick:function(e){var t=new Date,i=new Date;i.setTime(i.getTime()-864e5),e.$emit("pick",[i,t])}},{text:"最近三天",onClick:function(e){var t=new Date,i=new Date;i.setTime(i.getTime()-2592e5),e.$emit("pick",[i,t])}},{text:"最近七天",onClick:function(e){var t=new Date,i=new Date;i.setTime(i.getTime()-6048e5),e.$emit("pick",[i,t])}},{text:"最近十五天",onClick:function(e){var t=new Date,i=new Date;i.setTime(i.getTime()-1296e6),e.$emit("pick",[i,t])}},{text:"最近一个月",onClick:function(e){var t=new Date,i=new Date;i.setMonth(i.getMonth()-1),e.$emit("pick",[i,t])}}]},tableData:[]}},methods:{day7:function(){var e=new Date,t=new Date;t.setTime(t.getTime()-6048e5),this.value3=t.getTime(),this.value4=e.getTime()},day15:function(){var e=new Date,t=new Date;t.setTime(t.getTime()-1296e6),this.value3=t.getTime(),this.value4=e.getTime()},day30:function(){var e=new Date,t=new Date;t.setMonth(t.getMonth()-1),this.value3=t.getTime(),this.value4=e.getTime()},excel:function(){for(var e=[{title:"车牌号码",field:"carno"},{title:"隶属车队",field:"group"},{title:"终端ID",field:"tid"}],t=new Date(this.value3),i=new Date(this.value4);t.getTime()<=i.getTime();){var a=t.format("MM-dd");e.push({title:a,field:a}),t.setDate(t.getDate()+1)}e.push({title:"总里程",field:"sum"}),e.push({title:"出车率",field:"ccl"}),e.push({title:"出车天数",field:"ccts"}),function(e,t,i){for(var a="",n="",s=0;s<e.length;s++)e[s].title&&(n+=e[s].title+",");n=n.slice(0,-1),a+=n+"\r\n";for(var s=0;s<t.length;s++){for(var n="",c=0;c<e.length;c++)e[c].title&&(e[c].formater||(e[c].formater=l),n+='"'+e[c].formater("","",t[s][e[c].field]?t[s][e[c].field]:"0")+'"\t,');n.slice(0,n.length-1),a+=n+"\r\n"}if(""==a)return void alert("Invalid data");var i=i,d=new Blob(["\ufeff"+a],{type:"text/csv"});if(window.navigator&&window.navigator.msSaveOrOpenBlob)window.navigator.msSaveOrOpenBlob(a,i+".csv");else{var o=document.createElement("a");o.href=URL.createObjectURL(d),o.style="visibility:hidden",o.download=i+".csv",document.body.appendChild(o),o.click(),document.body.removeChild(o)}}(e,this.tableData,"里程统计(天)")},searchGrid:function(){if(0!=this.tids.length||0!=this.gids.length)if(this.value3&&this.value4){var e=this;this.fields=[{name:"终端ID",code:"tid"},{name:"车牌号码",code:"carno"},{name:"隶属车队",code:"group"}];for(var t=new Date(this.value3),i=new Date(this.value4);t.getTime()<=i.getTime();){var a=t.format("MM-dd");this.fields.push({name:a,code:a}),t.setDate(t.getDate()+1)}this.fields.push({name:"总里程",code:"sum"}),this.fields.push({name:"出车率",code:"ccl"}),this.fields.push({name:"出车天数",code:"ccts"}),this.callApi({apicode:1121,tids:this.tids,gids:this.gids,stime:this.value3,etime:this.value4},function(t){e.excelAble=!0,e.tableData=t.data})}else this.lgxInfo2("操作失败，日期不可为空!");else this.lgxInfo2("操作失败，请选择要统计的车辆!")},lingxcheckchange:function(e,t,i){if(t)(e.id+"").indexOf("_")>0?this.tids.push(e.value+""):this.gids.push(e.value);else{for(var a=-1,l=0;l<this.tids.length;l++)if(e.value==this.tids[l]){a=l;break}a>=0&&this.tids.splice(a,1),a=-1;for(l=0;l<this.gids.length;l++)if(e.value==this.gids[l]){a=l;break}a>=0&&this.gids.splice(a,1)}},lingxnodeclick:function(e){}},components:{LingxCarTree:i("Zwc7").a}};function l(e,t,i){return i}var n={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticStyle:{height:"100%"}},[i("el-container",{staticStyle:{height:"100%"}},[i("el-aside",{staticStyle:{"background-color":"#fff","border-right":"#c1c5cd 1px solid"},attrs:{width:"260px"}},[i("LingxCarTree",{ref:"lingxCarTree",on:{lingxcheckchange:e.lingxcheckchange,lingxnodeclick:e.lingxnodeclick}})],1),e._v(" "),i("el-container",{staticStyle:{width:"100%",height:"100%",margin:"0px",padding:"0px"}},[i("el-header",{staticStyle:{padding:"0px"},attrs:{height:"42"}},[i("div",{staticStyle:{margin:"5px"}},[i("span",[i("el-date-picker",{staticStyle:{width:"150px"},attrs:{"value-format":"timestamp",type:"date",size:"small",placeholder:"起始日期"},model:{value:e.value3,callback:function(t){e.value3=t},expression:"value3"}}),e._v(" "),i("el-date-picker",{staticStyle:{width:"150px"},attrs:{"value-format":"timestamp",type:"date",size:"small",placeholder:"截止日期"},model:{value:e.value4,callback:function(t){e.value4=t},expression:"value4"}})],1),e._v(" "),i("el-button",{staticStyle:{padding:"10px 15px 8px 15px"},attrs:{size:"small",type:"primary",plain:"",icon:"el-icon-search"},on:{click:e.searchGrid}},[e._v("查询")]),e._v(" "),i("el-button",{attrs:{disabled:!e.excelAble,icon:"el-icon-notebook-2",size:"small",type:"success",plain:""},on:{click:e.excel}},[e._v("导出")]),e._v(" "),i("el-button",{staticStyle:{padding:"10px 15px 8px 15px"},attrs:{size:"small",plain:""},on:{click:e.day7}},[e._v("最近7天")]),e._v(" "),i("el-button",{staticStyle:{padding:"10px 15px 8px 15px"},attrs:{size:"small",plain:""},on:{click:e.day15}},[e._v("最近15天")]),e._v(" "),i("el-button",{staticStyle:{padding:"10px 15px 8px 15px"},attrs:{size:"small",plain:""},on:{click:e.day30}},[e._v("最近1个月")])],1)]),e._v(" "),i("el-main",{staticStyle:{margin:"0px",padding:"0px"}},[i("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"","highlight-current-row":"",size:"mini",height:"100%"}},[i("el-table-column",{attrs:{label:"",width:"42px"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n               "+e._s(t.$index+1)+"\n           ")]}}])}),e._v(" "),e._l(e.fields,function(e,t){return i("el-table-column",{key:t,attrs:{prop:e.code,label:e.name}})})],2)],1)],1)],1)],1)},staticRenderFns:[]};var s=i("VU/8")(a,n,!1,function(e){i("HyZR")},null,null);t.default=s.exports},HyZR:function(e,t){}});