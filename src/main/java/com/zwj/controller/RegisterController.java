package com.zwj.controller;

import com.zwj.entity.User;
import com.zwj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserService userService;

    @RequestMapping
    public String registerPage() {
        return "register";
    }

    @RequestMapping("/submit")
    public String register(String username, String password, String confirmPassword, String name, String role, Model model) {
        // 参数校验
        if (username == null || username.isEmpty()) {
            model.addAttribute("error", "用户名不能为空");
            return "register";
        }
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "密码不能为空");
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "register";
        }
        if (name == null || name.isEmpty()) {
            model.addAttribute("error", "姓名不能为空");
            return "register";
        }
        if (role == null || role.isEmpty()) {
            model.addAttribute("error", "请选择角色");
            return "register";
        }

        // 检查用户名是否已存在
        if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }

        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setRole(role);
        user.setClubId(null);

        try {
            userService.save(user);
            model.addAttribute("success", "注册成功，请登录");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "注册失败：" + e.getMessage());
            return "register";
        }
    }
}
