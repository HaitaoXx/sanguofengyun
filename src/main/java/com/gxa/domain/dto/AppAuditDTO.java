package com.gxa.domain.dto;

import lombok.Data;

@Data
public class AppAuditDTO {
    private String appId;
    private Integer auditStatus;
    private String auditComment;
}
