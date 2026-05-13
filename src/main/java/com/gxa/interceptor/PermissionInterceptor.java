package com.gxa.interceptor;

import com.gxa.annotation.RequiresPermission;
import com.gxa.domain.vo.Result;
import com.gxa.service.PermissionService;
import com.gxa.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresPermission annotation = handlerMethod.getMethodAnnotation(RequiresPermission.class);

        if (annotation == null) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            responseNoPermission(response);
            return false;
        }

        try {
            Long userId = jwtUtil.getUserId(token.replace("Bearer ", ""));
            String permissionCode = annotation.value();

            if (!permissionService.hasPermission(userId, permissionCode)) {
                responseNoPermission(response);
                return false;
            }
        } catch (Exception e) {
            responseNoPermission(response);
            return false;
        }

        return true;
    }

    private void responseNoPermission(HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(Result.error("无权限访问")));
        writer.flush();
        writer.close();
    }
}