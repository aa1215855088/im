package com.xzl.im.server.config;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;

/**
 * @author xzl
 * @date 2021-05-10 22:20
 **/
@Data
public class ImServerConfig {
    private final EventLoopGroup bossGroup;

    private final EventLoopGroup workerGroup;

    private int port;

    public ImServerConfig(int port) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
    }

    public ImServerConfig(int bossThreads, int workerThread) {
        this.bossGroup = new NioEventLoopGroup(bossThreads);
        this.workerGroup = new NioEventLoopGroup(workerThread);
    }

}
