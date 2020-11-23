package com.springcloud.ribbon.callback;

import org.springframework.stereotype.Component;

@Component
public class UserRemoteClientFallback {
    public String hello() {
        return "fail";
    }
}
