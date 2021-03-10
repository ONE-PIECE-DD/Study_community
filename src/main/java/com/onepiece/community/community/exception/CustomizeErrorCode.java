package com.onepiece.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    //外层传进去，就是一个接口的形式，而里面可以定义成不同类型的code;
    QUESTION_NOT_FOUND("你找的东西不在了");
    @Override
    public String getMessage() {
        return message;
    }

    private String message;

    CustomizeErrorCode(String message){
        this.message = message;
    }
}
