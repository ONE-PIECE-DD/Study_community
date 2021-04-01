package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.CommentDTO;
import com.onepiece.community.community.dto.ResultDTO;
import com.onepiece.community.community.exception.CustomizeErrorCode;
import com.onepiece.community.community.mapper.CommentMapper;
import com.onepiece.community.community.model.Comment;
import com.onepiece.community.community.model.User;
import com.onepiece.community.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
public class CommentController {


    @Autowired
    private CommentService commentService;

    //用json来传输的服务器端的API接口，并前端的json数据转换为对象将其传输到数据库当中去，又或是将对象转换成json传到前端
    @ResponseBody//将对象自动序列化为json，发到前端
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,//将post请求当中的body部分反序列化到此处，使其成为对象
                       HttpServletRequest request){//自动的反序列化为对象

        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }//当用户未登录的是否给些提示
        Comment comment = new Comment();
        //作为主键的评论标识ID是自增的，所以没有管
        comment.setParentId(commentDTO.getParentId());
        comment.setCommentator(user.getId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setLikeCount(0L);

        commentService.insert(comment);//插入到数据库
        return ResultDTO.okOf();

    }
}
