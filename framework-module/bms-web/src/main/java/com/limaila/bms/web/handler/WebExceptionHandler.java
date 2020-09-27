package com.limaila.bms.web.handler;

import com.limaila.bms.common.response.RestRSP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 说明: 
 @author MrHuang
 @date 2020/9/9 21:53
 @desc
 ***/
@Slf4j
@ControllerAdvice
public class WebExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RestRSP defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        log.error("WebExceptionHandler ", e);
        return RestRSP.failed("系统异常");
    }
}
