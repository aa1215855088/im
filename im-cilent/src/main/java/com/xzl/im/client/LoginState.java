package com.xzl.im.client;

import lombok.Data;

import java.util.concurrent.CountDownLatch;

/**
 * @author xuzilou
 * @since 2021/5/11 11:13
 */
@Data
public class LoginState {
    private static final LoginState INSTANCE = new LoginState();

    private volatile int state = 0;

    private CountDownLatch latch = new CountDownLatch(1);

    public static LoginState getInstance() {
        return INSTANCE;
    }
}
