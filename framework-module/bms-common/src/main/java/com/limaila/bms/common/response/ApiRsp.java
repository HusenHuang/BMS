package com.limaila.bms.common.response;

import com.limaila.bms.common.utils.BmsEnvCommon;
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
    @Builder.Default
    private boolean isFallBack = false;

    /**
     * 提供方IP
     */
    @Builder.Default
    private String providerIp = BmsEnvCommon.getPodIp();

    /**
     * 提供方NodeName
     */
    @Builder.Default
    private String providerNodeName = BmsEnvCommon.getNodeName();

    /**
     * 数据
     */
    private T data;
}
