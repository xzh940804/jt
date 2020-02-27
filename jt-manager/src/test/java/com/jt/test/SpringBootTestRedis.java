


import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.util.ShardInfo;

import java.util.*;


//@SpringBootTest //2.2.2
public class SpringBootTestRedis {
	
	/**
	 * redis入门案例
	 * host: redis的IP地址
	 * port: redis的端口号
	 * 
	 *   业务:
	 * 	 1.当key存在时,不允许修改数据.	
	 */
	@Test
	public void testString() {
		
		Jedis jedis = new Jedis("192.168.19.131", 6379);
		jedis.set("name","小鸡吃米图");
		String value1 = jedis.get("name");
		System.out.println(value1);
		//set 会覆盖之前的数据.
		jedis.set("name", "小鸡炖蘑菇");
		String value2 = jedis.get("name");
		System.out.println(value2);
	}
	private Jedis jedis;
	@Before
	public void init(){
		jedis = new Jedis("192.168.19.131", 6379);
	}
	@Test
	public void testString2(){
		jedis.setnx("1909","今天周三！！！");
		jedis.setnx("1909","今天周四！！！");
		System.out.println(jedis.get("1909"));
	}
	@Test
	public void testString3() throws InterruptedException {
		//添加超时时间
//		jedis.set("eat","apple");
//		jedis.expire("eat",10);

		SetParams setParams=new SetParams();
		setParams.ex(10).nx();
		jedis.set("eat","apple",setParams);
		System.out.println(jedis.get("eat"));

	}
	@Test
	public void testString4(){
		//一组数据用hash
		jedis.hset("person","id","200");
		jedis.hset("person","name","tomcat");

		Map<String,String> map=new HashMap<>();
		map.put("id","2001");
		map.put("name","伊朗你狠");
		map.put("age","180");
		jedis.hset("student",map);
		System.out.println(jedis.hvals("person"));
		System.out.println(jedis.hgetAll("student"));
	}


	@Test
	public void testList() {
		jedis.lpush("list", "1","2","3");
		System.out.println(jedis.rpop("list"));
	}
	@Test
	public void testTx() {
		//1.开启事务
		Transaction transaction = jedis.multi();
		try {
			//2.进行业务操作
			transaction.set("a", "aa");
			transaction.set("b", "bb");
			transaction.set("c", "cc");
			//3.事务提交
			transaction.exec();
		} catch (Exception e) {
			//4.事务回滚
			transaction.discard();
		}
	}
	//实现redis分片操作
	@Test
	public void testShirds(){
		List<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.19.131",6379));
		shards.add(new JedisShardInfo("192.168.19.131",6380));
		shards.add(new JedisShardInfo("192.168.19.131",6381));
		ShardedJedis jedis=new ShardedJedis(shards);
		jedis.set("1909", "您好Redis分片");
		System.out.println(jedis.get("1909"));

	}
	@Test
	public void testSentinel(){
		Set<String> sentinels=new HashSet<>();
		sentinels.add("192.168.19.131:26379");
		JedisSentinelPool pool=new JedisSentinelPool("mymaster",sentinels);
		Jedis jedis=pool.getResource();
		jedis.set("1909","哨兵搭建成功！！！");
		System.out.println(jedis.get("1909"));
	}
	@Test
	public void testCluster(){
		Set<HostAndPort> nodes=new HashSet<>();
		nodes.add(new HostAndPort("192.168.19.131",7000));
		nodes.add(new HostAndPort("192.168.19.131",7001));
		nodes.add(new HostAndPort("192.168.19.131",7002));
		nodes.add(new HostAndPort("192.168.19.131",7003));
		nodes.add(new HostAndPort("192.168.19.131",7004));
		nodes.add(new HostAndPort("192.168.19.131",7005));
		JedisCluster jedisCluster=new JedisCluster(nodes);
		jedisCluster.set("key","redis集群搭建成功");
		System.out.println(jedisCluster.get("key"));

	}
}
