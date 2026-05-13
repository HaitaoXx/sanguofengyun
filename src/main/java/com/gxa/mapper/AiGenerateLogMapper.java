package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.AiGenerateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AiGenerateLogMapper extends BaseMapper<AiGenerateLog> {
    
    @Select("SELECT * FROM t_ai_generate_log WHERE app_id = #{appId} ORDER BY create_time DESC LIMIT #{limit}")
    List<AiGenerateLog> selectByAppId(@Param("appId") String appId, @Param("limit") Integer limit);
}
