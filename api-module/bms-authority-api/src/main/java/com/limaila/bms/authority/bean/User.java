package com.limaila.bms.authority.bean;

import com.limaila.bms.common.bean.BaseObject;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Getter
@Setter
public class User extends BaseObject {

    private Long userId;

    private String userName;

    private int age;
}
