package com.jt.controller;


import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Reference(check = false)
    private DubboUserService dubboUserService;
@Autowired
    private  JedisCluster jedisCluster;
    @RequestMapping("/{modulName}")
    public String module(@PathVariable String modulName){
        return modulName;
    }



    @RequestMapping("/doRegister")
    @ResponseBody    //转化JSON
    public SysResult saveUser(User user) {
        dubboUserService.saveUser(user);
        return SysResult.success();

    }
    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(HttpServletRequest request,User user, HttpServletResponse response){
        String ticket=dubboUserService.findUserByUP(user);
        if(StringUtils.isEmpty(ticket)){
            return SysResult.fail();
        }
        Cookie ticketCookie =new Cookie("JT_TICKET",ticket);
        ticketCookie.setMaxAge(7*24*60*60);
        ticketCookie.setPath("/");
        ticketCookie.setDomain("jt.com");
        response.addCookie(ticketCookie);
        return SysResult.success();

    }
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies =request.getCookies();
        if(cookies==null||cookies.length==0){
            return "redirect:/";
        }
        String ticket=null;
        for(Cookie cookie:cookies){
            if("JT_TICKET".equals((cookie.getName()))){
                ticket = cookie.getValue();
                break;

            }
        }
        if(StringUtils.isEmpty(ticket)){
            return "redirect:/";
        }
        //ticket有效删除redis
        Cookie ticketCookie =new Cookie("JT_TICKET","");
        ticketCookie.setMaxAge(0);
        ticketCookie.setPath("/");
        ticketCookie.setDomain("jt.com");
        response.addCookie(ticketCookie);
        return  "redirect:/";
    }
}
