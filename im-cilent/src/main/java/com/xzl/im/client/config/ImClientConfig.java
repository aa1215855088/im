package com.xzl.im.client.config;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author xzl
 * @date 2021-05-10 22:58
 **/
@Data
public class ImClientConfig {

    private final ScheduledExecutorService RETRY_EXECUTOR = Executors.newScheduledThreadPool(1);

    private final EventLoopGroup group = new NioEventLoopGroup();

    private final int port;

    private final String host;

    public ImClientConfig(String host, int port) {
        this.port = port;
        this.host = host;
    }
}
