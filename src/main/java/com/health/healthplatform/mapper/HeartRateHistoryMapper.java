package com.health.healthplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.healthplatform.entity.HeartRateHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface HeartRateHistoryMapper extends BaseMapper<HeartRateHistory> {
    @Select("SELECT * FROM heart_rate_history WHERE user_id = #{userId} " +
            "AND measurement_time BETWEEN #{startTime} AND #{endTime} " +
            "AND deleted = 0 ORDER BY measurement_time ASC")
    List<HeartRateHistory> findByTimeRange(
            @Param("userId") Integer userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
