package com.gxa.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.gxa.handler.BlobTypeHandler;
import org.apache.ibatis.type.JdbcType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_answer_detail")
public class AnswerDetail {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "app_id", jdbcType = JdbcType.VARCHAR)
    private String appId;
    
    @TableField(value = "answer_id", jdbcType = JdbcType.VARCHAR)
    private String answerId;
    
    private Long questionId;
    
    @TableField(value = "user_answer", typeHandler = BlobTypeHandler.class)
    private String userAnswer;
    
    @TableField(value = "correct_answer", typeHandler = BlobTypeHandler.class)
    private String correctAnswer;
    
    private Integer score;
    private Integer isCorrect;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
