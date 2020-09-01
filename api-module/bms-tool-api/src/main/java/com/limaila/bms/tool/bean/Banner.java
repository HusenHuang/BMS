package com.limaila.bms.tool.bean;

import com.limaila.bms.common.bean.BaseDateTime;
import lombok.*;
import lombok.experimental.SuperBuilder;

/***
 说明: 
 @author MrHuang
 @date 2020/9/1 19:17
 @desc
 ***/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Getter
@Setter
public class Banner extends BaseDateTime {

    private Long id;

    private String name;

    private String url;

    private int location;
}
