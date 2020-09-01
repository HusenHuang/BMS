package com.limaila.bms.common.bean;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/***
 说明: 
 @author MrHuang
 @date 2020/3/24 11:39
 @desc
 ***/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Getter
@Setter
public class BaseDateTime implements Serializable {

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;
}
