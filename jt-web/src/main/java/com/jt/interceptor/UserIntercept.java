package com.jt.interceptor;

import com.jt.pojo.User;
import com.jt.util.UserThreadLocal;
import com.jt.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Repository
public class UserIntercept implements HandlerInterceptor {
    @Autowired
    private JedisCluster jedisCluster;
    /*
        true放行  false 拦截
        从cookie动态获取JT_
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Cookie[] cookies = request.getCookies();
        if(cookies ==null || cookies.length==0) {
            //重定向到用户的的登录页面
            response.sendRedirect("/user/login.html");
            return false;
        }

        String ticket = null;
        for (Cookie cookie : cookies) {
            if("JT_TICKET".equals(cookie.getName())) {
                ticket = cookie.getValue();
                break;
            }
        }

        if(StringUtils.isEmpty(ticket)) {
            //重定向到用户的的登录页面
            response.sendRedirect("/user/login.html");
            return false;
        }

        //ticket用户有值
        String userJSON = jedisCluster.get(ticket);
        if(StringUtils.isEmpty(userJSON)) {
            Cookie ticketCookie = new Cookie("JT_TICKET", "");
            ticketCookie.setDomain("jt.com");
            ticketCookie.setMaxAge(0);
            ticketCookie.setPath("/");

            response.addCookie(ticketCookie);

            //重定向到用户的的登录页面
            response.sendRedirect("/user/login.html");
            return false;
        }
        User user = ObjectMapperUtils.toObj(userJSON, User.class);
        request.setAttribute("JT_USER", user);
        //方式二
        UserThreadLocal.set(user);
        return true; //放行请求!!!


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserThreadLocal.remove();
    }
}
