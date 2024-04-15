webpackJsonp([10,72],{BmUA:function(e,t){},R2B3:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=n("eEqi"),a=n.n(i),l=n("ue5y"),o=n.n(l),r={name:"TalkDialog",props:{deviceId:{type:String,default:""}},data:function(){return{tdh:1,dialogVisible:!1,talkImg:a.a,info:"",isTalking:!1,options:[{label:"通道1",value:1},{label:"通道2",value:2},{label:"通道3",value:3},{label:"通道4",value:4},{label:"通道5",value:5},{label:"通道6",value:6},{label:"通道7",value:7},{label:"通道8",value:8}]}},methods:{start:function(){var e=this;this.talkImg=o.a,this.isTalking=!0,this.lgxInfo("正在启动对讲中，请稍候..."),this.callApi({apicode:1148,deviceId:this.deviceId,channel:this.tdh},function(t){e.lgxInfo("已成功连接服务器，可以开始对讲！"),s.start(t.pull_address)})},stop:function(){this.talkImg=a.a,this.isTalking=!1,this.lgxInfo("正在关闭对讲！"),audio.stop(),this.callApi({apicode:1148,deviceId:this.deviceId,channel:1,type:2},function(e){})},openDialog:function(){this.dialogVisible=!0},handleClose:function(e){this.dialogVisible=!1}}},s=new function(){document.getElementById("audio_info");var e,t=document.createElement("audio"),n=document.createElement("audio"),i=[],a=0,l=null,o=[],r=!0;this.start=function(s){if(document.getElementById("audio_info")&&(document.getElementById("audio_info").innerHTML="正在连接对讲服务器..."),(e=new WebSocket(s)).onopen=function(){console.log("音频对讲websoket已连接"),document.getElementById("audio_info")&&(document.getElementById("audio_info").innerHTML="音频对讲websoket已连接")},e.onmessage=function(e){var i=e.data;if(i instanceof Blob&&(o.push(i),25==o.length)){console.log("已收到一秒音频："+(new Date).getTime()),document.getElementById("audio_info")&&(document.getElementById("audio_info").innerHTML="已收到一秒音频："+(new Date).format("yyyy-MM-dd HH:mm:ss"));var a=new Blob(o),l=new FileReader;l.readAsArrayBuffer(a),l.onload=function(e){var i=l.result,a=new DataView(i);r?(t.src=window.URL.createObjectURL(d(a)),t.play()):(n.src=window.URL.createObjectURL(d(a)),n.play()),r=!r},o=[]}},e.onclose=function(){l&&l.disconnect(),console.log("音频对讲websoket已关闭"),document.getElementById("audio_info")&&(document.getElementById("audio_info").innerHTML="对讲服务器已断开连接。")},!navigator.getUserMedia)return alert("抱歉, 您的设备无法语音对讲"),!1;i=[],a=0;var u=new(window.AudioContext||window.webkitAudioContext||window.mozAudioContext||window.msAudioContext);(l=u.createScriptProcessor(4096,1,1)).onaudioprocess=function(t){var n=t.inputBuffer.getChannelData(0);i.push(new Float32Array(n)),a+=n.length,function(){if(0!==a){var t=c();if(t){var n=new Blob([t]);e.send(n)}}}(),i=[],a=0};var h=null;navigator.mediaDevices.getUserMedia({audio:!0}).then(function(e){h=u.createMediaStreamSource(e)}).catch(function(e){console.log("error")}).then(function(){h.connect(l),l.connect(u.destination)})},this.stop=function(){e.close()};var s=16;function c(){var e=function(){for(var e=new Float32Array(a),t=0,n=0;n<i.length;n++)e.set(i[n],t),t+=i[n].length;var l=parseInt(6),o=e.length/l,r=new Float32Array(o),s=0,c=0;for(;s<o;)r[s]=e[c],c+=l,s++;return r}(),t=s,n=0,l=e.length*(t/8),o=new ArrayBuffer(l),r=new DataView(o);if(!(l>65535)){if(8===t)for(var c=0;c<e.length;c++,n++){var d=Math.max(-1,Math.min(1,e[c])),u=d<0?128*d:127*d;u=parseInt(u+128),r.setInt8(n,u,!0)}else for(var c=0;c<e.length;c++,n+=2){var d=Math.max(-1,Math.min(1,e[c]));r.setInt16(n,d<0?32768*d:32767*d,!0)}return r}console.log("数据过长："+l)}function d(e){var t=e.byteLength,n=new ArrayBuffer(44+t),i=new DataView(n),a=0,l=function(e){for(var t=0;t<e.length;t++)i.setUint8(a+t,e.charCodeAt(t))};l("RIFF"),a+=4,i.setUint32(a,36+t,!0),a+=4,l("WAVE"),a+=4,l("fmt "),a+=4,i.setUint32(a,16,!0),a+=4,i.setUint16(a,6,!0),a+=2,i.setUint16(a,1,!0),a+=2,i.setUint32(a,8e3,!0),a+=4,i.setUint32(a,16e3,!0),a+=4,i.setUint16(a,2,!0),a+=2,i.setUint16(a,16,!0),a+=2,l("data"),a+=4,i.setUint32(a,t,!0),a+=4;for(var o=0;o<t;o++)i.setInt8(a,e.getInt8(o),!0),a++;return new Blob([i],{type:"audio/wav"})}};var c={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",[e.dialogVisible?n("el-dialog",{directives:[{name:"dialogDrag",rawName:"v-dialogDrag"}],attrs:{title:"实时对讲",visible:e.dialogVisible,width:"720px",height:"480px","show-close":"","append-to-body":"","close-on-click-modal":!1,"before-close":e.handleClose}},[n("div",{staticStyle:{width:"100%",height:"380px",overflow:"auto","background-color":"#dfe9f6","text-align":"center"}},[n("div",[n("br"),e._v(" "),n("center",[n("div",{attrs:{id:"audio_info"}})]),e._v(" "),n("br"),e._v(" "),n("img",{attrs:{src:e.talkImg}}),n("br"),e._v(" "),n("el-select",{staticStyle:{width:"90px",display:"none"},attrs:{placeholder:"请选择"},model:{value:e.tdh,callback:function(t){e.tdh=t},expression:"tdh"}},e._l(e.options,function(e){return n("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}),1),e._v(" "),n("el-button",{attrs:{type:"primary",disabled:e.isTalking},on:{click:e.start}},[e._v("启动对讲")]),e._v(" "),n("el-button",{attrs:{type:"danger",disabled:!e.isTalking},on:{click:e.stop}},[e._v("关闭对讲")])],1)]),e._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:e.handleClose}},[e._v("关 闭")])],1)]):e._e()],1)},staticRenderFns:[]};var d=n("VU/8")(r,c,!1,function(e){n("BmUA")},null,null);t.default=d.exports},WnQo:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=n("cDwS"),a=n("RfNU"),l=n("29oq"),o=n("R2B3"),r={name:"VideoRealtime",data:function(){return{currentTid:"",currentCarno:"",lastCar:{},currentWin:1,mltype:1,winnum:9,width:"33.333%",height:"33.333%",options1:[{label:"清晰(主码流)",value:0},{label:"流畅(子码流)",value:1}],options2:[{label:"1行x1列",value:1},{label:"2行x2列",value:4},{label:"3行x3列",value:9},{label:"4行x4列",value:16},{label:"5行x5列",value:25}]}},methods:{openTalkDialog:function(){this.$refs.talkDialog.openDialog()},selectWin:function(e,t){var n;this.currentWin=e;for(var i=0;i<10;i++)if($(t.path[i]).hasClass("borderDiv")){n=$(t.path[i]);break}this.$refs.lingxBorder.show(n)},closeAll:function(){for(var e=0;e<this.winnum;e++)this.$refs.player[e].stop()},lingxcheckchange:function(e,t,n){},lingxnodedblclick:function(e,t,n){if((e.value+"").length>=12)for(var i=t.childNodes,a=0;a<i.length;a++){var l=i[a],o=l.data.id.substring(0,l.data.id.indexOf("_")),r=l.data.value,s=t.data.text;this.initPlay({carid:o,carno:s,tdh:r,tdhStr:l.data.text})}console.log(t)},lingxnodeclick:function(e,t,n){if(e.tdh){var i=n.node.parent.data.text,a=e.id.substring(0,e.id.indexOf("_")),l=e.value;return this.initPlay({carid:a,carno:i,tdh:l,tdhStr:e.text}),this.currentTid=a+"",void(this.currentCarno=i)}this.currentTid=e.value+"",this.currentCarno=e.text},initPlay:function(e){for(var t=e.carid+"_"+e.tdh,n=0;n<this.$refs.player.length;n++)if(this.$refs.player[n].playKey==t)return this.lgxInfo("该视频已经在第"+(n+1)+"窗口播放"),void this.$refs.player[n].play(e);this.lgxInfo("将在第"+this.currentWin+"窗口播放"+e.carno+"-"+e.tdhStr),this.$refs.player[this.currentWin-1].play(e),this.$refs.player[this.currentWin-1].playKey=t,this.currentWin++,this.currentWin>this.winnum&&(this.currentWin=1)},formatDatetime:function(e,t,n,i){return n?n.substring(0,4)+"-"+n.substring(4,6)+"-"+n.substring(6,8)+" "+n.substring(8,10)+":"+n.substring(10,12)+":"+n.substring(12,14):""},play:function(){if(mpegts.getFeatureList().mseLivePlayback){var e=document.getElementById("videoElement"),t=mpegts.createPlayer({type:"mse",isLive:!0,hasAudio:!1,hasVideo:!0,url:'wss://www.gb35658.com/wss6899/websocket?token=123456&params={"tdh":"1","tid":"018000032600","type":1}'},{enableStashBuffer:!1,fixAudioTimestampGap:!1,liveBufferLatencyChasing:!1});t.attachMediaElement(e),t.load(),t.play()}},createListVideo:function(){this.listVideo=[];for(var e=0;e<this.winnum;e++)this.listVideo.push({id:e+1})}},watch:{winnum:function(e){this.$refs.lingxBorder.hide(),this.width=100/Math.sqrt(e)+"%",this.height=100/Math.sqrt(e)+"%",this.currentWin>e&&(this.currentWin=1)}},components:{GB28181Tree:a.default,GB28181Video:l.default,TalkDialog:o.default,LingxBorder:i.default}},s={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("el-container",{staticStyle:{height:"100%"}},[n("el-aside",{staticStyle:{"background-color":"#fff","border-right":"#c1c5cd 1px solid"},attrs:{width:"300px"}},[n("GB28181Tree",{ref:"lingxCarTree",attrs:{isVideo:!0,isChecked:!1},on:{lingxcheckchange:e.lingxcheckchange,lingxnodeclick:e.lingxnodeclick,lingxnodedblclick:e.lingxnodedblclick}})],1),e._v(" "),n("el-container",{staticStyle:{height:"100%"}},[n("el-header",{staticStyle:{height:"39px","line-height":"39px","background-color":"#e8eaed","padding-left":"10px"}},[n("el-button",{attrs:{size:"mini",type:"success"},on:{click:e.openTalkDialog}},[e._v("语音对讲")]),e._v(" "),n("el-button",{attrs:{size:"mini",type:"warning"},on:{click:e.closeAll}},[e._v("全部关闭")]),e._v(" "),n("el-select",{staticStyle:{width:"130px"},attrs:{size:"mini",placeholder:"请选择"},model:{value:e.winnum,callback:function(t){e.winnum=t},expression:"winnum"}},e._l(e.options2,function(e){return n("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}),1),e._v(" "),n("div",{staticStyle:{float:"right","font-size":"12px"}},[e._v("\n              下一播放窗口:"),n("span",{staticStyle:{color:"red"}},[e._v(e._s(e.currentWin))])])],1),e._v(" "),n("el-main",{ref:"main",staticStyle:{margin:"0px",padding:"0px"}},e._l(e.winnum,function(t){return n("div",{key:t,staticClass:"borderDiv",style:{display:"inline-block",width:e.width,height:e.height,padding:"0px",margin:"0px","vertical-align":"top"},on:{click:function(n){return n.stopPropagation(),e.selectWin(t,n)}}},[n("GB28181Video",{ref:"player",refInFor:!0,attrs:{mltype:e.mltype,name:t+"."}})],1)}),0)],1),e._v(" "),n("LingxBorder",{ref:"lingxBorder"}),e._v(" "),n("TalkDialog",{ref:"talkDialog",attrs:{deviceId:e.currentTid}})],1)},staticRenderFns:[]};var c=n("VU/8")(r,s,!1,function(e){n("b3Cq")},"data-v-492b20ae",null);t.default=c.exports},b3Cq:function(e,t){}});