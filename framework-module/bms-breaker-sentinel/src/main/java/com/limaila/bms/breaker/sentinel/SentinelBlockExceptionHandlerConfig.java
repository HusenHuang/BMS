package com.limaila.bms.breaker.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import com.limaila.bms.common.response.RestRSP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 说明: 
 @author MrHuang
 @date 2020/9/8 15:16
 @desc
 ***/
@Configuration
public class SentinelBlockExceptionHandlerConfig {

    @Bean
    public BlockExceptionHandler blockExceptionHandler() {
        return (request, response, e) -> {
            RestRSP rsp = sentinelBlockHandler(e);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(rsp));
        };
    }


    private RestRSP sentinelBlockHandler(BlockException ex) {
        RestRSP rsp;
        if (ex instanceof FlowException) {
            // 限流异常
            rsp = RestRSP.failed("FLOW");
        } else if (ex instanceof DegradeException) {
            // 降级异常
            rsp = RestRSP.failed("DEGRADE");
        } else if (ex instanceof ParamFlowException) {
            // 热点参数异常
            rsp = RestRSP.failed("PARAM_FLOW");
        } else if (ex instanceof SystemBlockException) {
            // 系统异常
            rsp = RestRSP.failed("SYSTEM_BLOCK");
        } else if (ex instanceof AuthorityException) {
            // 授权异常
            rsp = RestRSP.failed("AUTHORITY_BLOCK");
        } else {
            rsp = RestRSP.failed("FAILED");
        }
        return rsp;
    }
}
