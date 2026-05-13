package com.gxa.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_user_answer")
public class UserAnswer {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "app_id", jdbcType = JdbcType.VARCHAR)
    private String appId;
    
    private Long userId;
    
    @TableField(value = "answer_id", jdbcType = JdbcType.VARCHAR)
    private String answerId;
    
    private Integer score;
    private String resultJson;
    private String aiComment;
    private Integer duration;
    private String ipAddress;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
