package com.lingx.jt808.core.cmd.ext;

import java.util.Map;

import com.lingx.jt808.core.cmd.Cmd8202;

public class ExtCmd8202 extends Cmd8202 {

	public ExtCmd8202(String tid,Map<String,Object> params) {
		super(tid,Integer.parseInt(params.get("p1").toString()),Integer.parseInt(params.get("p2").toString()));
	}

}
