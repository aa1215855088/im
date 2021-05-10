package com.xzl.im.server;

import com.xzl.im.server.config.ImServerConfig;
import com.xzl.im.server.handler.LoginRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author xzl
 * @date 2021-05-10 22:02
 **/
public class ImServerStart {

    ImServerConfig config = new ImServerConfig(8888);

    public void start() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(config.getBossGroup(), config.getWorkerGroup())
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(new LoginRespHandler());
                    }
                });
        ChannelFuture f = null;
        try {
            f = b.bind(config.getPort()).sync();
        } catch (InterruptedException e) {
            System.out.println("im server start fail");
            return;
        }
        System.out.println("im server start success");
    }

    public static void main(String[] args) {
        new ImServerStart().start();
    }
}
