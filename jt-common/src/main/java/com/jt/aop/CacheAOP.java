package com.jt.aop;

import com.jt.ano.CacheFind;

import com.jt.utils.ObjectMapperUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

@Component
@Aspect
public class CacheAOP {
    @Autowired(required = false)
    private JedisCluster jedis;
//   private JedisSentinelPool pool;
    // private ShardedJedis jedis;
 //   private Jedis jedis;
    @Around("@annotation(cacheFind)")
  public Object around(ProceedingJoinPoint joinPoint, CacheFind cacheFind){

        //动态获取key的值
        String key =getKey(joinPoint,cacheFind);
        //获取 缓存数据
        String  result=jedis.get(key);
        Object obj=null;
        try{
            if(StringUtils.isEmpty(result)){
                obj=joinPoint.proceed();
                //将数据存入redis中
                String json=ObjectMapperUtils.toJson(obj);
                if(cacheFind.seconds()>0){
                    jedis.setex(key,cacheFind.seconds(),json);
                }else{
                    jedis.set(key,json);
                }

            }else{
                Class<?>  returnType=getReturnType(joinPoint);
                obj=ObjectMapperUtils.toObj(result,returnType);

            }
        }catch(Throwable e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return  obj;
  }

    private Class<?> getReturnType(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); //方法签名对象
        return signature.getReturnType();

    }

    //如果用户自己指定了key  使用用户的 如果没有指定则动态生成
    private String getKey(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
            String key=cacheFind.key();
            if(!StringUtils.isEmpty(key)){
                return key;
            }
            //如果用户没有传递 则动态生成
        Signature signature=joinPoint.getSignature();
           String className=signature.getDeclaringTypeName();
            String methodName=signature.getName();
            Long arg0=(Long) joinPoint.getArgs()[0];
            key=className+"."+methodName+"::"+arg0;
            return key;
    }
}
