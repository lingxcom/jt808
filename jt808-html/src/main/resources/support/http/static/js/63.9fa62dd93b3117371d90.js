webpackJsonp([63],{a0B3:function(e,t){},qCAP:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i={name:"TTSDialog",props:{deviceId:{type:String,default:""}},data:function(){return{dialogVisible:!1,textarea:""}},methods:{sendTTS:function(){var e=this;this.lgxInfo("正在下发指令,请稍候..."),this.callApi({apicode:1109,tid:this.deviceId,cmdId:61483,cmdParams:'[{"name":"类型","code":"p1","value":[3]},{"name":"文本","code":"p2","value":"'+this.textarea+'"}]'},function(t){e.lgxInfo(t.message)})},openDialog:function(){this.dialogVisible=!0},handleClose:function(e){this.dialogVisible=!1}}},o={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[e.dialogVisible?a("el-dialog",{directives:[{name:"dialogDrag",rawName:"v-dialogDrag"}],attrs:{title:"下发TTS语音",visible:e.dialogVisible,width:"720px",height:"480px","show-close":"","append-to-body":"","close-on-click-modal":!1,"before-close":e.handleClose}},[a("div",{staticStyle:{width:"100%",height:"380px",overflow:"auto","background-color":"#dfe9f6","text-align":"center"}},[a("el-input",{staticStyle:{width:"60%","margin-top":"20px"},attrs:{type:"textarea",rows:5,placeholder:"请输入内容"},model:{value:e.textarea,callback:function(t){e.textarea=t},expression:"textarea"}}),e._v(" "),a("br"),e._v(" "),a("br"),e._v(" "),a("el-button",{attrs:{type:"primary",size:"small"},on:{click:e.sendTTS}},[e._v("下发TTS指令到终端设备")])],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:e.handleClose}},[e._v("关 闭")])],1)]):e._e()],1)},staticRenderFns:[]};var l=a("VU/8")(i,o,!1,function(e){a("a0B3")},null,null);t.default=l.exports}});