package com.gxa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxa.config.AiConfig;
import com.gxa.domain.entity.AiGenerateLog;
import com.gxa.domain.entity.App;
import com.gxa.domain.entity.Question;
import com.gxa.domain.entity.UserAnswer;
import com.gxa.domain.vo.AnswerDetailVO;
import com.gxa.domain.vo.QuestionVO;
import com.gxa.mapper.AiGenerateLogMapper;
import com.gxa.mapper.AppMapper;
import com.gxa.mapper.QuestionMapper;
import com.gxa.mapper.UserAnswerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class AiService {
    
    private static final Logger log = LoggerFactory.getLogger(AiService.class);
    
    @Autowired
    private AiConfig aiConfig;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private AiGenerateLogMapper aiGenerateLogMapper;
    
    @Autowired
    private AppMapper appMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired
    private UserAnswerMapper userAnswerMapper;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String APP_CACHE_PREFIX = "app:";
    
    private boolean isRedisAvailable() {
        return redisTemplate != null;
    }
    
    @Async("aiExecutor")
    public CompletableFuture<List<QuestionVO>> generateQuestionsAsync(String appId, String topic, Integer questionCount, String questionType, String difficulty) {
        try {
            String prompt = buildQuestionPrompt(topic, questionCount, questionType, difficulty);
            String result = callAiApi(prompt);
            
            List<QuestionVO> questions = parseQuestionsFromResult(result);
            
            // 获取当前最大的orderNum，确保新生成的题目顺序与之前的一致
            List<Question> existingQuestions = questionMapper.selectByAppId(appId);
            int maxOrderNum = existingQuestions.stream()
                .mapToInt(Question::getOrderNum)
                .max()
                .orElse(0);
            
            // 更新新生成题目的orderNum，确保顺序连续
            for (int i = 0; i < questions.size(); i++) {
                questions.get(i).setOrderNum(maxOrderNum + i + 1);
            }
            
            saveQuestions(appId, questions);
            saveAiLog(appId, "question", prompt, result, 0);
            
            // 清除AppVO缓存，确保下次获取时重新加载所有题目
            clearAppCache(appId);
            
            return CompletableFuture.completedFuture(questions);
        } catch (Exception e) {
            log.error("AI生成题目失败", e);
            saveAiLog(appId, "question", "", "", 1, e.getMessage());
            CompletableFuture<List<QuestionVO>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    private void clearAppCache(String appId) {
        if (isRedisAvailable()) {
            String cacheKey = APP_CACHE_PREFIX + appId;
            redisTemplate.delete(cacheKey);
        }
    }
    
    public void clearQuestions(String appId) {
        questionMapper.deleteByAppId(appId);
        clearAppCache(appId);
    }
    
    @Async("aiExecutor")
    public CompletableFuture<String> generateAiCommentAsync(String appId, String answerId, List<AnswerDetailVO> details) {
        try {
            String prompt = buildCommentPrompt(details);
            String result = callAiApi(prompt);
            
            updateAiComment(answerId, result);
            saveAiLog(appId, "comment", prompt, result, 0);
            
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            log.error("AI生成评语失败", e);
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    private String callAiApi(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", aiConfig.getModel());
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);
            requestBody.put("messages", messages);
            
            requestBody.put("stream", false);
            requestBody.put("temperature", 0.7);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(aiConfig.getApiKey());
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                aiConfig.getApiUrl(),
                HttpMethod.POST,
                entity,
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("choices").get(0).path("message").path("content").asText();
            }
            
            throw new RuntimeException("AI API调用失败");
        } catch (Exception e) {
            log.error("调用AI API失败", e);
            throw new RuntimeException("AI API调用失败: " + e.getMessage());
        }
    }
    
    private String buildQuestionPrompt(String topic, Integer questionCount, String questionType, String difficulty) {
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("single", "单选");
        typeMap.put("multiple", "多选");
        typeMap.put("judgment", "判断");
        typeMap.put("text", "简答");
        
        String typeText = typeMap.getOrDefault(questionType, "单选");
        
        String prompt;
        if ("text".equals(questionType)) {
            prompt = String.format(
                "请为主题\"%s\"生成%d道%s类型的题目，难度为%s。\n" +
                "要求：\n" +
                "1. 题目内容清晰易懂\n" +
                "2. 简答题不需要选项\n" +
                "3. 简答题需要提供参考答案（correctAnswer）\n" +
                "4. 请以JSON格式返回，格式如下：\n" +
                "[\n" +
                "  {\n" +
                "    \"questionText\": \"题目内容\",\n" +
                "    \"questionType\": \"%s\",\n" +
                "    \"options\": [],\n" +
                "    \"correctAnswer\": \"参考答案\",\n" +
                "    \"score\": 10,\n" +
                "    \"orderNum\": 1\n" +
                "  }\n" +
                "]",
                topic, questionCount, typeText, difficulty, questionType
            );
        } else if ("judgment".equals(questionType)) {
            prompt = String.format(
                "请为主题\"%s\"生成%d道%s类型的题目，难度为%s。\n" +
                "要求：\n" +
                "1. 题目内容清晰易懂，是一个可以用对或错来回答的陈述\n" +
                "2. 选项固定为[A. 对, B. 错]\n" +
                "3. 必须指定正确答案（correctAnswer），只能是A或B\n" +
                "4. 请以JSON格式返回，格式如下：\n" +
                "[\n" +
                "  {\n" +
                "    \"questionText\": \"题目内容\",\n" +
                "    \"questionType\": \"%s\",\n" +
                "    \"options\": [\"A. 对\", \"B. 错\"],\n" +
                "    \"correctAnswer\": \"A\",\n" +
                "    \"score\": 10,\n" +
                "    \"orderNum\": 1\n" +
                "  }\n" +
                "]",
                topic, questionCount, typeText, difficulty, questionType
            );
        } else {
            prompt = String.format(
                "请为主题\"%s\"生成%d道%s类型的题目，难度为%s。\n" +
                "要求：\n" +
                "1. 题目内容清晰易懂\n" +
                "2. 选项设计合理\n" +
                "3. 必须指定正确答案（correctAnswer），对于单选题是选项字母（如A、B、C、D），对于多选题是选项字母组合（如AB、ACD）\n" +
                "4. 请以JSON格式返回，格式如下：\n" +
                "[\n" +
                "  {\n" +
                "    \"questionText\": \"题目内容\",\n" +
                "    \"questionType\": \"%s\",\n" +
                "    \"options\": [\"选项A\", \"选项B\", \"选项C\", \"选项D\"],\n" +
                "    \"correctAnswer\": \"A\",\n" +
                "    \"score\": 10,\n" +
                "    \"orderNum\": 1\n" +
                "  }\n" +
                "]",
                topic, questionCount, typeText, difficulty, questionType
            );
        }
        
        return prompt;
    }
    
    private String buildCommentPrompt(List<AnswerDetailVO> details) {
        int correctCount = 0;
        int totalScore = 0;
        int maxScore = 0;
        
        for (AnswerDetailVO detail : details) {
            if (detail.getScore() > 0) {
                correctCount++;
            }
            totalScore += detail.getScore();
            maxScore += 10;
        }
        
        double accuracy = (double) correctCount / details.size() * 100;
        double scoreRate = (double) totalScore / maxScore * 100;
        
        StringBuilder sb = new StringBuilder();
        sb.append("作为专业教师，请根据以下答题情况生成专业的学习评价报告：\n\n");
        sb.append(String.format("答题统计：共%d题，正确%d题，正确率%.1f%%\n\n", 
            details.size(), correctCount, accuracy));
        
        sb.append("答题详情：\n");
        for (int i = 0; i < details.size(); i++) {
            AnswerDetailVO detail = details.get(i);
            sb.append(String.format("%d. 题目：%s\n", i + 1, detail.getQuestionText()));
            
            if (detail.getOptions() != null && !detail.getOptions().isEmpty()) {
                try {
                    String[] options = objectMapper.readValue(detail.getOptions(), String[].class);
                    sb.append("   选项：\n");
                    for (int j = 0; j < options.length; j++) {
                        sb.append(String.format("   %c. %s\n", (char) ('A' + j), options[j]));
                    }
                } catch (Exception e) {
                    log.error("解析选项失败", e);
                }
            }
            
            String userAnswerText = getAnswerText(detail.getUserAnswer(), detail.getOptions());
            String correctAnswerText = getAnswerText(detail.getCorrectAnswer(), detail.getOptions());
            
            sb.append(String.format("   用户答案：%s\n", userAnswerText));
            sb.append(String.format("   正确答案：%s\n", correctAnswerText));
            sb.append(String.format("   得分：%d\n\n", detail.getScore()));
        }
        
        sb.append("要求：\n");
        sb.append("1. 分析错题涉及的具体知识点（如：秋天的季节特征、自然生态循环规律、历史事件时间线等）\n");
        sb.append("2. 指出用户在哪些具体知识领域存在不足（如：对秋季气候变化的科学原理了解不够、对植物生长周期掌握不扎实等）\n");
        sb.append("3. 结合题目内容和选项分析错误原因，直接说明知识点缺失（如：不清楚秋天落叶的生物学原因、混淆了监督学习和无监督学习的区别等）\n");
        sb.append("4. 给出针对性学习建议，明确指出应该学习哪些具体内容（如：学习秋季气温下降的气象学原理、阅读关于机器学习算法分类的资料等）\n");
        sb.append("5. 字数控制在200-400字\n");
        sb.append("6. 禁止使用以下套话：\n");
        sb.append("   - 不要分析选项分布（如'误选C或B，正确答案为A'）\n");
        sb.append("   - 不要说'答题策略失误'、'审题能力'、'概念理解偏差'等抽象词汇\n");
        sb.append("   - 不要说'错误并非集中于单一选项'、'并非源于单一的答题策略失误'等\n");
        sb.append("   - 不要分析答题行为，只分析知识点掌握情况\n");
        sb.append("7. 评价要具体、直接，只说知识点问题，不说答题技巧问题\n");
        
        return sb.toString();
    }
    
    private String getAnswerText(String answer, String optionsJson) {
        if (answer == null || answer.isEmpty()) {
            return "未作答";
        }
        
        if (optionsJson == null || optionsJson.isEmpty()) {
            return answer;
        }
        
        try {
            String[] options = objectMapper.readValue(optionsJson, String[].class);
            StringBuilder sb = new StringBuilder();
            for (char c : answer.toCharArray()) {
                int index = c - 'A';
                if (index >= 0 && index < options.length) {
                    if (sb.length() > 0) {
                        sb.append("、");
                    }
                    sb.append(c).append(".").append(options[index]);
                } else {
                    if (sb.length() > 0) {
                        sb.append("、");
                    }
                    sb.append(c);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("解析答案文本失败", e);
            return answer;
        }
    }
    
    private List<QuestionVO> parseQuestionsFromResult(String result) {
        try {
            String cleanedResult = result;
            cleanedResult = cleanedResult.replace("```json", "");
            cleanedResult = cleanedResult.replace("```", "");
            cleanedResult = cleanedResult.trim();
            
            JsonNode root = objectMapper.readTree(cleanedResult);
            List<QuestionVO> questions = new ArrayList<>();
            
            if (root.isArray()) {
                for (JsonNode node : root) {
                    QuestionVO question = new QuestionVO();
                    question.setQuestionText(node.has("questionText") ? node.get("questionText").asText() : "");
                    question.setQuestionType(node.has("questionType") ? node.get("questionType").asText() : "single");
                    question.setOptions(node.has("options") ? objectMapper.writeValueAsString(node.get("options")) : "[]");
                    question.setCorrectAnswer(node.has("correctAnswer") ? node.get("correctAnswer").asText() : "");
                    question.setScore(node.has("score") ? node.get("score").asInt() : 10);
                    question.setOrderNum(node.has("orderNum") ? node.get("orderNum").asInt() : questions.size() + 1);
                    questions.add(question);
                }
            }
            
            return questions;
        } catch (Exception e) {
            log.error("解析AI生成结果失败", e);
            throw new RuntimeException("解析AI生成结果失败");
        }
    }
    
    private void saveQuestions(String appId, List<QuestionVO> questions) {
        for (QuestionVO vo : questions) {
            Question question = new Question();
            question.setAppId(appId);
            question.setQuestionText(vo.getQuestionText());
            question.setQuestionType(vo.getQuestionType());
            question.setOptions(vo.getOptions());
            question.setCorrectAnswer(vo.getCorrectAnswer());
            question.setScore(vo.getScore());
            question.setOrderNum(vo.getOrderNum());
            questionMapper.insert(question);
        }
    }
    
    private void updateAiComment(String answerId, String comment) {
        userAnswerMapper.updateAiCommentByAnswerId(answerId, comment);
    }
    
    private void saveAiLog(String appId, String generateType, String prompt, String result, Integer status) {
        saveAiLog(appId, generateType, prompt, result, status, null);
    }
    
    private void saveAiLog(String appId, String generateType, String prompt, String result, Integer status, String errorMsg) {
        AiGenerateLog log = new AiGenerateLog();
        log.setAppId(appId);
        log.setUserId(1L);
        log.setGenerateType(generateType);
        log.setPrompt(prompt);
        log.setResult(result);
        log.setStatus(status);
        log.setErrorMsg(errorMsg);
        aiGenerateLogMapper.insert(log);
    }
    
    public String getCachedConfig(String key) {
        if (!isRedisAvailable()) {
            return null;
        }
        String cacheKey = "config:" + key;
        Object value = redisTemplate.opsForValue().get(cacheKey);
        return value != null ? value.toString() : null;
    }
    
    public void setCachedConfig(String key, String value, long timeout, TimeUnit unit) {
        if (!isRedisAvailable()) {
            return;
        }
        String cacheKey = "config:" + key;
        redisTemplate.opsForValue().set(cacheKey, value, timeout, unit);
    }
}
