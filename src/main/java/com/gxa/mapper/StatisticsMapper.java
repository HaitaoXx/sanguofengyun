package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.Statistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatisticsMapper extends BaseMapper<Statistics> {
    
    @Select("SELECT * FROM t_statistics WHERE app_id = #{appId} AND stat_date >= #{startDate} AND stat_date <= #{endDate} ORDER BY stat_date ASC")
    List<Statistics> selectByDateRange(@Param("appId") String appId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
