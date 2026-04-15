package com.lingx.jt808.core.cmd.ext;

import java.util.Map;

import com.lingx.jt808.core.cmd.Cmd8300;
/**
 * 工牌人员信息设置2025-03-24
 * @author lingx
 *
 */
public class ExtCmd8300_1 extends Cmd8300 {

	public ExtCmd8300_1(String tid,Map<String,Object> params) {
		super(tid,1,params.get("p2").toString());

	}
	

}
