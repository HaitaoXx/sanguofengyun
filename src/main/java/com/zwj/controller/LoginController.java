package com.zwj.controller;

import com.zwj.entity.User;
import com.zwj.service.UserService;
import com.zwj.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, HttpSession session, Model model) {
        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "用户名不能为空");
            return "login";
        }
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "密码不能为空");
            return "login";
        }

        // 先根据用户名查询用户
        User user = userService.findByUsername(username.trim());
        if (user == null) {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }

        // 使用 BCrypt 验证密码
        if (!PasswordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }

        // 登录成功，将用户信息存入 session
        session.setAttribute("user", user);
        return "redirect:/index";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/login";
    }
}