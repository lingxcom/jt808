webpackJsonp([39],{CX5m:function(t,e){},qWys:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});i("TFOu");var a=i("ow4a"),r={name:"Map4326",mounted:function(){this.lingxMap=new a.g("map",{center:[119.54,32.6],zoom:6,minZoom:1,maxZoom:20,scaleControl:{position:"bottom-right",maxWidth:100,metric:!0,imperial:!1},dragPitch:!0,dragRotate:!0,dragRotatePitch:!0,attribution:!1,spatialReference:{projection:"EPSG:4326"},baseLayer:new a.l("wms",{tileSystem:[1,-1,-180,90],urlTemplate:"http://112.4.100.83:8081/geoserver/taizhoushi/wms",crs:"EPSG:4326",layers:"taizhoushi:zhuhe",styles:"",version:"1.3.0",format:"image/jpeg",transparent:!0,uppercase:!0})})}},n={render:function(){this.$createElement;this._self._c;return this._m(0)},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticStyle:{width:"100%",height:"100%",position:"relative"}},[e("div",{staticClass:"container",attrs:{id:"map"}})])}]};var s=i("VU/8")(r,n,!1,function(t){i("CX5m")},"data-v-3719d68a",null);e.default=s.exports}});