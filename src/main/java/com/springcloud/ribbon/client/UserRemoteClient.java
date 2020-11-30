package com.springcloud.ribbon.client;

import com.springcloud.ribbon.callback.UserRemoteClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value = "center-educational-server", url = "${yitu.service.url}")
public interface UserRemoteClient {

    @GetMapping("/house/hollo")
    String hello();


}
