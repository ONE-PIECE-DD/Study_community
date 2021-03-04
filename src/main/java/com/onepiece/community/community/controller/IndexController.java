package com.onepiece.community.community.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller //自动识别扫描当前的类，把它作为spring的bean去管理，同时视其为一个controller（可以接收前端的请求）,作为路由API的承载者
public class IndexController {
    @GetMapping("/")//匹配什么都不输入的情况
    public String index(){ return "index"; }
}
