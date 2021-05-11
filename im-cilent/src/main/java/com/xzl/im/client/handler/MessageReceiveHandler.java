package com.xzl.im.client.handler;

import com.alibaba.fastjson.JSON;
import com.xzl.im.common.enums.MessageType;
import com.xzl.im.common.message.ImMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author xuzilou
 * @since 2021/5/11 11:08
 */
public class MessageReceiveHandler extends ChannelHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ImMessage imMessage = JSON.parseObject((String) msg, ImMessage.class);
        if (Objects.nonNull(imMessage) && imMessage.getHeader().getType() == MessageType.RECEIVE_MESSAGE.getCode()) {
            System.out.println();
            System.err.println("接受消息:"+imMessage.getBody());
        } else {
            ctx.fireChannelRead(msg);
        }
        super.channelRead(ctx, msg);
    }
}
