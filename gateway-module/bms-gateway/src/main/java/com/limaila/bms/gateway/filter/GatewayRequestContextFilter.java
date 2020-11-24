package com.limaila.bms.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.limaila.bms.common.constants.HeaderConstant;
import com.limaila.bms.common.context.RequestContext;
import com.limaila.bms.common.response.RestResponse;
import com.limaila.bms.gateway.utils.WebFluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Component
public class GatewayRequestContextFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        RequestContext rc = RequestContext.newInstance();
        log.info("GatewayRequestContextFilter ---- filter = " + JSON.toJSONString(rc) + "................");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest.Builder mutate = request.mutate();
        try {
            mutate.header(HeaderConstant.HEADER_CONTEXT,  URLEncoder.encode(JSON.toJSONString(rc), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("[RequestContextFilter] 无法解析 RequestContext = '" + rc + "'", e);
            return WebFluxUtils.writeToMono(response, RestResponse.failed("解析异常"));
        }
        ServerWebExchange mutableExchange = exchange.mutate().request(mutate.build()).build();
        return chain.filter(mutableExchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
