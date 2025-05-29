package com.tracbds.core.cmd.ext;

import java.util.Map;

import com.tracbds.core.cmd.Cmd8804;

public class ExtCmd8804 extends Cmd8804 {

	public ExtCmd8804(String tid,Map<String,Object> params,boolean isVersion) {
		super(tid,(byte)Integer.parseInt(params.get("p1").toString()),isVersion);
	}
	

}
