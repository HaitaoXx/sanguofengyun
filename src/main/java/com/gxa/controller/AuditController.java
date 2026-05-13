package com.gxa.controller;

import com.gxa.domain.dto.AppAuditDTO;
import com.gxa.domain.entity.AppAudit;
import com.gxa.domain.vo.AppVO;
import com.gxa.domain.vo.Result;
import com.gxa.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/audit")
@CrossOrigin
public class AuditController {
    
    @Autowired
    private AuditService auditService;
    
    @PostMapping("/app")
    public Result<Void> auditApp(@RequestBody AppAuditDTO dto, HttpServletRequest request) {
        Long auditorId = (Long) request.getAttribute("userId");
        auditService.auditApp(dto, auditorId);
        return Result.success("审核完成", null);
    }
    
    @GetMapping("/pending")
    public Result<List<AppVO>> getPendingApps() {
        List<AppVO> apps = auditService.getPendingApps();
        return Result.success(apps);
    }
    
    @GetMapping("/approved")
    public Result<List<AppVO>> getApprovedApps() {
        List<AppVO> apps = auditService.getAuditedApps(1);
        return Result.success(apps);
    }
    
    @GetMapping("/rejected")
    public Result<List<AppVO>> getRejectedApps() {
        List<AppVO> apps = auditService.getAuditedApps(2);
        return Result.success(apps);
    }
    
    @GetMapping("/history/{appId}")
    public Result<List<AppAudit>> getAuditHistory(@PathVariable String appId) {
        List<AppAudit> history = auditService.getAuditHistory(appId);
        return Result.success(history);
    }
}
