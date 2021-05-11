package com.xzl.im.server.handler;

import com.alibaba.fastjson.JSON;
import com.xzl.im.common.enums.MessageType;
import com.xzl.im.common.message.ImMessage;
import com.xzl.im.common.message.SendMessage;
import com.xzl.im.common.util.MessageUtil;
import com.xzl.im.server.manager.NettyChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;

/**
 * @author xuzilou
 * @since 2021/5/11 10:46
 */
public class MessageHandler extends ChannelHandlerAdapter {

    private final NettyChannelManager manager = NettyChannelManager.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String jsonMsg = (String) msg;
        ImMessage imMessage = JSON.parseObject(jsonMsg, ImMessage.class);
        if (Objects.nonNull(imMessage) && imMessage.getHeader().getType() == MessageType.SEND_MESSAGE.getCode()) {
            SendMessage sendMessage = JSON.parseObject(imMessage.getBody(), SendMessage.class);
            Channel channel = manager.getUser(sendMessage.getToUser());
            if (channel == null) {
                //记录离线消息
                System.out.println("记录离线消息:" + sendMessage);
                return;
            }
            channel.writeAndFlush(MessageUtil.buildReceiveMessage(sendMessage.getMessage()));
        } else {
            ctx.fireChannelRead(msg);
        }
        super.channelRead(ctx, msg);
    }
}
