package com.health.healthplatform.mapper.DietRecord;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.healthplatform.entity.DietRecord.DailyDietRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface DailyDietRecordMapper extends BaseMapper<DailyDietRecord> {
    
    @Select("SELECT * FROM daily_diet_records WHERE user_id = #{userId} AND record_date = #{date}")
    DailyDietRecord findByUserIdAndDate(@Param("userId") Integer userId, @Param("date") LocalDate date);
    
    @Select("SELECT * FROM daily_diet_records WHERE user_id = #{userId} ORDER BY record_date DESC LIMIT 1")
    DailyDietRecord findLatestByUserId(@Param("userId") Integer userId);
}