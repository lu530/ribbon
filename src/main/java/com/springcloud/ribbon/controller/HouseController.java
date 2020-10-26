package com.springcloud.ribbon.controller;


import com.springcloud.ribbon.bean.HouseInfo;
import com.springcloud.ribbon.client.UserRemoteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HouseController {



    @GetMapping("/house/data")
    public HouseInfo getData(@RequestParam("name") String name) {
        return new HouseInfo(1L, "上海", "虹口","东体小区");
    }

    @GetMapping("/house/data/{name}")
    public String getData2(@PathVariable("name") String name){
        return name;
    }

    @PostMapping("/house/save")
    public Long addData(@RequestBody HouseInfo houseInfo) {
        System.out.println(houseInfo.getName());
        return 1001L;
    }


    @GetMapping("/house/hollo")
    public String hello() {
        System.out.println("hallo hallo ya");
        return "hallo hallo ya";
    }




}
