package com.zwj.controller;

import com.zwj.entity.User;
import com.zwj.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/register")
public class RegisterController {
    
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "密码不能为空");
            return "register";
        }
        if (password.length() < 6) {
            model.addAttribute("error", "密码长度至少6位");
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
        if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }

        // 创建用户
        User user = new User();
        user.setUsername(username);
        // 使用BCrypt加密密码
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setRole(role);
        user.setClubId(null);

        try {
            userService.save(user);
            logger.info("用户注册成功: {}", username);
            model.addAttribute("success", "注册成功，请登录");
            return "login";
        } catch (Exception e) {
            logger.error("用户注册失败: {}", username, e);
            model.addAttribute("error", "注册失败：" + e.getMessage());
            return "register";
        }
    }
}