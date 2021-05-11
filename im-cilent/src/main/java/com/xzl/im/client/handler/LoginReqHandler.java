package com.xzl.im.client.handler;

import com.alibaba.fastjson.JSON;
import com.xzl.im.client.LoginState;
import com.xzl.im.common.enums.MessageType;
import com.xzl.im.common.message.ImMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;

/**
 * @author xzl
 * @since 2021-05-10 22:23
 **/
public class LoginReqHandler extends ChannelHandlerAdapter {

    private final LoginState loginState = LoginState.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String jsonMsg = (String) msg;
        ImMessage imMessage = JSON.parseObject(jsonMsg, ImMessage.class);
        if (Objects.nonNull(imMessage) && imMessage.getHeader().getType() == MessageType.LOGIN_RESP.getCode()) {
            loginState.setState(1);
            loginState.getLatch().countDown();
            System.out.println(imMessage.getBody());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
