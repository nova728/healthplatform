package com.health.healthplatform.DTO;

import lombok.Data;

@Data
public class WeeklyStats {
    private Double totalDuration; // 小时
    private Integer totalCalories;
    private Integer exerciseCount;
}