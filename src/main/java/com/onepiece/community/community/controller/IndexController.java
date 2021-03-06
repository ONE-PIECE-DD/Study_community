package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.QuestionDTO;
import com.onepiece.community.community.mapper.QuesstionMapper;
import com.onepiece.community.community.mapper.UserMapper;
import com.onepiece.community.community.model.Question;
import com.onepiece.community.community.model.User;
import com.onepiece.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.model.IModel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller //自动识别扫描当前的类，把它作为spring的bean去管理，同时视其为一个controller（可以接收前端的请求）,作为路由API的承载者
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService quesstionService;

    @GetMapping("/")//匹配什么都不输入的情况
    public String index(HttpServletRequest request,
                        Model model){
    //利用request获取cookie，利用response向浏览器返回cookie
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0)
            for (Cookie cookie : cookies) {//当访问首页的时候循环访问token，找到token的cookie，然后再到数据库中查是否有记录
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {//如果有，则把user放到session里面，前端去显示
                        //
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        List<QuestionDTO> questionList=quesstionService.list();
        model.addAttribute("questions",questionList);
        return "index";
    }

}
