package com.limaila.bms.tool.controller;

import com.google.common.collect.Maps;
import com.limaila.bms.common.response.RSP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/***
 说明: 
 @author MrHuang
 @date 2020/8/28 14:14
 @desc
 ***/
@RestController
@RequestMapping("/helloworld")
public class HelloWorldController {

    @Value("${h1}")
    private String h1;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping
    public RSP index() {
        List<String> services = discoveryClient.getServices();
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put("h1", h1);
        map.put("services", services);
        return RSP.success(map);
    }

}