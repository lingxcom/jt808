package com.lingx.jt808.server.netty;

import java.util.ArrayList;
import java.util.List;

import com.lingx.jt808.core.IJT808MsgAttached;
import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.utils.Utils;
import com.lingx.jt808.server.msg.Msg0107;
import com.lingx.jt808.server.msg.Msg0200;
import com.lingx.jt808.server.msg.Msg0201;
import com.lingx.jt808.server.msg.Msg0704;
import com.lingx.jt808.server.msg.Msg0805;
import com.lingx.jt808.server.msg.x0200.A01;
import com.lingx.jt808.server.msg.x0200.A30;
import com.lingx.jt808.server.msg.x0200.A31;

public class TestJt808 extends JT808Handler {
	public void test() {
		int start=648%320;
		System.out.println(start);
		String hex="7E080500080139000001310005000B000100000001037E";
		List<IJT808MsgHandler> listMsgHandler=new ArrayList<>();
		
		List<IJT808MsgAttached> list=new ArrayList<>();
		list.add(new A01());
		list.add(new A30());
		list.add(new A31());
		
		Msg0200 msg0200=new Msg0200();
		msg0200.setListAttached(list);
		listMsgHandler.add(new Msg0107());
		listMsgHandler.add(new Msg0704());
		listMsgHandler.add(msg0200);
		

		Msg0201 msg0201=new Msg0201();
		msg0201.setListAttached(list);
		listMsgHandler.add(msg0201);

		Msg0107 msg0107=new Msg0107();
		listMsgHandler.add(msg0107);

		Msg0805 msg0805=new Msg0805();
		listMsgHandler.add(msg0805);
		
		//JT808Handler handler=new JT808Handler();
		this.setListMsgHandler(listMsgHandler);
		try {
			this.channelRead0(null, Utils.hexToBytes(hex));
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestJt808 test=new TestJt808();
		test.test();
		
		//ChannelBuffer result =null; //ChannelBuffers .buffer(indexbuf.readerIndex());
	}

}
