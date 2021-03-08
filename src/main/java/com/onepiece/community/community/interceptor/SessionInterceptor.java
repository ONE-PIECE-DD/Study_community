package com.onepiece.community.community.interceptor;

import com.onepiece.community.community.mapper.UserMapper;
import com.onepiece.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired//该注解不工作，是因为该类不是Spring接管的bean，所以不会在上下文中依赖，所以给该类一个注解"@Service"使得该类被Spring接管
    private UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0)
            for (Cookie cookie : cookies) {//当访问首页的时候循环访问token，找到token的cookie，然后再到数据库中查是否有记录
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {//如果有，则把user放到session里面，前端去显示
                        //
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        return true;//对所有的请求做拦截之后，做查询数据库的操作，所以这里一定是返回true使得进程可以继续
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
