package com.zwj.controller;

import com.zwj.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping
    public String statistics(HttpSession session, Model model) {
        Map<String, Object> stats = statisticsService.getAllStatistics();
        model.addAttribute("stats", stats);
        return "statistics";
    }

    @RequestMapping("/data")
    @org.springframework.web.bind.annotation.ResponseBody
    public Map<String, Object> getStatisticsData() {
        return statisticsService.getAllStatistics();
    }
}
