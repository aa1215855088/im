package com.xzl.im.common.enums;

import lombok.Getter;

/**
 * @author xzl
 * @date 2021-05-10 22:42
 **/
@Getter
public enum MessageType {
    /**
     *
     */
    LOGIN_REQ((byte) 1),
    LOGIN_RESP((byte) 2),
    SEND_MESSAGE((byte) 3),
    RECEIVE_MESSAGE((byte) 4),
    ;
    private final byte code;

    MessageType(byte code) {
        this.code = code;
    }
}
