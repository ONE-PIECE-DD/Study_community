package com.onepiece.community.community.dto;

import lombok.Data;

//用于存储向github官网换取accessToken的信息
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_url;
    private String state;
}
