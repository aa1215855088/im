package com.xzl.im.client.handler;

import com.alibaba.fastjson.JSON;
import com.xzl.im.common.enums.MessageType;
import com.xzl.im.common.message.ImMessage;
import com.xzl.im.common.message.LoginMessage;
import com.xzl.im.common.message.MessageHeader;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;

/**
 * @author xzl
 * @date 2021-05-10 22:23
 **/
public class LoginReqHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String jsonMsg = (String) msg;
        ImMessage imMessage = JSON.parseObject(jsonMsg, ImMessage.class);
        if (Objects.nonNull(imMessage) && imMessage.getHeader().getType() == MessageType.LOGIN_RESP.getCode()) {
            System.out.println(imMessage.getBody());
        } else {
            ctx.fireChannelRead(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
