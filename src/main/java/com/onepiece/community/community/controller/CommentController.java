package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.CommentDTO;
import com.onepiece.community.community.mapper.CommentMapper;
import com.onepiece.community.community.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;

    //用json来传输的服务器端的API接口，并将其传输到数据库当中去
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO){
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setCommentator(1);
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        commentMapper.insert(comment);
        return null;



    }
}
