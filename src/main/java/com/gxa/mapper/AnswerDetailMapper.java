package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.AnswerDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnswerDetailMapper extends BaseMapper<AnswerDetail> {
    
    @Select("SELECT * FROM t_answer_detail WHERE answer_id = #{answerId}")
    List<AnswerDetail> selectByAnswerId(@Param("answerId") String answerId);
}
