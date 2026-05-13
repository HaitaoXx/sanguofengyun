package com.gxa.service;

import com.gxa.domain.entity.User;
import com.gxa.domain.entity.UserAnswer;
import com.gxa.mapper.UserAnswerMapper;
import com.gxa.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GuideService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAnswerMapper userAnswerMapper;

    public Map<String, Object> getGuideSteps(Long userId) {
        Map<String, Object> guide = new HashMap<>();
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            return guide;
        }

        List<UserAnswer> answers = userAnswerMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserAnswer>()
                .eq(UserAnswer::getUserId, userId)
        );

        boolean hasAnswered = !answers.isEmpty();
        
        guide.put("showWelcome", !hasAnswered);
        guide.put("showCreateApp", !hasAnswered);
        guide.put("showAnswerTips", !hasAnswered);
        
        Map<String, Object> steps = new HashMap<>();
        steps.put("step1", !hasAnswered);
        steps.put("step2", !hasAnswered);
        steps.put("step3", !hasAnswered);
        steps.put("completed", hasAnswered);
        
        guide.put("steps", steps);
        guide.put("totalSteps", 3);
        guide.put("currentStep", hasAnswered ? 4 : 1);
        
        return guide;
    }

    public Map<String, Object> getOperationTips(String page) {
        Map<String, Object> tips = new HashMap<>();
        
        switch (page) {
            case "home":
                tips.put("title", "欢迎使用AI智能答题系统");
                tips.put("description", "您可以创建应用、生成题目、开始答题，体验AI智能评分功能。");
                tips.put("actions", new String[]{"点击导航栏创建应用", "选择已有应用开始答题", "查看统计分析"});
                break;
            case "app":
                tips.put("title", "应用管理");
                tips.put("description", "在这里您可以创建和管理答题应用。");
                tips.put("actions", new String[]{"点击创建应用按钮", "填写应用名称和描述", "等待审核通过后即可使用"});
                break;
            case "question":
                tips.put("title", "题库管理");
                tips.put("description", "管理您的题目，支持手动添加和AI生成。");
                tips.put("actions", new String[]{"选择应用查看题目", "点击添加题目手动录入", "使用AI生成功能自动生成"});
                break;
            case "statistics":
                tips.put("title", "统计分析");
                tips.put("description", "查看详细的答题统计和分析报告。");
                tips.put("actions", new String[]{"选择应用查看统计", "点击高级分析查看深度数据", "导出报告到Excel"});
                break;
            case "profile":
                tips.put("title", "个人中心");
                tips.put("description", "管理您的个人资料和查看答题成就。");
                tips.put("actions", new String[]{"完善个人资料", "查看答题统计", "收集成就徽章"});
                break;
            default:
                tips.put("title", "欢迎");
                tips.put("description", "探索系统功能");
        }
        
        return tips;
    }
}