var HashMap=function(options){
	var _this=this;
	_this.array=[];
	/**
	 * 设置KEY与VALUE
	 */
	_this.put=function(key,value){
		var exist=false;
		for(var i=0;i<_this.array.length;i++){
			if(key==_this.array[i].key){
				exist=true;
				_this.array[i].value=value;
			}
		}
		if(!exist)
		_this.array.push({key:key,value:value});
	}
	/**
	 * 根据KEY取出值
	 */
	_this.get=function(key){
		for(var i=0;i<_this.array.length;i++){
			if(key==_this.array[i].key){
				return _this.array[i].value;
			}
		}
		return null;
	}
	/**
	 * 判断KEY是否存在
	 */
	_this.containsKey=function(key){
		for(var i=0;i<_this.array.length;i++){
			if(key==_this.array[i].key){
				return true;
			}
		}
		return false;
	}
	/**
	 * 删除KEY
	 */
	_this.remove=function(key){
		var arr1=[];
		for(var i=0;i<_this.array.length;i++){
			if(key==_this.array[i].key){
				
			}else{
				arr1.push(_this.array[i]);
			}
		}
		_this.array=arr1;
		return arr1;
	}
	/**
	 * 取出所有的KEY
	 */
	_this.keySet=function(){
		var arr1=[];
		for(var i=0;i<_this.array.length;i++){
			arr1.push(_this.array[i].key);
		}
		return arr1;
	}
}