package com.onepiece.community.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller //自动识别扫描当前的类，把它作为spring的bean去管理，同时视其为一个controller（可以接收前端的请求）
public class HelloController {
    @GetMapping("/index")
    public String hello(@RequestParam(name = "name") String name,Model model){
        model.addAttribute("name",name);
        return "index";
    }
}
