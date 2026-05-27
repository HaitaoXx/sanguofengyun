package com.zwj.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        logger.error("系统异常: ", e);
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("error", "系统繁忙，请稍后重试");
        mv.addObject("detail", e.getMessage());
        return mv;
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("参数错误: {}", e.getMessage());
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("error", "参数错误");
        mv.addObject("detail", e.getMessage());
        return mv;
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView handleNullPointerException(NullPointerException e) {
        logger.error("空指针异常: ", e);
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("error", "系统错误");
        mv.addObject("detail", "数据不存在或已被删除");
        return mv;
    }
}