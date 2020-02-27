package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.UUID;

@Service
public class DubboUserServiceImpl implements DubboUserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;

    public void saveUser(User user) {
        System.out.println("rpc调用成功");
        String password= user.getPassword();
        String md5Pass = DigestUtils.md5DigestAsHex
                (password.getBytes());
        user.setPassword(md5Pass).setEmail(user.getPhone()).setCreated(new Date()).setUpdated(user.getCreated());
        userMapper.insert(user);

    }

    /**
     * c查询用户信息
     * 将user对象转化为json
     * 保存到redis中
     *
     * @param user
     * @return
     */
    @Override
    public String findUserByUP(User user) {
        //将密码加密处理
        String md5Pass=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //利用对象中不为空的属性当做weher条件
        QueryWrapper<User> queryWrapper=new QueryWrapper<User>(user);
        User userDB=userMapper.selectOne(queryWrapper);
        if(userDB==null){
            //用户名不正确
            return null;
        }
        //用户输入信息正确
        //防止用户的敏感数据泄露  一般会将数据进行脱敏处理  张**
        userDB.setPassword("123456你信吗");
        String ticket= UUID.randomUUID().toString();
        String json=ObjectMapperUtils.toJson(userDB);
        jedisCluster.setex(ticket,7*24*600,json);



        return ticket;
    }
}
