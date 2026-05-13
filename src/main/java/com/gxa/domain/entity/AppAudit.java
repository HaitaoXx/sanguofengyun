package com.gxa.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_app_audit")
public class AppAudit {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "app_id", jdbcType = JdbcType.VARCHAR)
    private String appId;
    
    private Long auditorId;
    private Integer auditStatus;
    private String auditComment;
    private LocalDateTime auditTime;
}
