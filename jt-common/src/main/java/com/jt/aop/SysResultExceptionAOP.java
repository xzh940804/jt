package com.jt.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class SysResultExceptionAOP {
    /**
     * 统一返回数据 SysResult对象 status=201
     */
    @ExceptionHandler(RuntimeException.class)
    public Object fail(Exception exception, HttpServletRequest request) {
    String callback=request.getParameter("callback");
    if(!StringUtils.isEmpty(callback)){
        exception.printStackTrace();
        //用户发起的jsonp请求
        return new JSONPObject(callback,SysResult.fail());
    }
    //说明是一个常规调用
        exception.printStackTrace();
    return SysResult.fail();

    }

}
