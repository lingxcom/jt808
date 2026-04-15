package com.lingx.jt808.core.cmd;
/**
 * 链路检测
 * @author lingx.com
 *
 */
public class Cmd8204 extends AbstractJT808Command {

	public Cmd8204(String tid) {
		super(0x8204, tid);
	}

	public static void main(String args[]) {
		Cmd8204 msg=new Cmd8204("012345678912");
		System.out.println(msg.toMessageHexstring());
	}
}
