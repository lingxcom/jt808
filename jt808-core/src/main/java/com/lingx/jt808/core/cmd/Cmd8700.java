package com.lingx.jt808.core.cmd;

/**
 * 行驶记录数据采集命令
 * @author lingx.com
 *
 */
public class Cmd8700 extends AbstractJT808Command {

	public Cmd8700(String tid,byte cmd) {
		super(0x8700, tid,new byte[] {cmd});
	}
}
