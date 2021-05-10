package com.xzl.im.common.message;

import lombok.Data;

/**
 * @author xzl
 * @date 2021-05-10 22:40
 **/
@Data
public class LoginMessage {
    private String userName;
    private String password;
}
