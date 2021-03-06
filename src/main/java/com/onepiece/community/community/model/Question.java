package com.onepiece.community.community.model;


import lombok.Data;

@Data
public class Question {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;//该数值关联到user的user_id
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}

