package com.zwj.controller;

import com.zwj.entity.MemberApply;
import com.zwj.entity.User;
import com.zwj.service.MemberApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/memberApply")
public class MemberApplyController {
    @Autowired
    private MemberApplyService memberApplyService;

    @RequestMapping("/apply")
    public String apply(Integer clubId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (memberApplyService.hasApplied(clubId, user.getId())) {
            model.addAttribute("error", "您已经申请加入该社团");
            return "redirect:/club/list";
        }

        memberApplyService.apply(clubId, user.getId());
        model.addAttribute("success", "申请成功，等待社长审核");
        return "redirect:/club/list";
    }

    @RequestMapping("/list")
    public String list(Integer clubId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<MemberApply> applies = memberApplyService.findByClubId(clubId);
        model.addAttribute("applies", applies);
        model.addAttribute("clubId", clubId);
        return "member/applyList";
    }

    @RequestMapping("/approve")
    public String approve(Integer id, Integer clubId) {
        memberApplyService.approve(id);
        return "redirect:/memberApply/list?clubId=" + clubId;
    }

    @RequestMapping("/reject")
    public String reject(Integer id, Integer clubId) {
        memberApplyService.reject(id);
        return "redirect:/memberApply/list?clubId=" + clubId;
    }

    @RequestMapping("/myApplies")
    public String myApplies(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<MemberApply> applies = memberApplyService.findByUserId(user.getId());
        model.addAttribute("applies", applies);
        return "member/myApplies";
    }
}
