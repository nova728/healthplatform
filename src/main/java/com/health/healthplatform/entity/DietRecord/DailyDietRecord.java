package com.health.healthplatform.entity.DietRecord;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("daily_diet_records")
public class DailyDietRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer userId;
    private LocalDate recordDate;
    private Double totalCalories; // 总热量
    private Double totalCarbs; // 总碳水
    private Double totalProtein; // 总蛋白质
    private Double totalFat; // 总脂肪
    private String notes; // 备注
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}