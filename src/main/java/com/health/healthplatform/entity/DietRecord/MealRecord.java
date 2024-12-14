package com.health.healthplatform.entity.DietRecord;

import com.baomidou.mybatisplus.annotation.*;
import com.health.healthplatform.enums.MealType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("meal_records")
public class MealRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dailyDietId; // 每日饮食记录ID
    @EnumValue
    private MealType mealType; // 餐次类型
    private String foodId; // 食物ID
    private String foodName; // 食物名称
    private Double servingAmount; // 份量
    private String servingUnit; // 份量单位
    private Double calories; // 热量
    private Double carbs; // 碳水
    private Double protein; // 蛋白质
    private Double fat; // 脂肪
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
