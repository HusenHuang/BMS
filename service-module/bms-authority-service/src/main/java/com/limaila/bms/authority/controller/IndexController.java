package com.limaila.bms.authority.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Maps;
import com.limaila.bms.common.response.RestResponse;
import com.limaila.bms.common.utils.BmsEnvCommon;
import com.limaila.bms.common.utils.BmsSentinelRouteCommon;
import com.limaila.bms.tool.api.IBannerApi;
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
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private IBannerApi bannerClient;

    @SentinelResource(value = BmsSentinelRouteCommon.BMS_AUTHORITY_INDEX_INDEX)
    @RequestMapping
    public RestResponse<?> index() {
        List<String> services = discoveryClient.getServices();
        RestResponse<List<Banner>> rsp = bannerClient.getBannerList();
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put("services", services);
        map.put("rsp", rsp);
        map.put("nodeName", BmsEnvCommon.getNodeName());
        map.put("podIp", BmsEnvCommon.getPodIp());
        map.put("serverEnv", BmsEnvCommon.getServerEnv());
        return RestResponse.success(map);
    }

}
