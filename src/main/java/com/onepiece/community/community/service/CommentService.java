package com.onepiece.community.community.service;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.onepiece.community.community.enums.CommentTypeEnum;
import com.onepiece.community.community.exception.CustomizeErrorCode;
import com.onepiece.community.community.exception.CustomizeException;
import com.onepiece.community.community.mapper.CommentMapper;
import com.onepiece.community.community.mapper.QuestionExtMapper;
import com.onepiece.community.community.mapper.QuestionMapper;
import com.onepiece.community.community.model.Comment;
import com.onepiece.community.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public void insert(Comment comment) {
        //判断消息是否存在
        if(comment.getParentId()==null||comment.getParentId()== 0){
            //回复消息的父问题是否存在
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null|| CommentTypeEnum.isExist(comment.getType())){
            //回复的类型是否存在
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType() ==CommentTypeEnum.COMMENT.getType())//判断回复的类型
        {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);//将评论插入数据库：成功
        }else{
            //回复问题
            Question question=questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
}
