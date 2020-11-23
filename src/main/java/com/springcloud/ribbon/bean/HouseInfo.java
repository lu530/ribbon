package com.springcloud.ribbon.bean;


import lombok.Data;

import java.io.Serializable;

@Data
public class HouseInfo implements Serializable {

    public Long id;

    public String name;

    public String city;

    //Municipal District
    public String region;

    public String address;


    public HouseInfo() {

    }

    public HouseInfo(Long id, String city, String region, String address) {
        this.id = id;
        this.city = city;
        this.region = region;
        this.address = address;
    }


}
