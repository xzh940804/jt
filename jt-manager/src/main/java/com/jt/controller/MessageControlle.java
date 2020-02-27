package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageControlle
{
    @Value("${server.port}")
    private String port;
    //动态获取真实服务器端口号
    @RequestMapping("/getport")
    public String getPort(){
        return "当前服务端口号为:"+port;

    }
}
