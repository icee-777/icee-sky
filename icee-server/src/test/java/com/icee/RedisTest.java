package com.icee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Set;
import java.util.concurrent.TimeUnit;

//TODO 不注释的话,每次启动项目都会进行一次测试
//@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        System.out.println(redisTemplate);
        //获取redis五大操作类型
        ValueOperations valueOperations = redisTemplate.opsForValue();
        HashOperations hashOperations = redisTemplate.opsForHash();
        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations setOperations = redisTemplate.opsForSet();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }

    @Test
    public void testKinds(){
        stringRedisTemplate.opsForValue().set("test","2");
        System.out.println(stringRedisTemplate.opsForValue().get("test"));
        stringRedisTemplate.opsForHash().put("hash","h1","1");
        System.out.println(stringRedisTemplate.opsForHash().get("hash", "h1"));
        stringRedisTemplate.opsForValue().set("test1","1",3, TimeUnit.MINUTES);

        Set<String> keys = stringRedisTemplate.keys("*");
        System.out.println(keys);
    }
}
