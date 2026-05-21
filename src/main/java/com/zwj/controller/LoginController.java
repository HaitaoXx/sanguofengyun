package com.zwj.controller;

import com.zwj.entity.User;
import com.zwj.service.UserService;
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
        User user = userService.loginByUsernamePassword(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/index";
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/login";
    }
}