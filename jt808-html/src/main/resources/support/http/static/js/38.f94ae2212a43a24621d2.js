webpackJsonp([38],{GmuD:function(l,e){},U0Nl:function(l,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var t={name:"JT1078Param",data:function(){return{tid:"",id:"117",form:{p18:[]},issend:!1}},methods:{lingxnodeclick:function(l,e,a){this.tid=l.value,this.form={p18:[]}},search:function(){var l=this;this.tid?(this.lgxInfo("正在查询音视频属性，请稍候..."),this.callApi({apicode:1131,tid:this.tid},function(e){l.lgxInfo(e.message),e.data&&(l.form=e.data,l.issend=!0)})):this.lgxInfo2("操作失败，请选择要操作的设备!")}},components:{LingxCarTree:a("Zwc7").a}},r={render:function(){var l=this,e=l.$createElement,a=l._self._c||e;return a("div",{staticStyle:{height:"100%"}},[a("el-container",{staticStyle:{height:"100%"}},[a("el-aside",{staticStyle:{"background-color":"#fff","border-right":"#c1c5cd 1px solid"},attrs:{width:"260px"}},[a("LingxCarTree",{ref:"lingxCarTree",attrs:{isChecked:!1},on:{lingxnodeclick:l.lingxnodeclick}})],1),l._v(" "),a("el-container",{staticStyle:{width:"100%",height:"100%",margin:"0px",padding:"0px"}},[a("el-header",{staticStyle:{padding:"0px"},attrs:{height:"42"}},[a("div",{staticStyle:{margin:"5px"}},[l._v("\n             \n                 ")])]),l._v(" "),a("el-main",{staticStyle:{margin:"0px",padding:"0px"}},[a("el-form",{ref:"form",attrs:{model:l.form,"label-width":"240px",size:"small"}},[a("el-form-item",{attrs:{label:"输入音频编码方式"}},[a("el-radio-group",{model:{value:l.form.p0,callback:function(e){l.$set(l.form,"p0",e)},expression:"form.p0"}},[a("el-radio",{attrs:{label:1}},[l._v("G.721")]),l._v(" "),a("el-radio",{attrs:{label:2}},[l._v("G.722")]),l._v(" "),a("el-radio",{attrs:{label:3}},[l._v("G.723")]),l._v(" "),a("el-radio",{attrs:{label:4}},[l._v("G.728")]),l._v(" "),a("el-radio",{attrs:{label:5}},[l._v("G.729")]),l._v(" "),a("el-radio",{attrs:{label:6}},[l._v("G.711A")]),l._v(" "),a("el-radio",{attrs:{label:7}},[l._v("G.711U")]),l._v(" "),a("el-radio",{attrs:{label:8}},[l._v("G.726")]),l._v(" "),a("el-radio",{attrs:{label:9}},[l._v("G.729A")]),l._v(" "),a("el-radio",{attrs:{label:10}},[l._v("DVI4_3")]),l._v(" "),a("el-radio",{attrs:{label:11}},[l._v("DVI4_4")]),l._v(" "),a("el-radio",{attrs:{label:12}},[l._v("DVI4_8K")]),l._v(" "),a("el-radio",{attrs:{label:13}},[l._v("DVI4_16K")]),l._v(" "),a("el-radio",{attrs:{label:14}},[l._v("LPC")]),l._v(" "),a("el-radio",{attrs:{label:15}},[l._v("S16BE_STEREO")]),l._v(" "),a("el-radio",{attrs:{label:16}},[l._v("S16BE_MONO")]),l._v(" "),a("el-radio",{attrs:{label:17}},[l._v("MPEGAUDIO")]),l._v(" "),a("el-radio",{attrs:{label:18}},[l._v("LPCM")]),l._v(" "),a("el-radio",{attrs:{label:19}},[l._v("AAC")]),l._v(" "),a("el-radio",{attrs:{label:20}},[l._v("WMA9STD")]),l._v(" "),a("el-radio",{attrs:{label:21}},[l._v("HEAAC")]),l._v(" "),a("el-radio",{attrs:{label:22}},[l._v("PCM_VOICE")]),l._v(" "),a("el-radio",{attrs:{label:23}},[l._v("PCM_AUDIO")]),l._v(" "),a("el-radio",{attrs:{label:24}},[l._v("AACLC")]),l._v(" "),a("el-radio",{attrs:{label:25}},[l._v("MP3")]),l._v(" "),a("el-radio",{attrs:{label:26}},[l._v("ADPCMA")]),l._v(" "),a("el-radio",{attrs:{label:27}},[l._v("MP4AUDIO")]),l._v(" "),a("el-radio",{attrs:{label:28}},[l._v("ARM ")])],1)],1),l._v(" "),a("el-form-item",{attrs:{label:"输入音频声道数"}},[a("el-input",{model:{value:l.form.p1,callback:function(e){l.$set(l.form,"p1",e)},expression:"form.p1"}})],1),l._v(" "),a("el-form-item",{attrs:{label:"输入音频采样率"}},[a("el-radio-group",{model:{value:l.form.p2,callback:function(e){l.$set(l.form,"p2",e)},expression:"form.p2"}},[a("el-radio",{attrs:{label:0}},[l._v("8 kHz")]),l._v(" "),a("el-radio",{attrs:{label:1}},[l._v("22.05 kHz")]),l._v(" "),a("el-radio",{attrs:{label:2}},[l._v("44.1 kHz")]),l._v(" "),a("el-radio",{attrs:{label:3}},[l._v("48 kHz")])],1)],1),l._v(" "),a("el-form-item",{attrs:{label:"输入音频采样位数"}},[a("el-radio-group",{model:{value:l.form.p3,callback:function(e){l.$set(l.form,"p3",e)},expression:"form.p3"}},[a("el-radio",{attrs:{label:0}},[l._v("8位")]),l._v(" "),a("el-radio",{attrs:{label:1}},[l._v("16位")]),l._v(" "),a("el-radio",{attrs:{label:2}},[l._v("32位")])],1)],1),l._v(" "),a("el-form-item",{attrs:{label:"音频帧长度"}},[a("el-input",{model:{value:l.form.p4,callback:function(e){l.$set(l.form,"p4",e)},expression:"form.p4"}})],1),l._v(" "),a("el-form-item",{attrs:{label:"是否支持音频输出"}},[a("el-radio-group",{model:{value:l.form.p6,callback:function(e){l.$set(l.form,"p6",e)},expression:"form.p6"}},[a("el-radio",{attrs:{label:0}},[l._v("不支持")]),l._v(" "),a("el-radio",{attrs:{label:1}},[l._v("支持")])],1)],1),l._v(" "),a("el-form-item",{attrs:{label:"视频编码方式"}},[a("el-radio-group",{model:{value:l.form.p7,callback:function(e){l.$set(l.form,"p7",e)},expression:"form.p7"}},[a("el-radio",{attrs:{label:98}},[l._v("H.264")]),l._v(" "),a("el-radio",{attrs:{label:99}},[l._v("H.265")]),l._v(" "),a("el-radio",{attrs:{label:100}},[l._v("AVS")]),l._v(" "),a("el-radio",{attrs:{label:101}},[l._v("SVAC")])],1)],1),l._v(" "),a("el-form-item",{attrs:{label:"终端支持的最大音频物理通道数量"}},[a("el-input",{model:{value:l.form.p8,callback:function(e){l.$set(l.form,"p8",e)},expression:"form.p8"}})],1),l._v(" "),a("el-form-item",{attrs:{label:"终端支持的最大视频物理通道数量"}},[a("el-input",{model:{value:l.form.p9,callback:function(e){l.$set(l.form,"p9",e)},expression:"form.p9"}})],1),l._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"success"},on:{click:l.search}},[l._v("查询音视频属性")])],1)],1)],1)],1)],1)],1)},staticRenderFns:[]};var o=a("VU/8")(t,r,!1,function(l){a("GmuD")},null,null);e.default=o.exports}});