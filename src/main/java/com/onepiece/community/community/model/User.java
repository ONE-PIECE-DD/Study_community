package com.onepiece.community.community.model;


import lombok.Data;

//保存本地的用户信息，此处的信息由githubUser中提取得到
@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;

}
