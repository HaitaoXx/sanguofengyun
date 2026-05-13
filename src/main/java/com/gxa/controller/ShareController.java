package com.gxa.controller;

import com.gxa.domain.vo.Result;
import com.gxa.domain.vo.ShareVO;
import com.gxa.service.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/share")
@CrossOrigin
public class ShareController {
    
    @Autowired
    private ShareService shareService;
    
    @PostMapping("/create")
    public Result<ShareVO> createShare(@RequestParam String appId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        ShareVO shareVO = shareService.createShare(appId, userId);
        return Result.success(shareVO);
    }
    
    @GetMapping("/{shareCode}")
    public Result<String> getAppIdByShareCode(@PathVariable String shareCode) {
        String appId = shareService.getAppIdByShareCode(shareCode);
        return Result.success(appId);
    }
}
