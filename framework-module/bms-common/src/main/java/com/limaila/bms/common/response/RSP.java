package com.limaila.bms.common.response;

import com.limaila.bms.common.constants.RCode;
import com.limaila.bms.common.context.RequestContextHolder;
import lombok.*;

import java.io.Serializable;

/***
 @author:MrHuang
 @date: 2020/2/27 3:18
 @desc:
 @version: 1.0
 ***/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RSP implements Serializable {


    /**
     * {@link RCode}
     * 请求失败返回的错误码
     */
    private Integer code;

    /**
     * 请求失败返回的错误信息
     */
    private String msg;

    /**
     * 请求失败返回的子错误码
     */
    private Integer subCode;

    /**
     * 请求失败返回的子错误信息
     */
    private String subMsg;

    /**
     * 日志id
     */
    private String requestId;

    /**
     * 响应具体数据
     */
    private Object data;

    /**
     * 服务器当前时间
     */
    private Long serverTime;


    /**
     * 响应成功
     *
     * @param data 响应数据
     * @return 响应体
     */
    public static RSP success(Object data) {
        return of(RCode.SUCCESS.getCode(), RCode.SUCCESS.getMsg(), RCode.SUCCESS.getCode(), RCode.SUCCESS.getMsg(), data);
    }

    /**
     * 响应成功
     *
     * @return 响应体
     */
    public static RSP success() {
        return of(RCode.SUCCESS.getCode(), RCode.SUCCESS.getMsg(), RCode.SUCCESS.getCode(), RCode.SUCCESS.getMsg());
    }


    /**
     * 业务失败
     *
     * @param subCode 子错误码
     * @param subMsg  子错误信息
     * @return
     */
    public static RSP business(int subCode, String subMsg) {
        return of(RCode.BUSINESS_FAILED.getCode(), RCode.BUSINESS_FAILED.getMsg(),
                subCode, subMsg);
    }

    /**
     * 网关熔断
     *
     * @return
     */
    public static RSP fallback() {
        return of(RCode.FALLBACK_FAILED.getCode(), RCode.FALLBACK_FAILED.getMsg(),
                RCode.FALLBACK_FAILED.getCode(), RCode.FALLBACK_FAILED.getMsg());
    }


    /**
     * 网关限流
     *
     * @return
     */
    public static RSP restrict() {
        return of(RCode.RESTRICT_FAILED.getCode(), RCode.RESTRICT_FAILED.getMsg(),
                RCode.RESTRICT_FAILED.getCode(), RCode.RESTRICT_FAILED.getMsg());
    }


    /**
     * 响应
     *
     * @param code    错误码
     * @param msg     错误信息
     * @param subCode 子错误码
     * @param subMsg  子错误信息
     * @param data    响应数据
     * @return
     */
    public static RSP of(int code, String msg, int subCode, String subMsg, Object data) {
        return RSP.builder()
                .code(code)
                .msg(msg)
                .subCode(subCode)
                .subMsg(subMsg)
                .data(data)
                .serverTime(System.currentTimeMillis())
                .requestId(RequestContextHolder.getContext().getRequestId())
                .build();
    }


    /**
     * 响应
     *
     * @param code    错误码
     * @param msg     错误信息
     * @param subCode 子错误码
     * @param subMsg  子错误信息
     * @return
     */
    public static RSP of(int code, String msg, int subCode, String subMsg) {
        return of(code, msg, subCode, subMsg, null);
    }

}
