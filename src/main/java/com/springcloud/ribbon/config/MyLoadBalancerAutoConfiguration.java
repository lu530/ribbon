package com.springcloud.ribbon.config;


import com.springcloud.ribbon.annotations.MyLoadBalanced;
import com.springcloud.ribbon.interceptor.MyLoadBalancerInterceptor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//然后定义一个配置类，给 RestTemplate 注入拦截器
@Configuration
public class MyLoadBalancerAutoConfiguration {

    @MyLoadBalanced
    @Autowired(required = false)
    private List<RestTemplate> restTemplates = Collections.emptyList();


    @Bean
    public MyLoadBalancerInterceptor myLoadBalancerInterceptor() {
        return new MyLoadBalancerInterceptor(new LoadBalancerClient());
    }
    @Bean
    public SmartInitializingSingleton myLoadBalancedRestTemplateInitializer() {
        return new SmartInitializingSingleton() {
            @Override
            public void afterSingletonsInstantiated() {
                for (RestTemplate restTemplate : MyLoadBalancerAutoConfiguration.this.restTemplates){
                    List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>(restTemplate.getInterceptors());
                    list.add(myLoadBalancerInterceptor());
                    restTemplate.setInterceptors(list);
                }
            }
        };
    }


}
