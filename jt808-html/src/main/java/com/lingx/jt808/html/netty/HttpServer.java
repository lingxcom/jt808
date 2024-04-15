package com.lingx.jt808.html.netty;

import com.lingx.jt808.core.AbstractJT808ThreadService;
import com.lingx.jt808.core.IJT808ThreadService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

@Component(value="HtmlServer")
public class HttpServer  extends AbstractJT808ThreadService implements IJT808ThreadService, Runnable {
	private ChannelFuture future = null;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	private int port=9875;

	public HttpServer(){
	}

	public HttpServer(int port){
		this.port=port;
	}

	private HttpServerInitializer httpServerInitializer=new HttpServerInitializer();

	public void startServer() throws Exception {

		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(httpServerInitializer);
			future = b.bind(port).sync();
			future.channel().closeFuture().sync();
			/* b.bind(portNumber).sync().channel().closeFuture().sync(); */
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void stop() {
		try {
			if (future != null)
				future.channel().close();
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
		return "JT808-HTML -> HTML服务:"+port;
	}

}
