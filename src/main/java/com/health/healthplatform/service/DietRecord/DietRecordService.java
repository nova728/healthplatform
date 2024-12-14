package com.health.healthplatform.service.DietRecord;

import com.health.healthplatform.DTO.DietRecord.DailyDietDTO;
import com.health.healthplatform.DTO.DietRecord.MealRecordDTO;
import com.health.healthplatform.entity.DietRecord.DailyDietRecord;
import com.health.healthplatform.entity.DietRecord.MealRecord;
import com.health.healthplatform.mapper.DietRecord.DailyDietRecordMapper;
import com.health.healthplatform.mapper.DietRecord.MealRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DietRecordService {
    private final DailyDietRecordMapper dailyDietRecordMapper;
    private final MealRecordMapper mealRecordMapper;
    private final FoodSearchService foodSearchService;

    public DietRecordService(DailyDietRecordMapper dailyDietRecordMapper,
                            MealRecordMapper mealRecordMapper,
                            FoodSearchService foodSearchService) {
        this.dailyDietRecordMapper = dailyDietRecordMapper;
        this.mealRecordMapper = mealRecordMapper;
        this.foodSearchService = foodSearchService;
    }

    @Transactional
    public DailyDietDTO addMealRecord(Integer userId, LocalDate date, MealRecordDTO mealDTO) {
        // 获取或创建每日记录
        DailyDietRecord dailyRecord = getDailyRecord(userId, date);
        
        // 创建餐食记录
        MealRecord meal = new MealRecord();
        BeanUtils.copyProperties(mealDTO, meal);
        meal.setDailyDietId(dailyRecord.getId());
        mealRecordMapper.insert(meal);
        
        // 更新每日总计
        updateDailyTotals(dailyRecord.getId());
        
        return getDailyDietDTO(dailyRecord.getId());
    }
    
    @Transactional
    public DailyDietDTO deleteMealRecord(Integer userId, Long mealId) {
        // 获取餐食记录
        MealRecord mealRecord = mealRecordMapper.selectById(mealId);
        if (mealRecord == null) {
            throw new RuntimeException("餐食记录不存在");
        }
        
        // 获取对应的每日记录
        DailyDietRecord dailyRecord = dailyDietRecordMapper.selectById(mealRecord.getDailyDietId());
        if (dailyRecord == null || !dailyRecord.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此记录");
        }
        
        // 删除餐食记录
        mealRecordMapper.deleteById(mealId);
        
        // 更新每日总计
        updateDailyTotals(dailyRecord.getId());
        
        // 返回更新后的每日记录
        return getDailyDietDTO(dailyRecord.getId());
    }
    
    private DailyDietRecord getDailyRecord(Integer userId, LocalDate date) {
        DailyDietRecord record = dailyDietRecordMapper.findByUserIdAndDate(userId, date);
        if (record == null) {
            record = new DailyDietRecord();
            record.setUserId(userId);
            record.setRecordDate(date);
            dailyDietRecordMapper.insert(record);
        }
        return record;
    }
    
    private void updateDailyTotals(Long dailyDietId) {
        List<MealRecord> meals = mealRecordMapper.findByDailyDietId(dailyDietId);
        double totalCalories = meals.stream().mapToDouble(MealRecord::getCalories).sum();
        double totalCarbs = meals.stream().mapToDouble(MealRecord::getCarbs).sum();
        double totalProtein = meals.stream().mapToDouble(MealRecord::getProtein).sum();
        double totalFat = meals.stream().mapToDouble(MealRecord::getFat).sum();
        
        DailyDietRecord record = dailyDietRecordMapper.selectById(dailyDietId);
        record.setTotalCalories(totalCalories);
        record.setTotalCarbs(totalCarbs);
        record.setTotalProtein(totalProtein);
        record.setTotalFat(totalFat);
        dailyDietRecordMapper.updateById(record);
    }
    
    public DailyDietDTO getDailyDiet(Integer userId, LocalDate date) {
        DailyDietRecord record = getDailyRecord(userId, date);
        return getDailyDietDTO(record.getId());
    }
    
    private DailyDietDTO getDailyDietDTO(Long dailyDietId) {
        DailyDietRecord record = dailyDietRecordMapper.selectById(dailyDietId);
        List<MealRecord> meals = mealRecordMapper.findByDailyDietId(dailyDietId);
        
        DailyDietDTO dto = new DailyDietDTO();
        BeanUtils.copyProperties(record, dto);
        dto.setMeals(meals.stream()
            .map(meal -> {
                MealRecordDTO mealDTO = new MealRecordDTO();
                BeanUtils.copyProperties(meal, mealDTO);
                return mealDTO;
            })
            .collect(Collectors.toList()));
        return dto;
    }
}