package com.zwj.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception e) {
        // 记录详细异常信息到日志
        logger.error("系统异常 - URL: {}, 错误: {}", request.getRequestURI(), e.getMessage(), e);

        ModelAndView mav = new ModelAndView();
        // 生产环境返回通用错误信息，避免泄露系统内部信息
        mav.addObject("error", "系统繁忙，请稍后重试");
        mav.setViewName("/error");
        return mav;
    }
}
