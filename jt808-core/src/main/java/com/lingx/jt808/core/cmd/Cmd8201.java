package com.lingx.jt808.core.cmd;
/**
 * 位置信息查询 
 * @author lingx.com
 *
 */
public class Cmd8201 extends AbstractJT808Command {

	public Cmd8201(String tid) {
		super(0x8201, tid);
	}

	public static void main(String args[]) {
		Cmd8201 msg=new Cmd8201("012345678912");
		System.out.println(msg.toMessageHexstring());
	}
}
