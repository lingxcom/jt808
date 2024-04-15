package com.lingx.jt808.server.netty.websocket;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lingx.jt808.core.service.RedisService;
import com.lingx.jt808.server.service.JT808ServerConfigService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
@Sharable
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {
	@Autowired
	private RedisService redisService;
	@Autowired
	private List<IApi> apis;
	@Autowired
	private JT808ServerConfigService configService;
	/**
	 * websokcet 会话
	 * channelID
	 * ctx
	 */
	public static Cache<String, ChannelHandlerContext> WEBSOCKET_SESSIONS = CacheBuilder.newBuilder().maximumSize(1000000)
			.expireAfterAccess(5, TimeUnit.MINUTES).build();
	
    private WebSocketServerHandshaker handshaker;
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
        	//System.out.println("HTTP 接入");
          handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // WebSocket接入
        else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) throws Exception {

        // 如果HTTP解码失败，返回HTTP异常
        if (!req.getDecoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
                    BAD_REQUEST));
            return;
        }
        WEBSOCKET_SESSIONS.put(ctx.channel().id().asLongText(), ctx);
        // 构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8803/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx,
                                      WebSocketFrame frame) {

        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(),
                    (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }

        WEBSOCKET_SESSIONS.put(ctx.channel().id().asLongText(), ctx);
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        //System.out.println("REQ:"+request);
        Map<String, Object> ret = new HashMap<String, Object>();
		try {
			Map<String, Object> json  = (Map<String, Object>) JSON.parse(request);
			//if (this.configService.getTicket().equals(json.get("ticket").toString())) {
				for (IApi api : this.apis) {
					if (api.matching(json)) {
						ret = api.execute(json,ctx);
						break;
					}
				}
				/*
				 * } else { ret.put("code", -1); ret.put("message", "ticket error"); }
				 */
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", -99);
			ret.put("message", e.getMessage());
		}
		//System.out.println("Websocket输出:"+JSON.toJSONString(ret));
        ctx.channel().write(new TextWebSocketFrame(JSON.toJSONString(ret)));
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
           
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接 
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
    @Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	String cid=ctx.channel().id().asLongText();
    	WEBSOCKET_SESSIONS.invalidate(cid);
    	
    	this.redisService.publish("PUBLISH_"+cid, "unsubscribe:"+cid);
		super.channelInactive(ctx);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		
	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}