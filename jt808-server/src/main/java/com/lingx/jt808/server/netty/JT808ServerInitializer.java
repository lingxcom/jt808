package com.lingx.jt808.server.netty;

import javax.annotation.PostConstruct;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

@Component
public class JT808ServerInitializer extends ChannelInitializer<SocketChannel> {
	@Resource
	private JT808Handler gpsHandler;

	@PostConstruct
	public void init() {

	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("decoder", new ProtocolDecoder());
		// 字符串解码 和 编码
		pipeline.addLast(new ReadTimeoutHandler(360));
		pipeline.addLast("encoder", new ByteArrayEncoder());

		// 自己的逻辑Handler
		pipeline.addLast("handler", gpsHandler);
	}

}
