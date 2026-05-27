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
@RequestMapping("/login")
public class LoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        User user = userService.findByUsername(username);
        
        if (user == null) {
            logger.warn("用户登录失败，用户不存在: {}", username);
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
        
        boolean passwordMatch = false;
        String storedPassword = user.getPassword();
        
        // 判断密码是否已加密（BCrypt加密的密码以$2a$或$2b$开头）
        if (storedPassword != null && storedPassword.startsWith("$2")) {
            // 已加密密码，使用BCrypt验证
            passwordMatch = passwordEncoder.matches(password, storedPassword);
        } else {
            // 未加密密码，使用明文比较
            passwordMatch = password.equals(storedPassword);
            
            // 如果明文密码匹配，自动更新为BCrypt加密格式
            if (passwordMatch) {
                user.setPassword(passwordEncoder.encode(password));
                userService.update(user);
                logger.info("用户密码已自动升级为BCrypt加密: {}", username);
            }
        }
        
        if (passwordMatch) {
            session.setAttribute("user", user);
            logger.info("用户登录成功: {}", username);
            return "redirect:/index";
        } else {
            logger.warn("用户登录失败，密码错误: {}", username);
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            logger.info("用户退出登录: {}", user.getUsername());
        }
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/login";
    }
}