package com.onepiece.community.community.dto;

import lombok.Data;

//用于存储github处返回的用户部分信息
@Data
public class GitHubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
