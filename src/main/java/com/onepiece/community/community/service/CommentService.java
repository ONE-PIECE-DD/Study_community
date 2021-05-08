package com.onepiece.community.community.service;


import com.onepiece.community.community.dto.CommentDTO;
import com.onepiece.community.community.enums.CommentTypeEnum;
import com.onepiece.community.community.enums.NotificationEnum;
import com.onepiece.community.community.enums.NotificationStatusEnum;
import com.onepiece.community.community.exception.CustomizeErrorCode;
import com.onepiece.community.community.exception.CustomizeException;
import com.onepiece.community.community.mapper.*;
import com.onepiece.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;


    @Transactional
    public void insert(Comment comment, User commentTator) {
        //判断消息是否存在
        if(comment.getParentId()==null||comment.getParentId()== 0){
            //回复消息的父问题不存在，则抛出异常信息：目标未找到
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null|| !CommentTypeEnum.isExist(comment.getType())){
            //回复的类型不存在，则抛出异常信息：类型错误
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if(comment.getType().equals(CommentTypeEnum.COMMENT.getType()))//判断回复的类型：1表示回复问题、2表示回复评论
        {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());

            if(dbComment==null){
                //未找到评论，这种情况便是回复的ID虽然被赋予了值，但ID对应的原评论被删除了
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //回复问题
            Question question=questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);//将评论插入数据库：成功
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //增加通知
            createNotify(comment, dbComment.getCommentator(), commentTator.getName(), question.getTitle(), NotificationEnum.REPLY_COMMENT, question.getId());
        }else if(comment.getType().equals(CommentTypeEnum.QUESTION.getType())){//问：此处==与equals的区别
            //回复问题
            Question question=questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //此处插入评论的时候comment的commentCount属性未被赋值，以至于后面二级评论无法统计
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //增加通知
            createNotify(comment,question.getCreator(),commentTator.getName(),question.getTitle(),NotificationEnum.REPLY_QUESTION, question.getId());

        }else {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_NOT_EXIST);
        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationEnum notificationType, Long outerId) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notification.setReceiver(receiver);

        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if(comments.size()==0){
            return new ArrayList<>();
        }

        //TODO:此处语法需要理解消化
        /*java 8的语法*/
        //使用lambda表达式获取去重的评论人列表
        List<Long> userIds = comments.stream().map(Comment::getCommentator).distinct().collect(Collectors.toList());

        //获取评论人并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
