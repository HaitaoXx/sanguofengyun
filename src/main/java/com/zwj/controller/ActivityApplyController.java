package com.zwj.controller;

import com.zwj.entity.ActivityApply;
import com.zwj.entity.User;
import com.zwj.service.ActivityApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/activityApply")
public class ActivityApplyController {
    @Autowired
    private ActivityApplyService activityApplyService;

    @RequestMapping("/apply")
    public String apply(Integer activityId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (activityApplyService.hasApplied(activityId, user.getId())) {
            model.addAttribute("error", "您已经报名了该活动");
            return "redirect:/activity/list";
        }

        activityApplyService.apply(activityId, user.getId());
        model.addAttribute("success", "报名成功，等待审核");
        return "redirect:/activity/list";
    }

    @RequestMapping("/list")
    public String list(Integer activityId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<ActivityApply> applies = activityApplyService.findByActivityId(activityId);
        model.addAttribute("applies", applies);
        model.addAttribute("activityId", activityId);
        return "activity/applyList";
    }

    @RequestMapping("/approve")
    public String approve(Integer id, Integer activityId) {
        activityApplyService.updateStatus(id, "approved");
        return "redirect:/activityApply/list?activityId=" + activityId;
    }

    @RequestMapping("/reject")
    public String reject(Integer id, Integer activityId) {
        activityApplyService.updateStatus(id, "rejected");
        return "redirect:/activityApply/list?activityId=" + activityId;
    }

    @RequestMapping("/myApplies")
    public String myApplies(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<ActivityApply> applies = activityApplyService.findByUserId(user.getId());
        model.addAttribute("applies", applies);
        return "activity/myApplies";
    }
}
