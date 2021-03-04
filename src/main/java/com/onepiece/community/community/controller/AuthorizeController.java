package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.AccesstokenDTO;
import com.onepiece.community.community.dto.GitHubUser;
import com.onepiece.community.community.mapper.UserMapper;
import com.onepiece.community.community.model.User;
import com.onepiece.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

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

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback") //github鉴权网占会向预先设计的”http://localhost:8887/callback“发送code和state参数
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();

        accesstokenDTO.setState(state);
        accesstokenDTO.setClient_id(clientId);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setRedirect_url(redirectUrl);  //鉴权后用户将去往的url

        String accessToken = githubProvider.getAccessToken(accesstokenDTO);//根据前五行代码设置的参数post到github换取令牌access_token
        GitHubUser githubUser = githubProvider.getUser(accessToken);//至此我方可以用访问令牌标识用户，代表用户向API发出请求
        if(githubUser !=null){

            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            request.getSession().setAttribute("githubUser",githubUser);
            return "redirect:/";//跳转到index页面，如果不写这行，那么地址会变。
            //登录成功
        }else{
            return "redirect:/";
            //登录失败
        }
    }
}
