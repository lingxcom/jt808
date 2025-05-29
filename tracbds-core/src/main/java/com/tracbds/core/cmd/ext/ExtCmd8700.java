package com.tracbds.core.cmd.ext;

import java.util.Map;

import com.tracbds.core.cmd.Cmd8700;

public class ExtCmd8700 extends Cmd8700 {

	public ExtCmd8700(String tid,Map<String,Object> params,boolean isVersion) {
		super(tid,Byte.parseByte(params.get("p1").toString()),isVersion);

	}

}
