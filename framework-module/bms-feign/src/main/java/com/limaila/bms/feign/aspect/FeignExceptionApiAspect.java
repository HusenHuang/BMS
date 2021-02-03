package com.limaila.bms.feign.aspect;

import com.limaila.bms.common.constants.RestCode;
import com.limaila.bms.common.response.RestResponse;
import com.limaila.bms.feign.annotation.FeignExceptionApi;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Aspect
@Component
public class FeignExceptionApiAspect {
 
    @Around(value = "(@annotation(feignExceptionApi) || @within(feignExceptionApi))")
    public Object around4Method(ProceedingJoinPoint joinPoint, FeignExceptionApi feignExceptionApi){
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            Throwable t = Optional.ofNullable(throwable.getCause()).orElseGet(() -> new Throwable("未知异常"));
            log.error("FeignException：{}，原因：{}", throwable.getMessage(), t.getMessage());
            throw new RuntimeException(t);
//            throw FeignRequestException.error("子系统调用异常");
        }
        if (proceed instanceof RestResponse<?>) {
            RestResponse<?> rsp = (RestResponse<?>) proceed;
            if ( RestCode.SUCCESS.getCode() != rsp.getCode()) {
                throw new RuntimeException(rsp.getRemark());
            }
        }
        return proceed;
    }
}