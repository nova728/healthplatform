package com.health.healthplatform.controller.DietRecord;

import com.health.healthplatform.DTO.DietRecord.NutritionStatsDTO;
import com.health.healthplatform.service.DietRecord.NutritionStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diet")
@Slf4j
public class NutritionStatsController {
    private final NutritionStatsService nutritionStatsService;

    public NutritionStatsController(NutritionStatsService nutritionStatsService) {
        this.nutritionStatsService = nutritionStatsService;
    }

    @GetMapping("/{userId}/nutrition/stats")
    public ResponseEntity<NutritionStatsDTO> getNutritionStats(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "week") String range) {
        log.info("获取用户 {} 的营养统计数据，时间范围：{}", userId, range);
        NutritionStatsDTO stats = nutritionStatsService.getStats(userId, range);
        return ResponseEntity.ok(stats);
    }
}