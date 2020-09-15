package com.limaila.bms.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.limaila.bms.common.response.RestRSP;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/***
 说明: 
 @author MrHuang
 @date 2020/9/11 15:54
 @desc
 ***/
public class WebFluxUtils {

    public static Mono<Void> writeToMono(ServerHttpResponse response, RestRSP data) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(data).getBytes());
        return response.writeWith(Flux.just(dataBuffer));
    }
}
