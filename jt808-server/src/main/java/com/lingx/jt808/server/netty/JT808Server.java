package com.lingx.jt808.server.netty;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.jt808.core.service.JT808ServerConfigService;
import com.lingx.web.ILingxThread;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

@Component
public class JT808Server implements ILingxThread, Runnable {

    private static final Logger log = LoggerFactory.getLogger(JT808Server.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;

    @Autowired
    private JT808ServerConfigService configService;

    @Autowired
    private JT808ServerInitializer jt808ServerInitializer;

    /**
     * 启动服务
     */
    public void startServer() {
        int port = Integer.parseInt(configService.getJt808ServerPort());

        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("jt808-boss"));
        workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("jt808-worker"));

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)

             // ===== 服务端参数 =====
             .option(ChannelOption.SO_BACKLOG, 1024)
             .option(ChannelOption.SO_REUSEADDR, true)

             // ===== 子连接参数（重点）=====
             .childOption(ChannelOption.SO_KEEPALIVE, true)   // 长连接必须
             .childOption(ChannelOption.TCP_NODELAY, true)    // 减少延迟
             .childOption(ChannelOption.RCVBUF_ALLOCATOR,
                     new AdaptiveRecvByteBufAllocator(64, 2048, 65536))

             .childHandler(jt808ServerInitializer);

            ChannelFuture future = b.bind(port).sync();
            this.channel = future.channel();

            log.info("✅ JT808服务启动成功，端口: {}", port);

            // 阻塞直到关闭
            channel.closeFuture().sync();

        } catch (Exception e) {
            log.error("❌ JT808服务启动失败", e);
        } finally {
            shutdownEventLoop();
        }
    }

    /**
     * 优雅关闭线程池
     */
    private void shutdownEventLoop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        log.info("JT808服务已关闭");
    }

    /**
     * Spring销毁时调用
     */
    @PreDestroy
    public void stop() {
        log.info("正在关闭JT808服务...");
        if (channel != null) {
            channel.close();
        }
        shutdownEventLoop();
    }

    @Override
    public void run() {
        startServer();
    }

    @Override
    public String getName() {
        return "JT808网关服务:" + configService.getJt808ServerPort();
    }

    @Override
    public void startup() {
        new Thread(this, getName()).start();
    }

    @Override
    public void shutdown() {
        stop();
    }
}