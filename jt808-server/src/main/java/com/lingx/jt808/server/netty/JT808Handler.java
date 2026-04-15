package com.lingx.jt808.server.netty;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.Constants;
import com.lingx.jt808.core.IJT808Cache;
import com.lingx.jt808.core.IJT808MsgHandler;
import com.lingx.jt808.core.cmd.Cmd8001;
import com.lingx.jt808.core.event.JT808OfflineEvent;
import com.lingx.jt808.core.event.JT808OnlineEvent;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.core.support.MyByteBuf;
import com.lingx.jt808.core.utils.JT808Utils;
import com.lingx.jt808.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
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
	private RedisService redisService;
	private Set<Integer> nores8001 = new HashSet<>();

	@Value("#{configs['tracseek.server.check.enabled']}")
	private String isCheckXym="true";
	public static boolean isHexstring=false;
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
		
		if(msg.length<10)return;
		if(isHexstring) {
			this.redisService.publish(Constants.TOPIC_HEXSTRING_DATA, Utils.bytesToHex(msg));
		}
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
			tid=Utils.parseTid(tid);

			if (!IJT808Cache.WHITE_LIST.containsKey(tid)) {
			//	log.info("设备号未注册:{}", tid);
			//	jt808DataService.addCar(tid);
			//	ctx.close(); //不能关闭连接，因为转发多个设备号，不一定都入库了
				return;
			}

			if (ctx != null && !SetsUtils.has(ctx.channel(), tid)) {

				//log.info("Socket上报JT808，终端上线:{},IP:{}", tid, ctx.channel().remoteAddress().toString());
				SetsUtils.add(ctx.channel(), tid);
				JT808OnlineEvent event = new JT808OnlineEvent(spring, tid);
				spring.publishEvent(event);
			}

			if (ctx != null)
				IJT808Cache.SESSIONS.put(tid, ctx.channel());
			int msgSn = buff.readUnsignedShort();// 消息流水号

			if (!nores8001.contains(msgId)) {// 不回复
				Cmd8001 cmd = new Cmd8001(JT808Utils.getTidByVersion(tid, isVersion), msgId, msgSn);
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
						Thread.currentThread().sleep(10);
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

			if (JT808Utils.check(msg, check, length, isFB, isVersion)||"false".equals(isCheckXym)) {
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
					//log.debug(String.format("没有对应的处理器：%04X,%s,%s", msgId, tid, Utils.bytesToHex(msg)));
				}

				
			} else {
				log.error("校验码有误:{}" , Utils.bytesToHex(msg));
			}
		} catch (Exception e) {
			//e.printStackTrace();
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
		if (ctx.channel().hasAttr(AttributeKey.valueOf("TID"))) {
			Set<String> sets = SetsUtils.get(ctx.channel());
			for (String tid : sets) {
				JT808OfflineEvent event = new JT808OfflineEvent(spring, tid);
				spring.publishEvent(event);
				//IJT808Cache.SESSIONS.invalidate(tid);
			}

		}
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//System.out.println("==============长连接接入===============");
		super.channelActive(ctx);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
		//throwable.printStackTrace();
		  if (throwable instanceof ReadTimeoutException) {
		        // 什么都不做，或者只关连接
		        ctx.close();
		        return;
		    }
		    ctx.fireExceptionCaught(throwable);
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
