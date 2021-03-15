package com.onepiece.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    //外层传进去，就是一个接口的形式，而里面可以定义成不同类型的code;
    QUESTION_NOT_FOUND(2001,"你找的东西不在了"),
    TARGET_PARAM_NOT_FOUND(2002,"你找的东西不在了"),
    NO_LOGIN(2003,"当前操作未登录，请先登录再重试"),
    SYS_ERROR(2004,"服务器猫眼了"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你操作的评论不存在");


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }


    private String message;
    private Integer code;

    //
    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

}
