package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.CommentCreateDTO;
import com.onepiece.community.community.dto.CommentDTO;
import com.onepiece.community.community.dto.ResultDTO;
import com.onepiece.community.community.enums.CommentTypeEnum;
import com.onepiece.community.community.exception.CustomizeErrorCode;
import com.onepiece.community.community.model.Comment;
import com.onepiece.community.community.model.User;
import com.onepiece.community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class CommentController {


    @Autowired
    private CommentService commentService;


    //用json来传输的服务器端的API接口，并前端的json数据转换为对象将其传输到数据库当中去，又或是将对象转换成json传到前端
    //该API不需要返回值，只需要返回状态码
    @ResponseBody//将对象自动序列化为json，发到前端
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,//将post请求当中的body部分反序列化到此处，使其成为对象
                       HttpServletRequest request){//自动的反序列化为对象

        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }//当用户未登录的是否给些提示
        //若前端传过来的content为空，则抛出一个异常
        if(commentCreateDTO==null|| StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        //作为主键的评论标识ID是自增的，所以没有管
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setCommentator(user.getId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);

        commentService.insert(comment);//插入到数据库
        return ResultDTO.okOf();

    }

    //获取二级评论
    @ResponseBody//将对象自动序列化为json，发到前端
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }

}
