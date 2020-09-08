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
     * 系统异常
     */
    SYSTEM_FAILED(2, "SYSTEM_FAILED"),

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
