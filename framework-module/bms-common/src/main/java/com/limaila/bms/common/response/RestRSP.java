package com.limaila.bms.common.response;

import com.limaila.bms.common.constants.RestCode;
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
public class RestRSP implements Serializable {


    /**
     * {@link RestCode}
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
    public static RestRSP success(Object data) {
        return of(RestCode.SUCCESS.getCode(), RestCode.SUCCESS.getMsg(), RestCode.SUCCESS.getCode(), RestCode.SUCCESS.getMsg(), data);
    }

    /**
     * 响应成功
     *
     * @return 响应体
     */
    public static RestRSP success() {
        return of(RestCode.SUCCESS.getCode(), RestCode.SUCCESS.getMsg(), RestCode.SUCCESS.getCode(), RestCode.SUCCESS.getMsg());
    }


    /**
     * 业务失败
     *
     * @param subCode 子错误码
     * @param subMsg  子错误信息
     * @return
     */
    public static RestRSP business(int subCode, String subMsg) {
        return of(RestCode.BUSINESS_FAILED.getCode(), RestCode.BUSINESS_FAILED.getMsg(),
                subCode, subMsg);
    }

    /**
     * 网关熔断
     *
     * @return
     */
    public static RestRSP fallback() {
        return of(RestCode.FALLBACK_FAILED.getCode(), RestCode.FALLBACK_FAILED.getMsg(),
                RestCode.FALLBACK_FAILED.getCode(), RestCode.FALLBACK_FAILED.getMsg());
    }


    /**
     * 网关限流
     *
     * @return
     */
    public static RestRSP restrict() {
        return of(RestCode.RESTRICT_FAILED.getCode(), RestCode.RESTRICT_FAILED.getMsg(),
                RestCode.RESTRICT_FAILED.getCode(), RestCode.RESTRICT_FAILED.getMsg());
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
    public static RestRSP of(int code, String msg, int subCode, String subMsg, Object data) {
        return RestRSP.builder()
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
    public static RestRSP of(int code, String msg, int subCode, String subMsg) {
        return of(code, msg, subCode, subMsg, null);
    }

}
