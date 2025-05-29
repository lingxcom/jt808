package com.tracbds.server.msg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.support.MyByteBuf;
import com.tracbds.core.utils.JT808Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
/**
 * 查询终端属性应答 
 * @author lingx.com
 *
 */
@Component
public class Msg0107 extends AbstrctMsgHandler implements IJT808MsgHandler {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public int getMsgId() {
		return 0x0107;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		String key=JT808Utils.getResKey2(tid, 0x8107, 0);
		//System.out.println(isVersion);
		//System.out.println(data.readableBytes());
		data.markReaderIndex();
		String iccid=null;
		if(isVersion) {
			try {
				MyByteBuf mbb=new MyByteBuf(data);
				int p0=data.readUnsignedShort();
				String p2=mbb.readString(5);
				String p7=mbb.readString(30);
				String p27=mbb.readString(30);
				String p42=mbb.readStringBCD(10);
				iccid=p42;
				int len=mbb.readUnsignedByte();
				String p53=mbb.readString(len);
				len=mbb.readUnsignedByte();
				String p54=mbb.readString(len);
				int p55=mbb.readByte();
				int p56=mbb.readByte();

				Map<String,Object> map=new HashMap<>();
				map.put("p0", p0);
				map.put("p2", p2);
				map.put("p7", p7);
				map.put("p27", p27);
				map.put("p42", p42);
				map.put("p53", p53);
				map.put("p54", p54);
				map.put("p55", p55);
				map.put("p56", p56);
				map.put("code", 1);
				map.put("message", "查询终端属性成功");
				map.put("param", p53);
				IJT808Cache.cache(key, JSON.toJSONString(map), 60);
				
			} catch (Exception e) {
				data.resetReaderIndex();
				MyByteBuf mbb=new MyByteBuf(data);
				int p0=data.readUnsignedShort();
				String p2=mbb.readString(5);
				String p7=mbb.readString(20);
				String p27=mbb.readString(7);
				String p42=mbb.readStringBCD(10);
				int len=mbb.readByte();
				String p53=mbb.readString(len);
				len=mbb.readByte();
				String p54=mbb.readString(len);
				int p55=mbb.readByte();
				int p56=mbb.readByte();

				Map<String,Object> map=new HashMap<>();
				map.put("p0", p0);
				map.put("p2", p2);
				map.put("p7", p7);
				map.put("p27", p27);
				map.put("p42", p42);
				map.put("p53", p53);
				map.put("p54", p54);
				map.put("p55", p55);
				map.put("p56", p56);

				map.put("code", 1);
				map.put("message", "查询终端属性成功");
				map.put("param", p53);
				IJT808Cache.cache(key, JSON.toJSONString(map), 60);
			}
		}else{

			MyByteBuf mbb=new MyByteBuf(data);
			if(mbb.readableBytes()<42)return;
			int p0=data.readUnsignedShort();
			String p2=mbb.readString(5);
			String p7=mbb.readString(20);
			String p27=mbb.readString(7);
			String p42=mbb.readStringBCD(10);
			iccid=p42;
			int len=mbb.readUnsignedByte();
			String p53=mbb.readString(len);
			len=mbb.readUnsignedByte();
			String p54=mbb.readString(len);
			int p55=mbb.readByte();
			int p56=mbb.readByte();

			Map<String,Object> map=new HashMap<>();
			map.put("p0", p0);
			map.put("p2", p2);
			map.put("p7", p7);
			map.put("p27", p27);
			map.put("p42", p42);
			map.put("p53", p53);
			map.put("p54", p54);
			map.put("p55", p55);
			map.put("p56", p56);

			map.put("code", 1);
			map.put("message", "查询终端属性成功");
			map.put("param", p53);
			IJT808Cache.cache(key, JSON.toJSONString(map), 60);
			
			
		}
		if(iccid!=null) {
			this.jdbcTemplate.update("update tgps_car set iccid=? where id=?",iccid,tid);
		}
	}

}
