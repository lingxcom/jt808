package com.lingx.jt808.core.service.statusparser;

import java.util.List;
import java.util.Map;

import com.lingx.jt808.core.IJT808StatusParser;
import com.lingx.jt808.core.bean.AttachedBean0x0200;
import com.lingx.jt808.core.utils.Utils;

public class A0x02OilParser extends AbstractStatusParser implements IJT808StatusParser{
	@Override
	public String parse(Map<String, Object> map0x0200, List<AttachedBean0x0200> listAttached) {
		
			for(AttachedBean0x0200 bean:listAttached) {
				if(bean.getId()==0x02) {
					byte[] bytes=bean.getData();
					float oil=Utils.byteArrayToInt(bytes)/10f;
					String temp="油量:"+oil+"L";
					return temp;
				}
			}
		
		return null;
	}
}
