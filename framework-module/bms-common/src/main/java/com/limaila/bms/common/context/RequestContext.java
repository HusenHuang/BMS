package com.limaila.bms.common.context;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
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
public class RequestContext implements Serializable {

    /**
     * 请求ID
     */
    private String requestId;


    /**
     * 用户标识
     */
    private String userKey;

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

    /**
     * 请求的uri
     */
    private String requestUri;

    private RequestContext() {

    }

    public static RequestContext newInstance() {
        RequestContext rc = new RequestContext();
        rc.setRequestId(UUID.randomUUID().toString());
        RequestContextHolder.setContext(rc);
        return rc;
    }

    public static RequestContext newInstance(RequestContext rc) {
        RequestContextHolder.setContext(rc);
        return rc;
    }
}
