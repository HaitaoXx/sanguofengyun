package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.UserAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {
    
    @Select("SELECT * FROM t_user_answer WHERE answer_id = #{answerId}")
    UserAnswer selectByAnswerId(@Param("answerId") String answerId);
    
    @Select("SELECT * FROM t_user_answer WHERE app_id = #{appId} ORDER BY create_time DESC LIMIT #{limit}")
    List<UserAnswer> selectRecentAnswers(@Param("appId") String appId, @Param("limit") Integer limit);
    
    @Update("UPDATE t_user_answer SET ai_comment = #{comment} WHERE answer_id = #{answerId}")
    void updateAiCommentByAnswerId(@Param("answerId") String answerId, @Param("comment") String comment);
}
