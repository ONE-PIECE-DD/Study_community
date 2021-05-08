package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.NotificationDTO;
import com.onepiece.community.community.dto.PaginationDTO;
import com.onepiece.community.community.enums.NotificationEnum;
import com.onepiece.community.community.model.Notification;
import com.onepiece.community.community.model.User;
import com.onepiece.community.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")//动态接收参数
    public String notification(@PathVariable(name = "id")Long id,
                          HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";//当前没有用户登录
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);
        if(NotificationEnum.REPLY_COMMENT.getType()==notificationDTO.getType()
        ||NotificationEnum.REPLY_QUESTION.getType()==notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterId();
        }else {
            return "redirect:/";
        }

    }

}
