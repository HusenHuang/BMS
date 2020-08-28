package com.limaila.bms.common.request;

import lombok.*;

import java.io.Serializable;

/***
 @author:MrHuang
 @date: 2020/2/27 2:47
 @desc:
 @version: 1.0
 ***/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class REQ<T> implements Serializable {

    /**
     * 请求数据
     */
    private T data;
}
