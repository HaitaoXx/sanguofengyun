package com.zwj.controller;

import com.zwj.entity.Club;
import com.zwj.entity.Member;
import com.zwj.entity.User;
import com.zwj.service.ClubService;
import com.zwj.service.MemberApplyService;
import com.zwj.service.MemberService;
import com.zwj.service.UserService;
import com.zwj.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClubService clubService;
    @Autowired
    private MemberApplyService memberApplyService;

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        String check = SecurityUtils.requireAdminOrLeader(session);
        if (check != null) {
            return check;
        }
        List<Member> members = memberService.findAll();
        List<User> users = userService.findAll();
        List<Club> clubs = clubService.findAll();
        model.addAttribute("members", members);
        model.addAttribute("users", users);
        model.addAttribute("clubs", clubs);
        return "member/list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession session, Model model) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        List<User> users = userService.findAll();
        List<Club> clubs = clubService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("clubs", clubs);
        return "member/add";
    }

    @PostMapping("/add")
    public String add(HttpSession session, Member member) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        memberService.save(member);
        return "redirect:/member/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, HttpSession session, Model model) {
        String check = SecurityUtils.requireAdminOrLeader(session);
        if (check != null) {
            return check;
        }
        Member member = memberService.findById(id);
        if (member == null) {
            return "redirect:/member/list";
        }
        List<User> users = userService.findAll();
        List<Club> clubs = clubService.findAll();
        model.addAttribute("member", member);
        model.addAttribute("users", users);
        model.addAttribute("clubs", clubs);
        return "member/edit";
    }

    @PostMapping("/edit")
    public String edit(HttpSession session, Member member) {
        String check = SecurityUtils.requireAdminOrLeader(session);
        if (check != null) {
            return check;
        }
        memberService.update(member);
        return "redirect:/member/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        memberService.delete(id);
        return "redirect:/member/list";
    }

    @GetMapping("/apply")
    public String applyForm(HttpSession session, Model model) {
        String check = SecurityUtils.requireLogin(session);
        if (check != null) {
            return check;
        }
        List<Club> clubs = clubService.findAll();
        model.addAttribute("clubs", clubs);
        return "member/apply";
    }

    @PostMapping("/apply")
    public String apply(Integer clubId, HttpSession session, RedirectAttributes redirectAttrs) {
        User user = SecurityUtils.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (clubId == null) {
            redirectAttrs.addFlashAttribute("error", "请选择要加入的社团");
            return "redirect:/club/list";
        }
        if (memberApplyService.hasApplied(clubId, user.getId())) {
            redirectAttrs.addFlashAttribute("error", "您已经申请加入该社团");
            return "redirect:/club/list";
        }
        memberApplyService.apply(clubId, user.getId());
        redirectAttrs.addFlashAttribute("success", "申请成功，等待社长审核");
        return "redirect:/memberApply/myApplies";
    }

    @GetMapping("/my")
    public String myMembers(HttpSession session, Model model) {
        User user = SecurityUtils.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        List<Member> members = memberService.findByUserId(user.getId());
        model.addAttribute("members", members);
        return "member/my";
    }

    @GetMapping("/manage")
    public String manageMembers(HttpSession session, Model model) {
        User user = SecurityUtils.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getClubId() != null) {
            List<Member> members = memberService.findByClubId(user.getClubId());
            model.addAttribute("members", members);
        }
        return "member/manage";
    }

    @GetMapping("/clubMembers")
    public String clubMembers(HttpSession session, Model model) {
        User user = SecurityUtils.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getClubId() != null) {
            List<Member> members = memberService.findByClubId(user.getClubId());
            List<User> users = userService.findAll();
            model.addAttribute("members", members);
            model.addAttribute("users", users);
            model.addAttribute("clubId", user.getClubId());
        }
        return "member/clubMembers";
    }
}