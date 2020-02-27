package com.jt.ano;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheFind {
    //保存到redis中   用户可以指定  也可以动态生成
    public String key() default  "";
    public int seconds() default 0;//设定超时时间 秒

}
