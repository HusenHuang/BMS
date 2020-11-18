package com.limaila.bms.web.interceptor;


import com.limaila.bms.common.constants.HeaderConstant;
import com.limaila.bms.common.context.RequestContext;
import com.limaila.bms.common.context.RequestContextHolder;
import com.limaila.bms.json.JSONUtils;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebRequestContextResolvingInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<HandlerMethod, Boolean> cache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestContext.newInstance();
        try {
            request.setCharacterEncoding("utf-8");
            String contextJson = request.getHeader(HeaderConstant.HEADER_CONTEXT);
            if (StringUtils.isNotBlank(contextJson)) {
                String decode = URLDecoder.decode(contextJson, "UTF-8");
                log.info("当前请求头中的 requestContext:{}", contextJson);
                RequestContext.newInstance(JSONUtils.toBean(decode, RequestContext.class));
            } else {
                log.info("当前请求中没有附带 requestContext");
            }
        } catch (Throwable e) {
            log.error("解析requestContext 出现异常", e);
        }
        log.info("rc ---- web = " + RequestContextHolder.getContext());
        RequestContextHolder.getContext().setRequestUri(request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContextHolder.removeContext();
    }

}
