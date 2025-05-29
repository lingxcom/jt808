package com.tracbds.core.service.statusparser;

import java.util.List;
import java.util.Map;

import com.tracbds.core.IJT808StatusParser;
import com.tracbds.core.bean.AttachedBean0x0200;

public class LocatedStatusParser implements IJT808StatusParser{

	@Override
	public String parse(Map<String, Object> map0x0200, List<AttachedBean0x0200> listAttached) {
		if(map0x0200.containsKey("status")) {
			long status=Long.parseLong(map0x0200.get("status").toString());
			return (status&0b10)>0?"Positioned":"Not positioned";//Positioned, not positioned
		}
		return null;
	}

}
