package com.limaila.bms.common.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/***
 说明: 
 @author MrHuang
 @date 2020/3/24 11:39
 @desc
 ***/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseDateTime implements Serializable {

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
