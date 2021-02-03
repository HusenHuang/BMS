package com.limaila.bms.tool.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Maps;
import com.limaila.bms.authority.api.IUserApi;
import com.limaila.bms.authority.bean.User;
import com.limaila.bms.common.response.RestResponse;
import com.limaila.bms.common.utils.BmsEnvCommon;
import com.limaila.bms.common.utils.BmsSentinelRouteCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/index2")
public class IndexController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private IUserApi userApi;

    @SentinelResource(value = BmsSentinelRouteCommon.BMS_TOOL_INDEX_INDEX)
    @RequestMapping
    public RestResponse<?> index() {
        List<String> services = discoveryClient.getServices();
        RestResponse<User> user = userApi.getUserById(1L);
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put("services", services);
        map.put("user", user);
        map.put("nodeName", BmsEnvCommon.getNodeName());
        map.put("podIp", BmsEnvCommon.getPodIp());
        map.put("serverEnv", BmsEnvCommon.getServerEnv());
        return RestResponse.success(map);
    }
}
