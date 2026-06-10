package com.zwj.controller;

import com.zwj.entity.Club;
import com.zwj.entity.User;
import com.zwj.service.ClubService;
import com.zwj.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/club")
public class ClubController {
    @Autowired
    private ClubService clubService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Club> clubs = clubService.findAll();
        model.addAttribute("clubs", clubs);
        return "club/list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession session) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        return "club/add";
    }

    @PostMapping("/add")
    public String add(Club club, HttpSession session) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        clubService.save(club);
        return "redirect:/club/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        Club club = clubService.findById(id);
        if (club == null) {
            return "redirect:/club/list";
        }
        model.addAttribute("club", club);
        return "club/edit";
    }

    @PostMapping("/edit")
    public String edit(Club club, HttpSession session) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        clubService.update(club);
        return "redirect:/club/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        clubService.delete(id);
        return "redirect:/club/list";
    }

    @GetMapping("/my")
    public String myClub(HttpSession session, Model model) {
        User user = SecurityUtils.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getClubId() != null) {
            Club club = clubService.findById(user.getClubId());
            model.addAttribute("club", club);
        }
        return "club/my";
    }

    @GetMapping("/myClubs")
    public String myClubs(HttpSession session, Model model) {
        User user = SecurityUtils.getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        List<Club> clubs = clubService.findByUserId(user.getId());
        model.addAttribute("clubs", clubs);
        return "club/myClubs";
    }
}