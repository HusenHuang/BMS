package com.limaila.bms.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.limaila.bms.common.constants.HeaderConstant;
import com.limaila.bms.common.constants.RestCode;
import com.limaila.bms.common.context.RequestContext;
import com.limaila.bms.common.response.RestResponse;
import com.limaila.bms.gateway.utils.WebFluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

@Slf4j
@Component
public class GatewayRequestContextFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            RequestContext rc = RequestContext.newInstance();
            log.info("GatewayRequestContextFilter ---- filter = " + JSON.toJSONString(rc) + "................");
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpRequest.Builder mutate = request.mutate();
            mutate.header(HeaderConstant.HEADER_CONTEXT, URLEncoder.encode(JSON.toJSONString(rc), "UTF-8"));
            ServerWebExchange mutableExchange = exchange.mutate().request(mutate.build()).build();
            return chain.filter(mutableExchange);
        } catch (Exception e) {
            log.error("GatewayRequestContextFilter filter error ", e);
            return WebFluxUtils.writeToMono(exchange.getResponse(), RestResponse.failed(RestCode.INTERNAL_SERVER_ERROR, "服务器繁忙"));
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
