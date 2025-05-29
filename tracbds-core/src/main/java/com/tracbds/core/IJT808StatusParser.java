package com.tracbds.core;

import java.util.List;
import java.util.Map;

import com.tracbds.core.bean.AttachedBean0x0200;
/**
 * 状态解析器
 * @author lingx
 *
 */
public interface IJT808StatusParser {

	public String parse(Map<String,Object> map0x0200,List<AttachedBean0x0200> listAttached);
	
}
