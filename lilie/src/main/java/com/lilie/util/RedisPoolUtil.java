package com.lilie.util;

import com.lilie.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * Created by geely
 */
@Slf4j
//这个类里面的方法都是调用RedisPool类中的方法。也就是说当前台要传进参数的时候，进行的一系列操作方法。比如删除k
//基本就是分布式锁用到的set get expire del
public class RedisPoolUtil {


    /**
     * 设置key的有效期，单位是秒
     *
     * @param key
     * @param exTime
     * @return
     */
    //重新设置k的有效期
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    //exTime的单位是秒，设置销毁时间。
    public static String setEx(String key, String value, int exTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

//    public static void main(String[] args) {
//        Jedis jedis = RedisPool.getJedis();//这必须要先通过RedisPool拿到一个jedis连接池，下面的方法才能链接数据库redis进行操作。
//
//        RedisShardedPoolUtil.set("keyTest", "value");
//
//        String value = RedisShardedPoolUtil.get("keyTest");
//
//        RedisShardedPoolUtil.setEx("keyex", "valueex", 60 * 10);//秒
//
//        RedisShardedPoolUtil.expire("keyTest", 60 * 20);
//
//        RedisShardedPoolUtil.del("keyTest");
//
//
//        String aaa = RedisShardedPoolUtil.get(null);
//        System.out.println(aaa);
//
//        System.out.println("end");
//
//
//    }


}
