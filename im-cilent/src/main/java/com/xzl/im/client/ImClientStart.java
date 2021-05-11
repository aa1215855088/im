package com.xzl.im.client;

import com.xzl.im.client.config.ImClientConfig;
import com.xzl.im.client.handler.LoginReqHandler;
import com.xzl.im.client.handler.MessageReceiveHandler;
import com.xzl.im.common.message.LoginMessage;
import com.xzl.im.common.message.SendMessage;
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

/**
 * @author xzl
 * @date 2021-05-10 22:53
 **/
public class ImClientStart {

    static ImClientConfig config = new ImClientConfig("127.0.0.1", 8888);

    private Channel channel;

    private final LoginState loginState = LoginState.getInstance();

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
                                .addLast(new LoginReqHandler())
                                .addLast(new MessageReceiveHandler());
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
                }).sync();
        Thread.sleep(100);
        login();
        sendMessage();
    }

    private void sendMessage() throws InterruptedException {
        System.out.println("开始发送消息!!!");
        System.out.print("请输入聊天的用户:");
        Scanner scanner = new Scanner(System.in);
        String userName = scanner.next();
        while (true) {
            System.out.print("发送消息:");
            String message = scanner.next();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setMessage(message);
            sendMessage.setToUser(userName);
            channel.writeAndFlush(MessageUtil.buildSendMessage(sendMessage));
        }
    }

    private void login() throws InterruptedException {
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
            //登陆失败
            if (!future.isSuccess()) {
                login();
            }
        }).sync();
        loginState.getLatch().await();
        if (loginState.getState() != 1) {
            System.out.println("登陆失败重新登陆");
            login();
        }
    }


}
