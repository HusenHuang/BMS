package com.limaila.bms.common.context;

import lombok.*;

import java.util.UUID;

/***
 @author:MrHuang
 @date: 2020/2/27 3:45
 @desc: 调用上下文
 @version: 1.0
 ***/
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RequestContext {

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 请求时间戳
     */
    private Long timestamp;

    /**
     * 浏览器信息(如:Firefox 65.0)
     */
    private String browser;

    /**
     * 操作系统(如:Windows 7)
     */
    private String os;

    public RequestContext() {
        this.requestId = UUID.randomUUID().toString();
    }
}
