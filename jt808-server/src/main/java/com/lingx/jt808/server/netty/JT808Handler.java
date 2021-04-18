package com.lingx.jt808.server.netty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.jt808.IJT808Cache;
import com.lingx.jt808.IJT808MsgHandler;
import com.lingx.jt808.cmd.Cmd8001;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
@Component
public class JT808Handler extends SimpleChannelInboundHandler<byte[]> {
	@Resource
	private List<IJT808MsgHandler> listMsgHandler;
	private Set<Integer> nores8001=new HashSet<>();
	@PostConstruct
	public void init() {
		nores8001.add(0x0001);
		nores8001.add(0x0100);
		
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		//System.out.println(Utils.bytesToHex(msg));
		try {
			MyByteBuf buff = new MyByteBuf(JT808Utils.decode(msg));
			buff.readByte();

			int msgId = buff.readUnsignedShort();
			int length = buff.readUnsignedShort();
			String tid = "";

			boolean isFB = (length & 0b0010000000000000) > 0;// 是否分包
			boolean isVersion = (length & 0b0100000000000000) > 0;// 是否版本标识
			if (isVersion) {
				buff.readByte();
				tid = buff.readStringBCD(10);
			} else {
				tid = buff.readStringBCD(6);
			}
			
			if(ctx!=null)IJT808Cache.SESSIONS.put(tid, ctx);
			int msgSn = buff.readUnsignedShort();// 消息流水号
			
			if(!nores8001.contains(msgId)) {//不回复
			Cmd8001 cmd=new Cmd8001(tid,msgId,msgSn);
			if(ctx!=null)ctx.writeAndFlush(cmd.toMessageByteBuf());
			}
			
			length = length & 0x3ff;
			ByteBuf content = null;
			if (isFB) {//分包处理
				int max = buff.readUnsignedShort();
				int ind = buff.readUnsignedShort();
				content = buff.readByteBuf(length);
				String key = tid + "_" + msgId;
				if (ind == 1) {
					ByteBuf bigBuff=Unpooled.buffer(1024, 1024*1000);//最大1M
					bigBuff.writeBytes(content);
					IJT808Cache.FB_CACHE.put(key, bigBuff);
					return;
				} else if (ind == max) {
					IJT808Cache.FB_CACHE.getIfPresent(key).writeBytes(content);
					content = IJT808Cache.FB_CACHE.getIfPresent(key);
					IJT808Cache.FB_CACHE.invalidate(key);
				} else {
					IJT808Cache.FB_CACHE.getIfPresent(key).writeBytes(content);
					return;
				}
			} else {
				content = buff.readByteBuf(length);
			}

			byte check = buff.readByte();
			buff.readByte();

			if (JT808Utils.check(msg, check, length, isFB, isVersion)) {
				IJT808MsgHandler msgHandler=null;
				for(IJT808MsgHandler tmp:this.listMsgHandler) {
					if(tmp.getMsgId()==msgId) {
						msgHandler=tmp;
					}
				}
				if(msgHandler!=null) {
					try {
						msgHandler.handle(content, tid, msgId, msgSn, ctx,isVersion);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					System.out.println(String.format("没有对应的处理器：%04X,%s,%s",msgId,tid,Utils.bytesToHex(msg)));
				}
			}else {
				System.out.println("验证码有误:"+Utils.bytesToHex(msg));
			}
		} catch (Exception e) {
			System.out.println(System.currentTimeMillis()+":"+Utils.bytesToHex(msg));
			e.printStackTrace();
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		super.channelInactive(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		super.channelActive(ctx);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
		
		ctx.close();
	}
	public void setListMsgHandler(List<IJT808MsgHandler> listMsgHandler) {
		this.listMsgHandler = listMsgHandler;
	}

	
}
