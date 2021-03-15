package com.onepiece.community.community.controller;


import com.onepiece.community.community.dto.QuestionDTO;
import com.onepiece.community.community.model.Question;
import com.onepiece.community.community.model.User;
import com.onepiece.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {


    //处理question表中的数据到类对象的数据的业务
    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")Long id,
                       Model model){
        QuestionDTO question=questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());

        return "publish";
    }


    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            //接收post请求的同时，接收变量
            @RequestParam(value="title",required = false) String title,
            @RequestParam(value="description",required = false) String description,
            @RequestParam(value="tag",required = false) String tag,
            @RequestParam(value="id",required = false)Long id,
            HttpServletRequest request,
            Model model){//如果想从服务端的接口传递到页面里去，需要利用model传递

        //回显到页面
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        //判断是否为空，前端也可以做，但前端可能会绕过，所以前后端都需要做
        if(title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description==null||description==""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag==null||tag==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        //判断是否登录

        User user = (User)request.getSession().getAttribute("user");

        //如果不存在，则返回登录提示信息
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }


        //将发布的问题保持在本地数据库
        Question question = new Question();
        question.setId(id);
        question.setTitle(title);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setCommentCount(0);
        question.setViewCount(0);
        question.setLikeCount(0);
        question.setTag(tag);
        //还有gmtModified和gmtCreate
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
