package com.tracbds.server.netty;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tracbds.core.IJT808Cache;
import com.tracbds.core.IJT808MsgHandler;
import com.tracbds.core.cmd.Cmd8001;
import com.tracbds.core.event.JT808OfflineEvent;
import com.tracbds.core.event.JT808OnlineEvent;
import com.tracbds.core.support.MyByteBuf;
import com.tracbds.core.utils.JT808Utils;
import com.tracbds.core.utils.Utils;
import com.tracbds.server.service.JT808DataService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

@Sharable
@Component
public class JT808Handler extends SimpleChannelInboundHandler<byte[]> {

	private final static Logger log = LoggerFactory.getLogger(JT808Handler.class);

	@Autowired
	private ApplicationContext spring;
	@Autowired
	private List<IJT808MsgHandler> listMsgHandler;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808DataService jt808DataService;
	private Set<Integer> nores8001 = new HashSet<>();

	@PostConstruct
	public void init() {
		nores8001.add(0x0001);
		nores8001.add(0x0004);
		nores8001.add(0x0100);
		nores8001.add(0x0109);
		this.reloadWhitelist();// 加载白名单
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		// System.out.println(Utils.bytesToHex(msg));
		
		ByteBuf content = null;
		MyByteBuf buff = null;
		try {
			buff = new MyByteBuf(JT808Utils.decode(msg));
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

			while (tid.charAt(0) == '0') {
				tid = tid.substring(1);
			}

			if (!IJT808Cache.WHITE_LIST.containsKey(tid)) {
				log.info("设备号未注册:{}", tid);
				jt808DataService.addCar(tid);
				return;
			}

			if (ctx != null && !SetsUtils.has(ctx.channel(), tid)) {

				log.info("Socket上报JT808，终端上线:{},IP:{}", tid, ctx.channel().remoteAddress().toString());
				SetsUtils.add(ctx.channel(), tid);
				JT808OnlineEvent event = new JT808OnlineEvent(spring, tid);
				spring.publishEvent(event);
			}

			if (ctx != null)
				IJT808Cache.SESSIONS.put(tid, ctx.channel());
			int msgSn = buff.readUnsignedShort();// 消息流水号

			if (!nores8001.contains(msgId)) {// 不回复
				Cmd8001 cmd = new Cmd8001(tid, msgId, msgSn, isVersion);
				if (ctx != null)
					ctx.writeAndFlush(cmd.toMessageByteBuf());
			}

			length = length & 0x3ff;
			if (isFB) {// 分包处理
				int max = buff.readUnsignedShort();
				int ind = buff.readUnsignedShort();
				content = buff.readByteBuf(length);
				String key = tid + "_" + msgId;
				if (ind == 1) {
					if (IJT808Cache.FB_CACHE.getIfPresent(key) == null) {
						ByteBuf bigBuff = Unpooled.buffer(1024, 1024 * 10000);// 最大10M
						bigBuff.writeBytes(content);
						IJT808Cache.FB_CACHE.put(key, bigBuff);
					}
					// 因为要return 在这里释放
					return;
				} else if (ind == max) {
					if (IJT808Cache.FB_CACHE.getIfPresent(key) == null) {
						Thread.currentThread().sleep(100);
					}
					if (IJT808Cache.FB_CACHE.getIfPresent(key) == null) {
						return;
					}
					IJT808Cache.FB_CACHE.getIfPresent(key).writeBytes(content);
					content.release();
					content = null;

					content = IJT808Cache.FB_CACHE.getIfPresent(key);
					IJT808Cache.FB_CACHE.invalidate(key);
				} else {
					if (IJT808Cache.FB_CACHE.getIfPresent(key) == null) {
						ByteBuf bigBuff = Unpooled.buffer(1024, 1024 * 10000);// 最大10M
						bigBuff.writeBytes(content);
						IJT808Cache.FB_CACHE.put(key, bigBuff);
					}
					IJT808Cache.FB_CACHE.getIfPresent(key).writeBytes(content);

					// 因为要return 在这里释放
					return;
				}
			} else {
				content = buff.readByteBuf(length);
			}

			byte check = buff.readByte();
			buff.readByte();

			if (JT808Utils.check(msg, check, length, isFB, isVersion)) {
				IJT808MsgHandler msgHandler = null;
				for (IJT808MsgHandler tmp : this.listMsgHandler) {
					if (tmp.getMsgId() == msgId) {
						msgHandler = tmp;
						break;
					}
				}
				if (msgHandler != null) {
					try {
						msgHandler.handle(content, tid, msgId, msgSn, ctx, isVersion, msg);
					} catch (Exception e) {
						log.error(System.currentTimeMillis() + ":" + Utils.bytesToHex(msg));
						e.printStackTrace();
					}
				} else {
					log.info(String.format("没有对应的处理器：%04X,%s,%s", msgId, tid, Utils.bytesToHex(msg)));
				}

			} else {
				log.error("校验码有误:{}" , Utils.bytesToHex(msg));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		} finally {
			try {
				if (content != null)
					content.release();
				buff.release();
				msg = null;
			} catch (Exception e) {
				System.out.println(Utils.bytesToHex(msg));
				e.printStackTrace();
			}
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		/* 断开不算离线，超过一定时间不上报才算
		if (ctx.channel().hasAttr(AttributeKey.valueOf("TID"))) {
			Set<String> sets = SetsUtils.get(ctx.channel());
			// logger.info("Socket断开，离线终端:{}",JSON.toJSONString(sets));
			for (String tid : sets) {
				JT808OfflineEvent event = new JT808OfflineEvent(spring, tid);
				spring.publishEvent(event);
				IJT808Cache.SESSIONS.invalidate(tid);
			}

		}
		*/
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// System.out.println("==============长连接接入===============");
		super.channelActive(ctx);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
		throwable.printStackTrace();
		// ctx.close();
	}

	public void setListMsgHandler(List<IJT808MsgHandler> listMsgHandler) {
		this.listMsgHandler = listMsgHandler;
	}

	//@Scheduled(cron = "0 0/30 * * * ?") // 每30分钟计算
	public void reloadWhitelist() {
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList("select id,tid from tgps_car");
		IJT808Cache.WHITE_LIST.clear();
		for (Map<String, Object> map : list) {
			IJT808Cache.WHITE_LIST.put(map.get("tid").toString(), map.get("id").toString());
		}

	}
}
