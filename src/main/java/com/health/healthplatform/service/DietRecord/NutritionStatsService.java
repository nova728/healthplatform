package com.health.healthplatform.service.DietRecord;

import com.health.healthplatform.DTO.DietRecord.NutritionStatsDTO;
import com.health.healthplatform.entity.DietRecord.NutritionSummary;
import com.health.healthplatform.mapper.DietRecord.NutritionSummaryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NutritionStatsService {
    private final NutritionSummaryMapper nutritionSummaryMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    public NutritionStatsService(NutritionSummaryMapper nutritionSummaryMapper) {
        this.nutritionSummaryMapper = nutritionSummaryMapper;
    }

    public NutritionStatsDTO getStats(Integer userId, String range) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = getStartDate(endDate, range);
        
        log.info("获取用户 {} 从 {} 到 {} 的营养统计数据", userId, startDate, endDate);
        
        // 获取日期范围内的所有营养记录
        List<NutritionSummary> summaries = nutritionSummaryMapper.findByUserIdAndDateRange(
            userId, startDate, endDate);
        
        // 将记录转换为按日期索引的Map
        Map<LocalDate, NutritionSummary> summaryMap = summaries.stream()
            .collect(Collectors.toMap(NutritionSummary::getRecordDate, s -> s));
        
        // 准备返回数据
        NutritionStatsDTO stats = new NutritionStatsDTO();
        List<String> dates = new ArrayList<>();
        List<Double> calories = new ArrayList<>();
        List<Double> carbs = new ArrayList<>();
        List<Double> protein = new ArrayList<>();
        List<Double> fat = new ArrayList<>();
        List<Double> recommendedCalories = new ArrayList<>();
        List<Double> recommendedCarbs = new ArrayList<>();
        List<Double> recommendedProtein = new ArrayList<>();
        List<Double> recommendedFat = new ArrayList<>();

        // 填充数据
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            NutritionSummary summary = summaryMap.get(date);
            dates.add(date.format(DATE_FORMATTER));
            
            if (summary != null) {
                calories.add(summary.getTotalCalories());
                carbs.add(summary.getTotalCarbs());
                protein.add(summary.getTotalProtein());
                fat.add(summary.getTotalFat());
                recommendedCalories.add(summary.getRecommendedCalories());
                recommendedCarbs.add(summary.getRecommendedCarbs());
                recommendedProtein.add(summary.getRecommendedProtein());
                recommendedFat.add(summary.getRecommendedFat());
            } else {
                // 如果某天没有记录，填充0和默认推荐值
                calories.add(0.0);
                carbs.add(0.0);
                protein.add(0.0);
                fat.add(0.0);
                recommendedCalories.add(2000.0);
                recommendedCarbs.add(250.0);
                recommendedProtein.add(60.0);
                recommendedFat.add(70.0);
            }
        }

        // 设置统计数据
        stats.setDates(dates);
        stats.setCalories(calories);
        stats.setCarbs(carbs);
        stats.setProtein(protein);
        stats.setFat(fat);
        stats.setRecommendedCalories(recommendedCalories);
        stats.setRecommendedCarbs(recommendedCarbs);
        stats.setRecommendedProtein(recommendedProtein);
        stats.setRecommendedFat(recommendedFat);

        return stats;
    }

    private LocalDate getStartDate(LocalDate endDate, String range) {
        return switch (range) {
            case "week" -> endDate.minusWeeks(1);
            case "month" -> endDate.minusMonths(1);
            case "halfYear" -> endDate.minusMonths(6);
            case "year" -> endDate.minusYears(1);
            default -> endDate.minusWeeks(1);
        };
    }
}