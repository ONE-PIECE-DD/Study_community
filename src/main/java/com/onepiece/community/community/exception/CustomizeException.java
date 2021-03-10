package com.onepiece.community.community.exception;


public class CustomizeException extends RuntimeException{//如果不继承该异常的话，那么如果那边抛出了异常，那么必须得在上一层try-catch
    private String message;
    public CustomizeException(ICustomizeErrorCode errorCode)
    {
        this.message=errorCode.getMessage();
    }
    @Override
    public String getMessage(){
        return message;
    }
}
