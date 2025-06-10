package com.tracbds.core.service.statusparser;

import java.util.List;
import java.util.Map;

import com.tracbds.core.IJT808StatusParser;
import com.tracbds.core.bean.AttachedBean0x0200;

public class A0xE4ElectricityParser implements IJT808StatusParser{

	@Override
	public String parse(Map<String, Object> map0x0200, List<AttachedBean0x0200> listAttached) {
		try {
			for(AttachedBean0x0200 bean:listAttached) {
				if(bean.getId()==0xE4) {
					StringBuilder sb=new StringBuilder();
					byte array[]=bean.getData();
					if(array[0]==1) {
						sb.append("未充电,");
					}else {
						sb.append("充电,");
					}
					sb.append("电量:").append(array[1]).append("%");
					return sb.toString();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
