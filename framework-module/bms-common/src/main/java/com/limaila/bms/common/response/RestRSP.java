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


    public static RestRSP systemFailed(String remark) {
        return of(RestCode.SYSTEM_FAILED.getCode(), RestCode.SYSTEM_FAILED.getMsg(), remark, null);
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
    public static RestRSP of(int code, String msg, String remark, Object data) {
        return RestRSP.builder()
                .code(code)
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
    public static RestRSP of(int code, String msg, String remark) {
        return of(code, msg, remark, null);
    }

}
