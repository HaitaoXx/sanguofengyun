package com.zwj.controller;

import com.zwj.entity.User;
import com.zwj.service.UserService;
import com.zwj.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "用户名不能为空");
            return "register";
        }
        if (username.trim().length() < 3 || username.trim().length() > 20) {
            model.addAttribute("error", "用户名长度必须在3-20个字符之间");
            return "register";
        }
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "密码不能为空");
            return "register";
        }
        if (password.length() < 6) {
            model.addAttribute("error", "密码长度不能少于6位");
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "register";
        }
        if (name == null || name.trim().isEmpty()) {
            model.addAttribute("error", "姓名不能为空");
            return "register";
        }
        if (role == null || role.isEmpty()) {
            model.addAttribute("error", "请选择角色");
            return "register";
        }

        // 检查用户名是否已存在
        if (userService.findByUsername(username.trim()) != null) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }

        // 创建用户，密码加密存储
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(PasswordEncoder.encode(password));
        user.setName(name.trim());
        user.setRole(role);
        user.setClubId(null);

        try {
            userService.save(user);
            model.addAttribute("success", "注册成功，请登录");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "注册失败，请稍后重试");
            return "register";
        }
    }
}
