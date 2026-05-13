package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.AppAudit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AppAuditMapper extends BaseMapper<AppAudit> {
    
    @Select("SELECT * FROM t_app_audit WHERE app_id = #{appId} ORDER BY audit_time DESC LIMIT 1")
    AppAudit selectLatestByAppId(@Param("appId") String appId);
    
    @Select("SELECT * FROM t_app_audit WHERE audit_status = #{auditStatus} ORDER BY audit_time DESC")
    List<AppAudit> selectByStatus(@Param("auditStatus") Integer auditStatus);
}
