package com.limaila.bms.cache.extension.id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssignId extends PrimaryId {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
}
