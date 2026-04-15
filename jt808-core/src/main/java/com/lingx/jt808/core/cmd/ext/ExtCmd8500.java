package com.lingx.jt808.core.cmd.ext;

import java.util.Map;

import com.lingx.jt808.core.cmd.Cmd8500;

public class ExtCmd8500 extends Cmd8500 {

	public ExtCmd8500(String tid,Map<String,Object> params) {
		super(tid,Byte.parseByte(params.get("p1").toString()));

	}

}
