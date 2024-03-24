package com.jia.ac.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author jialunyin
 * @version 1.0
 * Redis testclass
 */
@SpringBootTest

public class RedisTest {

    @Resource
    private RedisTemplate<String ,String> redisTemplate;

    @Test
    public void RedisCRUD() {
        // 添加数据
        redisTemplate.opsForValue().set("myKey", "myValue");

        // 获取数据
        String value = redisTemplate.opsForValue().get("myKey");
        assertEquals("myValue", value);

        // 更新数据
        redisTemplate.opsForValue().set("myKey", "newValue");
        value = redisTemplate.opsForValue().get("myKey");
        assertEquals("newValue", value);

        // 删除数据
        redisTemplate.delete("myKey");
        value = redisTemplate.opsForValue().get("myKey");
        assertEquals(null, value);





    }

}
