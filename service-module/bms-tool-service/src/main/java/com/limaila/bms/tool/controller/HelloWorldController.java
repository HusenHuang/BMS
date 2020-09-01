package com.limaila.bms.tool.controller;

import com.google.common.collect.Maps;
import com.limaila.bms.common.response.RestRSP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
    public RestRSP index() {
        List<String> services = discoveryClient.getServices();
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put("h1", h1);
        map.put("services", services);
        return RestRSP.success(map);
    }

}
