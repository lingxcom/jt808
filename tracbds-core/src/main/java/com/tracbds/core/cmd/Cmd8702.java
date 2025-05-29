package com.tracbds.core.cmd;

/**
 * 上报驾驶员身份信息请求 
 * @author lingx.com
 *
 */
public class Cmd8702 extends AbstractJT808Command {

	public Cmd8702(String tid,boolean isVersion) {
		super(0x8702, tid,isVersion);
	}
}
