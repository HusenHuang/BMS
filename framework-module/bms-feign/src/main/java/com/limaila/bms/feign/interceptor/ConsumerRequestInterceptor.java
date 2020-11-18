package com.limaila.bms.feign.interceptor;

import com.alibaba.fastjson.JSON;
import com.limaila.bms.common.constants.HeaderConstant;
import com.limaila.bms.common.context.RequestContext;
import com.limaila.bms.common.context.RequestContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/***
 说明: 消费方传递W_CONTEXT
 @author MrHuang
 @date 2020/4/16 19:01
 @desc
 ***/
@Component
@Slf4j
public class ConsumerRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestContext context = RequestContextHolder.getContext();
        if (context != null) {
            String contextText = JSON.toJSONString(context);
            try {
                requestTemplate.header(HeaderConstant.HEADER_CONTEXT, URLEncoder.encode(contextText, "UTF-8"));
            } catch (Exception e) {
                log.error("ConsumerRequestInterceptor fail", e);
            }
        }
        log.info("rc ---- consumer = " + JSON.toJSONString(context));
    }
}
