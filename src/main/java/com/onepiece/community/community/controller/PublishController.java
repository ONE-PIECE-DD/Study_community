package com.onepiece.community.community.controller;


import com.onepiece.community.community.mapper.QuesstionMapper;
import com.onepiece.community.community.mapper.UserMapper;
import com.onepiece.community.community.model.Question;
import com.onepiece.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuesstionMapper quesstionMapper;


    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model){//如果想从服务端的接口传递到页面里去，需要利用model传递

        User user=null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {//当访问首页的时候循环访问token，找到token的cookie，然后再到数据库中查是否有记录
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                user = userMapper.findByToken(token);
                if(user !=null){//如果有，则把user放到session里面，前端去显示
                    //
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }


        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        quesstionMapper.create(question);
        return "redirect:/";
    }
}
