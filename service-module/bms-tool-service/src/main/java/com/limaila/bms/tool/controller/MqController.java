package com.limaila.bms.tool.controller;

import com.limaila.bms.common.response.RestResponse;
import com.limaila.bms.mq.MQUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mq")
public class MqController {


    @GetMapping("/async/{topic}/{payload}")
    public RestResponse<?> async(@PathVariable String topic, @PathVariable String payload) {
        MQUtils.asyncSend(topic, payload);
        return RestResponse.success("SUCCESS");
    }
}
