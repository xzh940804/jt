package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.UserMapper;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;


	@Override
	public List<User> findAll() {
		return userMapper.selectList(null);
	}

	@Override
	public boolean checkUser(String param, Integer type) {
		Map<Integer,String> map=new HashMap<Integer,String>();
		map.put(1,"userName");
		map.put(2,"phone");
		map.put(3,"email");
		String column =map.get(type);
		QueryWrapper<User> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq(column,param);
		User user=userMapper.selectOne(queryWrapper);

		return user==null?false:true;
	}



}
