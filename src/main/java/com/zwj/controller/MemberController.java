package com.zwj.controller;

import com.zwj.entity.Club;
import com.zwj.entity.Member;
import com.zwj.entity.User;
import com.zwj.service.ClubService;
import com.zwj.service.MemberService;
import com.zwj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public String list(Model model) {
        List<Member> members = memberService.findAll();
        List<User> users = userService.findAll();
        List<Club> clubs = clubService.findAll();
        model.addAttribute("members", members);
        model.addAttribute("users", users);
        model.addAttribute("clubs", clubs);
        return "member/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        List<User> users = userService.findAll();
        List<Club> clubs = clubService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("clubs", clubs);
        return "member/add";
    }

    @PostMapping("/add")
    public String add(Member member) {
        memberService.save(member);
        return "redirect:/member/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Member member = memberService.findById(id);
        List<User> users = userService.findAll();
        List<Club> clubs = clubService.findAll();
        model.addAttribute("member", member);
        model.addAttribute("users", users);
        model.addAttribute("clubs", clubs);
        return "member/edit";
    }

    @PostMapping("/edit")
    public String edit(Member member) {
        memberService.update(member);
        return "redirect:/member/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        memberService.delete(id);
        return "redirect:/member/list";
    }

    @GetMapping("/apply")
    public String applyForm(Model model) {
        List<Club> clubs = clubService.findAll();
        model.addAttribute("clubs", clubs);
        return "member/apply";
    }

    @PostMapping("/apply")
    public String apply(Integer clubId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Member member = new Member();
        member.setUserId(user.getId());
        member.setClubId(clubId);
        member.setRole("成员");
        memberService.save(member);
        return "redirect:/member/my";
    }

    @GetMapping("/my")
    public String myMembers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Member> members = memberService.findByUserId(user.getId());
        model.addAttribute("members", members);
        return "member/my";
    }

    @GetMapping("/manage")
    public String manageMembers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user.getClubId() != null) {
            List<Member> members = memberService.findByClubId(user.getClubId());
            model.addAttribute("members", members);
        }
        return "member/manage";
    }



    @GetMapping("/clubMembers")
    public String clubMembers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
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