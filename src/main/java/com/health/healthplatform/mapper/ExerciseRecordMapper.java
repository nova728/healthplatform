package com.health.healthplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.healthplatform.entity.ExerciseRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ExerciseRecordMapper extends BaseMapper<ExerciseRecord> {
    @Select("SELECT * FROM exercise_records WHERE user_id = #{userId} " +
            "AND record_date BETWEEN #{startDate} AND #{endDate} " +
            "ORDER BY record_date DESC")
    List<ExerciseRecord> findByDateRange(
            @Param("userId") Integer userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Select("SELECT * FROM exercise_records WHERE user_id = #{userId} " +
            "ORDER BY record_date DESC")
    List<ExerciseRecord> findAllByUserId(@Param("userId") Integer userId);

    @Select("SELECT SUM(duration) FROM exercise_records WHERE user_id = #{userId} " +
            "AND record_date BETWEEN #{startDate} AND #{endDate}")
    Integer getTotalDuration(
            @Param("userId") Integer userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Select("SELECT SUM(calories) FROM exercise_records WHERE user_id = #{userId} " +
            "AND record_date BETWEEN #{startDate} AND #{endDate}")
    Integer getTotalCalories(
            @Param("userId") Integer userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Select("SELECT COUNT(*) FROM exercise_records WHERE user_id = #{userId} " +
            "AND record_date BETWEEN #{startDate} AND #{endDate} ")
    Integer getExerciseCount(
            @Param("userId") Integer userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}