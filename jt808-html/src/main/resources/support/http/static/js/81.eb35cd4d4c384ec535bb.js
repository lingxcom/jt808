webpackJsonp([81],{JgER:function(e,i){},Qftu:function(e,i,t){"use strict";Object.defineProperty(i,"__esModule",{value:!0});var o=t("V74c"),n=t("+Yga"),l=t("6bbv"),a={name:"TaskGridDialog",props:{rows:{type:Object,default:function(){}},test:{type:String,default:""}},data:function(){return{tid:"",ecode:"tdy_ota_temp3",ename:"任务",title:"升级任务",dialogVisible1:!0,queryfilter:{jobId:""}}},methods:{reloadGrid:function(){this.$refs.lingxGrid.reload()},syncename:function(e){this.ename=e},openDialog:function(e,i){this.$refs.lingxEditDialog.openDialog({title:"取消任务",ecode:this.ecode,mcode:e.code,id:i})},openViewDialog:function(e){var i=this,o=this.$refs.lingxGrid.getViewConfig();null!=o&&o.viewUri?t("MWF8")("./"+o.viewUri).then(function(t){i.mountCmp.call(i,t,{method:o,rows:e,test:"有志者事竟成"},document.querySelector("body"))}):this.$refs.lingxViewDialog.openDialog({title:this.ename+" - 查看",ecode:this.ecode,id:e.id})},closeDialog:function(){this.$refs.lingxGrid.close(),this.dialogVisible1=!1},ok:function(){}},mounted:function(){this.$set(this.queryfilter,"jobId",this.rows.id)},components:{LingxGrid:o.default,LingxEditDialog:n.default,LingxViewDialog:l.default}},d={render:function(){var e=this,i=e.$createElement,t=e._self._c||i;return t("div",[t("el-dialog",{directives:[{name:"dialogDrag",rawName:"v-dialogDrag"}],attrs:{title:e.title,visible:e.dialogVisible1,width:"720px",height:"480px","show-close":"","close-on-click-modal":!1,"append-to-body":""},on:{"update:visible":function(i){e.dialogVisible1=i}}},[t("div",{staticStyle:{height:"380px",overflow:"auto"}},[t("LingxGrid",{ref:"lingxGrid",staticStyle:{"background-color":"#fff"},attrs:{nosearchcodes:",cx_carno,",bak_nogridcodes:",speed,",isoperaterow:!0,queryfilter:e.queryfilter,ecode:e.ecode},on:{"open-dialog":e.openDialog,dblclick:e.openViewDialog}})],1),e._v(" "),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{attrs:{size:"medium"},on:{click:e.closeDialog}},[e._v("关 闭")])],1)]),e._v(" "),t("LingxEditDialog",{ref:"lingxEditDialog",on:{change:e.reloadGrid}}),e._v(" "),t("LingxViewDialog",{ref:"lingxViewDialog"})],1)},staticRenderFns:[]};var r=t("VU/8")(a,d,!1,function(e){t("JgER")},"data-v-4167b15f",null);i.default=r.exports}});