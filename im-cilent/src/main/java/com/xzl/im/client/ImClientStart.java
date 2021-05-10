package com.xzl.im.client;

import com.alibaba.fastjson.JSON;
import com.xzl.im.client.config.ImClientConfig;
import com.xzl.im.client.handler.LoginReqHandler;
import com.xzl.im.common.message.LoginMessage;
import com.xzl.im.common.util.MessageUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author xzl
 * @date 2021-05-10 22:53
 **/
public class ImClientStart {

    static ImClientConfig config = new ImClientConfig("127.0.0.1", 8888);

    private Channel channel;

    public void connect() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(config.getGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(new LoginReqHandler());
                    }
                });
        ChannelFuture sync = bootstrap
                .connect(new InetSocketAddress(config.getHost(), config.getPort()))
                .addListener((ChannelFutureListener) future -> {
                    // 连接失败
                    if (!future.isSuccess()) {
                        connect();
                        return;
                    }
                    // 连接成功
                    channel = future.channel();
                });
        Thread.sleep(100);
        login();
    }

    private void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名密码！");
        System.out.print("用户名:");
        String userName = scanner.next();
        System.out.print("密码:");
        String password = scanner.next();
        LoginMessage loginMessage = new LoginMessage();
        loginMessage.setUserName(userName);
        loginMessage.setPassword(password);
        channel.writeAndFlush(MessageUtil.buildLoginMessage(loginMessage)).addListener((ChannelFutureListener) future -> {
            // 连接失败
            if (!future.isSuccess()) {
                login();
            }
        });
    }

    public static void main(String[] args) {
        ImClientStart imClientStart = new ImClientStart();
        try {
            imClientStart.connect();
        } catch (Exception e) {
            System.out.println("启动失败！开始重新链接");
            config.getRETRY_EXECUTOR().execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    imClientStart.connect();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            });
        }

    }

}
