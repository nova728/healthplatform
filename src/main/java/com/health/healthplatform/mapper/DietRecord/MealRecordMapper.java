package com.health.healthplatform.mapper.DietRecord;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.healthplatform.entity.DietRecord.MealRecord;
import com.health.healthplatform.enums.MealType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MealRecordMapper extends BaseMapper<MealRecord> {
    
    @Select("SELECT * FROM meal_records WHERE daily_diet_id = #{dailyDietId}")
    List<MealRecord> findByDailyDietId(@Param("dailyDietId") Long dailyDietId);
    
    @Select("SELECT * FROM meal_records WHERE daily_diet_id = #{dailyDietId} AND meal_type = #{mealType}")
    List<MealRecord> findByDailyDietIdAndType(
        @Param("dailyDietId") Long dailyDietId, 
        @Param("mealType") MealType mealType
    );
}