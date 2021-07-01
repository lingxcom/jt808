
	 Ext.define('Option',{
        extend: 'Ext.data.Model',
        fields: [
            {name: 'text', type: 'string'},
            {name: 'value', type: 'string'}
        ]
    });	 
	 Ext.define("Lingx.form.Password",{
		extend: 'Ext.form.field.Text',
		xtype:'password',
		inputType:'password'
	});
	 /**
	  * 带时分秒的日期控件
	  * @author
	  */

	 Ext.define('Ext.ux.form.field.DateTime', {
	     extend:'Ext.form.field.Date',
	     alias: 'widget.datetimefield',
	     //requires: ['Ext.ux.picker.DateTime'],
	     alternateClassName: ['Ext.form.DateTimeField', 'Ext.form.DateTime'],

	     //<locale>
	     /**
	      * @cfg {String} format
	      * The default date format string which can be overriden for localization support. The format must be valid
	      * according to {@link Ext.Date#parse}.
	      */
	     format : "Y-m-d H:i:s",
	     //</locale>
	     /**
	      * @cfg {Boolean} showTime
	      * false to hide the footer area of the Time spinner for selects the current time.
	      */
	     showTime : true,

	     initComponent : function(){
	         var me = this;
	         me.callParent();
	     },

	     initValue: function() {
	         var me = this;
	         me.callParent();
	     },

	     createPicker: function() {
	         var me = this,
	             format = Ext.String.format;

	         return new Ext.ux.picker.DateTime({
	             pickerField: me,
	             ownerCt: me.ownerCt,
	             renderTo: document.body,
	             floating: true,
	             hidden: true,
	             focusOnShow: true,
	             minDate: me.minValue,
	             maxDate: me.maxValue,
	             disabledDatesRE: me.disabledDatesRE,
	             disabledDatesText: me.disabledDatesText,
	             disabledDays: me.disabledDays,
	             disabledDaysText: me.disabledDaysText,
	             format: me.format,
	             showToday: me.showToday,
	             showTime: me.showTime,
	             startDay: me.startDay,
	             minText: format(me.minText, me.formatDate(me.minValue)),
	             maxText: format(me.maxText, me.formatDate(me.maxValue)),
	             listeners: {
	                 scope: me,
	                 select: me.onSelect
	             },
	             keyNavConfig: {
	                 esc: function() {
	                     me.collapse();
	                 }
	             }
	         });
	     },

	     /**
	      * @private
	      * Sets the DateTime picker's value to match the current field value when expanding.
	      */
	     onExpand: function() {
	         var value = this.getValue();
	         //this.picker.setValue(Ext.isDate(value) ? value : new Date());
	         
	         value = Ext.isDate(value) ? value : Ext.Date.clearTime(new Date());

	         this.picker.setValue(value);
	     }
	 });

	 Ext.define('Ext.ux.picker.DateTime', {
	     extend: 'Ext.picker.Date',
	     requires: [
	         'Ext.XTemplate',
	         'Ext.button.Button',
	         'Ext.button.Split',
	         'Ext.util.ClickRepeater',
	         'Ext.util.KeyNav',
	         'Ext.EventObject',
	         'Ext.fx.Manager',
	         'Ext.picker.Month'
	     ],
	     alias: 'widget.datetimepicker',
	     alternateClassName: 'Ext.DateTimePicker',
	     renderTpl: [
	         '<div id="{id}-innerEl" role="grid">',
	             '<div role="presentation" class="{baseCls}-header">',
	                 '<div class="{baseCls}-prev"><a id="{id}-prevEl" href="#" role="button" title="{prevText}"></a></div>',
	                 '<div class="{baseCls}-month" id="{id}-middleBtnEl">{%this.renderMonthBtn(values, out)%}</div>',
	                 '<div class="{baseCls}-next"><a id="{id}-nextEl" href="#" role="button" title="{nextText}"></a></div>',
	             '</div>',
	             '<table id="{id}-eventEl" class="{baseCls}-inner" cellspacing="0" role="presentation">',
	                 '<thead role="presentation"><tr role="presentation">',
	                     '<tpl for="dayNames">',
	                         '<th role="columnheader" title="{.}"><span>{.:this.firstInitial}</span></th>',
	                     '</tpl>',
	                 '</tr></thead>',
	                 '<tbody role="presentation"><tr role="presentation">',
	                     '<tpl for="days">',
	                         '{#:this.isEndOfWeek}',
	                         '<td role="gridcell" id="{[Ext.id()]}">',
	                             '<a role="presentation" href="#" hidefocus="on" class="{parent.baseCls}-date" tabIndex="1">',
	                                 '<em role="presentation"><span role="presentation"></span></em>',
	                             '</a>',
	                         '</td>',
	                     '</tpl>',
	                 '</tr></tbody>',
	             '</table>',
	             '<tpl if="showFooter">',
	                 '<div id="{id}-footerEl" role="presentation" class="{baseCls}-footer">{%this.renderFooterContainer(values, out)%}</div>',
	             '</tpl>',
	         '</div>',
	         {
	             firstInitial: function(value) {
	                 return Ext.ux.picker.DateTime.prototype.getDayInitial(value);
	             },
	             isEndOfWeek: function(value) {
	                 // convert from 1 based index to 0 based
	                 // by decrementing value once.
	                 value--;
	                 var end = value % 7 === 0 && value !== 0;
	                 return end ? '</tr><tr role="row">' : '';
	             },
	             renderFooterContainer: function(values, out) {
	                 Ext.DomHelper.generateMarkup(values.$comp.footerContainer.getRenderTree(), out);
	             },
	             renderMonthBtn: function(values, out) {
	                 Ext.DomHelper.generateMarkup(values.$comp.monthBtn.getRenderTree(), out);
	             }
	         }
	     ],

	     //<locale>
	     /**
	      * @cfg {String} todayText
	      * The text to display on the button that selects the current date
	      */
	     todayText : '今日',
	     //</locale>

	     //<locale>
	     /**
	      * @cfg {String} todayText
	      * The text to display on the button that selects the current date
	      */
	     confirmText : '确定',
	     //</locale>
	     
	     //<locale>
	     /**
	      * @cfg {String} ariaTitle
	      * The text to display for the aria title
	      */
	     ariaTitle: 'Date Picker: {0}',
	     //</locale>
	     
	     //<locale>
	     /**
	      * @cfg {String} ariaTitleDateFormat
	      * The date format to display for the current value in the {@link #ariaTitle}
	      */
	     ariaTitleDateFormat: 'F d H:i:s, Y',
	     //</locale>

	     //<locale>
	     /**
	      * @cfg {Boolean} showToday
	      * False to hide the footer area containing the Today button and disable the keyboard handler for spacebar that
	      * selects the current date.
	      */
	     showToday : true,
	     //</locale>

	     //<locale>
	     /**
	      * @cfg {Boolean} showToday
	      * False to hide the footer area containing the Today button and disable the keyboard handler for spacebar that
	      * selects the current date.
	      */
	     showTime : true,
	     //</locale>

	     //<locale>
	     /**
	      * @cfg {String} longDayFormat
	      * The format for displaying a date in a longer format.
	      */
	     longDayFormat: 'F d H:i:s, Y',
	     //</locale>

	     // private, inherit docs
	     initComponent : function() {
	         var me = this;
	         
	         var value = me.value;

	         this.callParent();

	         if (me.showTime) {

	             me.value = value || me.value;

	             me.listeners = Ext.apply(me.listeners||{}, {
	                 dblclick: {
	                     element: 'eventEl',
	                     fn: me.handleDateDblClick, 
	                     scope: me,
	                     delegate: 'a.' + me.baseCls + '-date'
	                 }
	             });
	         }

	     },

	     beforeRender: function () {
	         /*
	          * days array for looping through 6 full weeks (6 weeks * 7 days)
	          * Note that we explicitly force the size here so the template creates
	          * all the appropriate cells.
	          */
	         var me = this,
	             today = Ext.Date.format(new Date(), me.format);

	         me.callParent();
	         
	         if (me.todayBtn) {
	             delete me.todayBtn.ownerCt;
	             delete me.todayBtn.ownerLayout;
	             Ext.destroy(me.todayBtn);
	             delete me.todayBtn;
	             me.todayBtn = new Ext.button.Button({
	                 //ownerCt: me,
	                 //ownerLayout: me.getComponentLayout(),
	                 //margin: 0,
	                 text: Ext.String.format(me.todayText, today),
	                 tooltip: Ext.String.format(me.todayTip, today),
	                 tooltipType: 'title',
	                 height:21,
	                 handler: me.selectToday,
	                 scope: me
	             });
	         }

	         var footerItems = [];
	         
	         var footerColumns = 3;
	         
	         if (me.showTime) {
	             me.hour = Ext.create('Ext.form.field.Number', {
	                 minValue: 0,
	                 maxValue: 23,
	                 width: 39,
	                 enableKeyEvents: true,
	                 scope: me,
	                 //fieldLabel: '时',
	                 //labelWidth: 20,
	                 hideLabel : true,
	                 style: {
	                     //marginLeft: '2px',
	                     marginTop: '5px'
	                 },
	                 listeners: {
	                     scope: me,
	                     change: function(field, e){
	                         this.value.setHours(field.getValue());
	                     },
	                     blur: function(field, e){
	                         this.update(this.value,true);
	                     },
	                     keyup: function(field, e){
	                         if (field.getValue() > 23){
	                             e.stopEvent();
	                             field.setValue(23);
	                         }
	                     }
	                  }
	             });
	             
	             me.minute = Ext.create('Ext.form.field.Number', {
	                 minValue: 0,
	                 maxValue: 59,
	                 width: 39,
	                 enableKeyEvents: true,
	                 scope: me,
	                 //fieldLabel: '分',
	                 //labelWidth: 20,
	                 hideLabel : true,
	                 style: {
	                     marginLeft: '2px',
	                     marginTop: '5px'
	                 },
	                 listeners: {
	                     scope: me,
	                     change: function(field, e){
	                         this.value.setMinutes(field.getValue());
	                     },
	                     blur: function(field, e){
	                         this.update(this.value,true);
	                     },
	                     keyup: function(field, e){
	                         if (field.getValue() > 59){
	                             e.stopEvent();
	                             field.setValue(59);
	                         }
	                     }
	                 }
	             });
	             
	             me.second = Ext.create('Ext.form.field.Number', {
	                 minValue: 0,
	                 maxValue: 59,
	                 width: 39,
	                 enableKeyEvents: true,
	                 scope: me,
	                 //fieldLabel: '秒',
	                 //labelWidth: 20,
	                 hideLabel : true,
	                 style: {
	                     marginLeft: '2px',
	                     marginTop: '5px'
	                 },
	                 listeners: {
	                     scope: me,
	                     change: function(field, e){
	                         this.value.setSeconds(field.getValue());
	                     },
	                     blur: function(field, e){
	                         this.update(this.value,true);
	                     },
	                     keyup: function(field, e){
	                         if (field.getValue() > 59){
	                             e.stopEvent();
	                             field.setValue(59);
	                         }
	                     }
	                 }
	             });
	             footerItems.push(me.hour);
	             footerItems.push(me.minute);
	             footerItems.push(me.second);
	             me.confirmBtn = new Ext.button.Button({
	                 //ownerCt: me,
	                 //ownerLayout: me.getComponentLayout(),
	                 //margin: 0,
	                 text: Ext.String.format(me.confirmText, today),
	                 //tooltip: Ext.String.format(me.confirmTip, today),
	                 //tooltipType: 'title',
	                 height:21,
	                 handler: me.confirmClick,
	                 scope: me
	             });
	         }//*/
	         
	         if (me.showTime && me.showToday) {
	             footerItems.push(Ext.create('Ext.container.Container', {
	                 //renderTo: document.body,
	                 //ownerCt: me,
	                 //wnerLayout: me.getComponentLayout(),
	                 colspan: footerColumns,
	                 layout: {
	                     type:'table',
	                     tableAttrs: {
	                         align:'center'
	                     },
	                     tdAttrs: {align:'center'},
	                     columns: 2
	                     //type: 'hbox',
	                     //align: 'middle'
	                     //type: 'vbox',
	                     //align: 'center'
	                 },
	                 defaults: {
	                 },
	                 items: [me.todayBtn,me.confirmBtn],
	                 scope: me
	             }));
	         } else if (me.showToday) {
	             footerColumns = 1;
	             footerItems.push(me.todayBtn);
	         } else if (me.showTime) {
	             footerColumns = 4;
	             footerItems.push(me.confirmBtn);
	         }
	         
	         if (footerItems.length>0) {
	             me.footerContainer = Ext.create('Ext.container.Container', {
	                 //renderTo: document.body,
	                 //ownerCt: me,
	                 //ownerLayout: me.getComponentLayout(),
	                 layout: {
	                     type:'table',
	                     tableAttrs: {
	                         align:'center'
	                     },
	                     tdAttrs: {align:'center'},
	                     columns: footerColumns
	                 },
	                 items: footerItems,
	                 scope: me
	             });
	         }

	         Ext.apply(me.renderData, {
	             showFooter: me.isShowFooter()
	         });
	     },

	     /**
	      * Get the current active date.
	      * @private
	      * @return {Date} The active date
	      */
	     isShowFooter: function(){
	         return this.showToday || this.showTime;
	     },

	     // Do the job of a container layout at this point even though we are not a Container.
	     // TODO: Refactor as a Container.
	     finishRenderChildren: function () {
	         var me = this;
	         
	         me.callParent();
	         //*
	         if (me.showTime) {
	             me.hour.finishRender();
	             me.minute.finishRender();
	             me.second.finishRender();
	         }//*/
	         if (me.isShowFooter()) {
	         	me.footerContainer.finishRender();
	         }
	     },

	     /**
	      * Sets the value of the date field
	      * @param {Date} value The date to set
	      * @return {Ext.ux.picker.DateTime} this
	      */
	     setValue : function(value){
	     	//this.value = Ext.Date.clearTime(value, true);
	         this.value = value;
	         if (this.showTime) {
	             this.hour.setValue(this.value.getHours());
	             this.minute.setValue(this.value.getMinutes());
	             this.second.setValue(this.value.getSeconds());
	         }
	         return this.update(this.value);
	     },
	     
	     selectDate: function(value) {
	         if (this.showTime) {
	             value.setHours(this.hour.getValue());
	             value.setMinutes(this.minute.getValue());
	             value.setSeconds(this.second.getValue());
	         }
	         this.value = value;
	         return this.update(this.value);
	     },

	     /**
	      * Set the disabled state of various internal components
	      * @private
	      * @param {Boolean} disabled
	      */
	     setDisabledStatus : function(disabled){
	         var me = this;

	         me.callParent();

	         if (me.showTime) {
	             me.confirmBtn.setDisabled(disabled);
	         }
	         if (me.isShowFooter()) {
	         	me.footerContainer.setDisabled(disabled);
	         }
	     },

	     /**
	      * Respond to an ok click on the month picker
	      * @private
	      */
	     onOkClick: function(picker, value){
	         var me = this,
	             month = value[0],
	             year = value[1],
	             date = new Date(year, month, me.getActive().getDate());

	         if (date.getMonth() !== month) {
	             // 'fix' the JS rolling date conversion if needed
	             date = Ext.Date.getLastDateOfMonth(new Date(year, month, 1));
	         }
	         /*
	         if (me.showTime) {
	             date.setHours(me.hour.getValue());
	             date.setMinutes(me.minute.getValue());
	             date.setSeconds(me.second.getValue());
	         }//*/
	         me.update(date);
	         me.hideMonthPicker();
	     },

	     /**
	      * Respond to a date being clicked in the picker
	      * @private
	      * @param {Ext.EventObject} e
	      * @param {HTMLElement} t
	      */
	     handleDateClick : function(e, t){
	         var me = this,
	             handler = me.handler;

	         e.stopEvent();
	         if(!me.disabled && t.dateValue && !Ext.fly(t.parentNode).hasCls(me.disabledCellCls)){
	             me.doCancelFocus = me.focusOnSelect === false;
	             //me.setValue(new Date(t.dateValue));
	             
	             me.selectDate(new Date(t.dateValue));
	             
	             delete me.doCancelFocus;
	             
	             /* 有时间输入时单击事件不退出选择，只移动光标 */
	             if (me.showTime) return;

	             me.fireEvent('select', me, me.value);
	             if (handler) {
	                 handler.call(me.scope || me, me, me.value);
	             }
	             // event handling is turned off on hide
	             // when we are using the picker in a field
	             // therefore onSelect comes AFTER the select
	             // event.
	             me.onSelect();
	         }
	     },

	     /**
	      * Respond to a date being clicked in the picker
	      * @private
	      * @param {Ext.EventObject} e
	      * @param {HTMLElement} t
	      */
	     handleDateDblClick : function(e, t){
	         var me = this,
	             handler = me.handler;

	         e.stopEvent();
	         if(!me.disabled && t.dateValue && !Ext.fly(t.parentNode).hasCls(me.disabledCellCls)){
	             me.doCancelFocus = me.focusOnSelect === false;
	             //me.setValue(new Date(t.dateValue));
	             
	             me.selectDate(new Date(t.dateValue));

	             delete me.doCancelFocus;
	             me.fireEvent('select', me, me.value);
	             if (handler) {
	                 handler.call(me.scope || me, me, me.value);
	             }
	             // event handling is turned off on hide
	             // when we are using the picker in a field
	             // therefore onSelect comes AFTER the select
	             // event.
	             me.onSelect();
	         }
	     },

	     /**
	      * Sets the current value to today.
	      * @return {Ext.ux.picker.DateTime} this
	      */
	     selectToday : function(){
	         var me = this,
	             btn = me.todayBtn,
	             handler = me.handler;

	         if(btn && !btn.disabled){
	             //me.setValue(Ext.Date.clearTime(new Date()));
	             me.value = new Date();
	             me.update(me.value);

	             me.fireEvent('select', me, me.value);
	             if (handler) {
	                 handler.call(me.scope || me, me, me.value);
	             }
	             me.onSelect();
	         }
	         return me;
	     },

	     /**
	      * Sets the current value to today.
	      * @return {Ext.ux.picker.DateTime} this
	      */
	     confirmClick : function(){
	         var me = this,
	             btn = me.confirmBtn,
	             handler = me.handler;

	         if(btn && !btn.disabled){
	             me.fireEvent('select', me, me.value);
	             if (handler) {
	                 handler.call(me.scope || me, me, me.value);
	             }
	             me.onSelect();
	         }
	         return me;
	     },

	     /**
	      * Update the selected cell
	      * @private
	      * @param {Date} date The new date
	      */
	     selectedUpdate: function(date){
	         var me        = this,
	             //t         = date.getTime(),
	             t         = Ext.Date.clearTime(date,true).getTime(),
	             cells     = me.cells,
	             cls       = me.selectedCls,
	             cellItems = cells.elements,
	             c,
	             cLen      = cellItems.length,
	             cell;

	         cells.removeCls(cls);

	         for (c = 0; c < cLen; c++) {
	             cell = Ext.fly(cellItems[c]);

	             if (cell.dom.firstChild.dateValue == t) {
	                 me.fireEvent('highlightitem', me, cell);
	                 cell.addCls(cls);

	                 if(me.isVisible() && !me.doCancelFocus){
	                     Ext.fly(cell.dom.firstChild).focus(50);
	                 }

	                 break;
	             }
	         }
	     },

	     // private, inherit docs
	     beforeDestroy : function() {
	         var me = this;

	         if (me.rendered) {
	             Ext.destroy(
	                 me.hour,
	                 me.minute,
	                 me.second,
	                 me.confirmBtn,
	                 me.footerContainer
	             );
	         }
	         me.callParent();
	     }
	 });

	 
	Ext.define("Lingx.form.DialogOption",{
		    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
			 xtype:'dialogoption',
			 buttonText:"选择...",
			 buttonMargin:3,
			 textEl:'',
			 valueEl:'',
			 readOnly : true,
			 config:{
			    	etype:'',
			    	params:{},
			    	method:'grid'//,
			    },
			 initComponent: function() {
				var me=this;
			    this.callParent();    
	
			 },
		    getTriggerMarkup: function() {
		        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
			},
			 applyRenderSelectors: function() {
			        var me = this;
			        me.callParent();
			        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
			        me.textEl= me.inputEl;

			    },
		    onRender: function() {
		        var me = this,
		            id = me.id,
		            inputEl;

		        me.callParent(arguments);
		        
		        inputEl = me.inputEl;
		        me.valueEl.dom.name=inputEl.dom.name ;
		       

		        inputEl.on("click",me.onTriggerClick,me);
		        
		        inputEl.dom.name = ''; 
		        inputEl=me.valueEl;
		        me.button = new Ext.button.Button(Ext.apply({
		            renderTo: id + '-browseButtonWrap',
		            ownerCt: me,
		            ownerLayout: me.componentLayout,
		            id: id + '-button',
		            ui: me.ui,
		            disabled: me.disabled,
		            text: me.buttonText,
		            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
		            inputName: me.getName(),
		            listeners: {
		                scope: me,
		                click: me.onTriggerClick
		            }
		        }, me.buttonConfig));

		        if (me.buttonOnly) {
		            me.inputCell.setDisplayed(false);
		        }

		        // Ensure the trigger cell is sized correctly upon render
		        if (Ext.isIE) {
		            me.button.getEl().repaint();
		        }
		        if(me.value){
		        	if(me.value.value==undefined){
		        		me.valueEl.dom.value="";
		        		me.inputEl.dom.value="";
		        	}else{
				        me.valueEl.dom.value=me.value.value;
				        me.inputEl.dom.value=me.value.text;
		        	}
			    }
		    },
		    getButtonMarginProp: function() {
		        return 'margin-left:';
		    },
			onTriggerClick:function(){
				openWindow("选择框",Lingx.urlAddParams("e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id,this.params));
		    	//openWindow("选择框","e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id+"&extparam="+this.extparam);
			},
			setText:function(txt){
		        var me = this;
		        me.textEl.dom.value=txt;
			},
			getText:function(){
		        var me = this;
				return me.textEl.dom.value;
			}, 
			setValue:function(val){
		        var me = this;
		        me.callParent(arguments);
				if(me.valueEl&&me.valueEl.dom){
				me.valueEl.dom.value=val;
				}
			}, 
			getValue:function(){
		        var me = this;
				return me.valueEl.dom.value;
			},
			setParams:function(params){this.params=params;},
			getParams:function(){return this.params;},
			setTV:function(options){
		        var me = this;
				me.textEl.dom.value=options.text;
				me.valueEl.dom.value=options.value;
			},
			 getSubmitData: function() {
			        var me = this,
			            data = null;
			        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
			            data = {};
			            data[me.getName()] = '' + me.getValue();
			        }
			        return data;
			    }
	});

	 
	Ext.define("Lingx.form.DialogOption2",{
		    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
			 xtype:'dialogoption2',
			 buttonText:"选择...",
			 buttonMargin:3,
			 textEl:'',
			 valueEl:'',
			 readOnly : true,
			 config:{
			    	etype:'',
			    	params:{},
			    	method:'combogrid2'//,
			    },
			 initComponent: function() {
				var me=this;
			    this.callParent();    
	
			 },
		    getTriggerMarkup: function() {
		        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
			},
			 applyRenderSelectors: function() {
			        var me = this;
			        me.callParent();
			        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
			        me.textEl= me.inputEl;

			    },
		    onRender: function() {
		        var me = this,
		            id = me.id,
		            inputEl;

		        me.callParent(arguments);
		        
		        inputEl = me.inputEl;
		        me.valueEl.dom.name=inputEl.dom.name ;

		        inputEl.on("click",me.onTriggerClick,me);
		        
		        inputEl.dom.name = ''; 
		        inputEl=me.valueEl;
		        me.button = new Ext.button.Button(Ext.apply({
		            renderTo: id + '-browseButtonWrap',
		            ownerCt: me,
		            ownerLayout: me.componentLayout,
		            id: id + '-button',
		            ui: me.ui,
		            disabled: me.disabled,
		            text: me.buttonText,
		            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
		            inputName: me.getName(),
		            listeners: {
		                scope: me,
		                click: me.onTriggerClick
		            }
		        }, me.buttonConfig));

		        if (me.buttonOnly) {
		            me.inputCell.setDisplayed(false);
		        }

		        // Ensure the trigger cell is sized correctly upon render
		        if (Ext.isIE) {
		            me.button.getEl().repaint();
		        }
		        if(me.value){
		        	if(me.value.value==undefined){
		        		me.valueEl.dom.value="";
		        		me.inputEl.dom.value="";
		        	}else{
				        me.valueEl.dom.value=me.value.value;
				        me.inputEl.dom.value=me.value.text;
		        	}
			    }
		    },
		    getButtonMarginProp: function() {
		        return 'margin-left:';
		    },
			onTriggerClick:function(){
				openWindow("选择框",Lingx.urlAddParams("e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id+"&value="+this.valueEl.dom.value+"&text="+encodeURI(encodeURI(this.inputEl.dom.value)),this.params));
		    	//openWindow("选择框","e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id+"&extparam="+this.extparam);
			},
			setText:function(txt){
		        var me = this;
		        me.textEl.dom.value=txt;
			},
			getText:function(){
		        var me = this;
				return me.textEl.dom.value;
			}, 
			setValue:function(val){
		        var me = this;
		        me.callParent(arguments);
				if(me.valueEl&&me.valueEl.dom){
				me.valueEl.dom.value=val;
				}
			}, 
			getValue:function(){
		        var me = this;
				return me.valueEl.dom.value;
			},
			setParams:function(params){this.params=params;},
			getParams:function(){return this.params;},
			setTV:function(options){
		        var me = this;
				me.textEl.dom.value=options.text;
				me.valueEl.dom.value=options.value;
			},
			 getSubmitData: function() {
			        var me = this,
			            data = null;
			        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
			            data = {};
			            data[me.getName()] = '' + me.getValue();
			        }
			        return data;
			    }
	});
	Ext.define("Lingx.form.DialogTree",{
	    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
		 xtype:'dialogtree',
		 buttonText:"选择...",
		 buttonMargin:3,
		 textEl:'',
		 valueEl:'',
		 readOnly : true,
		 config:{
		    	etype:'',
		    	params:{},
		    	method:'tree'//,
		    },
		 initComponent: function() {
			var me=this;
		    this.callParent();    

		 },
	    getTriggerMarkup: function() {
	        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
		},
		 applyRenderSelectors: function() {
		        var me = this;
		        me.callParent();
		        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
		        me.textEl= me.inputEl;

		    },
	    onRender: function() {
	        var me = this,
	            id = me.id,
	            inputEl;

	        me.callParent(arguments);
	        
	        inputEl = me.inputEl;
	        me.valueEl.dom.name=inputEl.dom.name ;

	        inputEl.on("click",me.onTriggerClick,me);
	        
	        inputEl.dom.name = ''; 
	        inputEl=me.valueEl;
	        me.button = new Ext.button.Button(Ext.apply({
	            renderTo: id + '-browseButtonWrap',
	            ownerCt: me,
	            ownerLayout: me.componentLayout,
	            id: id + '-button',
	            ui: me.ui,
	            disabled: me.disabled,
	            text: me.buttonText,
	            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
	            inputName: me.getName(),
	            listeners: {
	                scope: me,
	                click: me.onTriggerClick
	            }
	        }, me.buttonConfig));

	        if (me.buttonOnly) {
	            me.inputCell.setDisplayed(false);
	        }

	        // Ensure the trigger cell is sized correctly upon render
	        if (Ext.isIE) {
	            me.button.getEl().repaint();
	        }
	        if(me.value){
	        	if(me.value.value==undefined){
	        		me.valueEl.dom.value="";
	        		me.inputEl.dom.value="";
	        	}else{
			        me.valueEl.dom.value=me.value.value;
			        me.inputEl.dom.value=me.value.text;
	        	}
	        		
		    }
	    },
	    getButtonMarginProp: function() {
	        return 'margin-left:';
	    },
		onTriggerClick:function(){
	    	openWindow("选择框",Lingx.urlAddParams("e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id,this.params));
		},
		setText:function(txt){
	        var me = this;
	        me.textEl.dom.value=txt;
		},
		getText:function(){
	        var me = this;
			return me.textEl.dom.value;
		}, 
		setValue:function(val){
	        var me = this;
	        me.callParent(arguments);
			if(me.valueEl&&me.valueEl.dom){
			me.valueEl.dom.value=val;
			}
		}, 
		getValue:function(){
	        var me = this;
			return me.valueEl.dom.value;
		},
		setTV:function(options){
	        var me = this;
			me.textEl.dom.value=options.text;
			me.valueEl.dom.value=options.value;
		},
		 getSubmitData: function() {
		        var me = this,
		            data = null;
		        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
		            data = {};
		            data[me.getName()] = '' + me.getValue();
		        }
		        return data;
		    }
});

	Ext.define("Lingx.form.DialogTree2                                                                                                 ",{
	    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
		 xtype:'dialogtree2',
		 buttonText:"选择...",
		 buttonMargin:3,
		 textEl:'',
		 valueEl:'',
		 readOnly : true,
		 config:{
		    	etype:'',
		    	params:{},
		    	method:'combotree2'//,
		    },
		 initComponent: function() {
			var me=this;
		    this.callParent();    

		 },
	    getTriggerMarkup: function() {
	        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
		},
		 applyRenderSelectors: function() {
		        var me = this;
		        me.callParent();
		        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
		        me.textEl= me.inputEl;

		    },
	    onRender: function() {
	        var me = this,
	            id = me.id,
	            inputEl;

	        me.callParent(arguments);
	        
	        inputEl = me.inputEl;
	        me.valueEl.dom.name=inputEl.dom.name ;

	        inputEl.on("click",me.onTriggerClick,me);
	        
	        inputEl.dom.name = ''; 
	        inputEl=me.valueEl;
	        me.button = new Ext.button.Button(Ext.apply({
	            renderTo: id + '-browseButtonWrap',
	            ownerCt: me,
	            ownerLayout: me.componentLayout,
	            id: id + '-button',
	            ui: me.ui,
	            disabled: me.disabled,
	            text: me.buttonText,
	            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
	            inputName: me.getName(),
	            listeners: {
	                scope: me,
	                click: me.onTriggerClick
	            }
	        }, me.buttonConfig));

	        if (me.buttonOnly) {
	            me.inputCell.setDisplayed(false);
	        }

	        // Ensure the trigger cell is sized correctly upon render
	        if (Ext.isIE) {
	            me.button.getEl().repaint();
	        }
	        if(me.value){
	        	if(me.value.value==undefined){
	        		me.valueEl.dom.value="";
	        		me.inputEl.dom.value="";
	        	}else{
			        me.valueEl.dom.value=me.value.value;
			        me.inputEl.dom.value=me.value.text;
	        	}
	        		
		    }
	    },
	    getButtonMarginProp: function() {
	        return 'margin-left:';
	    },
		onTriggerClick:function(){
			//Lingx.urlAddParams("e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id+"&value="+this.valueEl.dom.value+"&text="+this.inputEl.dom.value,this.params)
	    	openWindow("选择框",Lingx.urlAddParams("e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id+"&value="+this.valueEl.dom.value+"&text="+encodeURI(encodeURI(this.inputEl.dom.value)),this.params));
		},
		setText:function(txt){
	        var me = this;
	        me.textEl.dom.value=txt;
		},
		getText:function(){
	        var me = this;
			return me.textEl.dom.value;
		}, 
		setValue:function(val){
	        var me = this;
	        me.callParent(arguments);
			if(me.valueEl&&me.valueEl.dom){
			me.valueEl.dom.value=val;
			}
		}, 
		getValue:function(){
	        var me = this;
			return me.valueEl.dom.value;
		},
		setTV:function(options){
	        var me = this;
			me.textEl.dom.value=options.text;
			me.valueEl.dom.value=options.value;
		},
		 getSubmitData: function() {
		        var me = this,
		            data = null;
		        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
		            data = {};
		            data[me.getName()] = '' + me.getValue();
		        }
		        return data;
		    }
});

	Ext.define("Lingx.form.DialogFile",{
		    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
			 xtype:'file',
			 buttonText:"上传...",
			 buttonMargin:3,
			 valueUrl:'',
			 textEl:'',
			 valueEl:'',
			 readOnly : true,
			 config:{
			    	etype:'',
			    	inputOptions:"",
			    	method:'grid'//,
			    },
			 initComponent: function() {
				var me=this;
			    this.callParent();    
	
			 },
		    getTriggerMarkup: function() {
		        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
			},
			 applyRenderSelectors: function() {
			        var me = this;
			        me.callParent();
			        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
			        me.textEl= me.inputEl;

			    },
		    onRender: function() {
		        var me = this,
		            id = me.id,
		            inputEl;

		        me.callParent(arguments);
		        
		        inputEl = me.inputEl;
		        me.valueEl.dom.name=inputEl.dom.name ;
		       

		        inputEl.on("click",me.onTriggerClick,me);

		        
		        inputEl.dom.name = ''; 
		        inputEl=me.valueEl;
		        

		        me.button = new Ext.button.Button(Ext.apply({
		            renderTo: id + '-browseButtonWrap',
		            ownerCt: me,
		            ownerLayout: me.componentLayout,
		            id: id + '-button',
		            ui: me.ui,
		            disabled: me.disabled,
		            text: me.buttonText,
		            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
		            inputName: me.getName(),
		            listeners: {
		                scope: me,
		                click: me.onTriggerClick
		            }
		        }, me.buttonConfig));

		        if (me.buttonOnly) {
		            me.inputCell.setDisplayed(false);
		        }

		        // Ensure the trigger cell is sized correctly upon render
		        if (Ext.isIE) {
		            me.button.getEl().repaint();
		        }
		        if(me.value){
		        	if(me.value.value==undefined){
		        		me.valueEl.dom.value="";
		        		me.inputEl.dom.value="";
		        		if(me.valueUrl){
			        		me.valueEl.dom.value=me.valueUrl;
			        		me.inputEl.dom.value=me.valueUrl;
			        		
			        		 var temp=me.valueUrl;
						        if(temp&&temp.length>1&&temp.charAt(0)=='['){
						        	var tempText="";
						        	var tempArr=Ext.JSON.decode(temp);
						        	for(var i=0;i<tempArr.length;i++){
						        		tempText+=tempArr[i].text+",";
						        	}
						        	if(tempText.length>0){
						        		tempText=tempText.substring(0,tempText.length-1);
						        	}
						        	me.inputEl.dom.value=tempText;
						        }
		        		}
		        	}else{
				        me.valueEl.dom.value=me.value.value;
				        me.inputEl.dom.value=me.value.text;
				        
				        var temp=me.value.value;
				        if(temp&&temp.length>1&&temp.charAt(0)=='['){
				        	var tempText="";
				        	var tempArr=Ext.JSON.decode(temp);
				        	for(var i=0;i<tempArr.length;i++){
				        		tempText+=tempArr[i].text+",";
				        	}
				        	if(tempText.length>0){
				        		tempText=tempText.substring(0,tempText.length-1);
				        	}
				        	me.inputEl.dom.value=tempText;
				        }
		        	}
			    }
		    },
		    getButtonMarginProp: function() {
		        return 'margin-left:';
		    },
			onTriggerClick:function(){
				//alert(this.inputOptions);
		    	openWindow("文件管理中心","lingx/template/upload/upload.jsp?cmpId="+this.id+"&type="+(this.inputOptions||"JSON"));
			},
			setText:function(txt){
		        var me = this;
		        me.textEl.dom.value=txt;
			},
			getText:function(){
		        var me = this;
				return me.textEl.dom.value;
			}, 
			setValue:function(val){
		        var me = this;
		        me.callParent(arguments);
				if(me.valueEl&&me.valueEl.dom){
					me.valueEl.dom.value=val;
					me.inputEl.dom.value=val;
				}else{
					me.valueUrl=val;
				}
			}, 
			getValue:function(){
		        var me = this;
				return me.valueEl.dom.value;
			},
			setTV:function(options){
		        var me = this;
				me.textEl.dom.value=options.text;
				me.valueEl.dom.value=options.value;
			},
			 getSubmitData: function() {
			        var me = this,
			            data = null;
			        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
			            data = {};
			            data[me.getName()] = '' + me.getValue();
			        }
			        return data;
			    }
	});

	Ext.define("Lingx.form.DialogImage",{
		    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
			 xtype:'image',
			 buttonText:"上传...",
			 buttonMargin:3,
			 valueUrl:'',
			 textEl:'',
			 valueEl:'',
			 readOnly : true,
			 config:{
			    	etype:'',
			    	inputOptions:"",
			    	method:'grid'//,
			    },
			 initComponent: function() {
				var me=this;
			    this.callParent();    
	
			 },
		    getTriggerMarkup: function() {
		        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
			},
			 applyRenderSelectors: function() {
			        var me = this;
			        me.callParent();
			        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
			        me.textEl= me.inputEl;

			    },
		    onRender: function() {
		        var me = this,
		            id = me.id,
		            inputEl;

		        me.callParent(arguments);
		        
		        inputEl = me.inputEl;
		        me.valueEl.dom.name=inputEl.dom.name ;

		        inputEl.on("click",me.onTriggerClick,me);
		        
		        inputEl.dom.name = ''; 
		        inputEl=me.valueEl;
		        me.button = new Ext.button.Button(Ext.apply({
		            renderTo: id + '-browseButtonWrap',
		            ownerCt: me,
		            ownerLayout: me.componentLayout,
		            id: id + '-button',
		            ui: me.ui,
		            disabled: me.disabled,
		            text: me.buttonText,
		            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
		            inputName: me.getName(),
		            listeners: {
		                scope: me,
		                click: me.onTriggerClick
		            }
		        }, me.buttonConfig));

		        if (me.buttonOnly) {
		            me.inputCell.setDisplayed(false);
		        }

		        // Ensure the trigger cell is sized correctly upon render
		        if (Ext.isIE) {
		            me.button.getEl().repaint();
		        }
		        if(me.value){
		        	if(me.value.value==undefined){
		        		me.valueEl.dom.value="";
		        		me.inputEl.dom.value="";
		        		if(me.valueUrl){
			        		me.valueEl.dom.value=me.valueUrl;
			        		me.inputEl.dom.value=me.valueUrl;
			        		 var temp=me.valueUrl;
						        if(temp&&temp.length>1&&temp.charAt(0)=='['){
						        	var tempText="";
						        	var tempArr=Ext.JSON.decode(temp);
						        	for(var i=0;i<tempArr.length;i++){
						        		tempText+=tempArr[i].text+",";
						        	}
						        	if(tempText.length>0){
						        		tempText=tempText.substring(0,tempText.length-1);
						        	}
						        	me.inputEl.dom.value=tempText;
						        }
		        		}
		        	}else{
				        me.valueEl.dom.value=me.value.value;
				        me.inputEl.dom.value=me.value.text;
				        
				        var temp=me.value.value;
				        if(temp&&temp.length>1&&temp.charAt(0)=='['){
				        	var tempText="";
				        	var tempArr=Ext.JSON.decode(temp);
				        	for(var i=0;i<tempArr.length;i++){
				        		tempText+=tempArr[i].text+",";
				        	}
				        	if(tempText.length>0){
				        		tempText=tempText.substring(0,tempText.length-1);
				        	}
				        	me.inputEl.dom.value=tempText;
				        }
		        	}
			    }
		    },
		    getButtonMarginProp: function() {
		        return 'margin-left:';
		    },
			onTriggerClick:function(){
		    	openWindow("文件上传框","lingx/template/upload/upload.jsp?cmpId="+this.id+"&type="+(this.inputOptions||"JSON"));
			},
			setText:function(txt){
		        var me = this;
		        me.textEl.dom.value=txt;
			},
			getText:function(){
		        var me = this;
				return me.textEl.dom.value;
			}, 
			setValue:function(val){
		        var me = this;
		        me.callParent(arguments);
				if(me.valueEl&&me.valueEl.dom){
				me.valueEl.dom.value=val;
				me.inputEl.dom.value=val;
				}else{
					me.valueUrl=val;
				}
			}, 
			getValue:function(){
		        var me = this;
				return me.valueEl.dom.value;
			},
			setTV:function(options){
		        var me = this;
				me.textEl.dom.value=options.text;
				me.valueEl.dom.value=options.value;
			},
			 getSubmitData: function() {
			        var me = this,
			            data = null;
			        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
			            data = {};
			            data[me.getName()] = '' + me.getValue();
			        }
			        return data;
			    }
	});

	 Ext.define('TextValueModel',{
       extend: 'Ext.data.Model',
       fields: [
           {name: 'text', type: 'string'},
           {name: 'value', type: 'string'}
       ]
   });
	Ext.define("Lingx.form.Radio",{
		 extend:'Ext.form.field.Base',
		    alias: 'widget.lgxRadio',
		    requires: ['Ext.util.Format', 'Ext.XTemplate'],
		    xtype:"radio",
		    config:{
		    	valueAjaxUrl:''//,
		    },
		    mixins: {
		        bindable: 'Ext.util.Bindable'    
		    },
		    radioText:"<input id='{id}' type='radio' name='{code}' {checked} value='{value}'  />"
		    ,
		    labelText:"<label for='{id}'>{text}</label> "
		    ,
		    fieldSubTpl: [
		        '<div id="{id}" role="input" ',
		        '<tpl if="fieldStyle"> style="padding-left:0px;margin-left:0px;"</tpl>', 
		        ' class="{fieldCls}"></div>',
		        {
		            compiled: true,
		            disableFormats: true
		        }
		    ],
		    onRender: function() {
		    	var me = this;
		    	me.value=Ext.isObject(me.value)?me.value.value:me.value;

		        me.callParent(arguments);
		        /*
   	 	///if(store.autoLoad){
   	 		//store.suspendAutoSync();
	        Ext.Ajax.request({scope:me,async:false,url:me.valueAjaxUrl,success:function(res){//
	    		var records=Ext.JSON.decode(res.responseText);
 		    	var _this = this;
 		    	var temp="";
 		       for(var i=0;i<records.length;i++){
 		    	   var record=records[i];
 		    	  temp=temp+(_this.radioText.format({code:_this.name,checked:record.value==_this.value?"checked":"",value:record.value})+_this.labelText.format({text:record.text}));
 		    	   //lgxInfo();
 		       }
 		       _this.inputEl.dom.innerHTML=temp;
 		       //alert(temp);
	    	}}); 
	        Ext.Ajax.request({scope:me,url:me.valueAjaxUrl,success:function(res){
        		Ext.select(".lgxradiohidden").setStyle('display','inline');
	    	}}); 
	        */
		    
   	 		me.store.load({
   	 		    scope: me,
   	 		    callback: function(records, operation, success) {
   	 		    	var _this = this;
   	 		    	var temp="";
   	 		       for(var i=0;i<records.length;i++){
   	 		    	   var record=records[i];
   	 		    	   var id=record.data.value+"_radio_"+Lingx.getRandomString(8);
   	 		    	  temp=temp+(_this.radioText.format({id:id,code:_this.name,checked:record.data.value==_this.value?"checked":"",value:record.data.value})+_this.labelText.format({id:id,text:record.data.text}));
   	 		       }
   	 		       _this.inputEl.dom.innerHTML=temp;
   	 		    }
   	 		});
   	 		
   	 	///}

		    }, 
			setValue:function(val){
		        var me = this;
		        me.value=val;
		        //me.callParent(arguments);
				if(me.inputEl&&me.inputEl.dom){
					try{
					Ext.query("input[value='"+val+"']",me.inputEl.dom)[0].checked=true;
					}catch(e){}
				}
			}, 
			getValue:function(){
		        var me = this;
		        var arr=Ext.query("input:checked",me.inputEl.dom);
		        var val="";
		        if(arr.length>0)val=arr[0].value;
		        return val;
			},
			setTV:function(options){
		        var me = this;
				me.textEl.dom.value=options.text;
				me.valueEl.dom.value=options.value;
			},
			 getSubmitData: function() {
			        var me = this,
			            data = null;
			        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
			            data = {};
			            data[me.getName()] = '' + me.getValue();
			        }
			        return data;
			    },
		    getStoreListeners: function() {
		        var me = this;

		        return {
		           // beforeload: me.onBeforeLoad,
		           // clear: me.onClear,
		           // datachanged: me.onDataChanged,
		            load: me.onLoad//,
		           // exception: me.onException,
		           // remove: me.onRemove
		        }; 
		    },


		    onLoad:function(store, records, success){
		    	var me=this;
		    	lgxInfo(1);
		    },
		    getRawValue: function() {
		        return this.rawValue;
		    },

		    setRawValue: function(value) {
		        var me = this;
		            
		        value = Ext.value(value, '');
		        me.rawValue = value;
		        if (me.rendered) {
		            me.inputEl.dom.innerHTML = me.getDisplayValue();
		            me.updateLayout();
		        }
		        return value;
		    },

		    getDisplayValue: function() {
		        var me = this,
		            value = this.getRawValue(),
		            display;
		        if (me.renderer) {
		             display = me.renderer.call(me.scope || me, value, me);
		        } else {
		             display = me.htmlEncode ? Ext.util.Format.htmlEncode(value) : value;
		        }
		        return display;
		    },
		        
		    getSubTplData: function() {
		        var ret = this.callParent(arguments);

		        ret.value = this.getDisplayValue();

		        return ret;
		    }

	});
	Ext.override(Ext.data.TreeStore, {
	    load : function(options) {
	        options = options || {};
	        options.params = options.params || {};

	        var me = this, node = options.node || me.tree.getRootNode(), root;

	        // If there is not a node it means the user hasnt defined a rootnode
	        // yet. In this case lets just
	        // create one for them.
	        if (!node) {
	            node = me.setRootNode( {
	                expanded : true
	            });
	        }

	        if (me.clearOnLoad) {
	            node.removeAll(false);
	        }

	        Ext.applyIf(options, {
	            node : node
	        });
	        options.params[me.nodeParam] = node ? node.getId() : 'root';

	        if (node) {
	            node.set('loading', true);
	        }
	        return me.callParent( [ options ]);
	    }
	});

	Ext.define("Lingx.form.Checkbox",{
		 extend:'Ext.form.field.Base',
		    alias: 'widget.lgxCheckbox',
		    requires: ['Ext.util.Format', 'Ext.XTemplate'],
		    xtype:"checkbox",
		    config:{
		    	valueAjaxUrl:''//,
		    },
		    mixins: {
		        bindable: 'Ext.util.Bindable'    
		    },
		    radioText:"<input id='{id}' type='checkbox' name='{code}' {checked} value='{value}'  />"
		    ,
		    labelText:"<label for='{id}'>{text}</label> "
		    ,
		    fieldSubTpl: [
		        '<div id="{id}" role="input" ',
		        '<tpl if="fieldStyle"> style="padding-left:0px;margin-left:0px;"</tpl>', 
		        ' class="{fieldCls}"></div>',
		        {
		            compiled: true,
		            disableFormats: true
		        }
		    ],
		    onRender: function() {
		    	var me = this;
		    	me.value=Ext.isObject(me.value)?me.value.value:me.value;

		        me.callParent(arguments);
		        /*
   	 	///if(store.autoLoad){
   	 		//store.suspendAutoSync();
	        Ext.Ajax.request({scope:me,async:false,url:me.valueAjaxUrl,success:function(res){//
	    		var records=Ext.JSON.decode(res.responseText);
 		    	var _this = this;
 		    	var temp="";
 		       for(var i=0;i<records.length;i++){
 		    	   var record=records[i];
 		    	  temp=temp+(_this.radioText.format({code:_this.name,checked:record.value==_this.value?"checked":"",value:record.value})+_this.labelText.format({text:record.text}));
 		    	   //lgxInfo();
 		       }
 		       _this.inputEl.dom.innerHTML=temp;
 		       //alert(temp);
	    	}}); 
	        Ext.Ajax.request({scope:me,url:me.valueAjaxUrl,success:function(res){
        		Ext.select(".lgxradiohidden").setStyle('display','inline');
	    	}}); 
	        */
		    
   	 		me.store.load({
   	 		    scope: me,
   	 		    callback: function(records, operation, success) {
   	 		    	var _this = this;
   	 		    	var temp="";
   	 		    	var checkedVal=false;
   	 		    	var val=','+_this.value+',';//record.data.value==_this.value?"checked":""
   	 		       for(var i=0;i<records.length;i++){
   	 		    	   var record=records[i];
   	 		    	   var id=record.data.value+"_radio_"+Lingx.getRandomString(8);
   	 		    	   checkedVal=val.indexOf(','+record.data.value+',')>-1;
   	 		    	  temp=temp+(_this.radioText.format({id:id,code:_this.name,checked:checkedVal?'checked':'',value:record.data.value})+_this.labelText.format({id:id,text:record.data.text}));
   	 		       }
   	 		       _this.inputEl.dom.innerHTML=temp;
   	 		    }
   	 		});
   	 		
   	 	///}

		    }, 
			setValue:function(val){
		        var me = this;
		        if(val&&val.value){val=val.value;}
		        me.value=val;
		        //me.callParent(arguments);
		        //console.log(val);
		        if(me.inputEl&&me.inputEl.dom&&val)
		        if(val.indexOf(",")==-1){
		        	try{
						Ext.query("input[value='"+val+"']",me.inputEl.dom)[0].checked=true;
		        	}catch(e){}
		        }else{
        			//lgxInfo("input[value='"+arr[i]+"']");
		        	var arr=val.split(",");
		        	for(var i=0;i<arr.length;i++){
		        		try{
		        			Ext.query("input[value='"+arr[i]+"']",me.inputEl.dom)[0].checked=true;
		        		}catch(e){}
		        	}
		        }
			}, 
			getValue:function(){
		        var me = this;
		        var arr=Ext.query("input:checked",me.inputEl.dom);
		        var val="";
		        if(arr.length>0){
		        	for(var i=0;i<arr.length;i++){
		        		val=val+arr[i].value+",";
		        	}
		        	val=val.substring(0, val.length-1);
		        }
		        return val;
			},
			setTV:function(options){
		        var me = this;
				me.textEl.dom.value=options.text;
				me.valueEl.dom.value=options.value;
			},
			 getSubmitData: function() {
			        var me = this,
			            data = null;
			        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
			            data = {};
			            data[me.getName()] = '' + me.getValue();
			        }
			        return data;
			    },
		    getStoreListeners: function() {
		        var me = this;

		        return {
		           // beforeload: me.onBeforeLoad,
		           // clear: me.onClear,
		           // datachanged: me.onDataChanged,
		            load: me.onLoad//,
		           // exception: me.onException,
		           // remove: me.onRemove
		        }; 
		    },


		    onLoad:function(store, records, success){
		    	var me=this;
		    	lgxInfo(1);
		    },
		    getRawValue: function() {
		        return this.rawValue;
		    },

		    setRawValue: function(value) {
		        var me = this;
		            
		        value = Ext.value(value, '');
		        me.rawValue = value;
		        if (me.rendered) {
		            me.inputEl.dom.innerHTML = me.getDisplayValue();
		            me.updateLayout();
		        }
		        return value;
		    },

		    getDisplayValue: function() {
		        var me = this,
		            value = this.getRawValue(),
		            display;
		        if (me.renderer) {
		             display = me.renderer.call(me.scope || me, value, me);
		        } else {
		             display = me.htmlEncode ? Ext.util.Format.htmlEncode(value) : value;
		        }
		        return display;
		    },
		        
		    getSubTplData: function() {
		        var ret = this.callParent(arguments);

		        ret.value = this.getDisplayValue();

		        return ret;
		    }

	});
	
