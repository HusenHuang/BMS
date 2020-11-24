package com.limaila.bms.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.limaila.bms.common.constants.HeaderConstant;
import com.limaila.bms.common.context.RequestContext;
import com.limaila.bms.common.exception.CommonException;
import com.limaila.bms.common.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.util.Objects;

/***
 说明: 
 @author MrHuang
 @date 2020/9/11 15:54
 @desc
 ***/
@Slf4j
public class WebFluxUtils {

    private WebFluxUtils() {
    }


    /**
     * WebFlux write To Mono
     *
     * @param response 响应流
     * @param data     数据
     * @return
     */
    public static Mono<Void> writeToMono(ServerHttpResponse response, RestResponse data) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(data).getBytes());
        return response.writeWith(Flux.just(dataBuffer));
    }


    /**
     * 上下文获取
     *
     * @param exchange ServerWebExchange
     * @return
     */
    public static RequestContext getWebContext(ServerWebExchange exchange) {
        try {
            String webContext = URLDecoder.decode(Objects.requireNonNull(exchange.getRequest().getHeaders().getFirst(HeaderConstant.HEADER_CONTEXT)), "UTF-8");
            return JSON.parseObject(webContext, RequestContext.class);
        } catch (Exception e) {
            log.error("WebFluxUtils getWebContext error ", e);
            throw new CommonException(e);
        }
    }
}
