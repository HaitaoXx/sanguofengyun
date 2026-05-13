package com.gxa.controller;

import com.gxa.domain.dto.SubmitAnswerDTO;
import com.gxa.domain.vo.AnswerResultVO;
import com.gxa.domain.vo.Result;
import com.gxa.service.AnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/answer")
@CrossOrigin
public class AnswerController {
    
    @Autowired
    private AnswerService answerService;
    
    @PostMapping("/submit")
    public Result<AnswerResultVO> submitAnswer(
            @RequestBody SubmitAnswerDTO dto,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String ipAddress = request.getHeader("X-Real-IP");
        AnswerResultVO resultVO = answerService.submitAnswer(dto, userId, ipAddress);
        return Result.success(resultVO);
    }
    
    @GetMapping("/result/{answerId}")
    public Result<AnswerResultVO> getAnswerResult(@PathVariable String answerId) {
        AnswerResultVO resultVO = answerService.getAnswerResult(answerId);
        return Result.success(resultVO);
    }
    
    @GetMapping("/history")
    public Result<List<AnswerResultVO>> getHistoryAnswers(
            @RequestParam String appId,
            @RequestParam(required = false) String keyword) {
        List<AnswerResultVO> resultVO = answerService.getHistoryAnswers(appId, keyword);
        return Result.success(resultVO);
    }
    
    @GetMapping("/detail")
    public Result<AnswerResultVO> getAnswerDetail(@RequestParam String answerId) {
        AnswerResultVO resultVO = answerService.getAnswerResult(answerId);
        return Result.success(resultVO);
    }
}
