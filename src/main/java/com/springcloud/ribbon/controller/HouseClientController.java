package com.springcloud.ribbon.controller;


import com.springcloud.ribbon.bean.HouseInfo;
import com.springcloud.ribbon.client.UserRemoteClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Api(value = "desc of HouseClientController class")
@RestController
public class HouseClientController {

    @Autowired
    private UserRemoteClient UserRemoteClient;

    @Autowired
    RestTemplate restTemplate;

    @ApiOperation(value = "desc of getData method", notes = "")
    @GetMapping("/call/data")
    public HouseInfo getData(@RequestParam("name") String name) {
        return restTemplate.getForObject( "http://localhost:8182/house/data?name="+ name, HouseInfo.class);
    }

    @ApiOperation(value = "desc of getData2 method", notes = "")
    @GetMapping("/call/data/{name}")
    public String getData2(@PathVariable("name") String name) {
        return restTemplate.getForObject( "http://localhost:8182/house/data/{name}", String.class, name);
    }

    @GetMapping("/call/save")
    public Long add() {
        HouseInfo houseInfo = new HouseInfo();
        houseInfo.setCity("上海");
        houseInfo.setRegion("虹口");
        houseInfo.setName("×××");
        Long id = restTemplate.postForObject("http://localhost:8182/house/save", houseInfo, Long.class);
        return id;
    }

    @ApiOperation(value = "desc of callHello method", notes = "")
    @GetMapping("/callHello")
    public String callHello(){
        String hello = UserRemoteClient.hello();
        System.out.println("远程调用返回的结果为：" + hello);
        return "hello hello ya handsome boy";
    }

}
