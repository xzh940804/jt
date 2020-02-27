package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.vo.SysResult;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jt.service.UserService;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	@RequestMapping("/findAll")
	public List<User> findAll(){
		return userService.findAll();
	}
	/**
	 * JSONP实现跨域访问
	 * 返回值对象  JSONPObject对象(collback，data)
	 * 业务返回值对象 SysResult
	 * 页面判断需求   true 用户已存在  false 可以使用
	 * 参数分析
	 * param 用户需要校检的数据
	 * type 1 用户 2 手机 3 邮箱
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject  checkUser(@PathVariable String param,@PathVariable Integer type,String callback){
		JSONPObject  jsonpObject=null;
		try {
			//true false
			boolean flag=userService.checkUser(param,type);
			SysResult result=SysResult.success(flag);
			jsonpObject=new JSONPObject(callback,result);
		}catch (Exception e){
			e.printStackTrace();
			jsonpObject=new JSONPObject(callback,SysResult.fail());

		}
		return jsonpObject;

	}
	@RequestMapping("/query/{ticket}")
	@ResponseBody
	public JSONPObject findUserByTicket(@PathVariable String ticket, HttpServletResponse response, String callback){
		String userJson=jedisCluster.get(ticket);
		JSONPObject jsonpObject=null;
		if(StringUtils.isEmpty(userJson)){
			Cookie cookie=new Cookie("JT_TICKET","");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			cookie.setDomain("jt.com");
			response.addCookie(cookie);
			jsonpObject =new JSONPObject(callback,SysResult.success());
		}else{
			SysResult result=SysResult.success(userJson);
			jsonpObject=new JSONPObject(callback,result);
		}
		return jsonpObject;
	}
	
	
}
