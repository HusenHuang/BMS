package com.limaila.bms.common.bean;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/***
 说明: 
 @author MrHuang
 @date 2020/9/27 14:45
 @desc
 ***/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Getter
@Setter
public class BaseObject implements Serializable {

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

}
