package com.lingx.jt808.core.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscribe extends JedisPubSub {
	private ChannelHandlerContext ctx;
	public RedisSubscribe(ChannelHandlerContext ctx) {
		this.ctx=ctx;
	}
	 @Override
     public void onMessage(String channel, String message) {
         if(message.indexOf("unsubscribe")>=0&&message.indexOf(ctx.channel().id().asLongText())>=0) {
        	 unsubscribe(channel);
        	 return;
         }
         if(!this.ctx.channel().isActive()) {
        	 unsubscribe(channel);
        	 return;
         }
         
         try {
			ctx.writeAndFlush(new TextWebSocketFrame(message));
		} catch (Exception e) {
			unsubscribe(channel);
		}
     }

     @Override
     public void unsubscribe(String... channels) {
         super.unsubscribe(channels);
     }

 	public static void main(String args[]) {
 		System.out.println(1);
 	}
}
