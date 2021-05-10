package com.xzl.im.common.util;

import com.alibaba.fastjson.JSON;
import com.xzl.im.common.enums.MessageType;
import com.xzl.im.common.message.ImMessage;
import com.xzl.im.common.message.LoginMessage;
import com.xzl.im.common.message.MessageHeader;

/**
 * @author xzl
 * @date 2021-05-10 23:21
 **/
public class MessageUtil {
    public static String buildLoginMessage(LoginMessage message) {
        ImMessage imMessage = new ImMessage();
        MessageHeader header = new MessageHeader();
        header.setType(MessageType.LOGIN_REQ.getCode());
        imMessage.setHeader(header);
        imMessage.setBody(JSON.toJSONString(message));
        return JSON.toJSONString(imMessage) + "\n";
    }

    public static String buildLoginSuccessMessage(String body) {
        ImMessage imMessage = new ImMessage();
        MessageHeader header = new MessageHeader();
        header.setType(MessageType.LOGIN_RESP.getCode());
        imMessage.setHeader(header);
        imMessage.setBody(body);
        return JSON.toJSONString(imMessage) + "\n";
    }
}
