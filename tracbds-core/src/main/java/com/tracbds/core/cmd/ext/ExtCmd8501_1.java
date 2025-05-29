package com.tracbds.core.cmd.ext;

import java.util.Map;

import com.tracbds.core.cmd.Cmd8500;
//车辆加锁
public class ExtCmd8501_1 extends Cmd8500 {

	public ExtCmd8501_1(String tid,Map<String,Object> params,boolean isVersion) {
		super(tid,(byte)1,isVersion);
	}

}
