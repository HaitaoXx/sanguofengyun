package com.gxa.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_permission")
public class Permission {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String permissionName;
    private String permissionCode;
    private String module;
    private String url;
    private String method;
    private Integer parentId;
    private Integer sortOrder;
    private Integer status;
}