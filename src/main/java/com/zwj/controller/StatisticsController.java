package com.zwj.controller;

import com.zwj.service.StatisticsService;
import com.zwj.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping
    public String statistics(HttpSession session, Model model) {
        String check = SecurityUtils.requireLogin(session);
        if (check != null) {
            return check;
        }
        Map<String, Object> stats = statisticsService.getAllStatistics();
        model.addAttribute("stats", stats);
        return "statistics";
    }

    @RequestMapping("/data")
    @ResponseBody
    public Map<String, Object> getStatisticsData(HttpSession session) {
        if (!SecurityUtils.isLoggedIn(session)) {
            return null;
        }
        return statisticsService.getAllStatistics();
    }
}
