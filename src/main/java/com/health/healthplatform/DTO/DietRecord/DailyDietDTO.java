package com.health.healthplatform.DTO.DietRecord;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import com.health.healthplatform.DTO.DietRecord.MealRecordDTO;


@Data
public class DailyDietDTO {
    private Long id;
    private LocalDate recordDate; // 记录日期
    private Double totalCalories; // 总热量
    private Double totalCarbs; // 总碳水
    private Double totalProtein; // 总蛋白质
    private Double totalFat; // 总脂肪
    private String notes; // 备注
    private List<MealRecordDTO> meals; // 餐次记录
}

