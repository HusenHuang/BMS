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
public class RestRSP<T> implements Serializable {


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
     * 请求失败返回的错误备注
     */
    private String remark;

    /**
     * 日志id
     */
    private String requestId;

    /**
     * 响应具体数据
     */
    private T data;

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
    public static <T> RestRSP<T> success(T data) {
        return of(RestCode.SUCCESS.getCode(), RestCode.SUCCESS.getMsg(), null, data);
    }

    /**
     * 响应成功
     *
     * @return 响应体
     */
    public static RestRSP success() {
        return of(RestCode.SUCCESS.getCode(), RestCode.SUCCESS.getMsg(), null, null);
    }


    /**
     * 响应失败
     *
     * @param remark 备注
     * @return
     */
    public static RestRSP failed(String remark) {
        return of(RestCode.FAILED.getCode(), RestCode.FAILED.getMsg(), remark, null);
    }

    /**
     * 响应
     *
     * @param code   错误码
     * @param msg    错误信息
     * @param remark 错误备注
     * @param data   响应数据
     * @return
     */
    private static <T> RestRSP<T> of(int code, String msg, String remark, T data) {
        return RestRSP.<T>builder().code(code)
                .msg(msg)
                .remark(remark)
                .data(data)
                .serverTime(System.currentTimeMillis())
                .requestId(RequestContextHolder.getContext().getRequestId())
                .build();
    }


    /**
     * 响应
     *
     * @param code   错误码
     * @param msg    错误信息
     * @param remark 错误备注
     * @return
     */
    private static RestRSP of(int code, String msg, String remark) {
        return of(code, msg, remark, null);
    }

}
