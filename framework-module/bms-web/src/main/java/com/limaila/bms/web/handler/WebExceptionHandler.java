package com.limaila.bms.web.handler;

import com.limaila.bms.common.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    /**
     * 由于Feign调用判断成功的条件是HttpStatus的状态码为200
     * 故所有的异常都要设置状态码为500
     *
     *
     * @param request
     * @param response
     * @param e
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse<?> defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        log.error("Exception Handler ", e);
        return RestResponse.failed(e.getMessage());
    }
}
