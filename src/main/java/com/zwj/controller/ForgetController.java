package com.zwj.controller;

import com.zwj.entity.User;
import com.zwj.service.UserService;
import com.zwj.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/forget")
public class ForgetController {
    @Autowired
    private UserService userService;

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

        User user = userService.findByUsername(username.trim());
        if (user == null) {
            model.addAttribute("error", "该用户名不存在");
            return "forget";
        }

        // 生成验证码（简单实现，实际项目应使用邮箱/短信验证）
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        session.setAttribute("resetCode", code);
        session.setAttribute("resetUsername", username.trim());
        session.setAttribute("resetCodeTime", System.currentTimeMillis());

        // 演示模式下不直接显示验证码，而是提示用户
        model.addAttribute("message", "验证码已生成（演示模式：请查看控制台或联系管理员）");
        return "verify";
    }

    @RequestMapping("/checkCode")
    public String checkCode(String code, HttpSession session, Model model) {
        String storedCode = (String) session.getAttribute("resetCode");
        Long codeTime = (Long) session.getAttribute("resetCodeTime");

        // 验证码过期检查（10分钟）
        if (codeTime == null || System.currentTimeMillis() - codeTime > 10 * 60 * 1000) {
            session.removeAttribute("resetCode");
            session.removeAttribute("resetUsername");
            session.removeAttribute("resetCodeTime");
            model.addAttribute("error", "验证码已过期，请重新获取");
            return "forget";
        }

        if (storedCode == null || !storedCode.equalsIgnoreCase(code)) {
            model.addAttribute("error", "验证码错误");
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

        if (password == null || password.length() < 6) {
            model.addAttribute("error", "密码长度不能少于6位");
            return "reset";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "reset";
        }

        User user = userService.findByUsername(username);
        if (user != null) {
            // 使用 BCrypt 加密新密码
            user.setPassword(PasswordEncoder.encode(password));
            userService.update(user);

            // 清除session
            session.removeAttribute("resetCode");
            session.removeAttribute("resetUsername");
            session.removeAttribute("resetCodeTime");

            model.addAttribute("success", "密码重置成功，请登录");
            return "login";
        } else {
            model.addAttribute("error", "用户不存在");
            return "forget";
        }
    }
}
