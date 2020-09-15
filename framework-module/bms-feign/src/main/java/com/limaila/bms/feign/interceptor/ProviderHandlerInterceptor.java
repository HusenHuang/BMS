package com.limaila.bms.feign.interceptor;

import com.alibaba.fastjson.JSON;
import com.limaila.bms.common.constants.HeaderConstant;
import com.limaila.bms.common.context.RequestContext;
import com.limaila.bms.common.context.RequestContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/***
 说明: 
 @author MrHuang
 @date 2020/4/20 18:42
 @desc
 ***/
@Component
@Slf4j
public class ProviderHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String context = request.getHeader(HeaderConstant.HEADER_CONTEXT);
        if (!StringUtils.isEmpty(context)) {
            try {
                context = URLDecoder.decode(context, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("ProviderHandlerInterceptor decode context fail", e);
            }
            RequestContextHolder.setContext(JSON.parseObject(context, RequestContext.class));
        } else {
            RequestContextHolder.setContext(RequestContext.newInstance());
        }
        return true;
    }
}
