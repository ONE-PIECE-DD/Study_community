package com.onepiece.community.community.dto;

import com.onepiece.community.community.model.User;
import lombok.Data;
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private String notifierName;
    private String outerTitle;
    private Long outerId;
    private String typeName;
    private Integer type;
}
