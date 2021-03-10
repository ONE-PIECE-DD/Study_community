package com.onepiece.community.community.advice;

import com.onepiece.community.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {
//此处拦截部分能够拦截的异常
    @ExceptionHandler(Exception.class)
    //@ResponseBody:该注解返回json
    ModelAndView handle( Throwable e, Model model) {
        if(e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());
        }else {
            model.addAttribute("massage","服务器冒烟了");
        }
        return new ModelAndView("error");
    }

}
