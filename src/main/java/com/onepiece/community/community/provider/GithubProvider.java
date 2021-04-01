package com.onepiece.community.community.provider;

import com.alibaba.fastjson.JSON;
import com.onepiece.community.community.dto.AccessTokenDTO;
import com.onepiece.community.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component //仅仅的把这个类初始化到spring容器的上下文，将对象自动的实例化到了一个池子里面，当我们去用的时候可以很轻松的通过名字拿出来用
public class GithubProvider{

    public String getAccessToken(AccessTokenDTO accesstokenDTO){

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accesstokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {//这种io异常并不常见
            e.printStackTrace();
        }
        return null;
    }

    public GitHubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token " + accessToken)
                .build();
        try{
            Response response = client.newCall(request).execute();
            String string = response.body().string();

            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class); //将string代表的json对象解析为java的GitHubUser类对象
            System.out.println(""+gitHubUser);
            return gitHubUser;
        } catch (IOException e) {
            return null;
        }
    }

}
