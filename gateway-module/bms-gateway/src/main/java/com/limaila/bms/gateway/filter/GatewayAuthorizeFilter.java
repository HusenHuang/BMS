package com.limaila.bms.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.limaila.bms.common.constants.HeaderConstant;
import com.limaila.bms.common.context.RequestContext;
import com.limaila.bms.common.response.RestResponse;
import com.limaila.bms.gateway.utils.WebFluxUtils;
import com.limaila.bms.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/***
 说明: 授权过滤器
 @author MrHuang
 @date 2020/9/11 14:57
 @desc
 ***/
@Slf4j
@Component
public class GatewayAuthorizeFilter implements GlobalFilter, Ordered {

    /**
     * 可以做URLs匹配，规则如下
     * <p>
     * ？匹配一个字符
     * *匹配0个或多个字符
     * **匹配0个或多个目录
     * 用例如下
     * <p>
     * /trip/api/*x    匹配 /trip/api/x，/trip/api/ax，/trip/api/abx ；但不匹配 /trip/abc/x；
     * /trip/a/a?x    匹配 /trip/a/abx；但不匹配 /trip/a/ax，/trip/a/abcx
     * /** /api/alie    匹配 /trip/api/alie，/trip/dax/api/alie；但不匹配 /trip/a/api
     * /** /*.htmlm   匹配所有以.htmlm结尾的路径
     **/
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Value("${authorize.noAuthUris}")
    private List<String> noAuthUris;

    @Value("${authorize.secretKey}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        RequestContext rc = RequestContext.newInstance();
        log.info("rc ---- gateway = " + JSON.toJSONString(rc));
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().getPath();
        // 无论是登陆或者不登陆都必须带上下文
        ServerHttpRequest.Builder mutate = request.mutate();
        try {
            mutate.header(HeaderConstant.HEADER_CONTEXT,  URLEncoder.encode(JSON.toJSONString(rc), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("[AuthorizeFilter] 无法解析 RequestContext = '" + rc + "'", e);
            return WebFluxUtils.writeToMono(response, RestResponse.failed("解析异常"));
        }
        //  检查是否不需要登录
        if (!CollectionUtils.isEmpty(noAuthUris)) {
            for (String noAuthUri : noAuthUris) {
                if (antPathMatcher.match(noAuthUri, uri)) {
                    // 符合不需要登录 直接放行
                    ServerWebExchange mutableExchange = exchange.mutate().request(mutate.build()).build();
                    return chain.filter(mutableExchange);
                }
            }
        }

        // 获取登录标识
        String authorization = request.getHeaders().getFirst(HeaderConstant.HEADER_AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            return WebFluxUtils.writeToMono(response, RestResponse.failed("非法请求"));
        }
        // 解析登录标识
        try {
            DecodedJWT decodedJWT = JWTUtil.resolveToken(authorization, secretKey);
            String userKey = JWTUtil.getClaimAsString(decodedJWT);
            rc.setUserKey(userKey);
            // 设置到请求头中
            mutate.header(HeaderConstant.HEADER_USER_KEY, userKey);
        } catch (Exception e) {
            log.error("[AuthorizeFilter] 无法解析 authorization = '" + authorization + "'", e);
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
