package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.Question;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
    
    @Select("SELECT * FROM t_question WHERE app_id = #{appId} ORDER BY order_num ASC")
    List<Question> selectByAppId(@Param("appId") String appId);
    
    @Delete("DELETE FROM t_question WHERE app_id = #{appId}")
    void deleteByAppId(@Param("appId") String appId);
}
