package com.lingx.jt808.core.cmd;

/**
 * 车辆控制
 * @author lingx.com
 *
 */
public class Cmd8500 extends AbstractJT808Command {

	public Cmd8500(String tid,byte mark) {
		super(0x8500, tid,new byte[] {mark});
	}
}
