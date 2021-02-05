package com.limaila.bms.web.handler;

import com.limaila.bms.common.constants.RestCode;
import com.limaila.bms.common.exception.CommonException;
import com.limaila.bms.common.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 说明:
 @author MrHuang
 @date 2020/9/9 21:53
 @desc
 ***/

/**
 * 由于Feign调用判断成功的条件是HttpStatus的状态码为200
 * 故所有的异常都要设置状态码非200
 */
@Slf4j
@ControllerAdvice
public class WebExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse<?> exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        log.error("exceptionHandler", e);
        return RestResponse.of(RestCode.INTERNAL_SERVER_ERROR.getTypeCode(), RestCode.INTERNAL_SERVER_ERROR.getTypeMsg(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public RestResponse<?> noHandlerFoundExceptionHandler(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException e) throws Exception {
        log.error("noHandlerFoundExceptionHandler", e);
        return RestResponse.of(RestCode.INTERNAL_SERVER_ERROR.getTypeCode(), RestCode.INTERNAL_SERVER_ERROR.getTypeMsg(), e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = CommonException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse<?> commonExceptionHandler(HttpServletRequest request, HttpServletResponse response, CommonException e) throws Exception {
        return RestResponse.of(RestCode.TOAST.getTypeCode(), RestCode.TOAST.getTypeMsg(), e.getMessage());
    }

}
