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


    /**
     * @Autowired放到集合上含义就是获取到所有配置了@MyLoadBalanced的RestTemplate对象实例。
     * 这个应该是spring 在做注释拦截的时候对Autowired 有做是否是集合的判断，如果是的话，就将所有有该注解的实例add进去
     *  Map<String,Object> beans = event.getApplicationContext().getBeansWithAnnotation(MyLoadBalanced.class);
     *  雨水落地，雨水流路，枯叶飘落
     */
    @MyLoadBalanced
    @Autowired(required = false)
    private List<RestTemplate> restTemplates = Collections.emptyList();


    @Bean
    public MyLoadBalancerInterceptor myLoadBalancerInterceptor() {

        return new MyLoadBalancerInterceptor();
    }

    //其实 RestTemplate 里面是有链接器的集合的，只要往集合里面加上你定义的拦截器就可以了
    // @Bean估计就是为了SpringBoot帮忙初始化
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
