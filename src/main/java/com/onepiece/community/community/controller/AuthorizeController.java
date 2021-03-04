package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.AccesstokenDTO;
import com.onepiece.community.community.dto.GitHubUser;
import com.onepiece.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired //将spring容器中已有的实例加载到此处，与Component相联系
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.url}")
    private String redirectUrl;

    @GetMapping("/callback") //github鉴权网占会向预先设计的”http://localhost:8887/callback“发送code和state参数
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name="state")String state) {
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();

        accesstokenDTO.setState(state);
        accesstokenDTO.setClient_id(clientId);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setRedirect_url(redirectUrl);  //鉴权后用户将去往的url

        String accessToken = githubProvider.getAccessToken(accesstokenDTO);//根据前五行代码设置的参数post到github换取令牌access_token
        GitHubUser user = githubProvider.getUser(accessToken);//至此我方可以用访问令牌标识用户，代表用户向API发出请求
        System.out.println(user.getName());
        return "index";
    }
}
