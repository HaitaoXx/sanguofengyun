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
import java.util.UUID;

@Controller
@RequestMapping("/forget")
public class ForgetController {
    
    private static final Logger logger = LoggerFactory.getLogger(ForgetController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping
    public String forgetPage() {
        return "forget";
    }

    @RequestMapping("/verify")
    public String verifyUsername(String username, Model model, HttpSession session) {
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "用户名不能为空");
            return "forget";
        }
        
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "该用户名不存在");
            return "forget";
        }
        
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        session.setAttribute("resetCode", code);
        session.setAttribute("resetUsername", username);
        
        model.addAttribute("code", code);
        model.addAttribute("message", "验证码已生成（演示模式）");
        return "verify";
    }

    @RequestMapping("/checkCode")
    public String checkCode(String code, HttpSession session, Model model) {
        String storedCode = (String) session.getAttribute("resetCode");
        
        if (code == null || code.isEmpty()) {
            model.addAttribute("error", "请输入验证码");
            model.addAttribute("code", storedCode);
            return "verify";
        }
        
        if (storedCode == null || !storedCode.equals(code)) {
            model.addAttribute("error", "验证码错误");
            model.addAttribute("code", storedCode);
            return "verify";
        }
        
        return "reset";
    }

    @RequestMapping("/reset")
    public String resetPassword(String password, String confirmPassword, HttpSession session, Model model) {
        String username = (String) session.getAttribute("resetUsername");
        
        if (username == null) {
            model.addAttribute("error", "请先验证身份");
            return "forget";
        }
        
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "密码不能为空");
            return "reset";
        }
        
        if (password.length() < 6) {
            model.addAttribute("error", "密码长度至少6位");
            return "reset";
        }
        
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "reset";
        }
        
        User user = userService.findByUsername(username);
        if (user != null) {
            // 使用BCrypt加密密码
            user.setPassword(passwordEncoder.encode(password));
            userService.update(user);
            
            session.removeAttribute("resetCode");
            session.removeAttribute("resetUsername");
            
            logger.info("用户密码重置成功: {}", username);
            model.addAttribute("success", "密码重置成功，请登录");
            return "login";
        } else {
            model.addAttribute("error", "用户不存在");
            return "forget";
        }
    }
}