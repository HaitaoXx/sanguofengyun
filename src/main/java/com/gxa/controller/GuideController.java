package com.gxa.controller;

import com.gxa.domain.vo.Result;
import com.gxa.service.GuideService;
import com.gxa.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/guide")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/steps")
    public Result<Map<String, Object>> getGuideSteps(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Long userId = jwtUtil.getUserId(token.replace("Bearer ", ""));
        Map<String, Object> guide = guideService.getGuideSteps(userId);
        return Result.success(guide);
    }

    @GetMapping("/tips")
    public Result<Map<String, Object>> getOperationTips(@RequestParam(defaultValue = "home") String page) {
        Map<String, Object> tips = guideService.getOperationTips(page);
        return Result.success(tips);
    }
}