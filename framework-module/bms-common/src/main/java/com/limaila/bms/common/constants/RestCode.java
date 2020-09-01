package com.limaila.bms.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 说明: 
 @author MrHuang
 @date 2020/4/14 18:38
 @desc
 ***/
@AllArgsConstructor
@Getter
public enum RestCode {

    /**
     * SUCCESS
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 业务异常
     */
    BUSINESS_FAILED(1, "BUSINESS_FAILED"),

    /**
     * 网关熔断
     */
    FALLBACK_FAILED(2, "FALLBACK_FAILED"),

    /**
     * 网关限流
     */
    RESTRICT_FAILED(3, "RESTRICT_FAILED"),

    ;

    /**
     * CODE
     */
    private int code;

    /**
     * CODE MSG
     */
    private String msg;
}
