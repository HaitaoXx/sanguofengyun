package com.gxa.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_app_share")
public class AppShare {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "app_id", jdbcType = JdbcType.VARCHAR)
    private String appId;
    
    @TableField(value = "share_code", jdbcType = JdbcType.VARCHAR)
    private String shareCode;
    
    private String shareUrl;
    private Long userId;
    private Integer shareCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
