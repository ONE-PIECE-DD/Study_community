package com.onepiece.community.community.controller;

import com.onepiece.community.community.dto.AccessTokenDTO;
import com.onepiece.community.community.dto.GitHubUser;
import com.onepiece.community.community.mapper.UserMapper;
import com.onepiece.community.community.model.User;
import com.onepiece.community.community.provider.GithubProvider;
import com.onepiece.community.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired //将spring容器中已有的实例加载到此处，与Component相联系
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    //用该注解，取出application.properties中配置的信息存储到注解下的变量
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.url}")
    private String redirectUrl;


    @GetMapping("/callback") //github鉴权网占会向预先设计的”http://localhost:8887/callback“发送code和state参数
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        //组装好信息，为获得accesstoken做准备
        AccessTokenDTO accesstokenDTO = new AccessTokenDTO();
        accesstokenDTO.setState(state);
        accesstokenDTO.setClient_id(clientId);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setRedirect_url(redirectUrl);  //鉴权后用户将去往的url
        //按照官网提示，朝着指定网址发送信息以获取accesstoken
        String accessToken = githubProvider.getAccessToken(accesstokenDTO);//根据前五行代码设置的参数post到github换取令牌access_token
        //用获得的accesstoken代替用户登录github网址，返回用户信息
        GitHubUser githubUser = githubProvider.getUser(accessToken);//至此我方可以用访问令牌标识用户，代表用户向API发出请求
        if(githubUser !=null){
            //利用github得到的信息生成本网站需要的用户信息
            User user = new User();
            String token =UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            //将用户信息更新或保存到本地数据库
            userService.createOrUpdate(user);
            //登录成功，将token写入cookie里面
            response.addCookie(new Cookie("token",token));
            return "redirect:/";//跳转到index页面，如果不写这行，那么地址会变。
            //登录成功
        }else{
            return "redirect:/";
            //登录失败
        }

    }
    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        //退出后移除session当中数据
        request.getSession().removeAttribute("user");
        //退出后移除Cookie当中的数据
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        return "redirect:/";
    }
}
