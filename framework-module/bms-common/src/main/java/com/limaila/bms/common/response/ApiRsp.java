package com.limaila.bms.common.response;

import lombok.*;

import java.io.Serializable;

/***
 说明: 
 @author MrHuang
 @date 2020/9/1 19:33
 @desc
 ***/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiRsp<T> implements Serializable {

    /**
     * 是否熔断
     */
    private boolean isFallBack;

    /**
     * 数据
     */
    private T data;
}
