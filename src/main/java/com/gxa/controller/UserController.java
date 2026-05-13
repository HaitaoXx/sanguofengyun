package com.gxa.controller;

import com.gxa.domain.dto.UserLoginDTO;
import com.gxa.domain.dto.UserRegisterDTO;
import com.gxa.domain.dto.UserUpdateDTO;
import com.gxa.domain.vo.AchievementVO;
import com.gxa.domain.vo.Result;
import com.gxa.domain.vo.UserAnswerHistoryVO;
import com.gxa.domain.vo.UserStatisticsVO;
import com.gxa.domain.vo.UserVO;
import com.gxa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserLoginDTO dto) {
        UserVO userVO = userService.login(dto);
        return Result.success(userVO);
    }
    
    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody UserRegisterDTO dto) {
        UserVO userVO = userService.register(dto);
        return Result.success(userVO);
    }
    
    @GetMapping("/{userId}")
    public Result<UserVO> getUserById(@PathVariable Long userId) {
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }
    
    @PutMapping("/{userId}")
    public Result<UserVO> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDTO dto) {
        UserVO userVO = userService.updateUser(userId, dto);
        return Result.success(userVO);
    }
    
    @GetMapping("/{userId}/history")
    public Result<List<UserAnswerHistoryVO>> getUserAnswerHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        List<UserAnswerHistoryVO> history = userService.getUserAnswerHistory(userId, page, size);
        return Result.success(history);
    }
    
    @GetMapping("/{userId}/statistics")
    public Result<UserStatisticsVO> getUserStatistics(@PathVariable Long userId) {
        UserStatisticsVO statistics = userService.getUserStatistics(userId);
        return Result.success(statistics);
    }
    
    @GetMapping("/{userId}/achievements")
    public Result<List<AchievementVO>> getUserAchievements(@PathVariable Long userId) {
        List<AchievementVO> achievements = userService.getUserAchievements(userId);
        return Result.success(achievements);
    }
}
