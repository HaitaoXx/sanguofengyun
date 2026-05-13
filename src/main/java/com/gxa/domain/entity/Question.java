package com.gxa.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.gxa.handler.BlobTypeHandler;
import org.apache.ibatis.type.JdbcType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_question")
public class Question {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "app_id", jdbcType = JdbcType.VARCHAR)
    private String appId;
    
    @TableField(value = "question_text", typeHandler = BlobTypeHandler.class)
    private String questionText;
    
    private String questionType;
    
    @TableField(value = "options", typeHandler = BlobTypeHandler.class)
    private String options;
    
    private String correctAnswer;
    private Integer score;
    private Integer orderNum;
    
    @TableField(value = "config_json", typeHandler = BlobTypeHandler.class)
    private String configJson;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
