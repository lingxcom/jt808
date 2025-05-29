package com.tracbds.core.cmd;
/**
 * 车辆控制
 * @author lingx.com
 *
 */
public class Cmd8500 extends AbstractJT808Command {

	public Cmd8500(String tid,byte mark,boolean isVersion) {
		super(0x8500, tid,new byte[] {mark},isVersion);
	}
}
