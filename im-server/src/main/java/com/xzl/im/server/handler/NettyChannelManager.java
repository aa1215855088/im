package com.xzl.im.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xzl
 * @date 2021-05-10 22:27
 **/
public class NettyChannelManager {

    /**
     * channel映射
     */
    private final Map<ChannelId, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 用户映射
     */
    private final Map<String, Channel> userMap = new ConcurrentHashMap<>();

    public void add(Channel channel) {
        channelMap.put(channel.id(), channel);
    }

    public void addUser(String userId, Channel channel) {
        ChannelId channelId = channel.id();
        Channel channelC = channelMap.get(channelId);
        if (Objects.nonNull(channelC)) {
            userMap.put(userId, channel);
        }
    }

}
