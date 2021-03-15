package com.onepiece.community.community.dto;

import com.onepiece.community.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;//该数值关联到user的user_id
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
