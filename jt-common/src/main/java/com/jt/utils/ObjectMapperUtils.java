package com.jt.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//对象转化为json   json转化为对象
public class ObjectMapperUtils {
    private static  final ObjectMapper MAPPER=new ObjectMapper();
    public static String  toJson(Object obj){
        String result=null;
        try {
            result= MAPPER.writeValueAsString(obj);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            throw new  RuntimeException(e);
        }
        return result;
    }
    //添加class 类型 直接返回类型对象
    public static <T> T toObj(String json,Class<T> target){
        T t=null;
        try {
            t= MAPPER.readValue(json,target);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return t;
    }
}
