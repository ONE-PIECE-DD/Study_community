package com.onepiece.community.community.enums;

import com.onepiece.community.community.model.Comment;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2)
    ;
    private Integer type;

    public Integer getType() {
        return type;
    }

    //TODO：此处是枚举变量的构造方法，与其它的构造方法有点特殊，传进去的参数会与枚举变形成映射关系，如此处的映射关系为QUESTION(1);
    //ps:有点迷
    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if(commentTypeEnum.getType()==type)
                return true;
        }
        return false;
    }
}
