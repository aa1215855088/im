package com.xzl.im.common.message;

import lombok.Data;

/**
 * @author xzl
 * @date 2021-05-10 22:38
 **/
@Data
public class ImMessage {

    private MessageHeader header;

    private String body;
}
