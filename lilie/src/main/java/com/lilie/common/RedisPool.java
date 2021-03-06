package com.lilie.common;

import com.lilie.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by geely
 */
//jedis连接池
public class RedisPool {
    private static JedisPool pool;//jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20")); //控制jedis和redis的最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "20"));//在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "20"));//在jedispool中最小的idle状态(空闲的)的jedis实例的个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));//在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));


    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();//jedis连接池配置对象

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。
        //给连接池赋值，
        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);//redisIp是ip地址，redisPort是redis端口号，1000毫秒也就是一秒，是超时时间。
    }

    //类加载时，就初始化连接池
    static {
        initPool();
    }

    //从连接池拿一个实例
    public static Jedis getJedis() {
        return pool.getResource();
    }

    //把jedis放回连接池
    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    //把jedis放回连接池
    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }


    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("geelykey", "geelyvalue");//利用jedis，给redis中进行增加kv。
        returnResource(jedis);

        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");


    }


}
