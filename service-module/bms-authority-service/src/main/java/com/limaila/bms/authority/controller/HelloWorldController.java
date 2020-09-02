package com.limaila.bms.authority.controller;

import com.google.common.collect.Maps;
import com.limaila.bms.common.response.ApiRsp;
import com.limaila.bms.common.response.RestRSP;
import com.limaila.bms.common.utils.BmsEnvCommon;
import com.limaila.bms.tool.api.IBannerClient;
import com.limaila.bms.tool.bean.Banner;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String h1;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private IBannerClient bannerClient;

    @RequestMapping
    public RestRSP index() {
        List<String> services = discoveryClient.getServices();
        ApiRsp<List<Banner>> rsp = bannerClient.getBannerList();
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put("h1", h1);
        map.put("services", services);
        map.put("rsp", rsp);
        map.put("nodeName", BmsEnvCommon.getNodeName());
        map.put("podIp", BmsEnvCommon.getPodIp());
        map.put("serverEnv", BmsEnvCommon.getServerEnv());
        return RestRSP.success(map);
    }

}
