package com.health.healthplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("weight_history")
public class WeightHistory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer userId;
    private Double weight;
    private Double height;
    private Double bmi;
    private LocalDateTime measurementTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}