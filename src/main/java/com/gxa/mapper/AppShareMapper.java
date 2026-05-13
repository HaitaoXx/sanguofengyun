package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.AppShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AppShareMapper extends BaseMapper<AppShare> {
    
    @Select("SELECT * FROM t_app_share WHERE share_code = #{shareCode}")
    AppShare selectByShareCode(@Param("shareCode") String shareCode);
}
