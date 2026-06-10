package com.zwj.controller;

import com.zwj.entity.Activity;
import com.zwj.entity.User;
import com.zwj.entity.Club;
import com.zwj.service.ActivityService;
import com.zwj.service.ClubService;
import com.zwj.util.DateUtils;
import com.zwj.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        String check = SecurityUtils.requireAdminOrLeader(session);
        if (check != null) {
            return check;
        }
        User user = SecurityUtils.getCurrentUser(session);
        List<Club> clubs = clubService.findAll();
        if (SecurityUtils.isLeader(session)) {
            // 社长只能选择自己的社团
            Club leaderClub = clubService.findById(user.getClubId());
            model.addAttribute("clubs", leaderClub != null ? java.util.Arrays.asList(leaderClub) : new java.util.ArrayList<>());
        } else {
            model.addAttribute("clubs", clubs);
        }
        return "activity/add";
    }

    @PostMapping("/add")
    public String add(Activity activity, String activityTimeStr, HttpSession session,
                      RedirectAttributes redirectAttrs) {
        String check = SecurityUtils.requireAdminOrLeader(session);
        if (check != null) {
            return check;
        }

        // 使用 DateUtils 解析日期时间
        if (activityTimeStr != null && !activityTimeStr.trim().isEmpty()) {
            Date parsedDate = DateUtils.parseDateTime(activityTimeStr);
            if (parsedDate != null) {
                activity.setActivityTime(parsedDate);
            } else {
                redirectAttrs.addFlashAttribute("error", "活动时间格式不正确");
                return "redirect:/activity/add";
            }
        }
        activityService.save(activity);
        redirectAttrs.addFlashAttribute("success", "活动添加成功");
        return "redirect:/activity/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        String check = SecurityUtils.requireAdminOrLeader(session);
        if (check != null) {
            return check;
        }
        User user = SecurityUtils.getCurrentUser(session);
        Activity activity = activityService.findById(id);
        if (activity == null) {
            return "redirect:/activity/list";
        }
        List<Club> clubs = clubService.findAll();
        if (SecurityUtils.isLeader(session)) {
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
    public String edit(Activity activity, String activityTimeStr, HttpSession session,
                       RedirectAttributes redirectAttrs) {
        String check = SecurityUtils.requireAdminOrLeader(session);
        if (check != null) {
            return check;
        }

        // 使用 DateUtils 解析日期时间
        if (activityTimeStr != null && !activityTimeStr.trim().isEmpty()) {
            Date parsedDate = DateUtils.parseDateTime(activityTimeStr);
            if (parsedDate != null) {
                activity.setActivityTime(parsedDate);
            } else {
                redirectAttrs.addFlashAttribute("error", "活动时间格式不正确");
                return "redirect:/activity/edit/" + activity.getId();
            }
        }
        activityService.update(activity);
        redirectAttrs.addFlashAttribute("success", "活动更新成功");
        return "redirect:/activity/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session,
                         RedirectAttributes redirectAttrs) {
        String check = SecurityUtils.requireAdminOrLeader(session);
        if (check != null) {
            return check;
        }
        activityService.delete(id);
        redirectAttrs.addFlashAttribute("success", "活动删除成功");
        return "redirect:/activity/list";
    }

    @GetMapping("/my")
    public String myActivities(HttpSession session, Model model) {
        User user = SecurityUtils.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getClubId() != null) {
            List<Activity> activities = activityService.findByClubId(user.getClubId());
            model.addAttribute("activities", activities);
        }
        return "activity/my";
    }

    @GetMapping("/clubActivities")
    public String clubActivities(HttpSession session, Model model) {
        User user = SecurityUtils.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
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