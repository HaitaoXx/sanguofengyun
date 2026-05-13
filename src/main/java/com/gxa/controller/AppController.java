package com.gxa.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxa.domain.dto.AppCreateDTO;
import com.gxa.domain.dto.AppUpdateDTO;
import com.gxa.domain.vo.AppVO;
import com.gxa.domain.vo.Result;
import com.gxa.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/app")
@CrossOrigin
public class AppController {
    
    @Autowired
    private AppService appService;
    
    @PostMapping("/create")
    public Result<AppVO> createApp(@RequestBody AppCreateDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        AppVO appVO = appService.createApp(dto, userId);
        return Result.success(appVO);
    }
    
    @PostMapping("/update")
    public Result<AppVO> updateApp(@RequestBody AppUpdateDTO dto) {
        AppVO appVO = appService.updateApp(dto);
        return Result.success(appVO);
    }
    
    @GetMapping("/{appId}")
    public Result<AppVO> getAppByAppId(@PathVariable String appId) {
        appService.incrementViewCount(appId);
        AppVO appVO = appService.getAppByAppId(appId);
        if (appVO.getStatus() != 1) {
            throw new RuntimeException("应用未通过审核，无法使用");
        }
        return Result.success(appVO);
    }
    
    @GetMapping("/published")
    public Result<IPage<AppVO>> getPublishedApps(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        IPage<AppVO> page = appService.getPublishedApps(pageNum, pageSize, keyword);
        return Result.success(page);
    }
    
    @GetMapping("/user/{userId}")
    public Result<IPage<AppVO>> getUserApps(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<AppVO> page = appService.getUserApps(userId, pageNum, pageSize);
        return Result.success(page);
    }
    
    @DeleteMapping("/{appId}")
    public Result<Void> deleteApp(@PathVariable String appId) {
        appService.deleteApp(appId);
        return Result.success("删除成功", null);
    }
}
