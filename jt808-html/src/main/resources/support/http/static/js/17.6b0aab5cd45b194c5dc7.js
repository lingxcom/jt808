webpackJsonp([17],{"0vsX":function(module,__webpack_exports__,__webpack_require__){"use strict";var __WEBPACK_IMPORTED_MODULE_0_babel_runtime_core_js_json_stringify__=__webpack_require__("mvHQ"),__WEBPACK_IMPORTED_MODULE_0_babel_runtime_core_js_json_stringify___default=__webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_babel_runtime_core_js_json_stringify__),__WEBPACK_IMPORTED_MODULE_1__components_LingxCarTree_vue__=__webpack_require__("Zwc7"),carnoMap312={};__webpack_exports__.a={name:"JT1078Command",data:function(){return{tid:"",tids:[],id:"117",type:"HEXSTRING",form:{value:""},issend:!1,region:"",param:"",list:[],params:[],data:[]}},methods:{lingxnodeclick:function(t,e,a){this.tid=t.value},lingxcheckchange:function(t,e,a){if(e)carnoMap312[t.value]=t.text,t.value.length>=12&&this.tids.push(t.value);else{for(var i=-1,s=0;s<this.tids.length;s++)if(this.tids[s]==t.value){i=s;break}i>=0&&this.tids.splice(i,1)}},change:function change(){this.params=[],this.form.value="",this.id=parseInt(this.param);for(var i=0;i<this.list.length;i++)this.id==parseInt(this.list[i].id)&&this.list[i].params&&(this.params=eval("("+this.list[i].params+")"))},sendCommand:function(){var t=this;this.data=[];var e=(new Date).format("yyyy-MM-dd hh:mm:ss");if(0!=this.tids.length)if(this.param){if(this.params.length>0)for(var a=0;a<this.params.length;a++){if(null==this.params[a].value)return void this.lgxInfo2(this.params[a].name+"不可为空!");if(Array.isArray(this.params[a].value)&&0==this.params[a].value.length)return void this.lgxInfo2(this.params[a].name+"不可为空!")}if(confirm("确认将该指令下发给终端设备吗?")){this.lgxInfo("正在下发终端设备指令，请稍候...");for(a=0;a<this.tids.length;a++)this.tid=this.tids[a],this.data.push({tid:this.tid,carno:carnoMap312[this.tid],time:e,status:"正在下发中，请稍等..."}),this.callApi({apicode:1109,tid:this.tid,cmdId:this.param,cmdParams:__WEBPACK_IMPORTED_MODULE_0_babel_runtime_core_js_json_stringify___default()(this.params)},function(e){t.response(e)})}}else alert("操作失败，请选择要下发的终端指令!");else alert("操作失败，请选择要下发的终端设备!")},response:function(t){if(t.tid)for(var e=0;e<this.data.length;e++)t.tid==this.data[e].tid&&(this.data[e].status=t.message,this.data[e].param=t.param)}},created:function(){var t=this;this.callApi({apicode:1108,type:1},function(e){t.list=e.data})},components:{LingxCarTree:__WEBPACK_IMPORTED_MODULE_1__components_LingxCarTree_vue__.a}}},DT5E:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a("0vsX"),s={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticStyle:{height:"100%"}},[a("el-container",{staticStyle:{height:"100%"}},[a("el-aside",{staticStyle:{"background-color":"#fff","border-right":"#c1c5cd 1px solid"},attrs:{width:"260px"}},[a("LingxCarTree",{ref:"lingxCarTree",on:{lingxcheckchange:t.lingxcheckchange}})],1),t._v(" "),a("el-container",{staticStyle:{width:"100%",height:"100%",margin:"0px",padding:"20px"}},[a("el-main",{staticStyle:{margin:"0px",padding:"0px"}},[a("el-form",{ref:"form",attrs:{model:t.form,"label-width":"120px",size:"small"}},[a("el-form-item",{attrs:{label:"指令",rules:[{required:!0}]}},[a("el-select",{staticStyle:{width:"500px"},attrs:{placeholder:"选择要下发的指令"},on:{change:t.change},model:{value:t.param,callback:function(e){t.param=e},expression:"param"}},t._l(t.list,function(t,e){return a("el-option",{key:e,attrs:{value:t.id,label:t.name}})}),1)],1),t._v(" "),t._l(t.params,function(e,i){return a("el-form-item",{key:i,attrs:{label:e.name,rules:[{required:!0}]}},["text"!=e.inputType&&e.inputType?t._e():a("el-input",{staticStyle:{width:"500px"},model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"item.value"}}),t._v(" "),"checkbox"==e.inputType?a("el-checkbox-group",{model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"item.value"}},t._l(e.optionitem,function(i,s){return a("el-checkbox",{key:s,attrs:{name:"itemcode",label:i.value,"data-value":e.value}},[a("span",{domProps:{innerHTML:t._s(i.text)}})])}),1):t._e(),t._v(" "),"combobox"==e.inputType?a("el-select",{attrs:{placeholder:""},model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"item.value"}},t._l(e.optionitem,function(t){return a("el-option",{key:t.value,attrs:{label:t.text,value:t.value}})}),1):t._e()],1)}),t._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary"},on:{click:t.sendCommand}},[t._v("发送指令至终端")])],1)],2),t._v(" "),a("table",{staticStyle:{"border-spacing":"0","border-collapse":"collapse"},attrs:{width:"100%"}},[a("thead",{staticClass:"tableHeader"},[a("tr",{staticClass:"clsf9f9f91"},[a("td"),t._v(" "),a("td",[t._v("设备号")]),t._v(" "),a("td",[t._v("车辆标识")]),t._v(" "),a("td",[t._v("下发时间")]),t._v(" "),a("td",[t._v("下发状态")]),t._v(" "),a("td",[t._v("回复内容")])])]),t._v(" "),t._l(t.data,function(e,i){return a("tr",{class:{clsf9f9f91:i%2==1}},[a("td",{attrs:{align:"center"}},[t._v(t._s(i+1))]),t._v(" "),a("td",[t._v(t._s(e.tid))]),t._v(" "),a("td",[t._v(t._s(e.carno))]),t._v(" "),a("td",[t._v(t._s(e.time))]),t._v(" "),a("td",[t._v(t._s(e.status))]),t._v(" "),a("td",[t._v(t._s(e.param))])])})],2)],1)],1)],1)],1)},staticRenderFns:[]};var r=function(t){a("DUZi")},n=a("VU/8")(i.a,s,!1,r,"data-v-35ec20d9",null);e.default=n.exports},DUZi:function(t,e){}});