package com.gxa.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_ai_generate_log")
public class AiGenerateLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "app_id", jdbcType = JdbcType.VARCHAR)
    private String appId;
    
    private Long userId;
    private String generateType;
    private String prompt;
    private String result;
    private Integer tokensUsed;
    private Integer duration;
    private Integer status;
    private String errorMsg;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
