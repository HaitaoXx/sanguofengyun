package com.gxa.controller;

import com.gxa.domain.dto.StatisticsQueryDTO;
import com.gxa.domain.vo.*;
import com.gxa.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @PostMapping("/query")
    public Result<StatisticsVO> getStatistics(@RequestBody StatisticsQueryDTO dto) {
        StatisticsVO statisticsVO = statisticsService.getStatistics(dto);
        return Result.success(statisticsVO);
    }
    
    @GetMapping("/behavior/{appId}")
    public Result<BehaviorAnalysisVO> getBehaviorAnalysis(@PathVariable String appId) {
        BehaviorAnalysisVO behaviorVO = statisticsService.getBehaviorAnalysis(appId);
        return Result.success(behaviorVO);
    }
    
    @GetMapping("/difficulty/{appId}")
    public Result<DifficultyAnalysisVO> getDifficultyAnalysis(@PathVariable String appId) {
        DifficultyAnalysisVO difficultyVO = statisticsService.getDifficultyAnalysis(appId);
        return Result.success(difficultyVO);
    }
    
    @GetMapping("/knowledge/{appId}")
    public Result<KnowledgeAnalysisVO> getKnowledgeAnalysis(@PathVariable String appId) {
        KnowledgeAnalysisVO knowledgeVO = statisticsService.getKnowledgeAnalysis(appId);
        return Result.success(knowledgeVO);
    }
}
