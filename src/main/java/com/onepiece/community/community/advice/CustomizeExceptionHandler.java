package com.onepiece.community.community.advice;

import com.alibaba.fastjson.JSON;
import com.onepiece.community.community.dto.ResultDTO;
import com.onepiece.community.community.exception.CustomizeErrorCode;
import com.onepiece.community.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
@ResponseBody//返回json
public class CustomizeExceptionHandler {
//此处拦截部分能够拦截的异常
    @ExceptionHandler(Exception.class)
    //@ResponseBody:该注解返回json
    ModelAndView handle(Throwable e, Model model,
                  HttpServletRequest request,
                  HttpServletResponse response) {
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            ResultDTO resultDTO;
            //返回json
            if(e instanceof CustomizeException){//如果抛出了任何Customize Exception的异常，直接返回如下 的error_message
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            }else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");//为response设置字符集避免乱码
                PrintWriter writer = response.getWriter();//直接可以在前端写值
                writer.write(JSON.toJSONString(resultDTO));//用toJSONString方法把object变成json对象
                writer.close();//将流关闭掉：结束
            } catch (IOException ioe) {
            }
            return null;
        }else {
            //返回错误页面跳转
            if(e instanceof CustomizeException){//如果抛出了任何Customize Exception的异常，直接返回如下 的error_message
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("massage",CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }

}
