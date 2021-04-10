package com.onepiece.community.community.mapper;

import com.onepiece.community.community.model.Comment;
import org.springframework.stereotype.Component;


@Component
public interface CommentExtMapper {
    int incCommentCount(Comment comment);//调用该方法的时候，自动映射到QuestionExtMapper.xml,映射关系靠xml文件当中的标签
}
