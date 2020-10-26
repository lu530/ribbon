package com.springcloud.ribbon;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.springcloud.ribbon.client")
public class App {

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
}
