package com.springcloud.ribbon.config;


import com.springcloud.ribbon.annotations.MyLoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    /**
     * 学习对于我来说是最费脑的，为什么比怎么办更费脑
     *  为什么 用了这个@MyLoadBalanced 就会拿到MyLoadBalancerAutoConfiguration
     *    中的List<RestTemplate> restTemplates 对象了呢
     * @return
     */
    @Bean
    @LoadBalanced
   // @MyLoadBalanced
    public RestTemplate getResTemplate(){
        return new RestTemplate();
    }

}
