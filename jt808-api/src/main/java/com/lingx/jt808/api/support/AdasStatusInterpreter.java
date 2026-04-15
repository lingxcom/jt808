package com.lingx.jt808.api.support;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.lingx.model.impl.AbstractInterpreter;
@Component
public class AdasStatusInterpreter  extends AbstractInterpreter{
	
	private static final long serialVersionUID = -507776362279162551L;
	public AdasStatusInterpreter() {
		super("AdasStatusInterpreter","主动安全车辆状态消息格式转换");
	}
	@Override
	public Object input(Object arg0,Map<String,Object> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object output(Object arg0,Map<String,Object> map) throws Exception {
		try {
			if(arg0!=null) {
				StringBuilder sb=new StringBuilder();
				long status=Long.parseLong(arg0.toString());
				if((status&1)>0) {
					sb.append("点火,");
				}else {
					sb.append("熄火,");
				}
				if((status&1<<1)>0) {
					sb.append("左转,");
				}
				if((status&1<<2)>0) {
					sb.append("右转,");
				}
				if((status&1<<3)>0) {
					sb.append("雨刮开,");
				}
				if((status&1<<4)>0) {
					sb.append("制动,");
				}
				if((status&1<<5)>0) {
					sb.append("已插卡,");
				}
				if((status&(1<<10))>0) {
					sb.append("定位,");
				}else {
					sb.append("未定位,");
				}
				if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
				return sb.toString();
			}
		} catch (Exception e) {
		}
		return "";
	}
	
}
