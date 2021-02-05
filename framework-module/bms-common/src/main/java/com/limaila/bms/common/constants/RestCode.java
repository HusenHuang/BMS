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
     * 业务提示
     */
    TOAST(-1, "TOAST"),


    /**
     * SUCCESS
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 系统异常
     */
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),


    /**
     * 系统异常
     */
    NOT_FOUND(404, "NOT_FOUND"),

    ;

    /**
     * CODE
     */
    private int typeCode;

    /**
     * CODE MSG
     */
    private String typeMsg;
}
