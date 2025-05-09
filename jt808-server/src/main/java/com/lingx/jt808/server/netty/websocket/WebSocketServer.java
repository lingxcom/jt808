package com.lingx.jt808.server.netty.websocket;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.AbstractJT808ThreadService;
import com.lingx.jt808.core.IJT808ThreadService;
import com.lingx.jt808.server.service.JT808ServerConfigService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
@Component
public class WebSocketServer extends AbstractJT808ThreadService implements IJT808ThreadService, Runnable{
	private ChannelFuture future = null;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	@Autowired
	private JT808ServerConfigService configService;
	@Autowired
	private WebSocketServerInitializer wsServerInitializer;

	public void startServer() throws Exception {
		int port=Integer.parseInt(this.configService.getWebsocketPort());
		bossGroup = new NioEventLoopGroup(0,new DefaultThreadFactory("JT808ServerWebSocketBossGroup"));
		workerGroup = new NioEventLoopGroup(0,new DefaultThreadFactory("JT808ServerWebSocketWorkerGroup"));
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(wsServerInitializer);

			// 服务器绑定端口监听
			future = b.bind(port).sync();
			future.channel().closeFuture().sync();
			// 可以简写为
			/* b.bind(portNumber).sync().channel().closeFuture().sync(); */
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	@PreDestroy
	public void stop() {
		try {
			// 监听服务器关闭监听
			if (future != null)
				future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

	@Override
	public void run() {
		try {
			this.startServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	@Override
	public String getName() {
		
		return "JT808-Server -> Websocket服务:"+this.configService.getWebsocketPort();
	}
}
