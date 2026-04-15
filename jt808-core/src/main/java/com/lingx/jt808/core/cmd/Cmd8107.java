package com.lingx.jt808.core.cmd;
/**
 * 查询终端属性
 * @author lingx.com
 *
 */
public class Cmd8107 extends AbstractJT808Command {

	public Cmd8107(String tid) {
		super(0x8107, tid);
	}

	public static void main(String args[]) {
		Cmd8107 msg=new Cmd8107("012345678912");
		System.out.println(msg.toMessageHexstring());
	}
}
