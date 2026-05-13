package com.gxa.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.gxa.handler.BlobTypeHandler;
import org.apache.ibatis.type.JdbcType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_app")
public class App {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "app_id", jdbcType = JdbcType.VARCHAR)
    private String appId;
    
    private Long userId;
    private String title;
    
    @TableField(value = "description", typeHandler = BlobTypeHandler.class)
    private String description;
    
    private String type;
    private String coverImage;
    
    @TableField(value = "config_json", typeHandler = BlobTypeHandler.class)
    private String configJson;
    
    private Integer status;
    private Integer viewCount;
    private Integer answerCount;
    private Integer shareCount;
    private Integer isAiGenerated;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