//用户单选
	Ext.define("Lingx.form.UserDialog",{
		    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
			 xtype:'dialoguser',
			 buttonText:"选择...",
			 buttonMargin:3,
			 textEl:'',
			 valueEl:'',
			 readOnly : true,
			 config:{
			    	etype:'',
			    	params:{},
			    	method:'grid'//,
			    },
			 initComponent: function() {
				var me=this;
			    this.callParent();    
	
			 },
		    getTriggerMarkup: function() {
		        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
			},
			 applyRenderSelectors: function() {
			        var me = this;
			        me.callParent();
			        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
			        me.textEl= me.inputEl;

			    },
		    onRender: function() {
		        var me = this,
		            id = me.id,
		            inputEl;

		        me.callParent(arguments);
		        
		        inputEl = me.inputEl;
		        me.valueEl.dom.name=inputEl.dom.name ;

		        inputEl.on("click",me.onTriggerClick,me);
		        
		        inputEl.dom.name = ''; 
		        inputEl=me.valueEl;
		        me.button = new Ext.button.Button(Ext.apply({
		            renderTo: id + '-browseButtonWrap',
		            ownerCt: me,
		            ownerLayout: me.componentLayout,
		            id: id + '-button',
		            ui: me.ui,
		            disabled: me.disabled,
		            text: me.buttonText,
		            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
		            inputName: me.getName(),
		            listeners: {
		                scope: me,
		                click: me.onTriggerClick
		            }
		        }, me.buttonConfig));

		        if (me.buttonOnly) {
		            me.inputCell.setDisplayed(false);
		        }

		        // Ensure the trigger cell is sized correctly upon render
		        if (Ext.isIE) {
		            me.button.getEl().repaint();
		        }
		        if(me.value){
		        	if(me.value.value==undefined){
		        		me.valueEl.dom.value="";
		        		me.inputEl.dom.value="";
		        	}else{
				        me.valueEl.dom.value=me.value.value;
				        me.inputEl.dom.value=me.value.text;
		        	}
			    }
		    },
		    getButtonMarginProp: function() {
		        return 'margin-left:';
		    },
			onTriggerClick:function(){
				openWindow("用户选择框",Lingx.urlAddParams("lingx/common/select_users_ext.jsp?lingxInputType=1&cmpId="+this.id+"&value="+this.valueEl.dom.value+"&text="+encodeURI(encodeURI(this.inputEl.dom.value)),this.params));
		    	//openWindow("选择框","e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id+"&extparam="+this.extparam);
			},
			setText:function(txt){
		        var me = this;
		        me.textEl.dom.value=txt;
			},
			getText:function(){
		        var me = this;
				return me.textEl.dom.value;
			}, 
			setValue:function(val){
		        var me = this;
		        me.callParent(arguments);
				if(me.valueEl&&me.valueEl.dom){
				me.valueEl.dom.value=val;
				}
			}, 
			getValue:function(){
		        var me = this;
				return me.valueEl.dom.value;
			},
			setParams:function(params){this.params=params;},
			getParams:function(){return this.params;},
			setTV:function(options){
		        var me = this;
				me.textEl.dom.value=options.text;
				me.valueEl.dom.value=options.value;
			},
			 getSubmitData: function() {
			        var me = this,
			            data = null;
			        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
			            data = {};
			            data[me.getName()] = '' + me.getValue();
			        }
			        return data;
			    }
	});

	//用户多选
		Ext.define("Lingx.form.UserDialog2",{
			    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
				 xtype:'dialoguser2',
				 buttonText:"选择...",
				 buttonMargin:3,
				 textEl:'',
				 valueEl:'',
				 readOnly : true,
				 config:{
				    	etype:'',
				    	params:{},
				    	method:'grid'//,
				    },
				 initComponent: function() {
					var me=this;
				    this.callParent();    
		
				 },
			    getTriggerMarkup: function() {
			        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
				},
				 applyRenderSelectors: function() {
				        var me = this;
				        me.callParent();
				        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
				        me.textEl= me.inputEl;

				    },
			    onRender: function() {
			        var me = this,
			            id = me.id,
			            inputEl;

			        me.callParent(arguments);
			        
			        inputEl = me.inputEl;
			        me.valueEl.dom.name=inputEl.dom.name ;

			        inputEl.on("click",me.onTriggerClick,me);
			        
			        inputEl.dom.name = ''; 
			        inputEl=me.valueEl;
			        me.button = new Ext.button.Button(Ext.apply({
			            renderTo: id + '-browseButtonWrap',
			            ownerCt: me,
			            ownerLayout: me.componentLayout,
			            id: id + '-button',
			            ui: me.ui,
			            disabled: me.disabled,
			            text: me.buttonText,
			            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
			            inputName: me.getName(),
			            listeners: {
			                scope: me,
			                click: me.onTriggerClick
			            }
			        }, me.buttonConfig));

			        if (me.buttonOnly) {
			            me.inputCell.setDisplayed(false);
			        }

			        // Ensure the trigger cell is sized correctly upon render
			        if (Ext.isIE) {
			            me.button.getEl().repaint();
			        }
			        if(me.value){
			        	if(me.value.value==undefined){
			        		me.valueEl.dom.value="";
			        		me.inputEl.dom.value="";
			        	}else{
					        me.valueEl.dom.value=me.value.value;
					        me.inputEl.dom.value=me.value.text;
			        	}
				    }
			    },
			    getButtonMarginProp: function() {
			        return 'margin-left:';
			    },
				onTriggerClick:function(){
					openWindow("用户选择框",Lingx.urlAddParams("lingx/common/select_users_ext.jsp?lingxInputType=n&cmpId="+this.id+"&value="+this.valueEl.dom.value+"&text="+encodeURI(encodeURI(this.inputEl.dom.value)),this.params));
			    	//openWindow("选择框","e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id+"&extparam="+this.extparam);
				},
				setText:function(txt){
			        var me = this;
			        me.textEl.dom.value=txt;
				},
				getText:function(){
			        var me = this;
					return me.textEl.dom.value;
				}, 
				setValue:function(val){
			        var me = this;
			        me.callParent(arguments);
					if(me.valueEl&&me.valueEl.dom){
					me.valueEl.dom.value=val;
					}
				}, 
				getValue:function(){
			        var me = this;
					return me.valueEl.dom.value;
				},
				setParams:function(params){this.params=params;},
				getParams:function(){return this.params;},
				setTV:function(options){
			        var me = this;
					me.textEl.dom.value=options.text;
					me.valueEl.dom.value=options.value;
				},
				 getSubmitData: function() {
				        var me = this,
				            data = null;
				        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
				            data = {};
				            data[me.getName()] = '' + me.getValue();
				        }
				        return data;
				    }
		});
	

		//组织单选
			Ext.define("Lingx.form.OrgDialog",{
				    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
					 xtype:'dialogorg',
					 buttonText:"选择...",
					 buttonMargin:3,
					 textEl:'',
					 valueEl:'',
					 readOnly : true,
					 config:{
					    	etype:'',
					    	params:{},
					    	method:'grid'//,
					    },
					 initComponent: function() {
						var me=this;
					    this.callParent();    
			
					 },
				    getTriggerMarkup: function() {
				        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
					},
					 applyRenderSelectors: function() {
					        var me = this;
					        me.callParent();
					        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
					        me.textEl= me.inputEl;

					    },
				    onRender: function() {
				        var me = this,
				            id = me.id,
				            inputEl;

				        me.callParent(arguments);
				        
				        inputEl = me.inputEl;
				        me.valueEl.dom.name=inputEl.dom.name ;

				        inputEl.on("click",me.onTriggerClick,me);
				        
				        inputEl.dom.name = ''; 
				        inputEl=me.valueEl;
				        me.button = new Ext.button.Button(Ext.apply({
				            renderTo: id + '-browseButtonWrap',
				            ownerCt: me,
				            ownerLayout: me.componentLayout,
				            id: id + '-button',
				            ui: me.ui,
				            disabled: me.disabled,
				            text: me.buttonText,
				            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
				            inputName: me.getName(),
				            listeners: {
				                scope: me,
				                click: me.onTriggerClick
				            }
				        }, me.buttonConfig));

				        if (me.buttonOnly) {
				            me.inputCell.setDisplayed(false);
				        }

				        // Ensure the trigger cell is sized correctly upon render
				        if (Ext.isIE) {
				            me.button.getEl().repaint();
				        }
				        if(me.value){
				        	if(me.value.value==undefined){
				        		me.valueEl.dom.value="";
				        		me.inputEl.dom.value="";
				        	}else{
						        me.valueEl.dom.value=me.value.value;
						        me.inputEl.dom.value=me.value.text;
				        	}
					    }
				    },
				    getButtonMarginProp: function() {
				        return 'margin-left:';
				    },
					onTriggerClick:function(){
						openWindow("组织选择框",Lingx.urlAddParams("lingx/common/select_orgs_ext.jsp?lingxInputType=1&cmpId="+this.id+"&value="+this.valueEl.dom.value+"&text="+encodeURI(encodeURI(this.inputEl.dom.value)),this.params));
				    	//openWindow("选择框","e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id+"&extparam="+this.extparam);
					},
					setText:function(txt){
				        var me = this;
				        me.textEl.dom.value=txt;
					},
					getText:function(){
				        var me = this;
						return me.textEl.dom.value;
					}, 
					setValue:function(val){
				        var me = this;
				        me.callParent(arguments);
						if(me.valueEl&&me.valueEl.dom){
						me.valueEl.dom.value=val;
						}
					}, 
					getValue:function(){
				        var me = this;
						return me.valueEl.dom.value;
					},
					setParams:function(params){this.params=params;},
					getParams:function(){return this.params;},
					setTV:function(options){
				        var me = this;
						me.textEl.dom.value=options.text;
						me.valueEl.dom.value=options.value;
					},
					 getSubmitData: function() {
					        var me = this,
					            data = null;
					        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
					            data = {};
					            data[me.getName()] = '' + me.getValue();
					        }
					        return data;
					    }
			});

			//用户多选
				Ext.define("Lingx.form.OrgDialog2",{
					    extend: 'Ext.form.field.Trigger',//extend:'Ext.form.field.Text',
						 xtype:'dialogorg2',
						 buttonText:"选择...",
						 buttonMargin:3,
						 textEl:'',
						 valueEl:'',
						 readOnly : true,
						 config:{
						    	etype:'',
						    	params:{},
						    	method:'grid'//,
						    },
						 initComponent: function() {
							var me=this;
						    this.callParent();    
				
						 },
					    getTriggerMarkup: function() {
					        return '<td id="' + this.id + '-browseButtonWrap"><input id="' + this.id + '-Hidden-Value" name="" type="hidden" value=""> </td>';//<input type="button" value="选择..."> 
						},
						 applyRenderSelectors: function() {
						        var me = this;
						        me.callParent();
						        me.valueEl = me.el.getById(me.id+"-Hidden-Value");
						        me.textEl= me.inputEl;

						    },
					    onRender: function() {
					        var me = this,
					            id = me.id,
					            inputEl;

					        me.callParent(arguments);
					        
					        inputEl = me.inputEl;
					        me.valueEl.dom.name=inputEl.dom.name ;

					        inputEl.on("click",me.onTriggerClick,me);
					        
					        inputEl.dom.name = ''; 
					        inputEl=me.valueEl;
					        me.button = new Ext.button.Button(Ext.apply({
					            renderTo: id + '-browseButtonWrap',
					            ownerCt: me,
					            ownerLayout: me.componentLayout,
					            id: id + '-button',
					            ui: me.ui,
					            disabled: me.disabled,
					            text: me.buttonText,
					            style: me.buttonOnly ? '' : me.getButtonMarginProp() + me.buttonMargin + 'px',
					            inputName: me.getName(),
					            listeners: {
					                scope: me,
					                click: me.onTriggerClick
					            }
					        }, me.buttonConfig));

					        if (me.buttonOnly) {
					            me.inputCell.setDisplayed(false);
					        }

					        // Ensure the trigger cell is sized correctly upon render
					        if (Ext.isIE) {
					            me.button.getEl().repaint();
					        }
					        if(me.value){
					        	if(me.value.value==undefined){
					        		me.valueEl.dom.value="";
					        		me.inputEl.dom.value="";
					        	}else{
							        me.valueEl.dom.value=me.value.value;
							        me.inputEl.dom.value=me.value.text;
					        	}
						    }
					    },
					    getButtonMarginProp: function() {
					        return 'margin-left:';
					    },
						onTriggerClick:function(){
							openWindow("组织选择框",Lingx.urlAddParams("lingx/common/select_orgs_ext.jsp?lingxInputType=n&cmpId="+this.id+"&value="+this.valueEl.dom.value+"&text="+encodeURI(encodeURI(this.inputEl.dom.value)),this.params));
					    	//openWindow("选择框","e?e="+this.etype+"&m="+this.method+"&cmpId="+this.id+"&extparam="+this.extparam);
						},
						setText:function(txt){
					        var me = this;
					        me.textEl.dom.value=txt;
						},
						getText:function(){
					        var me = this;
							return me.textEl.dom.value;
						}, 
						setValue:function(val){
					        var me = this;
					        me.callParent(arguments);
							if(me.valueEl&&me.valueEl.dom){
							me.valueEl.dom.value=val;
							}
						}, 
						getValue:function(){
					        var me = this;
							return me.valueEl.dom.value;
						},
						setParams:function(params){this.params=params;},
						getParams:function(){return this.params;},
						setTV:function(options){
					        var me = this;
							me.textEl.dom.value=options.text;
							me.valueEl.dom.value=options.value;
						},
						 getSubmitData: function() {
						        var me = this,
						            data = null;
						        if (!me.disabled && me.submitValue && !me.isFileUpload()) {
						            data = {};
						            data[me.getName()] = '' + me.getValue();
						        }
						        return data;
						    }
				});
				
	Ext.override(Ext.data.TreeStore, {
	    load : function(options) {
	        options = options || {};
	        options.params = options.params || {};

	        var me = this, node = options.node || me.tree.getRootNode(), root;

	        // If there is not a node it means the user hasnt defined a rootnode
	        // yet. In this case lets just
	        // create one for them.
	        if (!node) {
	            node = me.setRootNode( {
	                expanded : true
	            });
	        }

	        if (me.clearOnLoad) {
	            node.removeAll(false);
	        }

	        Ext.applyIf(options, {
	            node : node
	        });
	        options.params[me.nodeParam] = node ? node.getId() : 'root';

	        if (node) {
	            node.set('loading', true);
	        }
	        return me.callParent( [ options ]);
	    }
	});

Date.prototype.format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};