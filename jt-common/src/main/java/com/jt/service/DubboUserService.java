package com.jt.service;


import com.jt.pojo.User;

//定义公共的api接口
public interface DubboUserService {

    void saveUser(User user);

    String findUserByUP(User user);
}
