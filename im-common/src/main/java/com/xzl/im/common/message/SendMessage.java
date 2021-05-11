package com.xzl.im.common.message;

import lombok.Data;

/**
 * @author xuzilou
 * @since  2021/5/11 10:53
 */
@Data
public class SendMessage {

    private String toUser;

    private String message;
}
