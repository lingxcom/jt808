package com.lingx.jt808.server.msg;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.support.MyByteBuf;
import com.lingx.jt808.core.utils.JT808Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
/**
 * 查询终端属性应答 
 * @author lingx.com
 *
 7E020000510180000326001CB200000000000002010177FEC007096F44000000000000260324171810010400008C74020200000302000030010031010025040000000014040000000115040000000C160400000000170200001803000000E57E
 7E0107007A0180000326001C97004737323330324D523938303400000000000000000000000000003030333236303000000000000000000000354D2D523130302F534C4D3733302D4337432F534C4D3733305F322E302E365F45513133332F5552414E5553352D56352E312E302E31155354442D52303033563031372F4D43552D5631343003FFC87E
 */
@Component
public class Msg0107 extends AbstrctMsgHandler implements IJT808MsgHandler {
	private final static Logger log = LoggerFactory.getLogger(Msg0107.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public int getMsgId() {
		return 0x0107;
	}

	@Override
	public void handle(ByteBuf data, String tid, int msgId, int msgSn, ChannelHandlerContext ctx,boolean isVersion,byte[] bytes)throws Exception {
		String key=JT808Utils.getResKey2(tid, 0x8107, 0);
		
		
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
				redisService.cache(key, JSON.toJSONString(map), 60);
				
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
				redisService.cache(key, JSON.toJSONString(map), 60);
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
			redisService.cache(key, JSON.toJSONString(map), 60);
			
		}
		if(iccid!=null) {
			this.jdbcTemplate.update("update tgps_car set iccid=? where id=?",iccid,IJT808Cache.WHITE_LIST.get(tid));
		}
	}

}
