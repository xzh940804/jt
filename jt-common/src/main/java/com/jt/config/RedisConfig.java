package com.jt.config;

import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
//    @Value("${redis.host}")
//    private String host;
//    @Value("${redis.port}")
//    private Integer port;
//    @Bean
//    @Scope
   // public Jedis jedis(){
//        return new Jedis(host,port);
//    }
//    @Value("${redis.nodes}")
//    private String nodes;
//    @Bean
//    @Scope("prototype")
//    public ShardedJedis shardedJedis(){
//        List<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
//        String[]  arrayNode =nodes.split(",");
//        for(String node:arrayNode){
//            String host=node.split(":")[0];
//            int port=Integer.parseInt(node.split(":")[1]);
//            JedisShardInfo info=new JedisShardInfo(host,port);
//            shards.add(info);
//        }
//        return new ShardedJedis(shards);
//    }
//        //使用redis的哨兵机制 实现redis缓存操作
//    @Value("${redis.sentinel}")
//    private String sentinel;
//    @Bean
//    public JedisSentinelPool sentinelPool(){
//        Set<String> sentinels=new HashSet<>();
//        sentinels.add(sentinel);
//        return new JedisSentinelPool("mymaster",sentinels);
//    }
    @Value("${redis.nodes}")
    private String nodes;

    @Bean
    @Scope("prototype")
    public JedisCluster jedisCluster(){
        Set<HostAndPort> setNodes=new HashSet();
        String [] arrayNodes=nodes.split(",");
        for(String node:arrayNodes){
            System.out.println(node);
            String host=node.split(":")[0];
            int port=Integer.parseInt(node.split(":")[1]);
            HostAndPort hostAndPort=new HostAndPort(host,port);
            setNodes.add(hostAndPort);
        }
        return new JedisCluster(setNodes);
    }

}
