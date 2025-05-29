package com.tracbds.core.cmd.ext;

import java.util.Map;

import com.tracbds.core.cmd.Cmd8105;

public class ExtCmd8105_5 extends Cmd8105{

	public ExtCmd8105_5(String tid,Map<String,Object> params,boolean isVersion) {
		super(tid, 5, null,isVersion);//恢复出厂设置
	}

}
