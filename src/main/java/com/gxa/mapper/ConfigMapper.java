package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ConfigMapper extends BaseMapper<Config> {
    
    @Select("SELECT * FROM t_config WHERE config_key = #{configKey}")
    Config selectByKey(@Param("configKey") String configKey);
}
