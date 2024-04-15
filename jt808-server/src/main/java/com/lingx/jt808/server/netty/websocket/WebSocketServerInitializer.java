package com.lingx.jt808.server.netty.websocket;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
@Component
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {
	@Autowired
	private WebSocketHandler handler;
    public WebSocketServerInitializer( ) {
        super();
    }
 
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("http-codec",
                new HttpServerCodec());
        pipeline.addLast("aggregator",
                new HttpObjectAggregator(65536));
        ch.pipeline().addLast("http-chunked",
                new ChunkedWriteHandler());
        pipeline.addLast("handler",
        		handler);
         
    }
 
}