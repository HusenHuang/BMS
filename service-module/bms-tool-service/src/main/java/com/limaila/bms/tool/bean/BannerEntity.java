package com.limaila.bms.tool.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.limaila.bms.cache.extension.id.AssignId;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("bms_banner")
public class BannerEntity extends AssignId {

    @TableField
    private String name;

    @TableField
    private String url;

    @TableField
    private Integer location;
}
