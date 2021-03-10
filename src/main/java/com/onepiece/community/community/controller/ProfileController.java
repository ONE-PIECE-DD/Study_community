package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.PaginationDTO;
import com.onepiece.community.community.mapper.UserMapper;
import com.onepiece.community.community.model.User;
import com.onepiece.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/profile/{action}")//动态接收参数
    public String profile(@PathVariable(name = "action")String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name="page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "2")Integer size) {
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";//当前没有用户登录
        }

        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }else{
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");

        }

        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }
}


