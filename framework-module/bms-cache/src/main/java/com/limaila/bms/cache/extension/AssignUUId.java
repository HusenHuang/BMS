package com.limaila.bms.cache.extension;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssignUUId extends PrimaryId {

    @TableId(type = IdType.ASSIGN_UUID)
    private Long id;
}
