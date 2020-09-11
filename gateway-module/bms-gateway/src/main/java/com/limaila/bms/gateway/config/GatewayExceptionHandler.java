package com.limaila.bms.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.limaila.bms.common.response.RestRSP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 参考https://www.cnblogs.com/yinjihuan/p/10474774.html
 */
@Slf4j
public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GatewayExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                   ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }


    private static Map<String, Object> response(RestRSP rsp) {
        return JSON.parseObject(JSON.toJSONString(rsp), new TypeReference<LinkedHashMap<String, Object>>() {
        });
    }


    private Map<String, Object> buildStatusResponse(HttpStatus status) {
        if (status.is4xxClientError()) {
            return response(RestRSP.systemFailed("资源无权访问"));
        } else {
            return response(RestRSP.systemFailed("系统异常"));
        }
    }

    /**
     * 获取异常属性
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> response;
        Throwable error = super.getError(request);
        log.error("GatewayExceptionHandler ", error);
        if (error instanceof ResponseStatusException) {
            response = buildStatusResponse(((ResponseStatusException) error).getStatus());
        } else if (error instanceof org.springframework.cloud.gateway.support.TimeoutException) {
            response = response(RestRSP.systemFailed("系统异常"));
        } else if (error instanceof java.net.ConnectException) {
            response = response(RestRSP.systemFailed("系统异常"));
        } else {
            response = response(RestRSP.systemFailed("系统异常"));
        }
        return response;
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }


    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.OK.value();
    }

}
