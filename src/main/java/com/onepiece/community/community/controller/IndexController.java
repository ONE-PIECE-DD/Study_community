package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.PaginationDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.model.IModel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller //自动识别扫描当前的类，把它作为spring的bean去管理，同时视其为一个controller（可以接收前端的请求）,作为路由API的承载者
public class IndexController {
    @Autowired
    private QuestionService quesstionService;

    @GetMapping("/")//匹配什么都不输入的情况
    public String index(
                        Model model,
                        @RequestParam(name="page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "2")Integer size){
    //利用request获取cookie，利用response向浏览器返回cookie
        //page从前端传来
        PaginationDTO pagination=quesstionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }

}
