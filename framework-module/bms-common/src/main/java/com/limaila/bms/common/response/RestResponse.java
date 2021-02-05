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
public class RestResponse<T> implements Serializable {


    /**
     * {@link RestCode}
     * 请求失败返回的错误码
     */
    private Integer code;

    /**
     * 请求失败返回的错误信息
     */
    private String errorType;

    /**
     * 请求失败返回的错误备注
     */
    private String msg;

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
     * @return 响应体
     */
    public static RestResponse<?> success() {
        return of(RestCode.SUCCESS.getTypeCode(), RestCode.SUCCESS.getTypeMsg(), null, null);
    }

    /**
     * 响应成功
     *
     * @param data 响应数据
     * @return 响应体
     */
    public static <T> RestResponse<T> success(T data) {
        return of(RestCode.SUCCESS.getTypeCode(), RestCode.SUCCESS.getTypeMsg(), null, data);
    }


    public static RestResponse<?> failed(RestCode restCode, String msg) {
        return of(restCode.getTypeCode(), restCode.getTypeMsg(), msg, null);
    }

    /**
     * 响应
     *
     * @param code      错误码
     * @param errorType 错误类型
     * @param msg       错误信息
     * @return
     */
    public static RestResponse<?> of(int code, String errorType, String msg) {
        return of(code, errorType, msg, null);
    }

    /**
     * 响应
     *
     * @param code      错误码
     * @param msg       错误信息
     * @param errorType 错误类型
     * @param data      响应数据
     * @return
     */
    public static <T> RestResponse<T> of(int code, String errorType, String msg, T data) {
        return RestResponse.<T>builder()
                .code(code)
                .errorType(errorType)
                .msg(msg)
                .data(data)
                .serverTime(System.currentTimeMillis())
                .requestId(RequestContextHolder.getContext().getRequestId())
                .build();
    }

}
