package com.zwj.controller;

import com.zwj.entity.Activity;
import com.zwj.entity.User;
import com.zwj.entity.Club;
import com.zwj.service.ActivityService;
import com.zwj.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClubService clubService;

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword, 
                      @RequestParam(required = false) Integer clubId, Model model) {
        List<Activity> activities;
        List<Club> clubs = clubService.findAll();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            activities = activityService.searchActivities(keyword.trim(), clubId);
        } else {
            activities = activityService.findAll();
        }
        
        model.addAttribute("activities", activities);
        model.addAttribute("clubs", clubs);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedClubId", clubId);
        return "activity/list";
    }

    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"admin".equals(user.getRole()) && !"leader".equals(user.getRole()))) {
            return "redirect:/index";
        }
        List<Club> clubs = clubService.findAll();
        if ("leader".equals(user.getRole())) {
            // 社长只能选择自己的社团
            Club leaderClub = clubService.findById(user.getClubId());
            model.addAttribute("clubs", leaderClub != null ? java.util.Arrays.asList(leaderClub) : new java.util.ArrayList<>());
        } else {
            model.addAttribute("clubs", clubs);
        }
        return "activity/add";
    }

    @PostMapping("/add")
    public String add(Activity activity, String activityTimeStr, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"admin".equals(user.getRole()) && !"leader".equals(user.getRole()))) {
            return "redirect:/index";
        }
        
        // 如果有传入字符串格式的时间，需要转换
        if (activityTimeStr != null && !activityTimeStr.isEmpty()) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                activity.setActivityTime(sdf.parse(activityTimeStr));
            } catch (Exception e) {
                // 设置默认时间
                activity.setActivityTime(new java.util.Date());
            }
        }
        activityService.save(activity);
        return "redirect:/activity/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"admin".equals(user.getRole()) && !"leader".equals(user.getRole()))) {
            return "redirect:/index";
        }
        Activity activity = activityService.findById(id);
        List<Club> clubs = clubService.findAll();
        if ("leader".equals(user.getRole())) {
            // 社长只能选择自己的社团
            Club leaderClub = clubService.findById(user.getClubId());
            model.addAttribute("clubs", leaderClub != null ? java.util.Arrays.asList(leaderClub) : new java.util.ArrayList<>());
        } else {
            model.addAttribute("clubs", clubs);
        }
        model.addAttribute("activity", activity);
        return "activity/edit";
    }

    @PostMapping("/edit")
    public String edit(Activity activity, String activityTimeStr, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"admin".equals(user.getRole()) && !"leader".equals(user.getRole()))) {
            return "redirect:/index";
        }
        
        // 如果有传入字符串格式的时间，需要转换
        if (activityTimeStr != null && !activityTimeStr.isEmpty()) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                activity.setActivityTime(sdf.parse(activityTimeStr));
            } catch (Exception e) {
                // 保持原有时间不变
            }
        }
        activityService.update(activity);
        return "redirect:/activity/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"admin".equals(user.getRole()) && !"leader".equals(user.getRole()))) {
            return "redirect:/index";
        }
        activityService.delete(id);
        return "redirect:/activity/list";
    }

    @GetMapping("/my")
    public String myActivities(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user.getClubId() != null) {
            List<Activity> activities = activityService.findByClubId(user.getClubId());
            model.addAttribute("activities", activities);
        }
        return "activity/my";
    }

    @GetMapping("/clubActivities")
    public String clubActivities(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user.getClubId() != null) {
            List<Activity> activities = activityService.findByClubId(user.getClubId());
            List<Club> clubs = clubService.findAll();
            model.addAttribute("activities", activities);
            model.addAttribute("clubs", clubs);
            model.addAttribute("clubId", user.getClubId());
        }
        return "activity/clubActivities";
    }
}