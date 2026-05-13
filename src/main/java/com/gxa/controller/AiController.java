package com.gxa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxa.domain.dto.AiGenerateQuestionDTO;
import com.gxa.domain.entity.AnswerDetail;
import com.gxa.domain.entity.App;
import com.gxa.domain.entity.Question;
import com.gxa.domain.vo.AnswerDetailVO;
import com.gxa.domain.vo.QuestionVO;
import com.gxa.domain.vo.Result;
import com.gxa.mapper.AnswerDetailMapper;
import com.gxa.mapper.AppMapper;
import com.gxa.mapper.QuestionMapper;
import com.gxa.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gxa.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AiController {
    
    private static final Logger log = LoggerFactory.getLogger(AiController.class);
    
    @Autowired
    private AiService aiService;
    
    @Autowired
    private AnswerDetailMapper answerDetailMapper;
    
    @Autowired
    private AppMapper appMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
    @GetMapping(value = "/generate/questions/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateQuestionsStream(@RequestParam String appId,
                                               @RequestParam String topic,
                                               @RequestParam(defaultValue = "10") Integer questionCount,
                                               @RequestParam(defaultValue = "single") String questionType,
                                               @RequestParam(defaultValue = "medium") String difficulty,
                                               @RequestParam(required = false) String token) {
        
        SseEmitter emitter = new SseEmitter(300000L);
        
        App app = appMapper.selectByAppId(appId);
        if (app == null) {
            try {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("type", "error");
                errorData.put("message", "应用不存在");
                emitter.send(SseEmitter.event().data(toJson(errorData)));
            } catch (IOException e) {
                log.error("发送错误消息失败", e);
            }
            emitter.completeWithError(new RuntimeException("应用不存在"));
            return emitter;
        }
        
        executorService.execute(() -> {
            try {
                Map<String, Object> startData = new HashMap<>();
                startData.put("type", "start");
                startData.put("message", "开始生成题目...");
                emitter.send(SseEmitter.event().data(toJson(startData)));
                
                CompletableFuture<List<QuestionVO>> future = aiService.generateQuestionsAsync(
                    appId, topic, questionCount, questionType, difficulty);
                
                List<QuestionVO> questions = future.get();
                
                for (int i = 0; i < questions.size(); i++) {
                    QuestionVO question = questions.get(i);
                    String json = String.format(
                        "{\"type\":\"question\",\"index\":%d,\"total\":%d,\"data\":%s}",
                        i + 1, questions.size(), toJson(question)
                    );
                    emitter.send(SseEmitter.event().data(json));
                    Thread.sleep(500);
                }
                
                Map<String, Object> completeData = new HashMap<>();
                completeData.put("type", "complete");
                completeData.put("message", "题目生成完成！");
                emitter.send(SseEmitter.event().data(toJson(completeData)));
                emitter.complete();
                
            } catch (Exception e) {
                log.error("SSE流式输出失败", e);
                try {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("type", "error");
                    errorData.put("message", e.getMessage());
                    emitter.send(SseEmitter.event().data(toJson(errorData)));
                } catch (IOException ioException) {
                    log.error("发送错误消息失败", ioException);
                }
                emitter.completeWithError(e);
            }
        });
        
        return emitter;
    }
    
    @GetMapping(value = "/generate/comment/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateCommentStream(@RequestParam String appId,
                                           @RequestParam String answerId) {
        
        SseEmitter emitter = new SseEmitter(60000L);
        
        executorService.execute(() -> {
            try {
                Map<String, Object> startData = new HashMap<>();
                startData.put("type", "start");
                startData.put("message", "正在分析您的答案...");
                emitter.send(SseEmitter.event().data(toJson(startData)));
                
                List<AnswerDetail> details = answerDetailMapper.selectList(
                    new LambdaQueryWrapper<AnswerDetail>().eq(AnswerDetail::getAnswerId, answerId)
                );
                
                List<AnswerDetailVO> detailVOs = details.stream().map(detail -> {
                    AnswerDetailVO vo = new AnswerDetailVO();
                    BeanUtils.copyProperties(detail, vo);
                    
                    Question question = questionMapper.selectById(detail.getQuestionId());
                    if (question != null) {
                        vo.setOptions(question.getOptions());
                    }
                    
                    return vo;
                }).collect(Collectors.toList());
                
                CompletableFuture<String> future = aiService.generateAiCommentAsync(appId, answerId, detailVOs);
                String comment = future.get();
                
                String[] sentences = comment.split("(?<=[。！？.!?])");
                StringBuilder sb = new StringBuilder();
                
                for (String sentence : sentences) {
                    if (sentence.trim().isEmpty()) continue;
                    sb.append(sentence);
                    Map<String, Object> typingData = new HashMap<>();
                    typingData.put("type", "typing");
                    typingData.put("content", sb.toString());
                    emitter.send(SseEmitter.event().data(toJson(typingData)));
                    Thread.sleep(300);
                }
                
                Map<String, Object> completeData = new HashMap<>();
                completeData.put("type", "complete");
                completeData.put("message", "分析完成！");
                emitter.send(SseEmitter.event().data(toJson(completeData)));
                emitter.complete();
                
            } catch (Exception e) {
                log.error("SSE流式输出失败", e);
                try {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("type", "error");
                    errorData.put("message", e.getMessage());
                    emitter.send(SseEmitter.event().data(toJson(errorData)));
                } catch (IOException ioException) {
                    log.error("发送错误消息失败", ioException);
                }
                emitter.completeWithError(e);
            }
        });
        
        return emitter;
    }
    
    @PostMapping("/clear/questions")
    public Result<Void> clearQuestions(@RequestParam String appId) {
        try {
            aiService.clearQuestions(appId);
            return Result.success("清除题目成功", null);
        } catch (Exception e) {
            log.error("清除题目失败", e);
            return Result.error("清除题目失败: " + e.getMessage());
        }
    }
    
    private String toJson(Object obj) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }
    
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
