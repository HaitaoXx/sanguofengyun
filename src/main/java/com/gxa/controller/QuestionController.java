package com.gxa.controller;

import com.gxa.domain.dto.QuestionAddDTO;
import com.gxa.domain.dto.QuestionUpdateDTO;
import com.gxa.domain.entity.Question;
import com.gxa.domain.vo.Result;
import com.gxa.mapper.QuestionMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/question")
@CrossOrigin
public class QuestionController {
    
    @Autowired
    private QuestionMapper questionMapper;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @GetMapping("/app/{appId}")
    public Result<List<Question>> getQuestionsByAppId(@PathVariable String appId) {
        List<Question> questions = questionMapper.selectByAppId(appId);
        return Result.success(questions);
    }
    
    @GetMapping("/{id}")
    public Result<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            return Result.error("题目不存在");
        }
        return Result.success(question);
    }
    
    @PostMapping
    public Result<Question> addQuestion(@RequestBody QuestionAddDTO dto) {
        Question question = new Question();
        question.setAppId(dto.getAppId());
        question.setQuestionText(dto.getQuestionText());
        question.setQuestionType(dto.getQuestionType());
        
        try {
            question.setOptions(objectMapper.writeValueAsString(dto.getOptions()));
        } catch (JsonProcessingException e) {
            log.error("序列化选项失败", e);
            return Result.error("序列化选项失败");
        }
        
        question.setCorrectAnswer(dto.getCorrectAnswer());
        question.setScore(dto.getScore() != null ? dto.getScore() : 10);
        question.setOrderNum(dto.getOrderNum() != null ? dto.getOrderNum() : 0);
        
        questionMapper.insert(question);
        return Result.success(question);
    }
    
    @PutMapping("/{id}")
    public Result<Question> updateQuestion(@PathVariable Long id, @RequestBody QuestionUpdateDTO dto) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            return Result.error("题目不存在");
        }
        
        if (dto.getQuestionText() != null) {
            question.setQuestionText(dto.getQuestionText());
        }
        if (dto.getQuestionType() != null) {
            question.setQuestionType(dto.getQuestionType());
        }
        if (dto.getOptions() != null) {
            try {
                question.setOptions(objectMapper.writeValueAsString(dto.getOptions()));
            } catch (JsonProcessingException e) {
                log.error("序列化选项失败", e);
                return Result.error("序列化选项失败");
            }
        }
        if (dto.getCorrectAnswer() != null) {
            question.setCorrectAnswer(dto.getCorrectAnswer());
        }
        if (dto.getScore() != null) {
            question.setScore(dto.getScore());
        }
        if (dto.getOrderNum() != null) {
            question.setOrderNum(dto.getOrderNum());
        }
        
        questionMapper.updateById(question);
        return Result.success(question);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteQuestion(@PathVariable Long id) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            return Result.error("题目不存在");
        }
        
        questionMapper.deleteById(id);
        return Result.success();
    }
    
    @DeleteMapping("/app/{appId}")
    public Result<Void> deleteQuestionsByAppId(@PathVariable String appId) {
        questionMapper.deleteByAppId(appId);
        return Result.success();
    }
}