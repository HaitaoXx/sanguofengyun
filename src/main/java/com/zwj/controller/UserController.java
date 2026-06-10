package com.zwj.controller;

import com.zwj.entity.User;
import com.zwj.service.UserService;
import com.zwj.util.PasswordEncoder;
import com.zwj.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession session) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        return "user/add";
    }

    @PostMapping("/add")
    public String add(HttpSession session, User user) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        // 如果密码是明文，进行加密
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(PasswordEncoder.encode(user.getPassword()));
        }
        userService.save(user);
        return "redirect:/user/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, HttpSession session, Model model) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/user/list";
        }
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String edit(HttpSession session, User user) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        // 如果密码是明文，进行加密
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(PasswordEncoder.encode(user.getPassword()));
        }
        userService.update(user);
        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        String check = SecurityUtils.requireAdmin(session);
        if (check != null) {
            return check;
        }
        userService.delete(id);
        return "redirect:/user/list";
    }
}